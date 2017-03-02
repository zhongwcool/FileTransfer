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
 * ���նˡ���ͬʱ���ն�����Ͷ˷������ļ���������������ļ���ͬ���Ļ��Ǿ����ˡ�
 */
class Server {

	private int listenPort;

	private String savePath;

	/**
	 * ���췽��
	 * 
	 * @param listenPort
	 *            �����˿�
	 * @param savePath
	 *            ���յ��ļ�Ҫ�����·��
	 * 
	 * @throws IOException
	 *             �����������·��ʧ��
	 */
	Server(int listenPort, String savePath) throws IOException {
		this.listenPort = listenPort;
		this.savePath = savePath;

		File file = new File(savePath);
		if (!file.exists() && !file.mkdirs()) {
			throw new IOException("�޷������ļ��� " + savePath);
		}
	}

	// ��ʼ����
	public void start() {
		new ListenThread().start();
	}

	// ���ϳ����ģ����ֽ�ת�� int��b ���Ȳ���С�� 4����ֻ��ȡǰ 4 λ��
	public static int b2i(byte[] b) {
		int value = 0;
		for (int i = 0; i < 4; i++) {
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * �����߳�
	 */
	private class ListenThread extends Thread {

		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(listenPort);

				// ��ʼѭ��
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
	 * ��ȡ���������ļ����߳�
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

		// �����ж�ȡ���ݲ�����
		private void readAndSave(InputStream input) throws IOException {
			String filename = getFileName(input);
			int file_len = readLength(input);
			System.out.println("�����ļ���" + filename + "�����ȣ�" + file_len);

			readAndSave0(input, savePath + filename, file_len);

			System.out.println("�ļ�����ɹ���" + file_len + "�ֽڣ���");
		}

		private void readAndSave0(InputStream input, String path, int file_len) throws IOException {
			FileOutputStream os = getFileOS(path);
			readAndWrite(input, os, file_len);
			os.close();
		}

		// �߶���д��ֱ����ȡ size ���ֽ�
		private void readAndWrite(InputStream input, FileOutputStream os, int size) throws IOException {
			byte[] buffer = new byte[4096];
			int count = 0;
			while (count < size) {
				int n = input.read(buffer);
				// ����û�п��� n = -1 �����
				os.write(buffer, 0, n);
				count += n;
			}
		}

		// ��ȡ�ļ���
		private String getFileName(InputStream input) throws IOException {
			int name_len = readLength(input);
			byte[] result = new byte[name_len];
			input.read(result);
			return new String(result);
		}

		// ��ȡһ������
		private int readLength(InputStream input) throws IOException {
			byte[] data = new byte[4]; //�ļ������ļ����ȹ̶�ռ4���ֽ�
			input.read(data);
			return b2i(data);
		}

		// �����ļ������������
		private FileOutputStream getFileOS(String filepath) throws IOException {
			File file = new File(filepath);
			if (!file.exists()) {
				file.createNewFile();
			}

			return new FileOutputStream(file);
		}
	}
}