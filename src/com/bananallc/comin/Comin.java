package com.bananallc.comin;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.bananallc.utilities.Consts;
import com.bananallc.utilities.FileHandler;
import com.bananallc.utilities.ZipHandler;
import com.google.gson.Gson;

public class Comin
{
	public int id;
	
	//****STORE****//
	public int cost, discount, review, maturity, version;
	public long downloadId;
	public String title, description, urlDownload, urlThumbnail, pubDate;
	public List<String> genres;
	public boolean downloaded, loaded, purchased;
	
	//****READER****//
	public int branchId, sceneId, choiceId;
	public List<Msc> musics;
	public List<Snd> sounds;
	public List<Branch> branches;
	
	public final static String TAG = "Comin";
	
	public final static String SQL_TABLE_NAME = "comins";

	public final static String KEY_TITLE = "title";
	public final static String KEY_DESCRIPTION = "description";
	public final static String KEY_URL_DOWNLOAD = "urlDownload";
	public final static String KEY_URL_THUMBNAIL = "urlThumbnail";
	public final static String KEY_PUB_DATE = "pubDate";
	public final static String KEY_VERSION = "version";
	public final static String KEY_MATURITY = "maturity";
	public final static String KEY_PURCHASED = "purchased";
	public final static String KEY_DOWNLOADED = "downloaded";
	public final static String KEY_LOADED = "loaded";
	public final static String KEY_BRANCH = "branch";
	public final static String KEY_SCENE = "scene";
	public final static String KEY_CHOICE = "choice";
	
	public final static String FOLDER_IMAGES = "img";
	public final static String FOLDER_SOUND = "snd";
	public final static String FOLDER_JSON = "json";
	public final static String FOLDER_THUMB = "thmb";
	public final static String FOLDER_FONTS = "font";
	public final static String FILE_JSON = "comin.json";
	public final static String FILE_DOWNLOAD = "data.comin";
	public final static String FILE_THUMB = "thumbnail.jpg";
	//private final static String COMIN = "c";
	
	public final static int LOADED = 1;
	public final static int TYPE_STORE = 0;
	public final static int TYPE_READER = 1;
	public final static int SCENE_NO_CHOICE = -1;
	
	//private final static int DEFAULT_BRANCH = 0;
	//private final static int DEFAULT_SCENE = 0;
	
	public static boolean download(int id)
	{
		boolean downloaded = false;
		try {
			
			final File zip = getZipPath(id);
			final File json = getJsonPath(id);

			final URL url = new URL("http://www.bananallc.com/comins/" + id + ".zip");
			Gdx.app.log(TAG, "Downloading File:" + url + ", size:" + url.openConnection().getHeaderField("Content-Length"));
			
			//Download ZIP
			//if (!zip.exists())
			//{
				org.apache.commons.io.FileUtils.copyURLToFile(url, zip);
			//}

			//Extract ZIP
			if (zip.exists())
				ZipHandler.extract(zip.getAbsolutePath(), getCominDir(id).getAbsolutePath());
			else
				Gdx.app.log(TAG, "Zip does not exist at " + zip.getAbsolutePath());
				
			if (json.exists())
				downloaded = true;
			else
				Gdx.app.log(TAG, "JSON does not exist at " + json.getAbsolutePath());
			
		} catch (Exception e) {
			
			Gdx.app.log(TAG, "Exception:" + e.getMessage());
			e.printStackTrace();
			
		}		
		return downloaded;
	}
	
	public static Comin deserialize(File file)
	{
		Comin comin = null;
		try 
		{
			FileReader fr = new FileReader(file);
			comin = new Gson().fromJson(fr, Comin.class);
			fr.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return comin;
	}
	
	public void setScene(int branch, int scene)
	{
		branchId = branch;
		sceneId = scene;
	}
	
	public List<Snd> getSounds()
	{
		return sounds;
	}
	
	public Scene getCurrentScene()
	{
		Scene scene = null;
		for (int a = 0; a < branches.size(); a++)
		{
			if (branches.get(a).id == branchId)
				scene = branches.get(a).getScene(sceneId);
		}
		return scene;
	}
	
	public List<Msc> getMusics()
	{
		return musics;
	}
	
	public static File getRootDir()
	{
		return Gdx.files.external(Consts.APP_NAME).file();
	}
	
	public static File getCominDir(int id)
	{
		return new File(getRootDir() + File.separator + id);
	}
	
	public static File getZipPath(int id)
	{
		return new File(getCominDir(id) + File.separator + FILE_DOWNLOAD);
	}
	
	public static File getJsonDir(int id)
	{
		return new File(getCominDir(id) + File.separator + FOLDER_JSON);
	}
	
	public static File getJsonPath(int id)
	{
		return new File(getJsonDir(id) + File.separator + FILE_JSON);
	}
	
	public static File getImageDir(int id)
	{
		return new File(getCominDir(id) + File.separator + FOLDER_IMAGES);
	}
	
	public static File getSoundDir(int id)
	{
		return new File(getCominDir(id) + File.separator + FOLDER_SOUND);
	}
	
	public static File getFontDir(int id)
	{
		return new File(getCominDir(id) + File.separator + FOLDER_FONTS);
	}
	
	public static File getThumbnailPath(int id)
	{
		return new File(getRootDir() + File.separator + FOLDER_THUMB + File.separator + id + FileHandler.FILETYPE_JPG);
	}
	
	public Group getWebThumb(final float x, final float y, final float w, final float h)
	{
		return new WebImage(urlThumbnail, Comin.getThumbnailPath(id), x, y, w, h).getImage();
	}
}
