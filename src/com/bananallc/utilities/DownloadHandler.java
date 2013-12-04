package com.bananallc.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class DownloadHandler {
	
	//private final static String PREFERENCES = "downloads";
	
	public static int status(String url)
	{
		//Preferences prefs = Gdx.app.getPreferences(PREFERENCES);
		return 0;
	}

	public static void download(String url, File dest)
	{
		try {
			//Gdx.app.log(TAG, "Downloading File:" + url + ", size:" + download.openConnection().getHeaderField("Content-Length"));
			FileUtils.copyURLToFile(new URL(url), dest);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String download(URL url)
	{
		try {
			BufferedReader reader;
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuilder response = new StringBuilder();
			String string = null;
			while ((string = reader.readLine()) != null)
				response.append(string);
			string = response.toString();
			return string;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
