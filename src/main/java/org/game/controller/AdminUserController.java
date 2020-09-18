package org.game.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.game.dao.AdminUserDao;
import org.game.pojo.AdminUser;
import org.game.result.Result;
import org.game.util.MD5;
import org.game.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @GetMapping("addAdminUser")
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



//    @GetMapping("updatePwd")
//    @ApiOperation(value = "修改密码")
//    public Result updatePwd(String newPwd, HttpServletRequest req){
//        AdminUser adminUser = adminUserDao.findByUserName(userName);
//        if(adminUser == null){
//            return new Result("")
//        }
//        return null;
//    }

}
