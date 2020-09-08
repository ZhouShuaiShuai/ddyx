package org.game.service;

import com.alibaba.fastjson.JSON;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.BettingDao;
import org.game.dao.BettingModelDao;
import org.game.dao.GameDao;
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
import java.util.Optional;

/**
 * @author Zhouyf
 * @Data 2020-08-07  21:21
 */
@Service
@Slf4j
public class BettingService {

    @Autowired
    private BettingDao bettingDao;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private GameService gameService;

    @Autowired
    private BettingModelDao bettingModelDao;

    @Autowired
    private UserDao userDao;

    public void saveAll(List<Betting> bettingList){
        bettingDao.saveAll(bettingList);
    }

    //没把结束的时候重置投注模式
    public void endModel(){
        Map<Integer,Map<String, Integer>> modelCon = BittingValue.modelCon;

        for(Integer userId : modelCon.keySet()){
            Map<String,Integer> con = modelCon.get(userId);
            if(con.isEmpty()){
                log.error("当前用户没有自动押注配置信息！ userId :"+ userId);
                BittingValue.modelCon.remove(userId);
                BittingValue.bettingModelList.remove(userId);
            }
            User user = userDao.findById(userId).get();
            if(con.get("startGameId") == -1){
                if(con.get("max") == null || user.getMoney().compareTo(new BigDecimal(con.get("max")))<0){
                    if(con.get("min") == null || user.getMoney().compareTo(new BigDecimal(con.get("min")))>0){
                        Integer money = gameDao.getLastGameMoney(userId);
//                    BettingModel bettingModel = BittingValue.bettingModelList.get(userId);
                        System.out.println("con : "+con);
                        BettingModel bettingModel = bettingModelDao.findById(con.get("startModelId")).get();
                        if(money==null){
                            return;
                        }else if(money>0){
                            Optional<BettingModel> model = bettingModelDao.findById(bettingModel.getWinModelId());
                            if(model!=null&&model.isPresent()){
                                Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
                                    put("max", con.get("max"));
                                    put("min", con.get("min"));
                                    put("num", con.get("num"));
                                    put("startGameId", -1);
                                    put("startModelId", model.get().getId());
                                }};
                                BittingValue.modelCon.put(userId,map);
                                return;
                            }else {
                                BittingValue.modelCon.remove(userId);
                                BittingValue.bettingModelList.remove(userId);
                            }
                        } else {
                            Optional<BettingModel> model = bettingModelDao.findById(bettingModel.getLoserModelId());
                            if(model!=null&& model.isPresent()){
                                Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
                                    put("max", con.get("max"));
                                    put("min", con.get("min"));
                                    put("num", con.get("num"));
                                    put("startGameId", -1);
                                    put("startModelId", model.get().getId());
                                }};
                                BittingValue.modelCon.put(userId,map);
                                return;
                            }else {
                                BittingValue.modelCon.remove(userId);
                                BittingValue.bettingModelList.remove(userId);
                            }
                        }
                    }
                    BittingValue.modelCon.remove(userId);
                }
            }
        }
    }

    public void model(){

        Map<Integer,Map<String, Integer>> modelCon = BittingValue.modelCon;
        if(modelCon==null || modelCon.isEmpty()){
            return;
        }
        for(Integer userId : modelCon.keySet()){
            Map<String,Integer> con = modelCon.get(userId);

            Integer startGameId = con.get("startGameId");

            Integer startModelId = con.get("startModelId");


            System.out.println("model con : "+con);
            if(startGameId.equals(BittingValue.game.getId()) || startGameId == -1){
                BettingModel bettingModel = bettingModelDao.findById(startModelId).get();
                BittingValue.bettingModelList.put(userId,bettingModel);
            }

            if(startGameId < BittingValue.game.getId() &&  startGameId != -1){
                BittingValue.modelCon.remove(userId);
            }
        }


        if(BittingValue.bettingModelList==null || BittingValue.bettingModelList.isEmpty()){
            return;
        }

        for(Integer userId : BittingValue.bettingModelList.keySet()){
            Map<String,Integer> con = modelCon.get(userId);

            if(con.isEmpty()){
                log.error("当前用户没有自动押注配置信息！ userId :"+ userId);
                BittingValue.modelCon.remove(userId);
                BittingValue.bettingModelList.remove(userId);
            }

            User user = userDao.findById(userId).get();
            if(con.get("max") == null || user.getMoney().compareTo(new BigDecimal(con.get("max")))<0){
                if(con.get("min") == null || user.getMoney().compareTo(new BigDecimal(con.get("min")))>0){

                    Integer num = con.get("num");
                    if(num>0) {
                        //投注
                        Map<Integer, Integer> betMap = (Map<Integer, Integer>) JSON.parse(BittingValue.bettingModelList.get(userId).getBettingMap());
                        Integer count = betMap.values().stream().mapToInt(c -> c).sum();
                        if (new BigDecimal(count).compareTo(user.getMoney()) < 0) {
                            System.out.println("UserId : "+ user.getId() +"   betMap : "+betMap);
                            gameService.betting(user, betMap);
                            Integer num2 = num-1;
                            Map<String, Integer> map = new LinkedHashMap<String, Integer>() {{
                                put("max", con.get("max"));
                                put("min", con.get("min"));
                                put("num", num2);
                                put("startGameId", -1);
                                put("startModelId", con.get("startModelId"));
                            }};
                            BittingValue.modelCon.put(userId,map);
                            return;
                        }
                    }
                }
            }
            BittingValue.modelCon.remove(userId);
        }

    }

}
