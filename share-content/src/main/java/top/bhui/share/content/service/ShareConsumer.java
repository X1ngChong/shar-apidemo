package top.bhui.share.content.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.content.domain.dto.UserAddBonusMsgDTO;
import top.bhui.share.content.domain.entity.User;
import top.bhui.share.content.feign.UserService;

/**
 * @author JXS
 */
@Service
@RocketMQMessageListener(topic = "ShareTopic_bhui",selectorExpression = "ShareApproved_bhui",consumerGroup = "share_group_bhui")
@AllArgsConstructor
public class ShareConsumer implements RocketMQListener<String> {
    private final UserService userService;


    @Override
    public void onMessage(String message) {
        // 1. 为用户修改积分
        String[] split = message.split(":");
        Long userId = Long.parseLong(split[0]);
        Integer bonus = Integer.parseInt(split[1]);
        UserAddBonusMsgDTO udto = new UserAddBonusMsgDTO();
        udto.setUserId(userId);
        udto.setBonus(bonus);

        userService.approveContent(udto);//通过并且添加了日志
    }
}
