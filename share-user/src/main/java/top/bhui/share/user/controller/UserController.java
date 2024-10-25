package top.bhui.share.user.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.user.domain.dto.LoginDTO;
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

}
