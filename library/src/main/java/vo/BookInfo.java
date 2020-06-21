package vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import entity.Book;
import entity.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author L.star
 * @date 2020/6/12 15:05
 */
@Getter
@Setter
public class BookInfo implements Serializable  {
    private Integer isbn;
    /**
     * 书名
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 出版年月
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date publishDate;
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
     * 分类名
     */
    private String categoryName;

}
