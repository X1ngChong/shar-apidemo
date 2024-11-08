package top.bhui.share.user.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Add Bonus Message Data Transfer Object  
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusMsgDTO {
    /**
     * 为谁加积分  
     */
    private Long userId;          // User ID for whom the bonus is added  

    /**
     * 加多少分  
     */
    private Integer bonus;        // Amount of bonus points to add  

    /**
     * 描述信息  
     */
    private String description;    // Description of the bonus addition  

    /**
     * 积分事件：签到、投稿、兑换等  
     */
    private String event;         // Points event (e.g., sign-in, submission, redemption)  
}