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
			new Client().sendFile("127.0.0.1", port, "d://ESP8266连接路由获取IP耗时测试演示指南.pdf"); 
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}

}
