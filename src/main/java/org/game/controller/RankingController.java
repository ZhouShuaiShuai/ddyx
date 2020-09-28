package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.UserInfoDao;
import org.game.pojo.UserInfo;
import org.game.result.PageResult;
import org.game.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value = "ranking")
@Api(tags = {"排行接口"})
@Slf4j
public class RankingController {

    @Autowired
    private UserInfoDao userInfoDao;

    @GetMapping("findRanking")
    @ApiOperation(value = "查询游戏数据排名")
    public Result findRanking(Integer gameId,Integer index) {
        Pageable pageable = PageRequest.of(index,20);
        return new Result(userInfoDao.findAllByGameIdOrderByYlDesc(gameId,pageable));
    }

/*    @GetMapping("findDayRanking")
    @ApiOperation(value = "获取今天的牛人榜")
    public Result findDayRanking(){
        List<Map<String,Object>> map = userInfoDao.findDayRanking();
        return new Result(map);
    }

    @GetMapping("findMonehRanking")
    @ApiOperation(value = "获取本月的牛人榜")
    public Result findMonehRanking(){
        List<Map<String,Object>> map = userInfoDao.findMonthRanking();
        return new Result(map);
    }

    @GetMapping("findYearRanking")
    @ApiOperation(value = "获取本年的牛人榜")
    public Result findYearRanking(){
        List<Map<String,Object>> map = userInfoDao.findYearRanking();
        return new Result(map);
    }

    @PostMapping("addUserInfoByAdmin")
    @ApiOperation(value = "手动添加牛人榜")
    public Result addUserInfoByAdmin(String userName,Long hl){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserName(userName);
        userInfo.setHl(hl);
        return new Result(userInfoDao.save(userInfo));
    }*/

}
