package com.ziroom.filetransport.action;

import com.ziroom.filetransport.cache.LocalCache;
import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.exception.ExceptionHandler;
import com.ziroom.filetransport.gui.MainFrame;
import com.ziroom.filetransport.model.Terminal;
import com.ziroom.filetransport.util.NetUtil;
import com.ziroom.filetransport.util.StrUtil;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
        JList<String> jList = mainFrame.getTerminalJList();
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
    public void showMessage(String text) {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JTextArea jTextArea = mainFrame.getMessageTextArea();
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

        updateFileCountAndSize(dlm, file);
    }

    /**
     * 选择文件夹下所有文件加入文件列表
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月26日 20:23:54
     */
    public void addFolderToList(File file) {
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
        File[] fileList = file.listFiles();  // 将该目录下的所有文件放置在一个File类型的数组中
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isFile()) {
                dlm.addElement(fileList[i]);
                updateFileCountAndSize(dlm, fileList[i]);
            }
        }

    }

    /**
     * 移除一条记录
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年05月02日 15:34:28
     */
    public void removeIndexAt(int i) {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JList jList = mainFrame.getFileJList();
        DefaultListModel<File> dlm = (DefaultListModel<File>) jList.getModel();
        dlm.remove(i);
        mainFrame.getSize().setText("0 MB");          // 文件总大小归0
        for (int j = 0; j < dlm.size(); j++) {
            updateFileCountAndSize(dlm, dlm.elementAt(j));
        }
        if ("0 MB".equals(mainFrame.getSize().getText())) {
            mainFrame.getCount().setText("0 个");         // 文件个数归0
        }
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
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JList jList = mainFrame.getFileJList();
        ListModel<File> listModel = jList.getModel();
        if (listModel.getSize() > 0) {
            ((DefaultListModel) listModel).removeAllElements();
        }

        mainFrame.getCount().setText("0 个");         // 文件个数归0
        mainFrame.getSize().setText("0 MB");          // 文件总大小归0
        mainFrame.getjProgressBar().setValue(0);      // 文件总大小归0
    }

    /**
     * 发送文件
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年05月02日 11:43:39
     */
    public void sendFile() {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);
        JList<String> terminalList = mainFrame.getTerminalJList();
        if (terminalList.getModel().getSize() == 0) {
            ExceptionHandler.alert("没有终端数据，请刷新后再试", 2);
            return;
        }

        JList<File> fileList = mainFrame.getFileJList();
        if (fileList.getModel().getSize() == 0) {
            ExceptionHandler.alert("请添加文件至待发送文件列表", 2);
            return;
        }

        // 获取链接
        int index = terminalList.getSelectedIndex();
        if (index == -1) {
            ExceptionHandler.alert("请选择一个终端", 2);
            return;
        }
        NetUtil.getConnection(
                ((HashMap<String, Terminal>) LocalCache.get(SystemParamConstant.TERMINAL_MAP))
                        .get(
                                terminalList.getModel().getElementAt(index)
                        )
        );

        // 初始化流
        NetUtil.initStream();

        // 获取文件
        DefaultListModel<File> dlmFile = (DefaultListModel<File>) fileList.getModel();

        try {
            for (int i = 0; i < dlmFile.size(); i++) {
                NetUtil.sendFile(dlmFile.getElementAt(i));
            }
        } catch (UnknownHostException e) {
            ExceptionHandler.alert("終端IP错误", 0);
            e.printStackTrace();
        } catch (SocketException e) {
            ExceptionHandler.alert("网络链接错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("网络IO错误", 0);
            e.printStackTrace();
        } finally {
            NetUtil.closeConnection();
        }
    }

    /**
     * 更新进度条
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年05月02日 11:43:53
     */
    public void updateProgressBar(int i) {
        ((MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME)).getjProgressBar().setValue(i);
    }

    /**
     * 更新文件格式和总大小
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年05月02日 14:50:13
     */
    private void updateFileCountAndSize(DefaultListModel<File> dlm, File file) {
        MainFrame mainFrame = (MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME);

        mainFrame.getCount().setText(String.valueOf(dlm.size()) + " 个");  // 文件个数

        JLabel jLabel = mainFrame.getSize();
        String str = jLabel.getText();
        String fileSize = StrUtil.getFormatFileSize(file.length());
        Double len = Double.valueOf(str.substring(0, str.indexOf(' '))) + Double.valueOf(fileSize);
        jLabel.setText(String.valueOf(len) + " MB");  // 文件总大小
    }
}
