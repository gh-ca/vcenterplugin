package com.dmeplugin.dmestore.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;


/**
 * @Description: TODO
 * @ClassName: ToolUtils
 * @Company: GH-CA
 * @author: yy
 * @create: 2020-09-02
 **/
public class ToolUtils {

    private final static Logger LOG = LoggerFactory.getLogger(ToolUtils.class);

    public final static String STORE_TYPE_VMFS = "VMFS";
    public final static String STORE_TYPE_NFS = "NFS";
    public final static String STORE_TYPE_ALL = "ALL";

    public final static int KI = 1024;
    public final static int MI = 1024 * 1024;
    public final static int GI = 1024 * 1024 * 1024;

    public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static Gson gson = new Gson();

    public static String getStr(Object obj) {
        return getStr(obj, null);
    }

    public static String getStr(Object obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.toString();
            }
        } catch (Exception e) {
            LOG.error("getStr error:" + e.toString());
        }
        return re;
    }

    public static int getInt(Object obj) {
        int re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Integer.parseInt(obj.toString());
            }
        } catch (Exception e) {
            LOG.error("getInt error:" + e.toString());
        }
        return re;
    }

    public static Integer getInt(Object obj, Integer defaultvalue) {
        Integer re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Integer.parseInt(obj.toString());
            }
        } catch (Exception e) {
            LOG.error("getInt2 error:" + e.toString());
        }
        return re;
    }

    public static double getDouble(Object obj) {
        double re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Double.parseDouble(obj.toString());
            }
        } catch (Exception e) {
            LOG.error("getDouble error:" + e.toString());
        }
        return re;
    }

    public static long getLong(Object obj) {
        long re = 0L;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = Long.parseLong(obj.toString());
            }
        } catch (Exception e) {
            LOG.error("getLong error:" + e.toString());
        }
        return re;
    }

    public static String jsonToStr(JsonElement obj) {
        String re = "";
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (Exception e) {
            LOG.error("jsonToStr error:" + e.toString());
        }
        return re;
    }

    public static String jsonToOriginalStr(JsonElement obj) {
        String re = null;
        try {
            if (null != obj && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (Exception e) {
            LOG.error("jsonToOriginalStr error:" + e.toString());
        }
        return re;
    }

    public static String jsonToStr(JsonElement obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsString();
            }
        } catch (Exception e) {
            LOG.error("jsonToStr2 error:" + e.toString());
        }
        return re;
    }


    public static Integer jsonToInt(JsonElement obj, Integer defaultValue) {
        Integer re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsInt();
            }
        } catch (Exception e) {
            LOG.error("jsonToInt error:" + e.toString());
        }
        return re;
    }

    public static Integer jsonToInt(JsonElement obj) {
        Integer re = 0;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsInt();
            }
        } catch (Exception e) {
            LOG.error("jsonToInt2 error:" + e.toString());
        }
        return re;
    }

    public static Long jsonToLon(JsonElement obj, Long defaultValue) {
        Long re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsLong();
            }
        } catch (Exception e) {
            LOG.error("jsonToLon error:" + e.toString());
        }
        return re;
    }

    public static Double jsonToDou(JsonElement obj, Double defaultValue) {
        Double re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsDouble();
            }
        } catch (Exception e) {
            LOG.error("jsonToDou error:" + e.toString());
        }
        return re;
    }

    public static Double jsonToDou(JsonElement obj) {
        Double re = 0.00;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsDouble();
            }
        } catch (Exception e) {
            LOG.error("jsonToDou2 error:" + e.toString());
        }
        return re;
    }

    public static Float jsonToFloat(JsonElement obj, Float defaultValue) {
        Float re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsFloat();
            }
        } catch (Exception e) {
            LOG.error("jsonToFloat error:" + e.toString());
        }
        return re;
    }

    public static Float jsonToFloat(JsonElement obj) {
        Float re = 0.0F;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsFloat();
            }
        } catch (Exception e) {
            LOG.error("jsonToFloat2 error:" + e.toString());
        }
        return re;
    }

    public static boolean jsonToBoo(JsonElement obj) {
        boolean re = false;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsBoolean();
            }
        } catch (Exception e) {
            LOG.error("jsonToBoo error:" + e.toString());
        }
        return re;
    }

    public static Boolean jsonToBoo(JsonElement obj, Boolean defaultVal) {
        Boolean re = defaultVal;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsBoolean();
            }
        } catch (Exception e) {
            LOG.error("jsonToBoo error:" + e.toString());
        }
        return re;
    }

    public static <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean jsonIsNull(JsonElement obj) {
        boolean re = false;
        try {
            if (StringUtils.isEmpty(obj) || obj.isJsonNull() || "{}".equals(gson.toJson(obj))) {
                re = true;
            }
        } catch (Exception e) {
            LOG.error("jsonIsNull error:" + e.toString());
        }
        return re;
    }

    public static String jsonToDateStr(JsonElement obj, String defaultValue) {
        String re = defaultValue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = SIMPLE_DATE_FORMAT.format(new Date(Long.parseLong(String.valueOf(obj.getAsBigInteger()))));
            }
        } catch (Exception e) {
            LOG.error("jsonToDateStr error:" + e.toString());
        }
        return re;
    }

    //从性能数据中提取对应指标的对应值
    public static JsonElement getStatistcValue(JsonObject statisticObject, String indicator, String type) {
        JsonElement object = null;
        if (null != statisticObject) {
            JsonElement indicatorEl = statisticObject.get(indicator);
            if (!ToolUtils.jsonIsNull(indicatorEl)) {
                JsonObject indicatorJson = indicatorEl.getAsJsonObject();
                JsonElement typeEl = indicatorJson.get(type);
                if (!ToolUtils.jsonIsNull(typeEl)) {
                    JsonObject typeJson = typeEl.getAsJsonObject();
                    Set<Map.Entry<String, JsonElement>> sets = typeJson.entrySet();
                    for (Map.Entry<String, JsonElement> set : sets) {
                        //只取一个
                        object = set.getValue();
                        break;
                    }
                }
            }
        }
        return object;
    }

    public static String getRequsetParams(String paramName, String paramValue) {
        JsonObject condition = new JsonObject();
        JsonArray constraint = new JsonArray();
        JsonObject consObj = new JsonObject();
        JsonObject simple = new JsonObject();
        simple.addProperty("name", "dataStatus");
        simple.addProperty("operator", "equal");
        simple.addProperty("value", "normal");
        consObj.add("simple", simple);
        constraint.add(consObj);
        JsonObject consObj1 = new JsonObject();
        JsonObject simple1 = new JsonObject();
        simple1.addProperty("name", paramName);
        simple1.addProperty("operator", "equal");
        simple1.addProperty("value", paramValue);
        consObj1.add("simple", simple1);
        consObj1.addProperty("logOp", "and");
        constraint.add(consObj1);
        condition.add("constraint", constraint);
        return condition.toString();
    }


}
