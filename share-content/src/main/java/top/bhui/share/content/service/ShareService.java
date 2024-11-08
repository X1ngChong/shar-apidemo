package top.bhui.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.content.domain.dto.ExchangeDTO;
import top.bhui.share.content.domain.dto.ShareRequestDTO;
import top.bhui.share.content.domain.dto.UserAddBonusMsgDTO;
import top.bhui.share.content.domain.entity.MidUserShare;
import top.bhui.share.content.domain.entity.Share;
import top.bhui.share.content.domain.entity.User;
import top.bhui.share.content.domain.resp.ShareResp;
import top.bhui.share.content.feign.UserService;
import top.bhui.share.content.mapper.MidUserShareMapper;
import top.bhui.share.content.mapper.ShareMapper;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ShareService {

    @Resource
    private ShareMapper shareMapper;

    @Resource
    private UserService userService;

    @Resource
    private MidUserShareMapper midUserShareMapper;

    public List<Share> getList(String title, Integer pageNo, Integer pageSize ,Long userId) {
        // Construct query conditions  
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();

        // Query all data in descending order by id  
        wrapper.orderByDesc(Share::getId);

        // If the title keyword is not empty, add a fuzzy query condition to filter the results  
        if (title != null) {
            wrapper.like(Share::getTitle, title);
        }

        // Filter out all data that has passed the audit and has showFlag set to true  
        wrapper.eq(Share::getAuditStatus, "PASS").eq(Share::getShowFlag, true);

        //分页
        Page<Share> page = Page.of(pageNo,pageSize);

        // Query the list based on conditions  
        List<Share> shares = shareMapper.selectList(page,wrapper);

        // Process the Share data list  
        List<Share> sharesDeal;

        // 1. If the user is not logged in, set downloadUrl to null for all  
        if (userId != null) {
            sharesDeal = shares.stream()
                    .peek(share -> share.setDownloadUrl(null))
                    .collect(Collectors.toList());
        }
        // 2. If the user is logged in, query the mid_user_share table  
        else {
            sharesDeal = shares.stream()
                    .peek(share -> {
                        MidUserShare midUserShare = midUserShareMapper.selectOne(
                                new QueryWrapper<MidUserShare>().lambda()
                                        .eq(MidUserShare::getUserId, userId)
                                        .eq(MidUserShare::getShareId, share.getId())
                        );
                        if (midUserShare == null) {
                            share.setDownloadUrl(null);
                        }
                    })
                    .collect(Collectors.toList());
        }

        return sharesDeal;
    }
    public ShareResp findById(Long shareId) {
        Share share = shareMapper.selectById(shareId);

        // 调用 feign 方法，根据用户 id 查询到用户信息
        CommonResp<User> commonResp = userService.getUser(share.getUserId());

        return ShareResp.builder()
                .share(share)
                .nickname(commonResp.getData().getNickname())
                .avatarUrl(commonResp.getData().getAvatarUrl())
                .build();
    }
    public Share exchange(ExchangeDTO exchangeDTO) {
        Long userId = exchangeDTO.getUserId();
        Long shareId = exchangeDTO.getShareId();

        // 1. 根据 id 查询 share，校验需要兑换的内容是否存在
        Share share = shareMapper.selectById(shareId);
        if (share == null) {
            throw new IllegalArgumentException("该分享内容不存在！");
        }

        // 2. 如果当前用户已经兑换过，则直接返回该分享内容（不需要扣积分）
        MidUserShare midUserShare = midUserShareMapper.selectOne(new QueryWrapper<MidUserShare>()
                .lambda()
                .eq(MidUserShare::getUserId, userId)
                .eq(MidUserShare::getShareId, shareId));
        if (midUserShare != null) {
            return share;
        }

        // 3. 判断用户积分是否足够兑换该内容
        CommonResp<User> commonResp = userService.getUser(userId);
        User user = commonResp.getData();

        // 兑换这条内容需要的积分
        Integer price = share.getPrice();

        // 用户的积分不够
        if (price > user.getBonus()) {
            throw new IllegalArgumentException("用户积分不够！");
        }

        // 4. 修改积分：乘以 -1 变成负值，就是扣分
        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(price * -1)
                .build());

        // 5. 向 mid_user_share 表插入一条数据，记录这个用户已经兑换过，就拥有了下载权限（前端 页面）
        midUserShareMapper.insert(MidUserShare.builder()
                .userId(userId)
                .shareId(shareId)
                .build());

        return share;
    }

    /**
     * 投稿
     *
     * @param shareRequestDTO 投稿参数
     * @return int
     */
    public int contribute(ShareRequestDTO shareRequestDTO) {
        Share share = Share.builder()
                .isOriginal(shareRequestDTO.getIsOriginal())
                .author(shareRequestDTO.getAuthor())
                .price(shareRequestDTO.getPrice())
                .downloadUrl(shareRequestDTO.getDownloadUrl())
                .summary(shareRequestDTO.getSummary())
                .buyCount(0)
                .title(shareRequestDTO.getTitle())
                .userId(shareRequestDTO.getUserId())
                .cover(shareRequestDTO.getCover())
                .showFlag(false)
                .auditStatus("NOT_YET")
                .reason("未审核") // "Not reviewed" in Chinese
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        return shareMapper.insert(share);
    }

    /**
     * 我的投稿
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    public List<Share> myContribute(Integer pageNo, Integer pageSize, Long userId) {
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Share::getId);
        wrapper.eq(Share::getUserId, userId);

        Page<Share> page = Page.of(pageNo, pageSize);
        return shareMapper.selectList(page, wrapper);
    }

    /**
     * 查询待审核的shares列表
     *
     * @return List<Share>
     */
    public List<Share> queryShareNotYet() {
        LambdaQueryWrapper<Share> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Share::getId);
        wrapper.eq(Share::getAuditStatus, "NOT_YET")
                .eq(Share::getShowFlag, false);
        return shareMapper.selectList(wrapper);
    }

    /**
     * 管理员审核文章
     * @param contentId
     */
    public void approveArticle(Long contentId) {
        Share share = shareMapper.selectById(contentId);
        if(share !=null){
            share.setAuditStatus("PASS");
            share.setReason("通过审核");
            shareMapper.updateById(share);
        }
    }
}