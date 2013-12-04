package com.bananallc.utilities;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.badlogic.gdx.Gdx;


public class ZipHandler
{ 
	private final static String TAG = "ZipHandler";
	
	public static boolean extract(String sourceFile, String destDir) 
	{ 
	    try  
	    { 
	    	Gdx.app.log(TAG, "Begin Extracting:" + sourceFile);
	    	Gdx.app.log(TAG, "Destination:" + destDir);
	    	ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile))); 
	    	ZipEntry zipEntry = null;
	    	
	    	int fileCount = 0;
	    	int folderCount = 0;
		      
			while ((zipEntry = zipInputStream.getNextEntry()) != null) 
			{ 
				String output = fixPath(destDir, zipEntry.getName());
		    	Gdx.app.log(TAG, "Extracting:" + output);
				
				if (zipEntry.isDirectory())
				{
					folderCount++;
					FileHandler.createDirectory(output);
				}
				else
				{
					fileCount++;
					int size;
					byte[] buffer = new byte[2048];

					FileOutputStream fout = new FileOutputStream(output); 
					BufferedOutputStream bufferOut = new BufferedOutputStream(fout, buffer.length);
			        	
					while((size = zipInputStream.read(buffer, 0, buffer.length)) != -1)
					{
						bufferOut.write(buffer, 0, size);
					}
		    		bufferOut.flush();
		    		bufferOut.close();			        	
		    		zipInputStream.closeEntry(); 
		    		fout.close(); 
				}
			} 
			zipInputStream.close(); 
	    	Gdx.app.log(TAG, "Extraction Complete, " + folderCount + " folders, " + fileCount + " files");
			return true;
	    } 
	    catch(Exception e) 
	    { 
	    	Gdx.app.log(TAG, "Exception:" + e.getMessage());
	    	return false;
	    } 
	} 
 	
 	private static String fixPath(String destDir, String name)
 	{
		String output = name.replace("/", File.separator);
		if (destDir.endsWith(File.separator) && output.startsWith(File.separator))
			output = destDir + output.substring(1);
		else if (!destDir.endsWith(File.separator) && !output.startsWith(File.separator))
			output = destDir + File.separator + output;
		else
			output = destDir + output;
 		
 		return output;
 	}
} 
