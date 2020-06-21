package service.impl;

import entity.Book;
import dao.BookDao;
import service.BookService;
import org.springframework.stereotype.Service;
import vo.BookInfo;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Book)表服务实现类
 *
 * @author makejava
 * @since 2020-06-02 15:59:36
 */
@Service("bookService")
public class BookServiceImpl implements BookService {

    @Resource
    private BookDao bookDao;

    /**
     * 通过isbn查询单条数据
     *
     * @param isbn 主键
     * @return 实例对象
     */
    public Book queryById(Integer isbn) {
        return this.bookDao.queryById(isbn);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    public List<Book> queryAllByLimit(int offset, int limit) {
        return this.bookDao.queryAllByLimit(offset, limit);
    }

    /**
     * 条件查询
     *
     * @param bookInfo
     * @return List<Book>
     */
    public List<BookInfo> queryAll(BookInfo bookInfo) {
        List<BookInfo>books = this.bookDao.queryAll(bookInfo);
        return books;
    }


    /**
     * 新增数据
     *
     * @param book 实例对象
     * @return 实例对象
     */
    public Book insert(Book book) {
        this.bookDao.insert(book);
        return book;
    }

    /**
     * 修改数据
     *
     * @param book 实例对象
     * @return 实例对象
     */
    public Book update(Book book) {
        this.bookDao.update(book);
        return this.queryById(book.getIsbn());
    }

    /**
     * 通过主键删除数据
     *
     * @param isbn 主键
     * @return 是否成功
     */
    public boolean deleteById(Integer isbn) {
        return this.bookDao.deleteById(isbn) > 0;
    }
}