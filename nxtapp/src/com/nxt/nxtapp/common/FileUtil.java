package com.nxt.nxtapp.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	/**
	 * �����ļ�
	 */
	public static void createFile(String path, String filename) {
		File file = new File(path + "/" + filename);
		if (!file.exists()) {
			try {
				if (!file.isDirectory()) {
					new File(path).mkdirs();
					file.createNewFile();
				} else {
					file.createNewFile();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �����ļ�
	 */
	public static void saveFile(String savePath, String fileName, String content) {
		File filePath = new File(savePath);
		if (!filePath.exists()) {
			// ����Ŀ¼
			filePath.mkdirs();
		}
		try {
			// �����
			FileWriter fw = new FileWriter(new File(savePath, fileName));
			// �������д����
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
