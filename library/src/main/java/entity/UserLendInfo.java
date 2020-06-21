package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.io.Serializable;

/**
 * 用户借书信息(UserLendInfo)实体类
 *
 * @author makejava
 * @since 2020-06-03 20:13:04
 */
@Getter
@Setter
@ToString
public class UserLendInfo implements Serializable {
    private static final long serialVersionUID = -16689132440779548L;
    
    private Integer userId;
    /**
    * 借出日期
    */
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date lendDay;
    /**
    * 还书日期
    */
    @DateTimeFormat(pattern ="yyyy-MM-dd" )
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date returnDay;
    /**
    * 状态
    */
    private String status;
    /**
    * 借的书名
    */
    private String lendBookName;
    /**
    * ISBN号
    */
    private Integer isbn;



}