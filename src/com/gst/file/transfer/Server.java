/**
 * 
 */
package com.gst.file.transfer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 接收端。可同时接收多个发送端发来的文件。但如果发来的文件是同名的话那就乱了。
 */
class Server {

	private int listenPort;

	private String savePath;

	/**
	 * 构造方法
	 * 
	 * @param listenPort
	 *            侦听端口
	 * @param savePath
	 *            接收的文件要保存的路径
	 * 
	 * @throws IOException
	 *             如果创建保存路径失败
	 */
	Server(int listenPort, String savePath) throws IOException {
		this.listenPort = listenPort;
		this.savePath = savePath;

		File file = new File(savePath);
		if (!file.exists() && !file.mkdirs()) {
			throw new IOException("无法创建文件夹 " + savePath);
		}
	}

	// 开始侦听
	public void start() {
		new ListenThread().start();
	}

	// 网上抄来的，将字节转成 int。b 长度不得小于 4，且只会取前 4 位。
	public static int b2i(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * 侦听线程
	 */
	private class ListenThread extends Thread {

		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(listenPort);

				// 开始循环
				while (true) {
					Socket socket = server.accept();
					new HandleThread(socket).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取流并保存文件的线程
	 */
	private class HandleThread extends Thread {

		private Socket socket;

		private HandleThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				InputStream input = socket.getInputStream();
				readAndSave(input);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// nothing to do
				}
			}
		}

		// 从流中读取内容并保存
		private void readAndSave(InputStream input) throws IOException {
			String filename = getFileName(input);
			int file_len = readLength(input);
			System.out.println("接收文件：" + filename + "，长度：" + file_len);

			readAndSave0(input, savePath + filename, file_len);

			System.out.println("文件保存成功（" + file_len + "字节）。");
		}

		private void readAndSave0(InputStream input, String path, int file_len) throws IOException {
			FileOutputStream os = getFileOS(path);
			readAndWrite(input, os, file_len);
			os.close();
		}

		// 边读边写，直到读取 size 个字节
		private void readAndWrite(InputStream input, FileOutputStream os, int size) throws IOException {
			byte[] buffer = new byte[4096];
			int count = 0;
			while (count < size) {
				int n = input.read(buffer);
				// 这里没有考虑 n = -1 的情况
				os.write(buffer, 0, n);
				count += n;
			}
		}

		// 读取文件名
		private String getFileName(InputStream input) throws IOException {
			int name_len = readLength(input);
			byte[] result = new byte[name_len];
			input.read(result);
			return new String(result);
		}

		// 读取一个数字
		private int readLength(InputStream input) throws IOException {
			byte[] data = new byte[4]; //文件名和文件长度固定占4个字节
			input.read(data);
			return b2i(data);
		}

		// 创建文件并返回输出流
		private FileOutputStream getFileOS(String filepath) throws IOException {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}

			return new FileOutputStream(file);
		}
	}
}