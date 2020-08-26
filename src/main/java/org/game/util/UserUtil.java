package org.game.util;

import org.game.pojo.UserInfo;
import org.game.dao.UserDao;
import org.game.enums.Magnification;
import org.game.filter.JWTToken;
import org.game.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-07-20  18:31
 */
public class UserUtil {

    public synchronized static User getUserByReq(HttpServletRequest req, UserDao userDao) {
        String xAuthToken = req.getHeader("x-auth-token");
        User user = JWTToken.getUserFromJwt(xAuthToken);
        if(user == null) return null;
        user = userDao.findById(user.getId()).get();
        return user;
    }

    public static List<UserInfo> init(Integer num , Integer gameId ){
        List<UserInfo> list = new LinkedList<>();
        int length = MD5.random.nextInt(180+50);
        for(int i = 0; i<= length ; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(UserNameUtil.getName()+"");
            userInfo.setGameId(gameId);
            userInfo.setNum(num);
            userInfo.setHeadImg(MD5.random.nextInt(15)+".jpg");
            Integer tze = MD5.random.nextInt((i+10)*350);
            userInfo.setTz(tze);
            userInfo.setHl((tze/ Magnification.getPlByNum(num) +200+ MD5.random.nextInt(i+100))*Magnification.getPlByNum(num));
            userInfo.setYl(userInfo.getHl()-userInfo.getTz());
            list.add(userInfo);
        }
        return list;
    }
}
