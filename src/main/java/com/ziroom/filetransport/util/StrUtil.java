package com.ziroom.filetransport.util;

import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;

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

    public static String getFormatFileSize(long length) {
        // 设置数字格式，保留一位有效小数
        DecimalFormat df = new DecimalFormat("#0.0");
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumFractionDigits(1);
        df.setMaximumFractionDigits(1);

//        double size = ((double) length) / (1 << 30);
//        if (size >= 1) {
//            return df.format(size) + " GB";
//        }
//        size = ((double) length) / (1 << 20);
//        if (size >= 1) {
//            return df.format(size) + " MB";
//        }
//        size = ((double) length) / (1 << 10);
//        if (size >= 1) {
//            return df.format(size) + " KB";
//        }
//        return length + " B";

        // MB
        return df.format(((double) length) / (1 << 20));
    }
}
