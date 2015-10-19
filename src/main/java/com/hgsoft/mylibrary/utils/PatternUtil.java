package com.hgsoft.mylibrary.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达工具类
 */
public class PatternUtil {

    /* 校验手机号码 */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /* 校验车牌号码 */
    public static boolean isVehPlate(String vehPlate) {
        Pattern p = Pattern.compile("(?!^\\d+$)(?!^[a-zA-Z]+$)[a-zA-Z0-9]{6}");
        Matcher m = p.matcher(vehPlate);
        return  m.matches();
    }

}
