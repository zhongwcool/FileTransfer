/**
 * 
 */
package com.gst.file.transfer;

import java.io.IOException;

/**
 * @author alex
 *
 */
public class FileTrasmission {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 7788;  
        try {
			new Server(port, "d://save//").start();
			new Client().sendFile("127.0.0.1", port, "d://ESP8266����·�ɻ�ȡIP��ʱ������ʾָ��.pdf"); 
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}

}
