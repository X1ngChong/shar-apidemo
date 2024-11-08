package top.bhui.share.content.service;

import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

/**
 * @author JXS
 */
@Service
public class ShareProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void sendSharesMessage(Long userId,int points){
        rocketMQTemplate.convertAndSend("ShareTopic_bhui:ShareApproved_bhui",userId+":"+points);
    }
}
