package top.bhui.share.content.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;


@SpringBootApplication
@ComponentScan("top.bhui")
@MapperScan("top.bhui.share.*.mapper")
@EnableFeignClients(basePackages = {"top.bhui"})
@Slf4j
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ContentApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功！");
        log.info("测试地址：http://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}