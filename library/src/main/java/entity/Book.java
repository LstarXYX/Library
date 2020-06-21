package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * (Book)实体类
 *
 * @author makejava
 * @since 2020-06-02 15:59:36
 */
@Getter
@Setter
@ToString
public class Book implements Serializable {
    private static final long serialVersionUID = 177363001224492997L;

    private Integer isbn;
    /**
     * 书名
     */
    @NotNull
    private String title;
    /**
     * 作者
     */
    @NotNull
    private String author;
    /**
     * 出版年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Object publishDate;
    /**
     * 分类id
     */
    private Integer categoryId;

    private Float price;
    /**
     * 主题词
     */
    private String themeWord;
    /**
     * 关键词
     */
    private String keyWord;
    /**
     * 库存
     */
    private Integer bookNum;
    /**
     * 图片路径
     */
    private String imagePath;


    /**
     * 对应的分类
     */
    private Category category;


    /**
     * 文件上传
     */
    private MultipartFile file;


    /**
     * 返回状态码
     */
    private int status;


    public Book() {
    }
}