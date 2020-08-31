package org.game.enums;

import lombok.Getter;

/**
 * 每一个号码的赔率
 */
@Getter
public enum Magnification {

    ZERO(0,1000),
    ONE(1,333),
    TWO(2,166),
    THREE(3,100),
    FOUR(4,66),
    FIVE(5,48),
    SIX(6,36),
    SEVEN(7,28),
    EIGHT(8,22),
    NINE(9,18),
    TEN(10,16),
    ELEVEN(11,15),
    TWELVE(12,14),
    THIRTEEN(13,13),
    FOURTEEN(14,13),
    FIFTEEN(15,14),
    SIXTEEN(16,15),
    SEVENTEEN(17,16),
    EIGHTEEN(18,18),
    NINETEEN(19,22),
    TWENTY(20,28),
    TWENTYONE(21,36),
    TWENTYTWO(22,48),
    TWENTYTHREE(23,66),
    TWENTYFOUR(24,100),
    TWENTYFIVE(25,166),
    TWENTYSIX(26,333),
    TWENTYSEVEN(27,1000);

    private Integer num; //号码

    private Integer pl; //倍率

    Magnification(Integer num,Integer pl){
        this.num = num;
        this.pl = pl;
    }

    public static Integer getPlByNum(Integer num){
        for (Magnification magnification : Magnification.values()) {
            if (magnification.getNum() == num) {
                return magnification.getPl();
            }
        }
        return 0;
    }

}
