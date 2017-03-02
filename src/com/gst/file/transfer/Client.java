/**
 * 
 */
package com.gst.file.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 发送端
 */
class Client {

	// 网上抄来的，将 int转成字节
	public static byte[] i2b(int length) {
		return new byte[] { 
				(byte) ((length >> 24) & 0xFF), 
				(byte) ((length >> 16) & 0xFF), 
				(byte) ((length >>  8) & 0xFF),
				(byte) (length         & 0xFF) 
				};
	}

	/**
	 * 发送文件。文件大小不能大于 {@link Integer#MAX_VALUE}
	 * 
	 * @param hostname
	 *            接收端主机名或 IP 地址
	 * @param port
	 *            接收端端口号
	 * @param filepath
	 *            文件路径
	 * 
	 * @throws IOException
	 *             如果读取文件或发送失败
	 */
	public void sendFile(String hostname, int port, String filepath) throws IOException {
		File file = new File(filepath);

		Socket socket = new Socket(hostname, port);
		OutputStream os = socket.getOutputStream();

		try {
			System.out.println("发送文件：" + file.getName() + "，长度：" + file.length());

			// 发送文件名和文件内容
			writeFileName(file, os);
			writeFileContent(file, os);
		} finally {
			os.close();
		}
		
		socket.close();
	}

	// 输出文件内容
	private void writeFileContent(File file, OutputStream os) throws IOException {
		FileInputStream is = new FileInputStream(file);
		
		// 输出文件长度
		os.write(i2b((int)file.length()));

		// 输出文件内容
		byte[] buffer = new byte[4096];
		int size;
		while ((size = is.read(buffer)) != -1) {
			os.write(buffer, 0, size);
		}
		
		is.close();
	}

	// 输出文件名
	private void writeFileName(File file, OutputStream os) throws IOException {
		byte[] fn_bytes = file.getName().getBytes();

		os.write(i2b(fn_bytes.length)); // 输出文件名长度
		os.write(fn_bytes); // 输出文件名
	}
}