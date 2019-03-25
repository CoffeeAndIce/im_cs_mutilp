package utils;

import java.util.TimeZone;

/**
*  @Author:      Coffeeanice
* @Description:   TODO: 包装常用工具类
* @Date:     2019/3/4 19:06
*/
public class CommonUtils {

    private final int Current_Millisecond = 1;

    private final int Today_Init_Millisecond = 2;

    private final int Today_Last_Millisecond = 3;
    /**
     * 获取时间
     * type
     * 0、当前毫秒数
     * 1、当天0点0分0秒的毫秒数
     * 2、今天23点59分59秒的毫秒数
     * @return
     *
     */
    public  static Long actTime (int type){
        //当前时间毫秒数
        Long current=System.currentTimeMillis();
        //今天零点零分零秒的毫秒数
        Long zero=current/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset();
        //今天23点59分59秒的毫秒数
        Long twelve=zero+24*60*60*1000-1;
        switch (type) {
            case 0:return current;
            case 1:return zero;
            case 2:return twelve;
            default: return null;
        }

    }
}
