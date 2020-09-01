package org.game.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.UserDao;
import org.game.pojo.BettingModel;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.BettingModelService;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-09-01  19:03
 */
@RestController
@RequestMapping(value = "model")
@Api(tags = {"投注模式"})
@Slf4j
public class BettingModelController {

    @Autowired
    private BettingModelService bettingModelService;

    @Autowired
    private UserDao userDao;

    @GetMapping("startModel")
    @ApiOperation(value = "开始自动投注")
    public Result startModel(Integer modelId){
        if(BittingValue.game.getReTime()<15){
            return new Result("正在计算，目前不能开启自动投注！",null);
        }
        return bettingModelService.startModel(modelId);
    }

    @GetMapping("setConfig")
    @ApiOperation(value = "设置用户投注配置")
    public Result setConfig(HttpServletRequest req,Integer max,Integer min ,Integer num){
        User user = UserUtil.getUserByReq(req, userDao);
        Map<String,Integer> map = new LinkedHashMap<>();
        map.put("max",max);
        map.put("min",min);
        map.put("num",num);
        BittingValue.modelCon.put(user.getId(),map);
        return new Result("OK");
    }

    @GetMapping("endModel")
    @ApiOperation(value = "结束自动投注")
    public Result endModel(Integer modelId){
        return bettingModelService.endModel(modelId);
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
                                  @RequestParam(value = "winModelId")Integer winModelId,
                                  @RequestParam(value = "loserModelId")Integer loserModelId,
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
                                @RequestParam(value = "winModelId")Integer winModelId,
                                @RequestParam(value = "loserModelId")Integer loserModelId,
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