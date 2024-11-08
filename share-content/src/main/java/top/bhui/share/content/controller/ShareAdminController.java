package top.bhui.share.content.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.content.domain.entity.Share;
import top.bhui.share.content.service.ShareService;

import java.util.List;

@RestController
@RequestMapping("/share/admin")
@Slf4j
@AllArgsConstructor
public class ShareAdminController {

    private final ShareService shareService;

    /**
     * 查询待审核的分享列表
     *
     * @return CommonResp<List<Share>>
     */
    @GetMapping("/list")
    public CommonResp<List<Share>> getSharesNotYet() {
        CommonResp<List<Share>> resp = new CommonResp<>();
        resp.setData(shareService.queryShareNotYet());
        return resp;
    }
}