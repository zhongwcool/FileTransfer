package com.demo.server;
import java.io.*;
import java.net.*;

/**
 * @author yeeku.H.lee kongyeeku@163.com
 * @version 1.0 <br>
 *          Copyright (C), 2005-2008, yeeku.H.Lee <br>
 *          This program is protected by copyright laws. <br>
 *          Program Name: <br>
 *          Date:
 */
// ������ÿ���߳�ͨ�ŵ��߳���
public class ServerThread implements Runnable {
	// ���嵱ǰ�߳��������Socket
	Socket currentSocket = null;
	
	// ���߳��������Socket����Ӧ��������
	BufferedReader bufferedReader = null;

	public ServerThread(Socket socket) throws IOException {
		currentSocket = socket;
		
		// ��ʼ����Socket��Ӧ��������
		bufferedReader = new BufferedReader(new InputStreamReader(currentSocket.getInputStream(), "utf-8")); // ��
	}

	public void run() {
		try {
			String content = null;
			
			// ����ѭ�����ϴ�Socket�ж�ȡ�ͻ��˷��͹���������
			while ((content = readFromClient()) != null) {
				// ����socketList�е�ÿ��Socket��
				// ��������������ÿ��Socket����һ��
				//System.out.println("The Count of Host in Connection: " + MyServer.socketList.size());
				for (Socket s : MyServer.socketList) {
					//System.out.println("Host in Server: " + s.toString() + " " + s.getPort() + " " + s.getLocalPort());
					//OutputStream os = s.getOutputStream();
					PrintWriter pw = new PrintWriter(s.getOutputStream());
					pw.print(content + "\n");
					pw.flush();
					//os.write((content + "\n").getBytes("utf-8"));
					//break;
				}
				System.out.println("The String Client input : " + content);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ȡ�ͻ������ݵķ���
	 * @return
	 */
	private String readFromClient() {
		try {
			return bufferedReader.readLine();
		}catch (IOException e) {	// �����׽���쳣��������Socket��Ӧ�Ŀͻ����Ѿ��ر�
			// ɾ����Socket��
			MyServer.socketList.remove(currentSocket);
		}
		return null;
	}
}
