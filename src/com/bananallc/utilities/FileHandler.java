package com.bananallc.utilities;

import java.io.File;

public class FileHandler
{
	public final static String FILETYPE_JPG = ".jpg";
	public final static String FILETYPE_PNG = ".png";
	public final static String FILETYPE_ZIP = ".zip";
	public final static String FILETYPE_JSON = ".json";
	
	public static void deleteFolderContents(String directory)
	{
		File folder = new File(directory);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
		{
			if (files[i].exists()) 
				files[i].delete();
		}
	}
	
	public static void deleteFile(String filePath)
	{
		File file = new File(filePath);
		if (file.exists()) 
			file.delete();
	}
	
	public static void createDirectory(String directoryPath)
	{
		new File (directoryPath).mkdirs();
	}
	
	public static boolean checkFileExists(String filePath)
	{
		File file = new File(filePath);
		return file.exists();
	}
}
