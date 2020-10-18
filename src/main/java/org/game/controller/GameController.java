package org.game.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.UserDao;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.GameService;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 提供玩游戏的接口
 */
@RestController
@RequestMapping(value = "game")
@Api(tags = {"游戏接口"})
@Slf4j
@EnableAsync
public class GameController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private GameService gameService;

    @GetMapping("find100GameNum")
    @ApiOperation(value = "进100期开奖记录")
    public Result find100GameNum(){
//        if(gameCount==null || gameCount == 0 ){
//            gameCount = 200;
//        }
        return gameService.find100GameNum();
    }

    @GetMapping("findNumJg")
    @ApiOperation(value = "获取游戏间隔")
    public Result findNumJg(){
        return gameService.findNumJg();
    }

    @GetMapping("getBeetingGame")
    @ApiOperation(value = "获取预投注")
    public Result getBeetingGame(HttpServletRequest req,Integer gameId){
        User user = UserUtil.getUserByReq(req, userDao);
        if(BittingValue.betMapf.get(gameId)!=null){
            Map<Integer,Map<Integer, Integer>> userMap = BittingValue.betMapf.get(gameId);
            if(userMap.get(user.getId())!=null){
                return new Result(JSON.toJSONString(userMap.get(user.getId())));
            }
        }
        return new Result("");
    }

    @GetMapping("setBeetingGame")
    @ApiOperation(value = "设置预投注")
    public Result setBeetingGame(HttpServletRequest req,Integer gameId,@RequestParam(value = "nums")List<Integer> nums,@RequestParam(value = "counts")List<Integer> counts){
        User user = UserUtil.getUserByReq(req, userDao);
        if(nums.size()<=0 || counts.size() <=0 || nums.size()!=counts.size()) return new Result("投注数量为空！或者不一致！",null);
        for(Integer count : counts){
            if(count > 10000000){
                return new Result("投注金额不能大于一千万",null);
            }
        }
        Map<Integer, Integer> betMap = new LinkedHashMap<>();
        for(int i =0;i<nums.size();i++){
            betMap.put(nums.get(i),counts.get(i));
        }
        Map<Integer,Map<Integer, Integer>> userMap = new LinkedHashMap<>();
        userMap.put(user.getId(),betMap);
        BittingValue.betMapf.put(gameId,userMap);
        return new Result("OK");
    }

    @GetMapping("betting")
    @ApiOperation(value = "用户投注")
    public Result betting(HttpServletRequest req, @RequestParam(value = "nums")List<Integer> nums,@RequestParam(value = "counts")List<Integer> counts){
        if(nums.size()<=0 || counts.size() <=0 || nums.size()!=counts.size()) return new Result("投注数量为空！或者不一致！",null);
        for(Integer count : counts){
            if(count > 10000000){
                return new Result("投注金额不能大于一千万",null);
            }
        }
        if(BittingValue.game.getReTime()<30){
            return new Result("投注已截至！",null);
        }

        Integer count = counts.stream().mapToInt(c -> c).sum();
        User user = UserUtil.getUserByReq(req, userDao);
        if(new BigDecimal(count).compareTo(user.getMoney()) > 0) return new Result("余额不足！",null);
        Map<Integer, Integer> betMap = new LinkedHashMap<>();
        for(int i =0;i<nums.size();i++){
            betMap.put(nums.get(i),counts.get(i));
        }
        return gameService.betting(user,betMap);
    }

    @GetMapping("getNumbers")
    @ApiOperation(value = "获取计算结果")
    public Result startCom(){
        return new Result(BittingValue.returnMap);
    }

    @GetMapping("getGameInfo")
    @ApiOperation(value = "获取游戏信息【原接口。不要使用】")
    public Result getGameInfo(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        return gameService.getGameInfo(user);
    }

    @GetMapping("getGameInfoH")
    @ApiOperation(value = "获取游戏信息{后台}")
    public Result getGameInfoH(){
        return gameService.getGameInfoH();
    }

    @GetMapping("getGameInfoQ")
    @ApiOperation(value = "获取游戏信息{前台}")
//    @LoadBalanced
    public Result getGameInfoQ(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        return gameService.getGameInfoQ(user);
    }

    @GetMapping("getUserBillInfo")
    @ApiOperation(value = "获取当前用户押注信息")
    public Result getUserBillInfo(HttpServletRequest req,Integer gameId){
        User user = UserUtil.getUserByReq(req, userDao);
        if(gameId > BittingValue.game.getId()){
            return new Result("查询的游戏还未开始！",null);
        }
        return gameService.getUserBillInfo(user,gameId);
    }

    @GetMapping("endCom")
    @ApiOperation(value = "游戏结束，返回选取的数字用于做用户结算{后台}")
    public Result endCom(Integer num){
        if(num == null) return new Result("num为空！",null);
        if(!BittingValue.falg) return new Result("当前游戏系统已自动开奖！剩余游戏时间："+ BittingValue.game.getReTime());
        return gameService.end(num);
    }

//    @GetMapping("renewGame")
//    @ApiOperation(value = "用户按照上一把的续压")
//    public Result renewGame(HttpServletRequest req){
//        User user = UserUtil.getUserByReq(req, userDao);
//        return gameService.renewGame(user);
//    }

    @GetMapping("findGameState")
    @ApiOperation(value = "获取当前游戏信息状态")
    public Result findGameState(Integer gameId){
        return gameService.findGameState(gameId);
    }

    @GetMapping("findBettingValue")
    @ApiOperation(value = "根据游戏ID获取当前用户押注状态")
    public Result findBettingValue(Integer gameId,Integer number,HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        return gameService.findBettingValue(gameId,user.getId(),number);
    }

}
