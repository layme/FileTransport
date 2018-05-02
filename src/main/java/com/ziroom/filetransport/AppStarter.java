package com.ziroom.filetransport;

import com.ziroom.filetransport.cache.LocalCache;
import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.gui.MainFrame;
import com.ziroom.filetransport.listener.WorkPortListener;
import com.ziroom.filetransport.model.Terminal;
import com.ziroom.filetransport.listener.BroadcastListener;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
 * @Date Created in 2018年04月25日 17:37
 * @since 1.0
 */
public class AppStarter {
    static {
        String pid = ManagementFactory.getRuntimeMXBean().getName();
        int indexOf = pid.indexOf('@');
        if (indexOf > 0)
        {
            pid = pid.substring(0, indexOf);
        }

        System.out.println("Start with JVM Process ID: " + pid);
    }

    public static void main(String[] args) {
        try {
            init();
        } catch (UnknownHostException e) {
            System.out.println("init error !");
            e.printStackTrace();
        }

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.getFrame().setVisible(true);
                    LocalCache.put(SystemParamConstant.MAIN_FRAME, mainFrame);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void init() throws UnknownHostException {
        // 加载本机信息
        LocalCache.put(SystemParamConstant.MASTER, new Terminal(
                InetAddress.getLocalHost().getHostName().toString(),
                InetAddress.getLocalHost().getHostAddress()
        ));

        LocalCache.put(SystemParamConstant.TERMINAL_MAP, new HashMap<String,Terminal>());

        // 开启广播监听线程
        new Thread(new BroadcastListener("BroadcastListener")).start();

        // 开启工作端口监听线程
        new Thread(new WorkPortListener("WorkPortListener")).start();
    }
}
