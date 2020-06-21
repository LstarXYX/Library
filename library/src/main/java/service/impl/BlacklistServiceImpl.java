package service.impl;

import dao.BlacklistDao;
import entity.Blacklist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.BlacklistService;

import java.util.List;

/**
 * @author L.star
 * @date 2020/6/1 22:22
 */
@Service("blacklistService")
public class BlacklistServiceImpl implements BlacklistService {

    @Autowired
    private BlacklistDao blacklistDao;

    public Blacklist queryById(Integer userid) {
        return blacklistDao.queryById(userid);
    }

    public List<Blacklist> queryAllByLimit(int offset, int limit) {
        return null;
    }

    public Blacklist insert(Blacklist blacklist) {
        blacklistDao.insert(blacklist);
        blacklist = blacklistDao.queryById(blacklist.getUserId());
        return blacklist;
    }

    public boolean deleteById(Integer userid) {
        blacklistDao.deleteById(userid);
        return true;
    }

    public List<Blacklist> queryAll(Blacklist blacklist) {
        return blacklistDao.queryAll(blacklist);
    }



}
