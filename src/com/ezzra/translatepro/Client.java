package com.ezzra.translatepro;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        requestService();
    }
    private static void requestService() throws IOException {
        System.out.println("客户端开始请求数据...");
        Scanner sc = new Scanner(System.in);
        while(true){
            try(Socket client = new Socket("127.0.0.1",40143);){
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String info = br.readLine();
                System.out.println("Server : "+info);
                String input = sc.nextLine();
                bw.write(input);
                bw.newLine();
                bw.flush();
                String res = br.readLine();
                System.out.println("Server : "+res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
