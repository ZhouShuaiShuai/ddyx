package org.game.service;

import com.alibaba.fastjson.JSON;
import org.game.config.BittingValue;
import org.game.dao.BettingDao;
import org.game.dao.BettingModelDao;
import org.game.pojo.BettingModel;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Zhouyf
 * @Data 2020-09-01  19:02
 */
@Service
public class BettingModelService {

    @Autowired
    private BettingModelDao bettingModelDao;

    public Result startModel(Integer modelId){
        BettingModel bettingModel = bettingModelDao.getOne(modelId);
        BittingValue.bettingModelList.put(modelId,bettingModel);
        return new Result("已启动，下把开始生效！");
    }

    public Result endModel(Integer modelId){
        BittingValue.bettingModelList.remove(modelId);
        return new Result("已关闭，下把开始生效！");
    }

    public Result findBettingModels(Integer userId){
        List<BettingModel> bettingModelList = bettingModelDao.findAllByUserId(userId);
        return new Result(bettingModelList);
    }

    public Result findBettingModel(Integer modelId){
        BettingModel model = bettingModelDao.findById(modelId).get();

        Map<Integer, Integer> betMap = (Map<Integer, Integer>) JSON.parse(model.getBettingMap());
        Integer money = betMap.values().stream().mapToInt(c -> c).sum();
        String winName = "",loserName ="";
        if(model.getWinModelId()!=null){
            Optional<BettingModel> winModel = bettingModelDao.findById(model.getWinModelId());
            if(!winModel.isPresent()) winName = winModel.get().getName();
        }
        if(model.getLoserModelId()!=null){
            Optional<BettingModel> loserModel = bettingModelDao.findById(model.getLoserModelId());
            if(!loserModel.isPresent()) loserName = loserModel.get().getName();
        }
        String finalWinName = winName;
        String finalLoserName = loserName;
        return new Result(new LinkedHashMap<String,Object>(){{
            this.put("model",model);
            this.put("winName", finalWinName);
            this.put("loserName", finalLoserName);
            this.put("投注总额", money);
        }});
    }

    public Result addBettingModel(BettingModel bettingModel){
        return new Result(bettingModelDao.save(bettingModel));
    }

    public Result updBettingModel(BettingModel bettingModel){
        return new Result(bettingModelDao.saveAndFlush(bettingModel));
    }

    public Result delBettingModel(Integer modelId){
        bettingModelDao.deleteById(modelId);
        return new Result("删除成功！");
    }

}


