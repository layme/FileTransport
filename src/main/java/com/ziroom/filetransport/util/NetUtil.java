package com.ziroom.filetransport.util;

import com.alibaba.fastjson.JSON;
import com.ziroom.filetransport.action.Action;
import com.ziroom.filetransport.cache.LocalCache;
import com.ziroom.filetransport.constant.SystemParamConstant;
import com.ziroom.filetransport.exception.ExceptionHandler;
import com.ziroom.filetransport.gui.MainFrame;
import com.ziroom.filetransport.listener.FileReceiveWorker;
import com.ziroom.filetransport.model.Terminal;

import javax.swing.*;
import java.io.*;
import java.net.*;
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
 * @Date Created in 2018年04月25日 16:39
 * @since 1.0
 */
public class NetUtil {
    private static Socket client;
    private static OutputStream os;

    /**
     * 发送广播
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月25日 16:56:01
     */
    public static void sendBroadcast() throws IOException {
        // 广播的实现 :由客户端发出广播
        String message = SystemParamConstant.BROADCAST_TEXT +
                JSON.toJSONString(LocalCache.get(SystemParamConstant.MASTER)) +
                SystemParamConstant.IS_BROADCAST;

        // 信息加密
        String token = StrUtil.conversion(message, SystemParamConstant.SECRET);

        InetAddress adds = InetAddress.getByName(SystemParamConstant.BROADCAST_ADDRESS);
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(token.getBytes(),
                token.length(), adds, SystemParamConstant.BROADCAST_PORT);
        ds.send(dp);
        System.out.println("send broadcast...");
        ds.close();
    }

    /**
     * 监听广播
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月25日 16:56:32
     */
    public static void listening() throws IOException {
        while (true) {
            byte[] buf = new byte[1024];//存储发来的消息
            StringBuffer sbuf = new StringBuffer();

            //绑定端口的
            DatagramSocket ds = new DatagramSocket(SystemParamConstant.BROADCAST_PORT);
            DatagramPacket dp = new DatagramPacket(buf, buf.length);
            System.out.println("enable listening port");
            ds.receive(dp);
            ds.close();
            for (int i = 0; i < 1024; i++) {
                if (buf[i] == 0) {
                    break;
                }
                sbuf.append((char) buf[i]);
            }
            String token = sbuf.toString();
            System.out.println("get broadcast ==> " + token);
            String msg = StrUtil.conversion(token, SystemParamConstant.SECRET);

            if (SystemParamConstant.BROADCAST_TEXT.equals(msg.substring(0, SystemParamConstant.BROADCAST_TEXT.length()))) {
                Terminal t = JSON.parseObject(msg.substring(SystemParamConstant.BROADCAST_TEXT.length(), msg.length() - 1), Terminal.class);
                System.out.println(t.toString());

                Action action = Action.getInstance();

                // 添加消息
                action.showMessage(
                        ((MainFrame) LocalCache.get(SystemParamConstant.MAIN_FRAME)).getMessageTextArea(),
                        t.getIp()
                );


                // 判断是否是本机
                if (!InetAddress.getLocalHost().getHostAddress().equals(t.getIp())) {
                    // 加入本地缓存
                    ((HashMap<String, Terminal>) LocalCache.get(SystemParamConstant.TERMINAL_MAP)).put(t.getName(), t);

                    // 刷新终端列表
                    action.refreshTerminalList((HashMap<String, Terminal>) (LocalCache.get(SystemParamConstant.TERMINAL_MAP)));

                    // 如果接收到的是广播，则发回馈信息
                    if (SystemParamConstant.IS_BROADCAST.equals(msg.substring(msg.length() - 1))) {
                        feedback(t.getIp());
                    }
                }
            }
        }
    }

    /**
     * 回馈广播
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月25日 16:56:32
     */
    public static void feedback(String ip) throws IOException {
        // 回馈自身信息
        String message = SystemParamConstant.BROADCAST_TEXT +
                JSON.toJSONString(LocalCache.get(SystemParamConstant.MASTER)) +
                SystemParamConstant.IS_FEEDBACK;

        // 信息加密
        String token = StrUtil.conversion(message, SystemParamConstant.SECRET);

        InetAddress adds = InetAddress.getByName(ip);
        DatagramSocket ds = new DatagramSocket();
        DatagramPacket dp = new DatagramPacket(token.getBytes(),
                token.length(), adds, SystemParamConstant.BROADCAST_PORT);
        ds.send(dp);
        System.out.println("feedback broadcast ==> " + token);
        ds.close();
    }

