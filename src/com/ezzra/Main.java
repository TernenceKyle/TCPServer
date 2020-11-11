package com.ezzra;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IOException {
	// write your code here
//        String path = Main.class.getClassLoader().getResource("config.properties").getPath();
        InputStream n = Class.forName("com.ezzra.Main").getClassLoader().getResourceAsStream("config.properties");
//        System.out.println(path);
        InputStream rin = Main.class.getClassLoader().getResourceAsStream("config.properties");
//        InputStream in = new FileInputStream(path);
        Reader reader = new InputStreamReader(n,"GBK");
        Properties prop = new Properties();
        prop.load(reader);
        System.out.println(prop);
    }
}
