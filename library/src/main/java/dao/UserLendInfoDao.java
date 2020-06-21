package dao;

import entity.UserLendInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户借书信息(UserLendInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-03 20:13:04
 */
public interface UserLendInfoDao {

    /**
     * 通过ID查询该id的借书信息
     *
     * @param userId
     * @return 实例对象
     */
    List<UserLendInfo> queryById(Integer userId);

    /**
     * 根据id或isbn查询
     *
     * @param userLendInfo
     * @return
     */
    List<UserLendInfo> queryByIdorISBN(UserLendInfo userLendInfo);

    /**
     * 查询单个信息
     *
     * @param userId
     * @param isbn
     * @return
     */
    UserLendInfo selectOne(@Param("userId") Integer userId, @Param("isbn") Integer isbn);


    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<UserLendInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 管理员查询所有借书信息
     *
     * @return 对象列表
     */
    List<UserLendInfo> queryAll();

    /**
     * 新增数据
     *
     * @param userLendInfo 实例对象
     * @return 影响行数
     */
    int insert(UserLendInfo userLendInfo);

    /**
     * 修改数据
     *
     * @param userLendInfo 实例对象
     * @return 影响行数
     */
    int update(UserLendInfo userLendInfo);

    /**
     * 通过主键删除数据
     *
     * @param userId
     * @return 影响行数
     */
    int deleteById(Integer userId);

}