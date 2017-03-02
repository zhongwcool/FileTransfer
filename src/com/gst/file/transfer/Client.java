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
 * ���Ͷ�
 */
class Client {

	// ���ϳ����ģ��� intת���ֽ�
	public static byte[] i2b(int length) {
		return new byte[] { 
				(byte) ((length >> 24) & 0xFF), 
				(byte) ((length >> 16) & 0xFF), 
				(byte) ((length >>  8) & 0xFF),
				(byte) (length         & 0xFF) 
				};
	}

	/**
	 * �����ļ����ļ���С���ܴ��� {@link Integer#MAX_VALUE}
	 * 
	 * @param hostname
	 *            ���ն��������� IP ��ַ
	 * @param port
	 *            ���ն˶˿ں�
	 * @param filepath
	 *            �ļ�·��
	 * 
	 * @throws IOException
	 *             �����ȡ�ļ�����ʧ��
	 */
	public void sendFile(String hostname, int port, String filepath) throws IOException {
		File file = new File(filepath);

		Socket socket = new Socket(hostname, port);
		OutputStream os = socket.getOutputStream();

		try {
			System.out.println("�����ļ���" + file.getName() + "�����ȣ�" + file.length());

			// �����ļ������ļ�����
			writeFileName(file, os);
			writeFileContent(file, os);
		} finally {
			os.close();
		}
		
		socket.close();
	}

	// ����ļ�����
	private void writeFileContent(File file, OutputStream os) throws IOException {
		FileInputStream is = new FileInputStream(file);
		
		// ����ļ�����
		os.write(i2b((int)file.length()));

		// ����ļ�����
		byte[] buffer = new byte[4096];
		int size;
		while ((size = is.read(buffer)) != -1) {
			os.write(buffer, 0, size);
		}
		
		is.close();
	}

	// ����ļ���
	private void writeFileName(File file, OutputStream os) throws IOException {
		byte[] fn_bytes = file.getName().getBytes();

		os.write(i2b(fn_bytes.length)); // ����ļ�������
		os.write(fn_bytes); // ����ļ���
	}
}