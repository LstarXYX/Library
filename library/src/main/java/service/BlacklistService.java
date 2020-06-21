package service;

import entity.Blacklist;
import java.util.List;

/**
 * 黑名单(Blacklist)表服务接口
 *
 * @author makejava
 * @since 2020-06-01 22:11:01
 */
public interface BlacklistService {

    /**
     * 通过ID查询单条数据
     *
     * @param  userid
     * @return 实例对象
     */
    Blacklist queryById(Integer userid);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<Blacklist> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据然后返回新的blacklist对象
     *
     * @param blacklist 实例对象
     * @return 实例对象
     */
    Blacklist insert(Blacklist blacklist);


    /**
     * 通过主键删除数据
     *
     * @param  userid
     * @return 是否成功
     */
    boolean deleteById(Integer userid);

    /**
     * 查询所有黑名单用户
     *
     * @return
     */
    List<Blacklist> queryAll(Blacklist blacklist);


}