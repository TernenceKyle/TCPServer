package com.ezzra.fileserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Test {
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5);
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    String index;
                    barrier.await();
                    try (Socket client = new Socket("127.0.0.1", 10086);) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                        String info = br.readLine();
                        String[] split = info.split(";");
                        System.out.println("===================== File Server ====================");
                        for (String s : split) {
                            String[] select = s.split(",");
                            System.out.println("\t<" + select[1] + ">\t 文件名: " + select[0]);
                        }
                        System.out.println("\t<0>\t\t刷新文件列表");
                        System.out.println("\t<88>\t退出系统");
                        System.err.println("##请输入想要下载的文件编号:");
                        int inde = new Scanner(System.in).nextInt();
                    }
//                    downLoadFile(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }
}
