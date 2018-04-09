package com.wang.project.utils;

import java.io.File;



/**
 * @author wyw
 * @data 2018年4月2日
 * @version 1.0
 */
public class FileUtil {

	/**
	 * 获取默认路径
	 * 
	 * @param clz
	 * @return
	 */
	public static synchronized String getDefaultValueForFilePathSynchronized(Class<?> clz) {
		String targetPath = "";
		String path = clz.getProtectionDomain().getCodeSource().getLocation().getPath();
		File file = new File(path);
		targetPath = file.getParentFile().getParentFile().getPath() + File.separator + "data";
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		return targetPath;
	}

	/**
	 * 获取默认路径
	 * 
	 * @param clz
	 * @return
	 */
	public static String getDefaultValueForFilePath(Class<?> clz) {
		String targetPath = "";
		String path = clz.getProtectionDomain().getCodeSource().getLocation().getPath();
		File file = new File(path);
		targetPath = file.getParentFile().getParentFile().getPath() + File.separator + "data";
		File targetFile = new File(targetPath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		return targetPath;
	}

	/**
	 * 获取文件的路径
	 * 
	 * @param file
	 * @return
	 */
	public synchronized static String getFilePath(File file) {
		String filePath = file.getPath();
		if (file.isDirectory()) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		if (file.isFile()) {
			int index = filePath.lastIndexOf("\\");
			String topFilePath = filePath.substring(0, index);
			File topFile = new File(topFilePath);
			if (!topFile.exists()) {
				topFile.mkdirs();
			}
		}
		return filePath;
	}
}
