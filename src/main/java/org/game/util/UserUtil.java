package org.game.util;

import org.game.dao.UserDao;
import org.game.filter.JWTToken;
import org.game.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Zhouyf
 * @Data 2020-07-20  18:31
 */
public class UserUtil {

    public synchronized static User getUserByReq(HttpServletRequest req, UserDao userDao) {
        String xAuthToken = req.getHeader("x-auth-token");
        User user = JWTToken.getUserFromJwt(xAuthToken);
        try {
            user = userDao.findById(user.getId()).get();
        }catch (NullPointerException e){
            System.out.println("用户为空！");
        }
        return user;
    }
}
