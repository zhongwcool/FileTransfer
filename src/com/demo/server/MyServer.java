package com.demo.server;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * @author yeeku.H.lee kongyeeku@163.com
 * @version 1.0 <br>
 *          Copyright (C), 2005-2008, yeeku.H.Lee <br>
 *          This program is protected by copyright laws. <br>
 *          Program Name: <br>
 *          Date:
 */
public class MyServer {
	// 定义保存所有Socket的ArrayList
	public static List<Socket> socketList = new ArrayList<Socket>();
	static int i = 0;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(30003);
		while (true) {
			// 此行代码会阻塞，将一直等待别人的连接
			Socket newSocket = serverSocket.accept();
			
			socketList.add(newSocket);
			System.out.println("I find a new socket connection: " + newSocket.toString());
			
			// 每当客户端连接后启动一条ServerThread线程为该客户端服务
			new Thread(new ServerThread(newSocket)).start();
		}
	}
}