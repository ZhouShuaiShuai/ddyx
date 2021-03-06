package org.game.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.BettingModelDao;
import org.game.dao.UserDao;
import org.game.pojo.BettingModel;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.BettingModelService;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.image.ImageWatched;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "model")
@Api(tags = {"投注模式"})
@Slf4j
public class BettingModelController {

    @Autowired
    private BettingModelService bettingModelService;

    @Autowired
    private BettingModelDao bettingModelDao;

    @Autowired
    private UserDao userDao;


    @GetMapping("getWinOrLoser")
    @ApiOperation(value = "我的赢亏")
    public Result getWinOrLoser(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        if(user!=null)
        return bettingModelService.getWinOrLoser(user);
        else
            return new Result("USER IS NULL!",null);
    }

    @GetMapping("getWinOrLoserByGame")
    @ApiOperation(value = "每把游戏的盈亏")
    public Result getWinOrLoserByGame(Integer pageIndex, Integer pageSize,HttpServletRequest req){
        Pageable pageable = PageRequest.of(pageIndex, pageSize,Sort.Direction.DESC, "game_id");
        User user = UserUtil.getUserByReq(req, userDao);
        if(user!=null)
            return bettingModelService.getWinOrLoserByGame(user,pageable);
        else
            return new Result("USER IS NULL!",null);
    }

    @GetMapping("getWinOrLoserByDay")
    @ApiOperation(value = "每日的盈亏")
    public Result getWinOrLoserByDay(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        if(user!=null)
            return bettingModelService.getWinOrLoserByDay(user);
        else
            return new Result("USER IS NULL!",null);
    }


    @GetMapping("getConfig")
    @ApiOperation(value = "获取用户投注详情")
    public Result getConfig(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        Map<String, Integer> startConfMap =  new LinkedHashMap<>();
        if(BittingValue.startModelCon.get(user.getId())!=null){
            startConfMap.putAll(BittingValue.startModelCon.get(user.getId()));
        }
        Map<String, Integer> confMap =  new LinkedHashMap<>();
        if(BittingValue.modelCon.get(user.getId())!=null){
            confMap.putAll(BittingValue.modelCon.get(user.getId()));
        }
        if(startConfMap.isEmpty()){
            return new Result(null);
        }else if(confMap.isEmpty()){
            BittingValue.startModelCon.remove(user.getId());
            return new Result(null);
        }else {
            Map<String,Object> resultMap = new LinkedHashMap<>();
            resultMap.put("投注期数",startConfMap.get("num"));
            resultMap.put("剩余期数",BittingValue.modelCon.get(user.getId()).get("num"));
            resultMap.put("已投期数",startConfMap.get("num") - BittingValue.modelCon.get(user.getId()).get("num"));
            resultMap.put("max",BittingValue.modelCon.get(user.getId()).get("max"));
            resultMap.put("min",BittingValue.modelCon.get(user.getId()).get("min"));
            resultMap.put("startGameId",BittingValue.startModelCon.get(user.getId()).get("startGameId"));
            resultMap.put("startModelId",BittingValue.startModelCon.get(user.getId()).get("startModelId"));
            return new Result(resultMap);
        }

    }

//    @GetMapping("startModel")
//    @ApiOperation(value = "开始自动投注")
//    public Result startModel(Integer modelId){
//        if(BittingValue.game.getReTime()<15){
//            return new Result("正在计算，目前不能开启自动投注！",null);
//        }
//        return bettingModelService.startModel(modelId);
//    }

