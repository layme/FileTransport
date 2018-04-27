package com.ziroom.filetransport.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Enumeration;

import com.ziroom.filetransport.action.Action;
import com.ziroom.filetransport.model.Terminal;

/**
 * Created by lanym on 2016/12/10.
 */
public class MainFrame {
    private JFrame frame;  // 主界面

    private JLabel terminaListJLabel;   // 终端列表
    private JLabel messageListJLabel;   // 消息列表
    private JLabel fileListJLabel;      // 文件列表
    private JLabel progressJLabel;      // 进度

    private JLabel countJLabel;      // 文件共_个
    private JLabel sizeJLabel;       // 总大小_MB
    private JLabel sendJLabel;       // 已发送_MB
    private JLabel speedJLabel;      // 速度_KB/S

    private JLabel count;      // 文件个数
    private JLabel size;       // 文件总大小
    private JLabel send;       // 已发送
    private JLabel speed;      // 速度

    private JList<Terminal> terminalJList;  // 终端列表
    private JList<File> fileJList;          // 文件列表

    private JTextArea messageTextArea;      // 消息框

    private JScrollPane terminalJScrollPane;  // 终端列表滚动条
    private JScrollPane messageJScrollPane;   // 消息滚动条
    private JScrollPane fileJScrollPane;      // 文件列表滚动条

    private JButton refreshBtn;   // 刷新终端列表
    private JButton fileBtn;      // 选择文件
    private JButton sendBtn;      // 发送按钮
    private JButton cleanBtn;     // 清楚按钮

    private JProgressBar jProgressBar;  // 进度条

    private JPopupMenu popupMenu = new JPopupMenu();  // 右击菜单

    private Action action;  // 操作

    public MainFrame() {
        initGlobalFont();
        initialize();
    }

    public JFrame getFrame() {
        return frame;
    }

    public JList<Terminal> getTerminalJList() {
        return terminalJList;
    }

    public JList<File> getFileJList() {
        return fileJList;
    }

    public JTextArea getMessageTextArea() {
        return messageTextArea;
    }

    public JLabel getCount() {
        return count;
    }

    public JLabel getSize() {
        return size;
    }

    public JLabel getSend() {
        return send;
    }

    public JLabel getSpeed() {
        return speed;
    }

