package com.ezzra.translatepro;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TranslateServer {
    //单词properties文件
    private static Properties wordsMap = new Properties();

    //初始化word映射
    static {
        try (final InputStream rin = TranslateServer.class.getClassLoader().getResourceAsStream("WordsMap.properties");
             InputStreamReader streamReader = new InputStreamReader(rin, "GBK");
        ) {
            wordsMap.load(streamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //自定义线程池
    private static ThreadPoolExecutor pool = new ThreadPoolExecutor(3, 10, 10, TimeUnit.MINUTES,
            new ArrayBlockingQueue<>(10), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws IOException {
        //开启服务
        startService();
    }
    public static void startService() throws IOException {
        //创建服务端的ServerSocket
        ServerSocket server = new ServerSocket(40143);
        //开启之后一直服务
        while (true) {
            //获取socket链接
            Socket accept = server.accept();
            //给线程池提交服务
            pool.submit(() -> {
                try(accept){
                    BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
                    //向客户端写入信息
                    bw.write("欢迎使用 Ciba 翻译工具！ 请输入需要翻译的英文单词:");
                    //添加信息结尾
                    bw.newLine();
                    //刷新缓冲区数据到网络输出流
                    bw.flush();
                    String s = br.readLine().toLowerCase().trim();
                    String res;
                    System.out.println("client : " + s);
                    if (wordsMap.containsKey(s)) {
                        res = Thread.currentThread().getName() + " ==>> "+wordsMap.getProperty(s);
                    } else {
                        res = Thread.currentThread().getName() +" ==>> "+ "词库中没有相关单词的记录";
                    }
                    bw.write(res);
                    bw.newLine();
                    bw.flush();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });

        }
    }
}
