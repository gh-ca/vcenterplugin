package com.dmeplugin.dmestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * 根据正则表达式、 提取返回正则表达式的内容
     *
     * @param regex
     * @param source
     * @return
     */
    public static String getMatcher(String regex, String source) {
        String result = "";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(source);
        while (matcher.find()) {
            result = matcher.group(0);
            break;
        }
        return result;
    }

    /**
     * 替换pattern占位符  比如：  #{key}
     *
     * @param sourStr 需要替换的模版
     * @param param   参数map，map的key与模版中的key对应
     * @return
     */
    public static String stringFormatFromMap(String pattern, String sourStr, Map<String, Object> param) {
        String targetStr = sourStr;
        if (param == null || param.size() == 0) {
            return targetStr;
        }
        Matcher matcher = Pattern.compile(pattern).matcher(targetStr);
        while (matcher.find()) {
            String key = matcher.group();
            //去掉{}，保留其中的key值
            String keyClone = getMatcher("[a-zA-Z_]+", key);
            Object value = param.get(keyClone);
            //替换
            if (value != null) {
                targetStr = targetStr.replace(key, value.toString());
            }
        }
        return targetStr;
    }

    public static String stringFormat(String pattern, String sourStr, String key, String value) {
        Map map = new HashMap(1);
        map.put(key, value);

        return stringFormatFromMap(pattern, sourStr, map);
    }

}
