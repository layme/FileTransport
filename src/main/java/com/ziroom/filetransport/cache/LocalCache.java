package com.ziroom.filetransport.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p></p>
 * <p>
 * <PRE>
 * <BR>    修改记录
 * <BR>-----------------------------------------------
 * <BR>    修改日期         修改人          修改内容
 * </PRE>
 *
 * @author renhy
 * @version 1.0
 * @Date Created in 2018年04月25日 16:32
 * @since 1.0
 */
public class LocalCache {
    private static Map<String, Object> cache = new HashMap<String, Object>();

    public static void put(String key, Object value) {
        cache.put(key, value);
    }

    public static void putAll(Map<String, Object> m) {
        cache.putAll(m);
    }


    public static Object get(String key) {
        return cache.get(key);
    }

    public static boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public static int size() {
        return cache.size();
    }

    public static void remove(String key) {
        cache.remove(key);
    }

    public static Set<String> keySet() {
        return cache.keySet();
    }
}
