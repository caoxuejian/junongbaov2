package com.nxt.nxtapp.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	/**
	 * 创建文件
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
	 * 保存文件
	 */
	public static void saveFile(String savePath, String fileName, String content) {
		File filePath = new File(savePath);
		if (!filePath.exists()) {
			// 创建目录
			filePath.mkdirs();
		}
		try {
			// 输出流
			FileWriter fw = new FileWriter(new File(savePath, fileName));
			// 输出流的写操作
			fw.write(content);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
