package test;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import bean.*;

/**
 * 一对多关系映射的CRUD
 * @author Lstar
 * @version 1.0
 * @date 2020/4/21 17:55
 */
public class testLinkman {
    /**
     * 保存操作
     * 创建一个新的联系人，需要关联一个客户
     */
    @Test
    public void test1(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        //查询一个客户
        CustomerEntity c1 = s.get(CustomerEntity.class,96L);
        //创建一个新的联系人
        LinkmanEntity l = new LinkmanEntity();
        l.setLkmName("一对多的联系人");
        //创建客户和联系人的关系 让联系人知道属于哪个客户
        l.setCustomer(c1);
        //保存联系人
        s.save(l);

        tx.commit();

    }

    /**
     * 特殊情况
     *  创建一个客户和一个联系人
     *  建立客户和联系人的双向关联关系
     *  使用符合原则的保存： 先保存主表实体，再保存从表实体
     *      此时保存会有问题
     *           保存了两个实体 理应只有两条insert语句
     *           而执行结果多了一条update语句    因为快照更新
     *      解决办法：
     *          让客户执行操作的时候，放弃维护关联关系的权利
     *          配置的方式：
     *              在Customer的映射配置文件中的set标签上使用inverse属性
     *               inverse含义：是否放弃维护关联关系的权利
     *                       true：放弃
     *                       false：不放弃（默认）
     */
    @Test
    public void test2(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        //创建一个客户
        CustomerEntity c1 =new CustomerEntity();
        c1.setCustName("一对多的客户2");
        //创建一个新的联系人
        LinkmanEntity l = new LinkmanEntity();
        l.setLkmName("一对多的联系人2");
        //创建客户和联系人的关系 双向
        l.setCustomer(c1);
        c1.getLinkman().add(l);
        //保存联系人
        s.save(c1);//持久态 有一级缓存和快照
        s.save(l);//持久态 有一级缓存和快照
        tx.commit();

    }


    /**
     * 级联保存
     * 配置方式 找到customer的映射配置文件set标签
     *     加入cascade属性
     *          cascade：配置级联操作
     *              级联保存更新的取值： save-update
     *
     */

    @Test
    public void test3(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        //创建一个客户
        CustomerEntity c1 =new CustomerEntity();
        c1.setCustName("级联保存");
        //创建一个新的联系人
        LinkmanEntity l = new LinkmanEntity();
        l.setLkmName("级联保存");
        //创建客户和联系人的关系 双向
        l.setCustomer(c1);
        c1.getLinkman().add(l);
        //保存联系人
        s.save(c1);//只保存一个就可 级联保存
        tx.commit();
    }

    /**
     * 更新操作
     *     创建一个新的联系人，查询一个已有客户
     *     联系人新联系人和已有客户的双向关联关系
     *     更新客户
     */
    @Test
    public void test4(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        //查询一个客户
        CustomerEntity c1 = s.get(CustomerEntity.class,96L);
        //创建一个新的联系人
        LinkmanEntity l = new LinkmanEntity();//瞬时
        l.setLkmName("级联更新联系人");
        //创建客户和联系人的关系（双向）
        l.setCustomer(c1);
        c1.getLinkman().add(l);
        //更新客户
        s.update(c1);

        tx.commit();

    }

    /**
     * 删除操作
     *     删除从表数据就是删除单表
     *     删除主表数据：
     *          看有没有从表数据引用
     *              有引用: 在删除时，hibernate会把从表中的外键字段设置为null，再删除
     *                      如果外键字段有非空约束，则hibernate不能更新外键字段为null
     *                      想删除，需要用级联删除  要配置inverse要为true
     *              没有引用：就是单表，直接删
     * 级联删除使用要慎重！！！
     */
    @Test
    public void test5(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        //查询一个客户
        CustomerEntity c1 = s.get(CustomerEntity.class,98L);
        //删除id为98的客户
        s.delete(c1);
        tx.commit();
    }

    /**
     * 对象导航查询
     *      当两个实体之间有关联关系时（四种任意一种）
     *      通过调用getXXX方法即可实现查询功能(有hibernate提供）
     *一对多时根据一的一方查询多的一方时，需要使用延迟加载（默认配置即可）
     * 多对一时，根据多的一方查询一的一方时，不需要使用延迟加载，使用立即加载，需要配置
     *          例找到联系人的映射配置文件： 在many to one 标签上 使用lazy属性
     *              取值有： false：使用立即加载
     *                       proxy：是看load方法是延迟加载还是立即加载
     *
     */
    @Test
    public void test6(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        CustomerEntity c = s.get(CustomerEntity.class,96L);
        System.out.println(c);
        System.out.println(c.getLinkman());

        tx.commit();
    }

    /**
     * load加载改为立即加载
     *      找到查询实体的映射配置文件，它的class标签上有lazy属性：是否延迟加载
     *
     */
    @Test
    public void test7(){
        Session s = hibernateUtil.getCurrentSession();
        Transaction tx = s.beginTransaction();
        CustomerEntity c = s.load(CustomerEntity.class,96L);
        System.out.println(c);
        System.out.println(c.getLinkman());
        tx.commit();


    }




}