    @GetMapping("setConfig")
    @ApiOperation(value = "设置用户投注配置")
    public Result setConfig(HttpServletRequest req,Integer max,Integer min ,Integer num,Integer startGameId,Integer startModelId){
        if(startGameId <= BittingValue.game.getId()){
            return new Result("当前游戏已开始，请重新配置开始期号！",null);
        }
        User user = UserUtil.getUserByReq(req, userDao);
        log.error("User : " + user);
        if(user!=null && max!=0 && user.getMoney().compareTo(new BigDecimal(max))>=0){
            return new Result("设置的最大盈利要比当前余额大才可以！",null);
        }
        Optional<BettingModel> model = bettingModelDao.findById(startModelId);
        if(model==null||!model.isPresent()){
            return new Result("投注ID设置有问题",null);
        }
        BettingModel bettingModel = model.get();
        Map<Integer, Integer> betMap = (Map<Integer, Integer>) JSON.parse(bettingModel.getBettingMap());
        Integer count = betMap.values().stream().mapToInt(c -> c).sum();
        if(user.getMoney().compareTo(new BigDecimal(count))<0){
            return new Result("投注金额大于余额！",null);
        }

        Map<String,Integer> map = new LinkedHashMap<>();
        map.put("max",max);
        map.put("min",min);
        map.put("num",num);
        map.put("startGameId",startGameId);
        map.put("startModelId",startModelId);
        BittingValue.modelCon.put(user.getId(),map);
        BittingValue.startModelCon.put(user.getId(),map);
        return new Result("OK");
    }

    @GetMapping("endModel")
    @ApiOperation(value = "结束自动投注")
    public Result endModel(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        BittingValue.modelCon.remove(user.getId());
        return new Result("已关闭，下把开始生效！");
    }


    @GetMapping("findBettingModels")
    @ApiOperation(value = "获取用户的投注模式列表")
    public Result findBettingModels(HttpServletRequest req){
        User user = UserUtil.getUserByReq(req, userDao);
        return bettingModelService.findBettingModels(user.getId());
    }

    @GetMapping("findBettingModel")
    @ApiOperation(value = "查看用户的投注模式详情")
    public Result findBettingModel(Integer modelId){
        if(StringUtils.isEmpty(modelId)) return new Result("模式ID为空！",null);
        return bettingModelService.findBettingModel(modelId);
    }


    @GetMapping("addBettingModel")
    @ApiOperation(value = "添加投注模式")
    public Result addBettingModel(@RequestParam(value = "name")String name,
                                  @RequestParam(value = "winModelId" ,required = false)Integer winModelId,
                                  @RequestParam(value = "loserModelId" ,required = false)Integer loserModelId,
                                  @RequestParam(value = "nums") List<Integer> nums,
                                  @RequestParam(value = "counts")List<Integer> counts ,
                                  HttpServletRequest req){
        if(nums.size()<=0 || counts.size() <=0 || nums.size()!=counts.size()) return new Result("投注数量为空！或者不一致！",null);
        for(Integer count : counts){
            if(count > 10000000){
                return new Result("投注金额不能大于一千万",null);
            }
        }
        User user = UserUtil.getUserByReq(req, userDao);
        Map<Integer, Integer> betMap = new LinkedHashMap<>();
        for(int i =0;i<nums.size();i++){
            betMap.put(nums.get(i),counts.get(i));
        }
        BettingModel bettingModel = new BettingModel(name,winModelId,loserModelId,user.getId(), JSON.toJSONString(betMap));
        return bettingModelService.addBettingModel(bettingModel);
    }

    @GetMapping("updBettingModel")
    @ApiOperation(value = "修改投注模式")
    public Result updBettingModel(
                                @RequestParam(value = "name")String name,
                                @RequestParam(value = "winModelId" ,required = false)Integer winModelId,
                                @RequestParam(value = "loserModelId" ,required = false)Integer loserModelId,
                                @RequestParam(value = "modelId")Integer modelId,
                                  @RequestParam(value = "nums") List<Integer> nums, @RequestParam(value = "counts")List<Integer> counts , HttpServletRequest req){
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
        User user = UserUtil.getUserByReq(req, userDao);
        BettingModel bettingModel = new BettingModel(name,winModelId,loserModelId,user.getId(), JSON.toJSONString(betMap));
        bettingModel.setId(modelId);
        return bettingModelService.updBettingModel(bettingModel);
    }

    @GetMapping("delBettingModel")
    @ApiOperation(value = "删除投注模式")
    public Result delBettingModel(Integer modelId){
        return bettingModelService.delBettingModel(modelId);
    }

}
