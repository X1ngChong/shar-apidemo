package top.bhui.share.user.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Bonus Event Log Class  
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BonusEventLog {
    private Long id;                // ID  
    private Long userId;            // User ID  
    private Integer value;          // Points operation value  
    private String description;      // Description  
    private String event;           // Event (e.g., sign-in, submission, redemption)  
    private Date createTime;        // Creation time  
}