package top.bhui.share.user.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.user.domain.dto.LoginDTO;
import top.bhui.share.user.domain.dto.UserAddBonusMsgDTO;
import top.bhui.share.user.domain.entity.User;
import top.bhui.share.user.domain.resp.UserLoginResp;
import top.bhui.share.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/count")
    public Long count(){
        return userService.count();
    }

    @PostMapping("/login")
    public CommonResp<UserLoginResp> login(@Valid @RequestBody LoginDTO loginDTO){
        UserLoginResp user = userService.login(loginDTO);
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        resp.setData(user);
        return resp;
    }

    @PostMapping("/register")
    public CommonResp<Long> register(@Valid @RequestBody LoginDTO loginDTO) {
        Long id = userService.register(loginDTO);
        CommonResp<Long> resp = new CommonResp<>();
        resp.setData(id);
        return resp;
    }

    @GetMapping("/{id}")
    public CommonResp<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        CommonResp<User> resp = new CommonResp<>();
        resp.setData(user);
        return resp;
    }

    @PostMapping("/updateBonus")
    public CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        Long userId = userAddBonusMsgDTO.getUserId();

        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(userAddBonusMsgDTO.getBonus())
                .description("兑换分享内容")
                .event("BUY")
                .build());

        CommonResp<User> resp = new CommonResp<>();
        resp.setData(userService.findById(userId));

        return resp;
    }

    @PostMapping("/approveContent")
    public CommonResp<User> approveContent(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO) {
        Long userId = userAddBonusMsgDTO.getUserId();

        userService.updateBonus(UserAddBonusMsgDTO.builder()
                .userId(userId)
                .bonus(userAddBonusMsgDTO.getBonus())
                .description("通过分享")
                .event("APPROVESHARE")
                .build());

        CommonResp<User> resp = new CommonResp<>();
        resp.setData(userService.findById(userId));

        return resp;
    }

}
