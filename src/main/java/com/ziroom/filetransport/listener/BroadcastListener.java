package com.ziroom.filetransport.listener;

import com.ziroom.filetransport.util.NetUtil;

import java.io.IOException;
import java.net.SocketException;

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
 * @Date Created in 2018年04月25日 20:16
 * @since 1.0
 */
public class BroadcastListener implements Runnable {
    private String id;

    public BroadcastListener(String id) {
        this.id = id;
    }

    public void run() {
        System.out.println("thread " + id + " run...");
        try {
            NetUtil.listening();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
