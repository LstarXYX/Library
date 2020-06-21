package service;

import entity.Book;
import vo.BookInfo;

import java.util.List;

/**
 * (Book)表服务接口
 *
 * @author makejava
 * @since 2020-06-02 15:59:36
 */
public interface BookService {

    /**
     * 通过ID查询单条数据
     *
     * @param isbn 主键
     * @return 实例对象
     */
    Book queryById(Integer isbn);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<Book> queryAllByLimit(int offset, int limit);

    /**
     * 根据查询条件查询
     *
     * @param book
     * @return
     */
    List<BookInfo> queryAll(BookInfo book);


    /**
     * 新增数据
     *
     * @param book 实例对象
     * @return 实例对象
     */
    Book insert(Book book);

    /**
     * 修改数据
     *
     * @param book 实例对象
     * @return 实例对象
     */
    Book update(Book book);

    /**
     * 通过主键删除数据
     *
     * @param isbn 主键
     * @return 是否成功
     */
    boolean deleteById(Integer isbn);

}