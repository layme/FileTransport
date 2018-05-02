package com.ziroom.filetransport.listener;

import com.ziroom.filetransport.exception.ExceptionHandler;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
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
 * @Date Created in 2018年04月26日 21:15
 * @since 1.0
 */
public class FileReceiveWorker implements Runnable {
    private Socket socket;

    public FileReceiveWorker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        FileOutputStream fos = null;

        try {
            // 获得输入流
            dis = new DataInputStream(socket.getInputStream());

            dos = new DataOutputStream(socket.getOutputStream());

            // 文件名和长度
            String fileName = dis.readUTF();
            long fileLength = dis.readLong();

            String msg = "是否接收文件来自" + socket.getInetAddress().getHostAddress() + "的文件" + fileName;

            if (ExceptionHandler.confirm(msg, "提示") == 0) {
                dos.write(1);  // 回复发送端同意接收
                dos.flush();
                // 文件路径选择框
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();

                // 如果选择路径
                if (file != null) {
                    System.out.println("选择路径：" + file.getAbsolutePath());

                    File directory = new File(file.getAbsolutePath());
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    File newFile = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                    fos = new FileOutputStream(newFile);

                    // 开始接收文件
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = dis.read(bytes, 0, bytes.length)) != -1) {
                        fos.write(bytes, 0, length);
                        fos.flush();
                    }
                    System.out.println("==> 文件接收成功 [File Name：" + fileName + "]");
                    dos.write(2);  // 回复发送端接收完毕
                    dos.flush();
                } else {
                    System.out.println("==> 未选择保存路径");
                }
            } else {
                dos.write(-1);  // 回复发送端拒绝接收
                dos.flush();
                System.out.println("==> 拒绝接收文件");
            }
        } catch (SocketException e) {
            ExceptionHandler.alert("网络链接错误", 0);
            e.printStackTrace();
        } catch (IOException e) {
            ExceptionHandler.alert("网络IO错误", 0);
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (dis != null)
                    dis.close();
                if (dos != null)
                    dos.close();
                socket.close();
            } catch (Exception e) {
            }
        }
    }

}
