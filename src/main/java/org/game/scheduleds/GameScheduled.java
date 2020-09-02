package org.game.scheduleds;

import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.pojo.Game;
import org.game.pojo.UserInfo;
import org.game.service.BettingService;
import org.game.service.GameService;
import org.game.util.MD5;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 游戏定时任务
 */
@Component
@Slf4j
public class GameScheduled {

    @Autowired
    private GameService gameService;

    @Autowired
    private BettingService bettingService;


    /**
     * fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
     * fixedDelay 表示任务执行之间的时间间隔，具体是指两次任务的结束时间间隔。
     */
//    @Scheduled(fixedDelay = 90000)
    @Async
    @Scheduled(fixedRate = 90000)
//    @Scheduled(fixedRate = 10000)
    public void startGame(){
//        while (!BittingValue.falg){
//            //如果90秒过了 还没有请求结束接口就自动结束，选择一个赢率最大的
//            Integer num = MD5.random.nextInt(28);
//            if(BittingValue.ylMap.get(num).compareTo(BittingValue.minRate)>=0){
//                gameService.end(num);
//                break;
//            }
//        }
//        BittingValue.falg = false;
        BittingValue.initBittingValue(BittingValue.game,bettingService);
        Game game = new Game();
        BittingValue.game = gameService.initGame(game);
        log.error("STARTING! NEW GAME!");
        //投入自动押注的金额
        bettingService.model();

//        gameService.saveGame();
    }



    @Async
    @Scheduled(initialDelay=80000, fixedRate=90000)
    public void endGame(){
//        while (!BittingValue.falg){
        while (true){
            Integer num = MD5.random.nextInt(28);
            if(BittingValue.ylMap.get(num)!=null && BittingValue.ylMap.get(num).compareTo(BittingValue.minRate)>=0){
                gameService.end(num);
                break;
            }
        }
            //如果90秒过了 还没有请求结束接口就自动结束，选择一个赢率最大的
//        }
        bettingService.endModel();
        BittingValue.falg = false;
    }


}
