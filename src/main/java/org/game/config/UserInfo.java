package org.game.config;

import lombok.Data;
import org.game.enums.Magnification;
import org.game.util.MD5;
import org.game.util.UserNameUtil;
import org.game.util.UserUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhouyf
 * @Data 2020-08-13  21:48
 * 模拟的用户数据
 */
@Data
public class UserInfo {

    private String userName;

    private String headImg;
    //投注
    private Integer tz;
    //获利
    private Integer hl;

    private Integer gameId;

    private Integer num;

    public static List<UserInfo> init(Integer num , Integer gameId ){
        List<UserInfo> list = new LinkedList<>();

        for(int i = 0; i<= 200 ; i++){
            UserInfo userInfo = new UserInfo();
           userInfo.setUserName(UserNameUtil.getName()+"");
           userInfo.setGameId(gameId);
           userInfo.setNum(num);
           userInfo.setHeadImg(MD5.random.nextInt(15)+".jpg");
           Integer tze = MD5.random.nextInt((i+10)*350);
           userInfo.setTz(tze);
           userInfo.setHl((tze/Magnification.getPlByNum(num) +200+ MD5.random.nextInt(i+100))*Magnification.getPlByNum(num));
           list.add(userInfo);
        }

        return list;
    }

}
