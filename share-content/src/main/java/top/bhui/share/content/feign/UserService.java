package top.bhui.share.content.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.bhui.share.common.resp.CommonResp;
import top.bhui.share.content.domain.dto.UserAddBonusMsgDTO;
import top.bhui.share.content.domain.entity.User;

@FeignClient(value = "user-service", path = "/user")
public interface UserService {
    @GetMapping("/{id}")
    CommonResp<User> getUser(@PathVariable Long id);

    @PostMapping("/updateBonus")
     CommonResp<User> updateBonus(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO);

    @PostMapping("/approveContent")
     CommonResp<User> approveContent(@RequestBody UserAddBonusMsgDTO userAddBonusMsgDTO);

}
