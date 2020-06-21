package entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 黑名单(Blacklist)实体类
 *
 * @author makejava
 * @since 2020-06-01 22:11:01
 */
@Setter
@Getter
public class Blacklist implements Serializable {
    private static final long serialVersionUID = -19686045866146412L;
    
    private Integer userId;
    
    private String userName;

    private String date;

    private Integer status;

}