    /**
     * 获取tcp链接
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 10:42:32
     */
    public static void getConnection(Terminal t) {
        try {
            client = new Socket(t.getIp(), SystemParamConstant.WORK_PORT);
            System.out.println("connect to server succeed");
        } catch (UnknownHostException e) {
            ExceptionHandler.alert("IP错误", 0);
            e.printStackTrace();
        } catch (SocketException e) {
            ExceptionHandler.alert("网络错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("未知错误", 0);
            e.printStackTrace();
        }
    }

    /**
     * 发送文件前的初始化
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 10:42:46
     */
    public static void prepareToSend() {
        try {
            //得到socket读写流
            os = client.getOutputStream();
            System.out.println("getOutputStream succeed");
        } catch (IOException e) {
            ExceptionHandler.alert("无法网络获取输出流", 0);
            e.printStackTrace();
        }
    }

    /**
     * 询问对方是否统一接收文件
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 18:19:03
     */
    public static boolean isAgree() {
        System.out.println("ask is agree");
        boolean flag = false;
        String msg = "是否同意接收" + ((Terminal) LocalCache.get(SystemParamConstant.MASTER)).getName() + "发来的文件(夹)？";
        System.out.println(msg);
        try {
            transport(StrUtil.conversion(msg, SystemParamConstant.SECRET));
        } catch (SocketException e) {
            ExceptionHandler.alert("套接字错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("IO错误", 0);
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 发送文件
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 10:46:19
     */
    public static void sendFile() throws IOException {
        File file = new File("target/classes/icon.jpg");
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            DataOutputStream dos = new DataOutputStream(os);
            DataInputStream dis = new DataInputStream(client.getInputStream());

            // 文件名和长度
            dos.writeUTF(file.getName());
            dos.flush();
            dos.writeLong(file.length());
            dos.flush();
            transport(dos, fis, file.length());

//            int i = 0;
//
//            while ((i = dis.readInt()) == 0) { }
//
//            if (i == 1) {
//                transport(dos, fis, file.length());
//            } else if (i == -1) {
//                ExceptionHandler.alert("对方拒绝接收文件", 3);
//            } else {
//                ExceptionHandler.alert("其他未知错误", 3);
//            }
//
//            while ((i = dis.readInt()) == 0) { }
//            if (i == 2) {
//                NetUtil.closeConnection();
//            } else {
//                try {
//                    Thread.sleep(10000);
//                    NetUtil.closeConnection();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
            fis.close();
        }
    }

    /**
     * 真正传输文件
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 11:36:04
     */
    public static void transport(DataOutputStream dos, InputStream is, long fileLength) throws IOException {
        // 开始传输文件
        System.out.println("==> 开始传输文件");
        byte[] bytes = new byte[1024];
        int length = 0;
        long progress = 0;
        while ((length = is.read(bytes, 0, bytes.length)) != -1) {
            dos.write(bytes, 0, length);
            dos.flush();
            progress += length;
            System.out.print("| " + (100 * progress / fileLength) + "% |");
        }
        System.out.println();
        System.out.println("==> 文件传输成功");
    }

    /**
     * 真正传输字符
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 11:36:04
     */
    public static void transport(String token) throws IOException {
        System.out.println(token);
        // 开始传输文件
        System.out.println("==> 开始传输字符");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
        bw.write(token);
        bw.flush();
        System.out.println("==> 字符传输成功");
    }

    /**
     * 服务端接收文件
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月26日 21:23:20
     */
    public static void workPortListening() throws IOException {
        ServerSocket serverSocket = new ServerSocket(SystemParamConstant.WORK_PORT);
        System.out.println("enable listening port");
        Socket socket;
        while (true) {
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
            socket = serverSocket.accept();

            System.out.println("handle a connection");

            /**
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后，
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能，
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式
             *
             * 每接收到一个Socket就建立一个新的线程来处理它
             */
            new Thread(new FileReceiveWorker(socket)).start();
        }
    }

    /**
     * 关闭tcp链接
     *
     * @param
     * @return
     * @author renhy
     * @created 2018年04月27日 10:45:57
     */
    public static void closeConnection() {
        try {
            if (client != null) {
                client.close();
            }
            if (os != null) {
                os.close();
            }
            System.out.println("send connection closed");
        } catch (IOException e) {
            ExceptionHandler.alert("关闭链接失败", 0);
            e.printStackTrace();
        }
    }
}
