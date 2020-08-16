package org.game.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.*;
import org.game.pojo.UserInfo;
import org.game.enums.Magnification;
import org.game.pojo.Betting;
import org.game.pojo.Game;
import org.game.pojo.User;
import org.game.pojo.YeBill;
import org.game.result.Result;
import org.game.util.MD5;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
public class GameService {

    @Autowired
    private GameDao gameDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private YeBillDao yeBillDao;

    @Autowired
    private BettingDao bettingDao;

    @Autowired
    private UserInfoDao userInfoDao;

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
        yeBill.setGameId(BittingValue.game.getId());
        yeBill.setGameMoney(new BigDecimal(-1).multiply(money));
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
        this.startCom();
        return gameDao.save(game);
    }

    public Result getGameInfo(User user){
        List<Game> games = gameDao.findTenGame();
        //查询模拟用户数据
        List<Game> userGames = games;
        Game userGame = BittingValue.game;
        userGame.setJackpot(new BigDecimal(MD5.random.nextInt(200) + (MD5.random.nextInt(10) * userGame.getReTime()*MD5.random.nextInt(5))));

        for(Game game : userGames){
            List<UserInfo> userInfos = userInfoDao.findAllByGameIdOrderByHlDesc(game.getId());
            game.setWinNum(userInfos.size());
            game.setJackpot(new BigDecimal(userInfos.stream().mapToInt(UserInfo::getHl).sum()));
        }


        //设置中奖金额为用户中奖金额
        for(Game game :games){
            game.setWinMoney(yeBillDao.findYkByUserIdAAndGameId(user.getId(),game.getId()));
        }
        return new Result(new LinkedHashMap<String,Object>(){{
            put("当前游戏信息",BittingValue.game);
            put("当前游戏信息222",userGame);
            put("最近十次游戏记录",games);
            put("最近十次游戏记录222",userGames);
            if(BittingValue.betMap.get(user.getId())!=null){
                put("当前用户押注信息",JSON.toJSONString(BittingValue.betMap.get(user.getId())));
                put("当前用户投注额",((BittingValue.betMap.get(user.getId()).values()).stream().mapToInt(c -> c).sum()));
            }else{
                put("当前用户押注信息",null);
                put("当前用户投注额",null);
            }
            put("当前用户信息",user);
        }});
    }

    private void start(){
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

//            BittingValue.ylMap.put(num,1d-(Magnification.getPlByNum(num) * je/Double.valueOf(sumMoney.toString())));
            BittingValue.ylMap.put(num,Double.valueOf(sumMoney.toString())-(Magnification.getPlByNum(num) * BittingValue.jeMap.get(num)));
        }

        //抛弃不在范围内的盈利率map
        for(Integer num : BittingValue.ylMap.keySet()){
            if(BittingValue.ylMap.get(num)>=BittingValue.minRate
                /*&& BittingValue.ylMap.get(num)<BittingValue.maxRate*/
            ){
                BittingValue.returnMap.put(num,BittingValue.ylMap.get(num));
            }
        }

        log.error("[GAME] "+BittingValue.game.toString());
        log.error("[ylMap] "+BittingValue.ylMap.toString());
        log.error("[jeMap] "+BittingValue.jeMap.toString());
        log.error("[betMap] "+BittingValue.betMap.toString());
        log.error("[returnMap] "+BittingValue.returnMap.toString());
    }

    public Result end(Integer checkNum){
        log.error("END!!!  "+checkNum);
        log.error("【GAMEID】  "+BittingValue.game.getId());

        Game endGame = BittingValue.game;
        endGame.setWinMoney(new BigDecimal(BittingValue.ylMap.get(checkNum)));
        BittingValue.falg = true;
        Set<Integer> userIds = BittingValue.betMap.keySet();

        List<UserInfo> winList = UserUtil.init(checkNum,endGame.getId());

        Integer winNum = 0; //游戏赢的人数
        for(Integer userId : userIds){
            UserInfo userInfo = new UserInfo();
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

                        YeBill yeBill = new YeBill(money, "第" + endGame.getId() + "期投注获利", user);
                        yeBill.setGameId(endGame.getId());
                        yeBill.setGameMoney(money);
                        yeBillDao.save(yeBill);

                        userInfo.setUserName(user.getUserName());
                        userInfo.setHeadImg(user.getHeadImg());
                        userInfo.setGameId(endGame.getId());
                        userInfo.setNum(checkNum);
                        userInfo.setTz(betMap.get(num));
                        userInfo.setHl(Integer.parseInt(money.toString()));
                    }
                }

                //累加每个数字投注的金额到jeMap中
                BittingValue.jeMap.put(num,betMap.get(num)+BittingValue.jeMap.get(num));
            }
            winList.add(userInfo);
        }

        endGame.setWinNum(winNum);

        //更新结束游戏的数据
        endGame.setNumber(checkNum);
        while (endGame.getReTime()>0){
            try {
                Thread.sleep(10L);
                log.error("WAIT 10ml");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        endGame.setReTime(BittingValue.game.getReTime());
        gameDao.saveAndFlush(endGame);
        userInfoDao.saveAll(winList);
        return new Result(BittingValue.game);
    }

    public Result findGameState(Integer gameId){
        Game game = gameDao.findById(gameId).get();
        if(game.getReTime()<=0 && !game.getId().equals(BittingValue.game.getId())) return new Result(false);   //gameId这把游戏结束了
        else return new Result(true);   //gameId这把游戏还没有结束
    }

    public Result findBettingValue(Integer gameId,Integer userId,Integer number) {
        Betting betting = bettingDao.findFirstByUserIdAndGameId(userId,gameId);
        if(betting!=null) {
            Map<Integer, Integer> betMap = (Map<Integer, Integer>) JSON.parse(betting.getBettingMap());
            LinkedHashMap<String, Object> returnMap = new LinkedHashMap<>();
            returnMap.put("押注信息", betting.getBettingMap());
            if(betMap.get(number)!=null) {
                returnMap.put("投注号码盈利", Magnification.getPlByNum(number) * betMap.get(number));
            }
            return new Result(returnMap);
        }else return new Result(null);
    }

    //开始计算
    public void startCom(){
        Runnable startCom = (() -> {
            Boolean falg = true;
            while (falg){
                if(BittingValue.game!=null && BittingValue.game.getReTime()<15 && BittingValue.game.getReTime()>10){
                    this.start();
                    falg = false;
                }else {
//                    log.error("sleep 1 M");
//                    log.error(BittingValue.game.toString());
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        BittingValue.executorService.submit(startCom);
    }



}
