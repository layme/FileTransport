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
 * @Date Created in 2018年04月27日 10:13
 * @since 1.0
 */
public class WorkPortListener implements Runnable {

    private String id;

    public WorkPortListener(String id) {
        this.id = id;
    }

    public void run() {
        System.out.println("thread " + id + " run...");
        try {
            NetUtil.workPortListening();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
