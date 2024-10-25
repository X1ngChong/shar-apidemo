package top.bhui.share.user.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.bhui.share.common.exception.BusinessException;
import top.bhui.share.common.exception.BusinessExceptionEnum;
import top.bhui.share.common.util.JwtUtil;
import top.bhui.share.common.util.SnowUtil;
import top.bhui.share.user.domain.dto.LoginDTO;
import top.bhui.share.user.domain.entity.User;
import top.bhui.share.user.domain.resp.UserLoginResp;
import top.bhui.share.user.mapper.UserMapper;

import java.util.Date;
import java.util.Map;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;

    public Long count(){
        return userMapper.selectCount(null);
    }

    public UserLoginResp login(LoginDTO loginDTO){
        User user = userMapper.selectOne(new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone()));

        if (user == null){
            throw new BusinessException(BusinessExceptionEnum.PHONE_NOT_EXIST);

        }
        if(!user.getPassword().equals(loginDTO.getPassword())){
            throw new BusinessException(BusinessExceptionEnum.PASSWORD_ERROR);
        }
        UserLoginResp userLoginResp = UserLoginResp.builder().user(user).build();
//        String key = "helloworld";
//        Map<String, Object> map = BeanUtil.beanToMap(userLoginResp);
//        String token = JWTUtil.createToken(map, key.getBytes());
        String token = JwtUtil.createToken(userLoginResp.getUser().getId(), userLoginResp.getUser().getPhone());
        userLoginResp.setToken(token);
        return userLoginResp;
    }

    public Long register(LoginDTO loginDTO){
        User userDb = userMapper.selectOne(
                new QueryWrapper<User>().lambda().eq(User::getPhone, loginDTO.getPhone())
        );

        if (userDb != null) {
            throw new BusinessException(BusinessExceptionEnum.PHONE_EXIST);
        }

        User saveUser = User.builder()
                .id(SnowUtil.getSnowflakeNextId())
                .phone(loginDTO.getPhone())
                .password(loginDTO.getPassword())
                .nickname("新用户")
                .avatarUrl("https://niit-soft.oss-cn-hangzhou.aliyuncs.com/avatar/8.jpg")
                .bonus(100)
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        userMapper.insert(saveUser);
        return saveUser.getId();
    }

}
