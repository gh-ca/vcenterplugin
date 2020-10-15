package com.dmeplugin.dmestore.utils;

import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


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

    public final static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getStr(Object obj) {
        String re = null;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.toString();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
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
            LOG.error("error:" + e.toString());
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
            LOG.error("error:"+e.toString());
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
            LOG.error("error:" + e.toString());
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
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static String jsonToStr(JsonElement obj) {
        String re = "";
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsString();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
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
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static String jsonToStr(JsonElement obj, String dedefaultvalue) {
        String re = dedefaultvalue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsString();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }


    public static Integer jsonToInt(JsonElement obj, Integer defaultvalue) {
        Integer re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsInt();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static Integer jsonToInt(JsonElement obj) {
        Integer re = 0;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsInt();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static Long jsonToLon(JsonElement obj, Long defaultvalue) {
        Long re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsLong();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static Double jsonToDou(JsonElement obj, Double defaultvalue) {
        Double re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsDouble();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
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
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static Float jsonToFloat(JsonElement obj, Float defaultvalue) {
        Float re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = obj.getAsFloat();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
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
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static boolean jsonToBoo(JsonElement obj) {
        boolean re = false;
        try {
            if (!StringUtils.isEmpty(obj)) {
                re = obj.getAsBoolean();
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
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
            if (StringUtils.isEmpty(obj) || obj.isJsonNull()) {
                re = true;
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }

    public static String jsonToDateStr(JsonElement obj, String defaultvalue) {
        String re = defaultvalue;
        try {
            if (!StringUtils.isEmpty(obj) && !obj.isJsonNull()) {
                re = sdf.format(new Date(Long.parseLong(String.valueOf(obj.getAsBigInteger()))));
            }
        } catch (Exception e) {
            LOG.error("error:" + e.toString());
        }
        return re;
    }


}
