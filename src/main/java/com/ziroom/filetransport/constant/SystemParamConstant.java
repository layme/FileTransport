package com.ziroom.filetransport.constant;

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
 * @Date Created in 2018年04月25日 16:22
 * @since 1.0
 */
public class SystemParamConstant {
    public final static Integer BROADCAST_PORT = 9090;  // 广播端口
    public final static String BROADCAST_ADDRESS = "255.255.255.255";  // 广播地址
    public final static String BROADCAST_TEXT = "8a9e999062bd13e50162bd90b8aa0003";  // 广播报文

    public final static Integer WORK_PORT = 9091;  // 工作端口

    public final static String MASTER = "master";  // 本机标识

    public final static char SECRET = 'z';  // 密钥

    public final static String IS_BROADCAST = "0";  // 广播
    public final static String IS_FEEDBACK = "1";   // 回馈信息

    public final static String MAIN_FRAME = "mainFrame";  // 主界面标识

    public final static String TERMINAL_MAP = "terminalMap";  // 终端集合
}
