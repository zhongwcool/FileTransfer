package com.gst.kai;

import java.net.*;
import java.io.*;

/**
 * Description: <br/>
 * ��վ: <a href="http://www.crazyit.org">���Java����</a> <br/>
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
		// ����һ��ServerSocket�����ڼ����ͻ���Socket����������
		ServerSocket server = new ServerSocket(30003);
		Socket s;
		// ����ѭ�����Ͻ������Կͻ��˵�����
		while (true) {
			// ÿ�����ܵ��ͻ���Socket�����󣬷�������Ҳ��Ӧ����һ��Socket
			s = server.accept();
			OutputStream os = s.getOutputStream();
			for(int i = 0; i < 10; i++) {
				os.write(("���ã����յ��˷�����������ף����"+i + "\r\n").getBytes("utf-8"));
				//os.write("\r\n".getBytes("utf-8"));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			os.write("\n".getBytes("utf-8"));
			// �ر������
			os.close();
			//s.close();
			System.out.println("���������");
		}
	}
}
