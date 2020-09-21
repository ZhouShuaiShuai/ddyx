package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.config.BittingValue;
import org.game.dao.AdminUserDao;
import org.game.dao.GameDao;
import org.game.dao.UserDao;
import org.game.dao.UserInfoDao;
import org.game.filter.JWTToken;
import org.game.pojo.AdminUser;
import org.game.pojo.Game;
import org.game.pojo.User;
import org.game.pojo.UserInfo;
import org.game.result.Result;
import org.game.util.MD5;
import org.game.util.StringUtils;
import org.game.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sun.awt.image.ImageWatched;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-09-18  15:41
 */
@RestController
@RequestMapping(value = "adminUser")
@Api(tags = {"管理员用户接口"})
@Slf4j
public class AdminUserController {

    @Autowired
    private AdminUserDao adminUserDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private UserInfoDao userInfoDao;

    @PostMapping("addAdminUser")
    @ApiOperation(value = "添加管理员用户,默认登录密码123456")
    public Result addAdminUser(String userName){
        if(StringUtils.isEmpty(userName)){
            return new Result("用户名不能为空！",null);
        }
        AdminUser adminUser = new AdminUser();
        adminUser.setUserName(userName);
        adminUser.setPwd(MD5.getMd5("123456"));
        adminUser.setRole("admin");
        AdminUser resultUser = adminUserDao.save(adminUser);
        resultUser.setPwd("默认密码为123456，请登录后自行修改！");
        return new Result(resultUser);
    }

//    @GetMapping("adminLogin")
    @PostMapping("adminLogin")
    @ApiOperation(value = "管理员登陆")
    public Result adminLogin(String userName,String pwd){
        if(StringUtils.isEmpty(userName)|| StringUtils.isEmpty(pwd)){
            return new Result("用户名或密码不能为空！",null);
        }
        AdminUser adminUser = adminUserDao.findByUserName(userName);
        if (adminUser == null) return new Result("未找到对应的系统用户！", null);

        if (!adminUser.getPwd().equals(MD5.getMd5(pwd))) return new Result("用户名或密码错误！", null);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("id", adminUser.getId());
        resultMap.put("角色", adminUser.getRole());
        resultMap.put("用户名", adminUser.getUserName());
        String jwt = JWTToken.buildAdminJwt(adminUser);
        resultMap.put("token", jwt);
        return new Result(resultMap);

    }


    @PostMapping("updatePwd")
    @ApiOperation(value = "修改密码")
    public Result updatePwd(String newPwd, HttpServletRequest req){
        AdminUser adminUser = UserUtil.getAdminUserByReq(req,adminUserDao);
        if(adminUser == null){
            return new Result("加密字符串错误！");
        }
        adminUser.setPwd(MD5.getMd5(newPwd));
        return new Result(adminUserDao.saveAndFlush(adminUser));
    }

    @PostMapping("deleteAdminUser")
    @ApiOperation(value = "删除管理员用户")
    public Result deleteAdminUser(String userName, HttpServletRequest req){
        AdminUser adminUser = UserUtil.getAdminUserByReq(req,adminUserDao);
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        if(adminUser.getRole()!="超级管理员"){
            return new Result("只有超级管理员能进行这项操作！");
        }
        AdminUser delUser = adminUserDao.findByUserName(userName);
        if (delUser == null) return new Result("未找到对应的系统用户！", null);

        adminUserDao.delete(delUser);
        return new Result("删除成功 userName : "+ userName);
    }

    @PostMapping("deleteUser")
    @ApiOperation(value = "删除用户")
    public Result deleteUser(String userName) {
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);

        userDao.delete(user);
        return new Result("删除成功 userName : "+ userName);
    }

    @PostMapping("updateUserJkMoney")
    @ApiOperation(value = "修改用户小金库")
    public Result updateUserJkMoney(String userName,Integer money){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setJkmoney(new BigDecimal(money));
        return new Result(userDao.saveAndFlush(user));
    }

    @PostMapping("updateUserMoney")
    @ApiOperation(value = "修改用户余额")
    public Result updateUserMoney(String userName,Integer money){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setMoney(new BigDecimal(money));
        return new Result(userDao.saveAndFlush(user));
    }

    @PostMapping("updateUserPwd")
    @ApiOperation(value = "重置登陆密码")
    public Result updateUserPwd(String userName){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setPwd(MD5.getMd5("123456"));
        return new Result(userDao.saveAndFlush(user));
    }

    @PostMapping("updateUserJkPwd")
    @ApiOperation(value = "重置金库密码")
    public Result updateUserJkPwd(String userName){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setJkpwd(MD5.getMd5("123456"));
        return new Result(userDao.saveAndFlush(user));
    }

    @GetMapping("getNowGame")
    @ApiOperation(value = "获取正在运行的游戏信息")
    public Result getNowGame(){
        Map<String,Object> resultMap = new LinkedHashMap<>();
        resultMap.put("游戏信息",BittingValue.game);
        Map<String,Map<Integer, Integer>> betMap = new LinkedHashMap<>();
        for(Integer userId : BittingValue.betMap.keySet()){
            betMap.put(userDao.findNameById(userId),BittingValue.betMap.get(userId));
        }

        resultMap.put("用户押注信息",betMap);

        return new Result(resultMap);
    }

    @GetMapping("getGames")
    @ApiOperation(value = "分页获取游戏信息")
    public Result getGames(Integer pageIndex, Integer pageSize){
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "id");
        Page<Game> games = gameDao.findGames(pageable);
        return new Result(games);
    }

    @GetMapping("AddUserInfo")
    @ApiOperation(value = "手动添加牛人帮数据")
    public Result AddUserInfo(UserInfo userInfo){
        Game game = gameDao.findById(userInfo.getGameId()).get();
        if(!game.getNumber().equals(userInfo.getNum())){
            return new Result("输入的开奖数字和对应游戏的开奖数字不符",null);
        }
        return new Result(userInfoDao.save(userInfo));
    }

    @GetMapping("fh")
    @ApiOperation(value = "封号")
    public Result fh(String userName){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setIsUse(0);
        return new Result(userDao.saveAndFlush(user));
    }

    @GetMapping("jf")
    @ApiOperation(value = "解封")
    public Result jf(String userName){
        if(StringUtils.isEmpty(userName)){
            return new Result("参数不能为空！");
        }
        User user = userDao.findByUserName(userName);
        if (user == null) return new Result("未找到对应的系统用户！", null);
        user.setIsUse(1);
        return new Result(userDao.saveAndFlush(user));
    }


}
