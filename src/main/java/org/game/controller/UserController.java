package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.UserDao;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.service.UserService;
import org.game.util.MD5;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @author Zhouyf
 * @Data 2020-07-16  12:42
 */
@RestController
@RequestMapping(value = "user")
@Api(tags = {"用户接口"})
@Slf4j
public class UserController extends UserErrorController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @GetMapping("findAll")
    @ApiOperation(value = "获取所有用户信息{后台}")
    public Result findAll() {
        return new Result(userService.findAll());
    }

    @GetMapping("login")
    @ApiOperation(value = "用户登录")
    public Result login(String userName, String password) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password))
            return new Result("用户名称或密码为空！userName :" + userName + " , password : " + password, "ERROR");
        return userService.login(userName, password);
    }

    @GetMapping("register")
    @ApiOperation(value = "用户注册")
    public Result register(String phone, String jkpwd, String pwd1, String pwd2, String yqm, String yzm, String userName) {
        if (!StringUtils.verPhoneNum(phone)) return new Result("请输入正确的手机号码！", null);
        if (jkpwd.length() != 6) return new Result("金库交易密码长度请设为6位数！", null);
        if (pwd1 == null || !pwd1.equals(pwd2)) return new Result("两次密码不一致！", null);
        if (userName == null) return new Result("请输入用户名！", null);
        User userRegister = userService.findUserByPhone(phone);
        if (userRegister != null) return new Result("该手机号码已被注册！", null);

        /**
         * @TODO yzm
         */

        User user = new User();
        user.setHy("普通会员");
        user.setPhone(phone);
        user.setName(userName);
        user.setUserName(userName);
        user.setJkpwd(MD5.getMd5(jkpwd));
        user.setPwd(MD5.getMd5(pwd1));
        user.setMoney(BigDecimal.valueOf(0));
        user.setJkmoney(BigDecimal.valueOf(0));
        user.setIsUse(1);
        user.setYqm(StringUtils.getYzm(8));
        //(数据类bai型)(最小值du+Math.random()*(最大值-最小值+1))
        int i = (int) (1 + Math.random() * (20 - 1 + 1));
        user.setHeadImg(i + ".jpg");
        return userService.addUser(user);
    }

    @GetMapping("update")
    @ApiOperation(value = "修改用户信息")
    public Result update(String headImg, String name, String sex, String bri, HttpServletRequest req) {
        User user = UserUtil.getUserByReq(req, userDao);
        user.setHeadImg(headImg);
        user.setName(name);
        user.setSex(sex);
        user.setBri(bri);
        return userService.update(user);
    }

    @GetMapping("forgetPwd")
    @ApiOperation(value = "忘记密码")
    public Result forgetPwd(String phone, String pwd1, String pwd2, String yzm) {
        if (!StringUtils.verPhoneNum(phone)) return new Result("请输入正确的手机号码！", null);
        if (pwd1 == null || pwd2 == null || !pwd1.equals(pwd2)) return new Result("两次密码不一致！", null);
        /**
         * @TODO yzm
         */
        return userService.updatePwdByPhone(phone, pwd1);
    }

    @GetMapping("updatePwd")
    @ApiOperation(value = "修改密码")
    public Result updatePwd(String pwd1, String pwd2, String yzm, HttpServletRequest req) {
        if (pwd1 == null || pwd2 == null || !pwd1.equals(pwd2)) return new Result("两次密码不一致！", null);
        /**
         * @TODO yzm
         */
        User user = UserUtil.getUserByReq(req, userDao);
        return userService.updatePwd(user, pwd1);
    }

    @GetMapping("updatePhone")
    @ApiOperation(value = "修改手机号码")
    public Result updatePhone(String phone, String yzm,String pwd,String jkPwd, HttpServletRequest req) {
        if (!StringUtils.verPhoneNum(phone)) return new Result("请输入正确的手机号码！", null);
        /**
         * @TODO yzm
         */
        User userRegister = userService.findUserByPhone(phone);
        if (userRegister != null) return new Result("该手机号码已被注册！", null);
        User user = UserUtil.getUserByReq(req, userDao);
        if(!(user.getPwd().equals(MD5.getMd5(pwd))&& user.getJkpwd().equals(MD5.getMd5(jkPwd)))) return new Result("密码错误！",null);
        return userService.updatePhone(user, phone);
    }

    @GetMapping("updateJkPwd")
    @ApiOperation(value = "修改金库交易密码")
    public Result updateJkPwd(String pwd1, String pwd2, String yzm, HttpServletRequest req) {
        if (pwd1 == null || !pwd1.equals(pwd2)) return new Result("两次密码不一致！", null);
        if (pwd1.length() != 6) return new Result("金库交易密码长度请设为6位数！", null);
        /**
         * @TODO yzm
         */
        User user = UserUtil.getUserByReq(req, userDao);
        return userService.updateJkPwd(user, pwd1);
    }

    @GetMapping("findUserByIdAndPhone")
    @ApiOperation(value = "根据id和电话模糊匹配")
    public Result findUserByIdAndPhone(String value){
        if(value!=null && (value.length()== 6 || value.length()==11) ){
            return userService.findUserByIdAndPhone(value);
        }
        return new Result(null);
    }

}
