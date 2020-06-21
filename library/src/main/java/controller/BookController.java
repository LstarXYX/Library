package controller;

import entity.Book;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import service.BookService;
import org.springframework.web.bind.annotation.*;
import status.ReturnStatus;
import vo.BookInfo;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * (Book)表控制层
 *
 * @author makejava
 * @since 2020-06-02 15:59:36
 */
@CrossOrigin
@Controller
@RequestMapping("/book")
public class BookController {
    /**
     * 服务对象
     */
    @Resource
    private BookService bookService;

    /**
     * 不同的条件查询
     *
     * @param book
     * @return List<Book>
     */
    //todo:@RequestBody
    @RequestMapping("/queryall")
    @ResponseBody
    public List<BookInfo> selectOne(BookInfo book) {
        return this.bookService.queryAll(book);
    }


    /**
     * 添加书本 可以上传图片文件保存到服务器
     * ISBN和imagepath不能有值
     * 书名和作者不能为空
     *
     * @param book
     * @return
     * @throws IOException
     */
    @RequestMapping("/addBook")
    @ResponseBody
    public Book insertBook(@Validated Book book) throws IOException {
        if (book.getTitle() == null || book.getAuthor() == null) {
//            book = new Book();
            book.setStatus(ReturnStatus.ERROR.getStatusCode());
            return book;
        }
        //保存数据库的路径
        String imagePath = null;
        //文件保存本地的路径
//        String localPath = "E:\\ideaProjects\\library\\src\\main\\webapp\\images\\";
        //todo:更改路径
        String localPath = "C:\\src\\tomcat9\\webapps\\ROOT\\images\\";
        //文件名
        String filename = null;
        //如果带有文件
        if (!(book.getFile() == null)) {
            //书名+UUID作为文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型
            String contentType = book.getFile().getContentType();
            //如果不是图片就不上传
            //获得文件后缀
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //判断类型是否图片
            if ( ("JPG".equals(suffixName.toUpperCase())) || ("PNG".equals(suffixName.toUpperCase())) || ("JPEG".equals(suffixName.toUpperCase()))) {
                //文件名
                filename = book.getTitle() + "_" + uuid + "." + suffixName;
                //文件保存路径
                book.getFile().transferTo(new File(localPath + filename));
                //todo:改ip地址
                //http://39.108.94.114/images/
                imagePath = "http://39.108.94.114/images/" + filename;
                book.setImagePath(imagePath);
            } else {
                System.out.println("文件类型不支持");
                book.setStatus(ReturnStatus.ERRORTYPE.getStatusCode());
                return book;
            }
        }
        bookService.insert(book);
        book.setFile(null);
        book.setStatus(ReturnStatus.NORMAL.getStatusCode());
        return book;
    }


    /**
     * 删除书籍 把库存设为0
     */
    @RequestMapping("/delbook")
    @ResponseBody
    public String delBook(Integer isbn) {
        boolean check = bookService.deleteById(isbn);
        if (check) {
            return ReturnStatus.NORMAL.getResultStatus();
        }
        return ReturnStatus.ERROR.getResultStatus();
    }


    /**
     * 更新书本信息
     *
     * @param book
     * @return
     * @throws IOException
     */
    @RequestMapping("/updateBook")
    @ResponseBody
    public Book updateBook(@Validated Book book) throws IOException {
        if (book.getIsbn() == null) {
//            book = new Book();
            book.setStatus(ReturnStatus.ERROR.getStatusCode());
            book.setFile(null);
            return book;
        }
        //如果带有文件
        if (!(book.getFile() == null)) {
            //保存数据库的路径
            String imagePath = null;
            //文件保存本地的路径
            //todo:改路径
//            String localPath = "E:\\ideaProjects\\library\\src\\main\\webapp\\images\\";
            String localPath = "C:\\src\\tomcat9\\webapps\\ROOT\\images\\";
            //文件名
            String filename = null;
            //书名+UUID作为文件名
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //获得文件类型
            String contentType = book.getFile().getContentType();
            //如果不是图片就不上传
            //获得文件后缀
            String suffixName = contentType.substring(contentType.indexOf("/") + 1);
            //判断类型是否图片
            if (("JPG".equals(suffixName.toUpperCase())) || ("PNG".equals(suffixName.toUpperCase())) || ("JPEG".equals(suffixName.toUpperCase()))) {
                //文件名
                filename = book.getTitle() + "_" + uuid + "." + suffixName;
                //文件保存路径
                book.getFile().transferTo(new File(localPath + filename));
                //todo:更改路径
                imagePath = "http://39.108.94.114/images/" + filename;
                book.setImagePath(imagePath);

                Book old_book = bookService.queryById(book.getIsbn());
                String old_imagePath = old_book.getImagePath();
                //是否有图片
                if (old_imagePath != null) {
                    //获取文件名
                    String old_fileName = old_imagePath.substring(old_imagePath.lastIndexOf("/") + 1);
                    File old_file = new File(localPath, old_fileName);
                    //如果文件存在
                    if (old_file.exists()) {
                        old_file.delete();
                    }
                }
            }else if ("octet-stream".equals(suffixName)){
              //不改图片 直接更新书本
                book.setFile(null);
                bookService.update(book);
                book.setStatus(ReturnStatus.NORMAL.getStatusCode());
                return book;
            } else {
                book.setStatus(ReturnStatus.ERRORTYPE.getStatusCode());
                return book;
            }
        }
        book.setFile(null);
        bookService.update(book);
        book.setStatus(ReturnStatus.NORMAL.getStatusCode());
        return book;
    }


    @RequestMapping(value = "/queryAllByLimit")
    @ResponseBody
    public List<Book> queryByPage(int offset,int limit){
        return bookService.queryAllByLimit(offset,limit);
    }


}