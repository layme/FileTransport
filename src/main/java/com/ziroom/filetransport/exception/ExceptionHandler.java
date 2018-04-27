package com.ziroom.filetransport.exception;

import com.ziroom.filetransport.cache.LocalCache;
import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.gui.MainFrame;

import javax.swing.*;

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
 * @Date Created in 2018年04月27日 09:28
 * @since 1.0
 */
public class ExceptionHandler {
    private static MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);

    /**
     * 提示窗口
     * @author renhy
     * @created 2018年04月27日 10:19:31
     * @param
     * @return
     */
    public static void alert(String msg, int level) {
        String title = "通知";
        int type = JOptionPane.INFORMATION_MESSAGE;

        switch (level) {
            case 0:
                title = "错误";
                type = JOptionPane.ERROR_MESSAGE;
                break;
            case 1:
                break;
            case 2:
                title = "警告";
                type = JOptionPane.WARNING_MESSAGE;
                break;
            case 3:
                title = "提示";
                type = JOptionPane.QUESTION_MESSAGE;
                break;
            case -1:
                title = "通知";
                type = JOptionPane.PLAIN_MESSAGE;
                break;
            default:
                break;
        }

        JOptionPane.showMessageDialog(mainFrame.getFrame(), msg, title, type);
    }

    /**
     * 确认窗口
     * 0-yes    1-no
     * @author renhy
     * @created 2018年04月27日 10:19:00
     * @param
     * @return
     */
    public static int confirm(String msg, String title) {
        return JOptionPane.showConfirmDialog(mainFrame.getFrame(), msg, title,JOptionPane.YES_NO_OPTION);
    }
}
