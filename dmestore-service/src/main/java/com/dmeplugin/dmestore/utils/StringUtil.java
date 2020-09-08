package com.dmeplugin.dmestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);


    private static Charset preferredACSCharset;
    private static final String UTF8 = "UTF-8";
    static {
        if (isUtf8Supported()) {
            preferredACSCharset = Charset.forName(UTF8);
        } else {
            preferredACSCharset = Charset.defaultCharset();
        }
    }


    public static boolean isUtf8Supported() {
        return Charset.isSupported(UTF8);
    }

    protected static Charset getDefaultCharset() {
        return Charset.defaultCharset();
    }


    public static Charset getPreferredCharset() {
        return preferredACSCharset;
    }
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

    /**
     * Returns {@code true} if the given string is null or is the empty string.
     *
     * <p>Consider normalizing your string references with {@link #nullToEmpty}.
     * If you do, you can use {@link String#isEmpty()} instead of this
     * method, and you won't need special null-safe forms of methods like {@link
     * String#toUpperCase} either. Or, if you'd like to normalize "in the other
     * direction," converting empty strings to {@code null}, you can use {@link
     * #emptyToNull}.
     *
     * @param string a string reference to check
     * @return {@code true} if the string is null or is the empty string
     */
    public static boolean isNullOrEmpty( String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String join(final Iterable<? extends Object> iterable, final String delim) {
        final StringBuilder sb = new StringBuilder();
        if (iterable != null) {
            final Iterator<? extends Object> iter = iterable.iterator();
            if (iter.hasNext()) {
                final Object next = iter.next();
                sb.append(next.toString());
            }
            while (iter.hasNext()) {
                final Object next = iter.next();
                sb.append(delim + next.toString());
            }
        }
        return sb.toString();
    }
}
