package org.game.service;

import org.game.config.BittingValue;
import org.game.dao.GameDao;
import org.game.dao.UserDao;
import org.game.dao.YeBillDao;
import org.game.enums.Magnification;
import org.game.pojo.Game;
import org.game.pojo.User;
import org.game.pojo.YeBill;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class GameService {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private YeBillDao yeBillDao;

    @Transactional(rollbackFor = Exception.class)
    public synchronized Result betting(User user, Map<Integer, Integer> betMap){
        //该用户这一把的投注金额
        BigDecimal money = new BigDecimal(betMap.values().stream().mapToInt(c -> c).sum());
        //把押注的金额加入奖池
        BittingValue.game.setJackpot(BittingValue.game.getJackpot().add(money));
        //把投注金额加入到计算的map中
        BittingValue.betMap.put(user.getId(),betMap);

        //从用户余额中减去该用户这一把的投注金额，然后添加到账单。如果同一把该用户继续投注则也是加入到该账单中
        user.setMoney(user.getMoney().subtract(money));
        userDao.saveAndFlush(user);
        //创建账单
        YeBill yeBill = new YeBill(money , "第"+BittingValue.game.getId()+"期投注", user);
        yeBillDao.save(yeBill);

        return new Result(new LinkedHashMap<String,Object>(){{
            put("用户账单",yeBill);
            put("当前游戏信息",BittingValue.game);
            put("当前该押注",BittingValue.betMap.get(user.getId()));
        }});
    }

    public synchronized Result renewGame(User user){
        Map<Integer, Integer> betMap = BittingValue.betMap.get(user.getId());
        return this.betting(user,betMap);
    }

    public Game initGame(Game game){
        return gameDao.save(game);
    }

    public Result getGameInfo(){
        return new Result(new LinkedHashMap<String,Object>(){{
            put("当前游戏信息",BittingValue.game);
            put("最近十次游戏记录",gameDao.findTenGame());
        }});
    }

    public Result start(){
        Set<Integer> userIds = BittingValue.betMap.keySet();
        BigDecimal sumMoney = BittingValue.game.getJackpot();
        for(Integer integer : userIds){
            Map<Integer, Integer> betMap = BittingValue.betMap.get(integer);
            for(Integer num : betMap.keySet()){
                //累加每个数字投注的金额到jeMap中
                BittingValue.jeMap.put(num,betMap.get(num)+BittingValue.jeMap.get(num));
            }
        }

        //计算盈利率
        for(Integer num : BittingValue.jeMap.keySet()){
            Integer je = BittingValue.jeMap.get(num);
            BittingValue.ylMap.put(num,1d-(Magnification.getPlByNum(num) * je/Double.valueOf(sumMoney.toString())));
        }

        //抛弃不在范围内的盈利率map
        for(Integer num : BittingValue.ylMap.keySet()){
            if(BittingValue.ylMap.get(num)>BittingValue.minRate && BittingValue.ylMap.get(num)<BittingValue.maxRate){
                BittingValue.returnMap.put(num,BittingValue.ylMap.get(num));
            }
        }

        return new Result(new LinkedHashMap<String,Object>(){{
            put("计算后返回的集合",BittingValue.returnMap);
            put("当前游戏信息",BittingValue.game);
        }});
    }

    public Result end(Integer checkNum){
        Integer winNum = 0;
        Set<Integer> userIds = BittingValue.betMap.keySet();
        for(Integer userId : userIds){
            Map<Integer, Integer> betMap = BittingValue.betMap.get(userId);
            for(Integer num : betMap.keySet()){
                if(checkNum.equals(num)){
                    winNum++;
                    synchronized (this) {
                        //计算赢利，然后加入到数据库中
                        BigDecimal money = new BigDecimal(Magnification.getPlByNum(num) * betMap.get(num));
                        User user = userDao.findById(userId).get();
                        user.setMoney(user.getMoney().add(money));
                        userDao.saveAndFlush(user);

                        YeBill yeBill = new YeBill(money, "第" + BittingValue.game.getId() + "期投注获利", user);
                        yeBillDao.save(yeBill);
                    }
                }
                //累加每个数字投注的金额到jeMap中
                BittingValue.jeMap.put(num,betMap.get(num)+BittingValue.jeMap.get(num));
            }
        }
        BittingValue.game.setWinNum(winNum);
        BittingValue.falg = true;
        return new Result(BittingValue.game);
    }

    public void saveGame(){
        Runnable saveGame = (() -> {
            Boolean falg = true;
            while (falg){
                if(BittingValue.game.getReTime()==0){
                    BittingValue.game.setReTime(0);
                    gameDao.saveAndFlush(BittingValue.game);
                    falg = false;
                }else {
                    try {
                        Thread.sleep(500l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        saveGame.run();
    }



}
