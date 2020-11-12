package com.ezzra.fileserver;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private static String host;
    private static int port1;
    private static int port2;
    private static String fileName;
    static {
        Properties prop = new Properties();
        InputStream in = Client.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(in);
            host = prop.getProperty("host");
            port1 = Integer.parseInt(prop.getProperty("port1"));
            port2 = Integer.parseInt(prop.getProperty("port2"));
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        getFile();
    }

    public static void downLoadFile(String index) throws IOException {
        try (Socket client = new Socket(host,port2)) {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("src\\" + fileName));
            InputStream in = client.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(in);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            bw.write(index);
            bw.newLine();
            bw.flush();
            System.err.println("文件正在传输中请勿关闭程序..");
            byte[] buffer = new byte[4 * 1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
            }
            System.out.println("文件传输完成! :" + fileName + "\n\n");
        }
    }

    public static void getFile() throws IOException, InterruptedException {
        String index;
        while (true) {
            try (Socket client = new Socket(host, port1);) {
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
                int i = new Scanner(System.in).nextInt();
               try {
                   if (i == 0) continue;
                   if (i == 88) System.exit(0);
                   index = i + "";
                   fileName = split[i - 1].split(",")[0];
               }catch (Exception e)
               {
                   System.err.println("请输入正确的编号！");
                   Thread.sleep(3000);
                   continue;
               }
            }
            downLoadFile(index);
        }
    }
}
