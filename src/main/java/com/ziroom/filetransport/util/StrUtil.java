package com.ziroom.filetransport.util;

import java.io.UnsupportedEncodingException;

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
 * @Date Created in 2018年04月25日 19:27
 * @since 1.0
 */
public class StrUtil {
    public static String conversion(String value, char key) {
        // 将要加密的内容转换为字符数组
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // 通过异或运算加密
            chars[i] = (char) (chars[i] ^ key);
        }

        // 返回加密后的字符串
        return new String(chars);
    }
}
