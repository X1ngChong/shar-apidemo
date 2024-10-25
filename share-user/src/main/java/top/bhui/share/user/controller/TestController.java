package top.bhui.share.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author bhui
 */
@RestController
public class TestController {
    @GetMapping("/hello")
    public  String hello(){
       // System.out.println(1/0);
        return "hello world";
    }
}
