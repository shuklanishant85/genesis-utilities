package com.genesis.rules.script.utils;

import java.io.RandomAccessFile;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringUtils;


public class FileReader {

	/***
	 * Method to read the file from file system using given path.
	 * 
	 * @param context
	 * @param fullPath
	 * @return
	 */
	public static String readFromFileSystem(String fullPath) {
		RandomAccessFile aFile = null;
		MappedByteBuffer mappedByteBuffer = null;
		FileChannel inChannel = null;
		String fileContent = StringUtils.EMPTY;
		try {
			aFile = new RandomAccessFile(fullPath, "r");
			inChannel = aFile.getChannel();
			mappedByteBuffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			mappedByteBuffer.load();
			CharBuffer charBuffer = Charset.forName("UTF-8").decode(mappedByteBuffer);
			fileContent = charBuffer.toString();
		} catch (Exception e) {
		} finally {
			try {
				if (null != mappedByteBuffer) {
					mappedByteBuffer.clear();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != inChannel) {
					inChannel.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != aFile) {
					aFile.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return fileContent;
	}
	
}
