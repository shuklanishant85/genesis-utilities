package com.genesis.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FindString {
	public static final String FIND_STRING = "this string";
	public static final String DIRECTORY = "";


	public static void main(String[] args) throws IOException {
		final File folder = new File(DIRECTORY);
		List<File> files = new ArrayList<>();
		files = listFilesForFolder(folder,files);
		List<String> foundFiles = new ArrayList<>();
		for (File file : files) {
			try {
				System.out.println("scanning file " + file.getPath());
				if (!FileUtils.readFileToString(file).contains(FIND_STRING)) {
					foundFiles.add(file.getPath());
					System.out.println("------------> found file: " + file.getPath());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			FileOutputStream stream =  new FileOutputStream(new File("files.txt"));
			for (String string : foundFiles) {
				stream.write(string.getBytes());
				stream.write("\n".getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static List<File> listFilesForFolder(final File folder, List<File> files) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry,files);
			} else {
				files.add(fileEntry);
			}
		}
		return files;
	}


}
