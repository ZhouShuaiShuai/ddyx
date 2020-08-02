package org.game.scheduleds;

import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.pojo.Game;
import org.game.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 游戏定时任务
 */
@Component
@Slf4j
public class GameScheduled {

    @Autowired
    private GameService gameService;


    /**
     * fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
     * fixedDelay 表示任务执行之间的时间间隔，具体是指两次任务的结束时间间隔。
     */
    @Scheduled(fixedDelay = 90000)
    public void startGame(){
        while (!BittingValue.falg){
            try {
                Thread.sleep(1000l);
                log.error("WAIT 1000ml");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        BittingValue.falg = false;
        BittingValue.initBittingValue();
        BittingValue.game = gameService.initGame(new Game());
        log.error("STARTING! NEW GAME!");
        log.error(BittingValue.game.toString());

        gameService.saveGame();
    }

    @Scheduled(fixedRate = 10000)
    public void getGame(){
        log.error("##########################################");
        log.error("[game]");
        log.error(BittingValue.game.toString());
        log.error("******************************************");
        log.error("[betMap]");
        log.error(BittingValue.betMap.toString());
        log.error("******************************************");
        log.error("[ylMap]");
        log.error(BittingValue.ylMap.toString());
        log.error("******************************************");
        log.error("[jeMap]");
        log.error(BittingValue.jeMap.toString());
        log.error("******************************************");
        log.error("[returnMap]");
        log.error(BittingValue.returnMap.toString());
        log.error("##########################################");
    }

}
