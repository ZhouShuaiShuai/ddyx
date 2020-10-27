package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.DetailMenuDao;
import org.game.dao.UserDao;
import org.game.dao.YhDao;
import org.game.pojo.DetailMenu;
import org.game.pojo.User;
import org.game.pojo.Yh;
import org.game.result.Result;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "yh")
@Api(tags = {"银行相关接口"})
@Slf4j
public class YhController {

    @Autowired
    private YhDao yhDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DetailMenuDao detailMenuDao;

    @GetMapping("getYh")
    @ApiOperation(value = "获取银行信息")
    public Result getYh(){
        return new Result(yhDao.findAll());
    }

    @GetMapping("updateYh")
    @ApiOperation(value = "修改银行信息，原有的会被删除，如果没有则直接新增")
    public Result updateYh(String yhName , String yhNum ,String skName){
        Yh yh = new Yh();
        yh.setId(1);
        yh.setSkName(skName);
        yh.setYhName(yhName);
        yh.setYhNum(yhNum);
        return new Result(yhDao.saveAndFlush(yh));
    }

    @GetMapping("getMoney")
    @ApiOperation(value = "用户体现")
    public Result getMoney(HttpServletRequest req, BigDecimal jdNum,String detailss){
        User user = UserUtil.getUserByReq(req, userDao);
        if(user==null || !StringUtils.isEmpty(user.getYhNum()))
            return new Result("用户银行卡号为空!请绑定银行卡后再操作！",null);

        if(!StringUtils.isEmpty(user.getYhName()))
            return new Result("用户所属银行为空!请绑定银行卡后再操作！",null);

        if(user.getJkmoney().compareTo(jdNum) == -1)
            return new Result("小金库中金豆数量不足！",null);

        if(detailMenuDao.findOnebyUserName(user.getUserName())>0)
            return new Result("不允许重复提交！",null);

        DetailMenu detailMenu = new DetailMenu();
        detailMenu.setUserName(user.getUserName());
        detailMenu.setJdNum(jdNum);
        detailMenu.setUserYhName(user.getYhName());
        detailMenu.setUserYhNum(user.getYhNum());
        detailMenu.setYtype(1);
        detailMenu.setYstate(1);
        detailMenu.setDetailss(detailss);
        detailMenuDao.save(detailMenu);
        return new Result("已提交！等待审核！");
    }

    @GetMapping("takeMoney")
    @ApiOperation(value = "用户充值")
    public Result takeMoney(HttpServletRequest req, BigDecimal jdNum,String detailss){
        User user = UserUtil.getUserByReq(req, userDao);
        if(user==null || !StringUtils.isEmpty(user.getYhNum()))
            return new Result("用户银行卡号为空!请绑定银行卡后再操作！",null);

        if(!StringUtils.isEmpty(user.getYhName()))
            return new Result("用户所属银行为空!请绑定银行卡后再操作！",null);

        if(detailMenuDao.findOnebyUserName(user.getUserName())>0)
            return new Result("不允许重复提交！",null);

        DetailMenu detailMenu = new DetailMenu();
        detailMenu.setUserName(user.getUserName());
        detailMenu.setJdNum(jdNum);
        detailMenu.setUserYhName(user.getYhName());
        detailMenu.setUserYhNum(user.getYhNum());
        detailMenu.setYtype(2);
        detailMenu.setYstate(1);
        detailMenu.setDetailss(detailss);

        detailMenuDao.save(detailMenu);
        return new Result("已提交！等待审核！");
    }

    @GetMapping("getDetail")
    @ApiOperation(value = "获取体现充值列表")
    public Result getDetail(Integer yType,Integer yState){
        List<DetailMenu> list = detailMenuDao.findAllByYtypeAndYstateOrderByUpdateDateDesc(yType,yState);
        return new Result(list);
    }

    @GetMapping("sh")
    @ApiOperation(value = "审核")
    public Result sh(Integer id,Integer yState){
        Optional<DetailMenu> detailMenu = detailMenuDao.findById(id);
        if(!detailMenu.isPresent()){
            return new Result("id错误！没找到对应账单！",null);
        }

        DetailMenu de = detailMenu.get();
        de.setYstate(yState);

        //扣款 账单
        return new Result(detailMenuDao.saveAndFlush(de));
    }

    @GetMapping("UserBd")
    @ApiOperation(value = "用户绑定或修改银行信息")
    public Result UserBd(HttpServletRequest req,String yhName,String yhNum,String yhUserName){
        User user = UserUtil.getUserByReq(req, userDao);
        user.setYhName(yhName);
        user.setYhNum(yhNum);
        user.setYhUserName(yhUserName);
        return new Result(userDao.saveAndFlush(user));
    }


}
