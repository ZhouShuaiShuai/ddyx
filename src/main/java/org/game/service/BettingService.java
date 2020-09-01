package org.game.service;

import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.BettingDao;
import org.game.dao.UserDao;
import org.game.pojo.Betting;
import org.game.pojo.BettingModel;
import org.game.pojo.User;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-08-07  21:21
 */
@Service
@Slf4j
public class BettingService {

    @Autowired
    private BettingDao bettingDao;

    private GameService gameService;

    @Autowired
    private UserDao userDao;

    public void saveAll(List<Betting> bettingList){
        bettingDao.saveAll(bettingList);
    }

    public void model(){
        Map<Integer, BettingModel> bettingModelMap = BittingValue.bettingModelList;
        Map<Integer,Map<String, Integer>> modelCon = BittingValue.modelCon;
        if(bettingModelMap.isEmpty()){
            return;
        }
        for(Integer userId : bettingModelMap.keySet()){
            Map<String,Integer> con = modelCon.get(userId);
            if(con.isEmpty()){
                log.error("当前用户没有自动押注配置信息！ userId :"+ userId);
                BittingValue.modelCon.remove(userId);
                BittingValue.bettingModelList.remove(userId);
            }

            User user = userDao.getOne(userId);
            if(con.get("max") == null || user.getMoney().compareTo(new BigDecimal(con.get("max")))<0){
                if(con.get("min") == null || user.getMoney().compareTo(new BigDecimal(con.get("min")))>0){

                    Integer num = con.get("num");
                    if(num>0) {
                        //投注
                        Map<Integer, Integer> betMap = (Map<Integer, Integer>) JSON.parse(bettingModelMap.get(userId).getBettingMap());
                        Integer count = betMap.values().stream().mapToInt(c -> c).sum();
                        if (new BigDecimal(count).compareTo(user.getMoney()) < 0) {
                            gameService.betting(user, betMap);
                            Integer num2 = num-1;
                            Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
                                put("max", con.get("max"));
                                put("min", con.get("min"));
                                put("num", num2);
                            }};
                            return;
                        }
                    }
                }
            }
            BittingValue.modelCon.remove(userId);
            BittingValue.bettingModelList.remove(userId);
        }

    }

}
