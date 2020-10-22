package org.game.util;

import org.game.dao.AdminUserDao;
import org.game.pojo.AdminUser;
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

    public synchronized static AdminUser getAdminUserByReq(HttpServletRequest req, AdminUserDao adminUserDao) {
        String xAuthToken = req.getHeader("x-auth-token");
        AdminUser user = JWTToken.getAdminUserFromJwt(xAuthToken);
        if(user == null) return null;
        user = adminUserDao.findById(user.getId()).get();
        return user;
    }

    public static List<UserInfo> init(Integer num , Integer gameId ){
        List<UserInfo> list = new LinkedList<>();

        List<String> nameList = UserNameUtil.getNameList();
        for(int i = 0; i< nameList.size() ; i++){
            UserInfo userInfo = new UserInfo();
            userInfo.setUserName(nameList.get(i)+UserNameUtil.lastNames[i]);
            userInfo.setGameId(gameId);
            userInfo.setNum(num);
            userInfo.setHeadImg(MD5.random.nextInt(1032)+".jpg");
            Integer tze = MD5.random.nextInt((i+100)*10*350*3)+987654;
            if(tze>100000000){
                tze = 100000000;
            }
            userInfo.setTz(tze);

            if(tze > 80000000){
                userInfo.setHl(tze * (MD5.random.nextInt(2)+1)/2 * (MD5.random.nextInt(2)+1) + tze);
            }else {
                userInfo.setHl(tze * (MD5.random.nextInt(5)+1)/3 * (MD5.random.nextInt(5)+1) + tze);
            }

////            userInfo.setHl((tze/ Magnification.getPlByNum(num) + MD5.random.nextInt(i+100))*Magnification.getPlByNum(num));
////            userInfo.setHl((tze * Magnification.getPlByNum(num) / (MD5.random.nextInt(10)+5)) + Magnification.getPlByNum(num) * (MD5.random.nextInt(i+1)+1000));
//            Integer bb = (MD5.random.nextInt(5)+1);
//            userInfo.setHl((tze * (bb/2)) + Magnification.getPlByNum(num) * (MD5.random.nextInt(i+1)+1000)+tze);
            userInfo.setYl(userInfo.getHl()-userInfo.getTz());
            list.add(userInfo);
        }
        return list;
    }
}
