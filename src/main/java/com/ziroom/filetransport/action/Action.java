package com.ziroom.filetransport.action;

import com.ziroom.filetransport.cache.LocalCache;
import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.exception.ExceptionHandler;
import com.ziroom.filetransport.gui.MainFrame;
import com.ziroom.filetransport.model.Terminal;
import com.ziroom.filetransport.util.NetUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;

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
 * @Date Created in 2018年04月26日 09:10
 * @since 1.0
 */
public class Action {

    private static Action ourInstance = new Action();

    public static Action getInstance() {
        return ourInstance;
    }

    private Action() {
    }

    public void todo() {
    }

    /**
     * 刷新同一局域网内终端列表
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月25日 16:55:40
     */
    public void sendBroadcast() {
        try {
            // 发送广播
            NetUtil.sendBroadcast();

            //监听线程会接收友方回馈
        } catch (UnknownHostException e) {
            ExceptionHandler.alert("广播地址错误", 0);
            e.printStackTrace();
        } catch (SocketException e) {
            ExceptionHandler.alert("广播网络错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("广播IO错误", 0);
            e.printStackTrace();
        }
    }

    public void refreshTerminalList(HashMap<String, Terminal> map) {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JList jList = mainFrame.getTerminalJList();
        jList.removeAll();
        jList.setListData(map.keySet().toArray(new String[map.size()]));
    }

    /**
     * 显示消息
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月26日 17:59:48
     */
    public void showMessage(JTextArea jTextArea, String text) {
        jTextArea.setText(LocalDateTime.now().toString() + " - get info from " + text + "\n" + jTextArea.getText());
    }

    /**
     * 选择文件加入文件列表
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月26日 20:23:54
     */
    public void addFileToList(File file) {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JList jList = mainFrame.getFileJList();
        ListModel<File> listModel = jList.getModel();
        DefaultListModel<File> dlm;
        if (listModel instanceof DefaultListModel) {
            dlm = (DefaultListModel) listModel;
        } else {
            dlm = new DefaultListModel<>();
            jList.setModel(dlm);
        }
        dlm.addElement(file);
    }

    /**
     * 清空文件列表
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月26日 20:36:18
     */
    public void cleanFileJList() {
        DefaultListModel<File> dlm = (DefaultListModel) (((MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME)).getFileJList().getModel());
        dlm.removeAllElements();
    }

    public void sendFile() {
        // 获取链接
        NetUtil.getConnection((Terminal) LocalCache.get(SystemParamConstant.MASTER));

        // 发送文件前准备
        NetUtil.prepareToSend();

        try {
            NetUtil.sendFile();
        } catch (UnknownHostException e) {
            ExceptionHandler.alert("終端IP错误", 0);
            e.printStackTrace();
        } catch (SocketException e) {
            ExceptionHandler.alert("网络链接错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("网络IO错误", 0);
            e.printStackTrace();
        }
        NetUtil.closeConnection();
    }
}
