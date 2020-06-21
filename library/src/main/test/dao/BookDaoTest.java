package dao;

import entity.Book;
import entity.UserInfo;
import entity.UserLendInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.UserInfoService;
import vo.BookInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author L.star
 * @date 2020/6/2 19:22
 */
public class BookDaoTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp()throws IOException {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:springConfig/applicationContext.xml");
        sqlSessionFactory =(SqlSessionFactory) ac.getBean("sqlSessionFactory");
    }

    private static Date randomDate(String beginDate,String endDate){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date start = format.parse(beginDate);
            Date end = format.parse(endDate);

            if(start.getTime() >= end.getTime()){
                return null;
            }
            long date = random(start.getTime(),end.getTime());
            return new Date(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }


    @Test
    public void queryAll() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDao bookDao = sqlSession.getMapper(BookDao.class);

        //查询条件
        BookInfo book = new BookInfo();
        book.setPrice(33.6f);

        //查询
        List<BookInfo>books = bookDao.queryAll(book);

        for (BookInfo b : books){
            System.out.println(b);
            System.out.println(b.getCategoryName());
            System.out.println(b.getCategoryId());
        }


    }

    @Test
    public void queryById(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDao bookDao = sqlSession.getMapper(BookDao.class);
        Book book  = bookDao.queryById(1000);
        System.out.println(book);
    }

    @Test
    public void updatebook(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDao bookDao = sqlSession.getMapper(BookDao.class);
        Book book  = bookDao.queryById(1000);
        book.setPrice(333f);
        bookDao.update(book);
        book = bookDao.queryById(book.getIsbn());
        System.out.println(book);
    }

    @Test
    public void updatebook2(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        BookDao bookDao = sqlSession.getMapper(BookDao.class);
        Integer id = 1501;
        Integer categoryId;
        Integer bookNum;
        for (id=1501;id<=2000;id++){
            categoryId =(int)(Math.random()*16+1);
            bookNum =(int)(Math.random()*20+1);
            Book book  = bookDao.queryById(id);
            book.setCategoryId(categoryId);
            book.setPublishDate(randomDate("2018-01-01","2019-01-31"));
            book.setBookNum(bookNum);
            bookDao.update(book);
            System.out.println(id+"已完成");
        }
    }


    @Test
    public void queryLend(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserLendInfoDao userLendInfoDao = sqlSession.getMapper(UserLendInfoDao.class);
        List<UserLendInfo> list = userLendInfoDao.queryById(111);
        System.out.println(list);

    }

    @Test
    public void insertUser(){
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserInfoDao userInfoDao = sqlSession.getMapper(UserInfoDao.class);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName("111");
        userInfo.setPassword("123123");
        userInfo.setRegisterDate(LocalDate.now().toString());
        userInfoDao.insert(userInfo);
        userInfo = userInfoDao.queryByName(userInfo.getUserName());
        System.out.println(userInfo.toString());

    }

}