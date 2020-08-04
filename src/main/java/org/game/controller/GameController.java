package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.UserDao;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.GameService;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供玩游戏的接口
 */
@RestController
@RequestMapping(value = "game")
@Api(tags = {"游戏接口"})
@Slf4j
public class GameController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GameService gameService;

    @GetMapping("betting")
    @ApiOperation(value = "用户投注")
    public Result betting(HttpServletRequest req, @RequestParam(value = "nums")List<Integer> nums,@RequestParam(value = "counts")List<Integer> counts){
        if(nums.size()<=0 || counts.size() <=0 || nums.size()!=counts.size()) return new Result("投注数量为空！或者不一致！",null);
        Integer count = counts.stream().mapToInt(c -> c).sum();
        User user = UserUtil.getUserByReq(req, userDao);
        if(new BigDecimal(count).compareTo(user.getMoney()) > 0) return new Result("余额不足！",null);
        Map<Integer, Integer> betMap = new LinkedHashMap<>();
        for(int i =0;i<nums.size();i++){
            betMap.put(nums.get(i),counts.get(i));
        }
        return gameService.betting(user,betMap);
    }

    @GetMapping("startCom")
    @ApiOperation(value = "投注结束，后台开始计算")
    public Result startCom(){
        return gameService.start();
    }

    @GetMapping("getGameInfo")
    @ApiOperation(value = "获取游戏信息")
    public Result getGameInfo(){
        return gameService.getGameInfo();
    }

    @GetMapping("endCom")
    @ApiOperation(value = "游戏结束，返回选取的数字用于做用户结算")
    public Result endCom(Integer num){
        if(num == null) return new Result("num为空！",null);
        return gameService.end(num);
    }

    @GetMapping("renewGame")
    @ApiOperation(value = "用户按照上一把的续压")
    public Result renewGame(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        return gameService.renewGame(user);
    }

}
