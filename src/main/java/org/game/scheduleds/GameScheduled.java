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
    @Async
    @Scheduled(fixedRate = 90000)
//    @Scheduled(fixedRate = 10000)
    public void startGame(){
        BittingValue.initBittingValue(BittingValue.game,bettingService);
        Game game = new Game();
        BittingValue.game = gameService.initGame(game);
        log.error("STARTING! NEW GAME!");

    }

    @Async
    @Scheduled(initialDelay=10000, fixedRate=90000)
    public void startModel(){
        //投入自动押注的金额
        while (true){
            if(BittingValue.game.getReTime() > 20){
                bettingService.model();
                break;
            }else {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Async
    @Scheduled(initialDelay=80000, fixedRate=90000)
    public void endGame(){
        boolean flag = true;
        while (flag){
            Integer num = MD5.random.nextInt(28);
            if(BittingValue.ylMap.get(num)!=null && BittingValue.ylMap.get(num).compareTo(BittingValue.minRate)>=0){
                gameService.end(num);
                flag=false;
            }
        }
        bettingService.endModel();
        BittingValue.falg = false;
    }


}
