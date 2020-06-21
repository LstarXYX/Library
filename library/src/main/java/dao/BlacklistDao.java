package dao;

import entity.Blacklist;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 黑名单(Blacklist)表数据库访问层
 *
 * @author makejava
 * @since 2020-06-01 22:11:01
 */
@Repository("blacklistDao")
public interface BlacklistDao {

    /**
     * 通过ID查询单条数据
     *
     * @param  userid
     * @return 实例对象
     */
    Blacklist queryById(Integer userid);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Blacklist> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param blacklist 实例对象
     * @return 对象列表
     */
    List<Blacklist> queryAll(Blacklist blacklist);

    /**
     * 新增数据
     *
     * @param blacklist 实例对象
     * @return 影响行数
     */
    int insert(Blacklist blacklist);


    /**
     * 通过主键删除数据
     *
     * @param  userid
     * @return 影响行数
     */
    int deleteById(Integer userid);

}