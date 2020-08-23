package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.UserInfoDao;
import org.game.result.Result;
import org.game.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-08-13  21:48
 * 数据排行
 */
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

}
