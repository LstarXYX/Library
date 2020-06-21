package test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class hibernateUtil {
    public static SessionFactory sessionFactory;
    static {
        Configuration configuration = new Configuration();
        configuration.configure();
         sessionFactory = configuration.buildSessionFactory();
    }
    public  static Session openSession()
    {
        return sessionFactory.openSession();
    }

    /**
     * 从当前线程获取Session对象
     * 绑定了之后 hibernate会在提交或回滚事务之后自动关闭session
     * @return
     */
    public static Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
        //只有配置了线程和session绑定才能用
    }

    public static void main(String[] args) {
        Session s = getCurrentSession();
    }

}
