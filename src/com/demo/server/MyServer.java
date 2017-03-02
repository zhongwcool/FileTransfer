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
	// ���屣������Socket��ArrayList
	public static List<Socket> socketList = new ArrayList<Socket>();
	static int i = 0;

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(30003);
		while (true) {
			// ���д������������һֱ�ȴ����˵�����
			Socket newSocket = serverSocket.accept();
			
			socketList.add(newSocket);
			System.out.println("I find a new socket connection: " + newSocket.toString());
			
			// ÿ���ͻ������Ӻ�����һ��ServerThread�߳�Ϊ�ÿͻ��˷���
			new Thread(new ServerThread(newSocket)).start();
		}
	}
}