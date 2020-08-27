package org.game.service;

import org.game.dao.BettingDao;
import org.game.pojo.Betting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-08-07  21:21
 */
@Service
public class BettingService {

    @Autowired
    private BettingDao bettingDao;

    public void saveAll(List<Betting> bettingList){
        bettingDao.saveAll(bettingList);
    }


}
