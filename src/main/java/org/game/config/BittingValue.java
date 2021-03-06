package org.game.config;

import com.alibaba.fastjson.JSON;
import org.game.pojo.Betting;
import org.game.pojo.BettingModel;
import org.game.pojo.Game;
import org.game.service.BettingService;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 开奖变量池
 * 没把开奖之前累加投注金额，开奖之后清空为初始值
 */

public class BittingValue {

    public static boolean flagModel = true;     //是否开启获利模式 true开启，false关闭

    public static Map<Integer,BettingModel> bettingModelList = new LinkedHashMap<>();  //每一把的用户自动投注

    public static Map<Integer,Map<String, Integer>> modelCon = new LinkedHashMap<>();   //用户押注配置
    public static Map<Integer,Map<String, Integer>> startModelCon = new LinkedHashMap<>();   //用户押注配置

    public static Integer moneyPool = 0; //奖金池
    public static Integer moneyPool2 = 0; //奖金池
    public static Integer moneyPool3 = 0; //奖金池

    /**
     * 每局投注的数字和金额的总和，每一个用户投注都会向里面添加
     */
    public static Map<Integer,Map<Integer, Integer>> betMap=new LinkedHashMap<>();
    /**
     * 未开始的用户投资信息
     */
    public static Map<Integer,Map<Integer,Map<Integer, Integer>>> betMapf=new LinkedHashMap<>();
    /**
     * 上一把用户的投注金额，方便续压
     */
    public static Map<Integer,Map<Integer, Integer>> betMap2=new LinkedHashMap<>();

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Game game = new Game();

    public final static double maxRate = 1.01;
    public final static double minRate = 0.0;

    public static Boolean falg = true;  //每一把是否开奖 未开奖是true，已开奖是false

    /*public final static Integer[] nums = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27};*/

    public static LinkedHashMap<Integer,Double> ylMap;
    public static LinkedHashMap<Integer,Integer> jeMap;
    public static LinkedHashMap<Integer,Double> returnMap;

    /**
     * 每一把初始化
     */
    public static void initBittingValue(Game game, BettingService bettingService){
        System.out.println("XXX"+betMap);
        System.out.println("XXX"+betMap2);
        BittingValue.moneyPool = BittingValue.moneyPool2;
        BittingValue.moneyPool2 = BittingValue.moneyPool3;
        BittingValue.moneyPool3 = 0;
        bettingModelList = new LinkedHashMap<>();
        /**
         * 把上一把的押注金额放入到betMap2中方便续压
         */
        if(null!=betMap && !betMap.isEmpty()) {
            BittingValue.betMap2 = new LinkedHashMap<>();
            BittingValue.betMap2.putAll(betMap);

            List<Betting> bettingList = new ArrayList<>();
            for (Integer integer : BittingValue.betMap.keySet()) {
                Betting betting = new Betting(integer, game.getId(), JSON.toJSONString(betMap.get(integer)));
                bettingList.add(betting);
            }
            bettingService.saveAll(bettingList);
        }
        jeMap = new LinkedHashMap<Integer,Integer>(){
            {
                put(0, 0);
                put(1, 0);
                put(2, 0);
                put(3, 0);
                put(4, 0);
                put(5, 0);
                put(6, 0);
                put(7, 0);
                put(8, 0);
                put(9, 0);
                put(10, 0);
                put(11, 0);
                put(12, 0);
                put(13, 0);
                put(14, 0);
                put(15, 0);
                put(16, 0);
                put(17, 0);
                put(18, 0);
                put(19, 0);
                put(20, 0);
                put(21, 0);
                put(22, 0);
                put(23, 0);
                put(24, 0);
                put(25, 0);
                put(26, 0);
                put(27, 0);
            }};
        ylMap = new LinkedHashMap<Integer,Double>(){
            {
            put(0,0.00d);put(1,0.00d);put(2,0.00d);put(3,0.00d);put(4,0.00d);
            put(5,0.00d);put(6,0.00d);put(7,0.00d);put(8,0.00d);put(9,0.00d);
            put(10,0.00d);put(11,0.00d);put(12,0.00d);put(13,0.00d);put(14,0.00d);
            put(15,0.00d);put(16,0.00d);put(17,0.00d);put(18,0.00d);put(19,0.00d);
            put(20,0.00d);put(21,0.00d);put(22,0.00d);put(23,0.00d);put(24,0.00d);
            put(25,0.00d);put(26,0.00d);put(27,0.00d);}};
        betMap = Collections.synchronizedMap(new HashMap<>());
        returnMap = new LinkedHashMap<>();
        falg = true;
        System.out.println("DDD"+betMap);
        System.out.println("DDD"+betMap2);
    }

}
