package com.gst.kai;

import java.net.*;
import java.io.*;

/**
 * Description: <br/>
 * 网站: <a href="http://www.crazyit.org">疯狂Java联盟</a> <br/>
 * Copyright (C), 2001-2012, Yeeku.H.Lee <br/>
 * This program is protected by copyright laws. <br/>
 * Program Name: <br/>
 * Date:
 * 
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class SimpleServer {
	public static void main(String[] args) throws IOException {
		// 创建一个ServerSocket，用于监听客户端Socket的连接请求
		ServerSocket server = new ServerSocket(30003);
		Socket s;
		// 采用循环不断接受来自客户端的请求
		while (true) {
			// 每当接受到客户端Socket的请求，服务器端也对应产生一个Socket
			s = server.accept();
			OutputStream os = s.getOutputStream();
			for(int i = 0; i < 10; i++) {
				os.write(("您好，您收到了服务器的新年祝福！"+i + "\r\n").getBytes("utf-8"));
				//os.write("\r\n".getBytes("utf-8"));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			os.write("\n".getBytes("utf-8"));
			// 关闭输出流
			os.close();
			//s.close();
			System.out.println("程序结束了");
		}
	}
}
