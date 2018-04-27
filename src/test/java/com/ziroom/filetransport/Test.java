package com.ziroom.filetransport;

import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.util.StrUtil;

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
 * @Date Created in 2018年04月27日 17:27
 * @since 1.0
 */
public class Test {
    @org.junit.Test
    public void secret() {
        char a1 = '坚', a2 = '持', a3 = '不', a4 = '懈';
        char secret = '1';
        a1 = (char) (a1 ^ secret);
        a2 = (char) (a2 ^ secret);
        a3 = (char) (a3 ^ secret);
        a4 = (char) (a4 ^ secret);
        System.out.println("密文:" + a1 + a2 + a3 + a4);
        a1 = (char) (a1 ^ secret);
        a2 = (char) (a2 ^ secret);
        a3 = (char) (a3 ^ secret);
        a4 = (char) (a4 ^ secret);
        System.out.println("原文:" + a1 + a2 + a3 + a4);
    }

    @org.junit.Test
    public void serect1() {
        String msg = "我们的爱轻得像空气";
        System.out.println("原文:" + msg);
        System.out.println("密文:" + StrUtil.conversion(msg, SystemParamConstant.SECRET));
        System.out.println("反译:" + StrUtil.conversion(StrUtil.conversion(msg, SystemParamConstant.SECRET), SystemParamConstant.SECRET));
    }
}
