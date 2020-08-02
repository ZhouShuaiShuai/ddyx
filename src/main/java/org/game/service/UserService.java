package org.game.service;

import org.game.dao.UserDao;
import org.game.filter.JWTToken;
import org.game.pojo.User;
import org.game.result.Result;
import org.game.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhouyf
 * @Data 2020-07-17  22:46
 */
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    public List<User> findAll() {
        return userDao.findAll();
    }

    public Result login(String username, String password) {
        User user = userDao.findByUserName(username);
        if (user == null) return new Result("未找到对应的系统用户！", null);

        if (!user.getPwd().equals(MD5.getMd5(password))) return new Result("用户名或密码错误！", null);
        if (user.getIsUse() == 0) return new Result("该用户未启用！", null);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("userName", user.getUserName());
        resultMap.put("name", user.getName());
        resultMap.put("phone", user.getPhone());
        resultMap.put("bri", user.getBri());
        resultMap.put("sex", user.getSex());
        resultMap.put("hy", user.getHy());
        resultMap.put("money", user.getMoney());
        resultMap.put("jkmoney", user.getJkmoney());
        resultMap.put("headImg", user.getHeadImg());
        resultMap.put("id", user.getId());
//        resultMap.put("yqm", user.getYqm());
        String jwt = JWTToken.buildJwt(user);
        resultMap.put("token", jwt);
        return new Result(resultMap);
    }

    public Result addUser(User user) {
        return new Result(userDao.save(user));
    }

    public Result update(User user) {
        return new Result(userDao.saveAndFlush(user));
    }

    public User findUserByPhone(String phone) {
        return userDao.findUserByPhone(phone);
    }

    public Result updatePwdByPhone(String phone, String pwd) {
        User user = userDao.findUserByPhone(phone);
        if (user == null) return new Result("改手机号还没有注册用户！", null);
        user.setPwd(MD5.getMd5(pwd));
        return new Result(userDao.saveAndFlush(user));
    }

    public Result updatePwd(User user, String pwd) {

        user.setPwd(MD5.getMd5(pwd));
        return new Result(userDao.saveAndFlush(user));
    }

    public Result updatePhone(User user, String phone) {

        user.setPhone(phone);
        return new Result(userDao.saveAndFlush(user));
    }

    public Result updateJkPwd(User user, String pwd) {

        user.setJkpwd(MD5.getMd5(pwd));
        return new Result(userDao.saveAndFlush(user));
    }

}
