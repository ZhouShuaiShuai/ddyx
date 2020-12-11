package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.JkBillDao;
import org.game.dao.UserDao;
import org.game.pojo.JkBill;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.BillService;
import org.game.util.MD5;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * 对于金豆的操作
 */
@RestController
@RequestMapping(value = "bill")
@Api(tags = {"金豆操作"})
@Slf4j
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JkBillDao jkBillDao;

    @GetMapping("saveMoney")
    @ApiOperation(value = "存入小金库")
    public Result saveMoney(BigDecimal money, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);

        if (money.compareTo(user.getMoney()) > 0) return new Result("余额不足;", null);
        return billService.saveMoney(user, money);
    }

    @GetMapping("takeOutMoney")
    @ApiOperation(value = "从小金库取出")
    public Result takeOutMoney(BigDecimal money, String pwd, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);
        if (!user.getJkpwd().equals(MD5.getMd5(pwd))) return new Result("密码错误！;", null);
        if (money.compareTo(user.getJkmoney()) > 0) return new Result("小金库余额不足;", null);

        return billService.takeOutMoney(user, money);
    }

    @GetMapping("transferMoney")
    @ApiOperation(value = "转账")
    public Result transferMoney(BigDecimal money, String pwd, Integer otherUserId, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);
        if (!user.getJkpwd().equals(MD5.getMd5(pwd))) return new Result("密码错误！;", null);
        if (money.compareTo(user.getJkmoney()) > 0) return new Result("小金库余额不足;", null);
        if (user.getId().equals(otherUserId)) return new Result("不能给自己转账！;", null);
        return billService.transferMoney(user, otherUserId, money);
    }

    @GetMapping("getYeLog")
    @ApiOperation(value = "余额的变动日志")
    public Result getYeLog(Integer pageIndex, Integer pageSize, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "create_date");
        return billService.getYeLog(user.getId(), pageable);
    }

    @GetMapping("getJkLog")
    @ApiOperation(value = "小金库的变动日志")
    public Result getJkLog(Integer pageIndex, Integer pageSize, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "create_date");
        return billService.getJkLog(user.getId(), pageable);
    }

    @GetMapping("addJkLog")
    @ApiOperation(value = "添加小金库的变动日志")
    public Result addJkLog(String userName,String type,BigDecimal money){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setJkmoney(user.getJkmoney().add(money));
//        user.setMoney(user.getMoney().add(money));
        userDao.saveAndFlush(user);
        JkBill jkBill = new JkBill(money,type,user);
        return new Result(jkBillDao.save(jkBill));
    }

}