    private void initGlobalFont() {
        FontUIResource fontUIResource = new FontUIResource(new Font("宋体", Font.PLAIN, 16));
        for (Enumeration<?> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontUIResource);
            }
        }
    }

    private void initialize() {
        //frame setting
        frame = new JFrame();
        frame.setTitle("File Transport");
        frame.getContentPane().setLayout(null);
        frame.setBounds(500, 300, 835, 550);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("icon.jpg");
        frame.setIconImage(icon.getImage());

        //action
        action = Action.getInstance();

        //终端列表 lable
        terminaListJLabel = new JLabel("终端列表");
        terminaListJLabel.setLabelFor(terminalJList);
        terminaListJLabel.setBounds(23, 17, 65, 18);
        frame.getContentPane().add(terminaListJLabel);

        //终端列表 list
        terminalJList = new JList<Terminal>();
        frame.getContentPane().add(terminalJList);

        //终端列表滚动条
        terminalJScrollPane = new JScrollPane();
        terminalJScrollPane.setAutoscrolls(true);
        terminalJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        terminalJScrollPane.setBounds(23, 45, 220, 443);
        frame.getContentPane().add(terminalJScrollPane);
        terminalJScrollPane.setViewportView(terminalJList);

        // 刷新终端列表 btn
        refreshBtn = new JButton("刷新终端列表");
        refreshBtn.setBounds(270, 45, 140, 40);
        frame.getContentPane().add(refreshBtn);
        refreshBtn.addActionListener((e) -> {
            action.sendBroadcast();
        });

        // 选择文件 btn
        fileBtn = new JButton("选择文件(夹)");
        fileBtn.setBounds(438, 45, 140, 40);
        frame.getContentPane().add(fileBtn);
        fileBtn.addActionListener((e) -> {
            JFileChooser jfc = new JFileChooser();

            jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            jfc.showDialog(new JLabel(), "选择");

            File file = jfc.getSelectedFile();
            if (file != null) {
                if (file.isDirectory()) {
                    System.out.println("文件夹:" + file.getAbsolutePath());

                } else if (file.isFile()) {
                    System.out.println("文件:" + file.getAbsolutePath());
                    action.addFileToList(file);
                }
                System.out.println(jfc.getSelectedFile().getName());
            }
        });

        //消息列表 lable
        messageListJLabel = new JLabel("消息列表");
        messageListJLabel.setLabelFor(messageTextArea);
        messageListJLabel.setBounds(602, 17, 65, 18);
        frame.getContentPane().add(messageListJLabel);

        //消息列表 textArea
        messageTextArea = new JTextArea();
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageTextArea.setEnabled(false);
        messageTextArea.setFont(new Font("宋体", Font.PLAIN, 12));
        frame.getContentPane().add(messageTextArea);

        //消息列表滚动条
        messageJScrollPane = new JScrollPane();
        messageJScrollPane.setAutoscrolls(true);
        messageJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        messageJScrollPane.setBounds(602, 45, 194, 83);
        frame.getContentPane().add(messageJScrollPane);
        messageJScrollPane.setViewportView(messageTextArea);

        //文件列表 lable
        fileListJLabel = new JLabel("文件列表");
        fileListJLabel.setLabelFor(fileJList);
        fileListJLabel.setBounds(270, 110, 65, 18);
        frame.getContentPane().add(fileListJLabel);

        popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem("移除")); //添加菜单项

        //文件列表 list
        fileJList = new JList<File>();
        frame.getContentPane().add(fileJList);
        fileJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
            }
        });

        //文件列表滚动条
        fileJScrollPane = new JScrollPane();
        fileJScrollPane.setAutoscrolls(true);
        fileJScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        fileJScrollPane.setBounds(270, 138, 526, 189);
        frame.getContentPane().add(fileJScrollPane);
        fileJScrollPane.setViewportView(fileJList);

        // 发送文件 btn
        sendBtn = new JButton("发送文件");
        sendBtn.setBounds(270, 348, 140, 40);
        frame.getContentPane().add(sendBtn);
        sendBtn.addActionListener((e) -> {
            action.sendFile();
        });

        // 清空文件列表 btn
        cleanBtn = new JButton("清空文件列表");
        cleanBtn.setBounds(439, 348, 140, 40);
        frame.getContentPane().add(cleanBtn);
        cleanBtn.addActionListener((e) -> {
            action.cleanFileJList();
        });

        //进度 lable
        progressJLabel = new JLabel("进度");
        progressJLabel.setLabelFor(jProgressBar);
        progressJLabel.setBounds(270, 418, 33, 18);
        frame.getContentPane().add(progressJLabel);

        // 进度条
        jProgressBar = new JProgressBar();
        jProgressBar.setBounds(270, 446, 526, 25);
        jProgressBar.setMinimum(1);
        jProgressBar.setMaximum(20);
        frame.add(jProgressBar);

        //文件共 lable
        countJLabel = new JLabel("文件共    个");
        countJLabel.setBounds(615, 348, 97, 18);
        frame.getContentPane().add(countJLabel);

        //count lable
        count = new JLabel("0");
        count.setBounds(665, 348, 97, 18);
        frame.getContentPane().add(count);

        //总大小 lable
        sizeJLabel = new JLabel("总大小    MB");
        sizeJLabel.setBounds(615, 370, 97, 18);
        frame.getContentPane().add(sizeJLabel);

        //size lable
        size = new JLabel("0");
        size.setBounds(665, 370, 97, 18);
        frame.getContentPane().add(size);

        //已发送 lable
        sendJLabel = new JLabel("已发送    MB");
        sendJLabel.setBounds(584, 418, 97, 18);
        frame.getContentPane().add(sendJLabel);

        //send lable
        send = new JLabel("0");
        send.setBounds(634, 418, 89, 18);
        frame.getContentPane().add(send);

        //速度 lable
        speedJLabel = new JLabel("速度    KB/S");
        speedJLabel.setBounds(700, 418, 97, 18);
        frame.getContentPane().add(speedJLabel);

        //speed lable
        speed = new JLabel("0");
        speed.setBounds(735, 418, 89, 18);
        frame.getContentPane().add(speed);
    }
}