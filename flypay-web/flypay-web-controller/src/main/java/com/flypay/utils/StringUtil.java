package com.flypay.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private static final Logger LOGGER = Logger.getLogger(StringUtil.class);
    /**
     * 判断字符串是否为null,"" "  "
     * @param arg
     * @return
     */
    public static boolean hasText(String arg){
        LOGGER.info("对字符串 :|" + arg + "|进行判断是否为空/串");
        if( arg == null || "".equals(arg) || arg.isEmpty() || "null".equals(arg)) return false;
        arg = replaceBlank(arg);
        if("".equals(arg) || arg.isEmpty()) return false;
        return true;
    }

    public static String replaceBlank(String arg) {
        String result= null;
        if (arg == null) {
            return result;
        } else {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(arg);
            result= m.replaceAll("");
            return result;
        }
    }

    public static boolean isNum(String arg){
        return arg.matches("[0-9]+");
    }
}
