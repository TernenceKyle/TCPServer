package com.ezzra.fileserver;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
//@Config(port = 1022)
public class FileServer {
    /*
    * 一个简易的tcp嵌套字实现的文件下载系统
    * */
    public static ArrayList<File> fileList = new ArrayList<>();
    //服务器资源文件夹位置
    private final static File filePackage = new File("C:\\Users\\Ezzra\\Desktop\\FileService");
    //两个负责不同功能模块的端口
    private static ServerSocket serverTF;
    private static ServerSocket serverFL;
    //自定义的线程池
    private static ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(3, 5, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(5), new ThreadPoolExecutor.CallerRunsPolicy());
    //初始化静态的对象
    static {
        scannFile(filePackage, fileList);
        Properties prop = new Properties();
        try {
            InputStream in = FileServer.class.getClassLoader().getResourceAsStream("config.properties");
            prop.load(in);
            in.close();
            serverTF = new ServerSocket(Integer.parseInt(prop.getProperty("port2")));
            serverFL = new ServerSocket(Integer.parseInt(prop.getProperty("port1")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        //开启服务向客户端传送文件列表
        stratService();
        //传输文件服务
        transFile();
    }
    //扫描文件夹下的所有文件
    public static void scannFile(File target, ArrayList<File> list) {
        if (target.isFile()) list.add(target);
        else {
            File[] files = target.listFiles();
            for (File f : files) {
                scannFile(f, list);
            }
        }
    }
    private static void stratService() throws IOException {
        poolExecutor.submit(()->{
            System.out.println(Thread.currentThread().getName() + " 开始提供文件列表服务...");
            while (true) {
                Socket accept = serverFL.accept();
                poolExecutor.submit(()->{
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(accept.getOutputStream()));
                        Iterator<File> iterator = fileList.iterator();
                        int count = 1;
                        StringBuilder sb = new StringBuilder();
                        for (File file : fileList) {
                            sb.append(file.getName() + "," + count + ";");
                            count++;
                        }
                        bw.write(sb.toString());
                        bw.newLine();
                        System.out.println(Thread.currentThread().getName() + "->" + accept.getInetAddress().getHostName() + " 服务状态: " + "提供文件列表信息结束。\n");
                        bw.flush();
                        accept.close();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
    private static void transFile() throws IOException {
        poolExecutor.submit(()->{
            System.out.println(Thread.currentThread().getName() + " 开始提供传输文件服务...");
            while (true) {
                Socket accept = serverTF.accept();
                poolExecutor.submit(()->{
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(accept.getInputStream()));
                        String index = br.readLine();
                        int dex = Integer.parseInt(index.trim());
                        File file = fileList.get(dex - 1);
                        System.out.println(file.getName());
                        BufferedOutputStream bos = new BufferedOutputStream(accept.getOutputStream());
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file.getPath()));
                        byte[] buffer = new byte[2 * 1024];
                        int len;
                        while ((len = bis.read(buffer)) != -1) {
                            bos.write(buffer, 0, len);
                            bos.flush();
                        }
                        bis.close();
                        System.out.println(Thread.currentThread().getName() + "->" + accept.getInetAddress().getHostName() + " 服务状态: " + "传输文件结束!\n");
                        accept.shutdownOutput();
                        accept.close();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
