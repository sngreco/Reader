package com.bananallc.comin;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.bananallc.utilities.DownloadHandler;

public class WebImage {
	
	private FileHandle file;
	private Texture texture;
	private Image image;
	private Group group;
	
	private final static String TAG = "WebImage";
	
	public WebImage(final String url, final File path, final float x, final float y, final float w, final float h)
	{
		Gdx.app.log(TAG, "url:" + url + ", path:" + path.getAbsolutePath() + ", x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
        file = new FileHandle(path);
        
		if (file.exists())
		{
			texture = new Texture(file);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			image = new Image(texture);
		}
		else
		{
			image = new Image();
			new Thread(new Runnable() {
				@Override
				public void run() {
					DownloadHandler.download(url, file.file());						
					Gdx.app.postRunnable(new Runnable() {
						@Override
						public void run() {
				            group.clear();
							
							texture = new Texture(file);
							texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
							
							image = new Image(texture);
							image.setWidth(w);
							image.setHeight(h);
							image.setPosition(x, y);

							group.addActor(image);
						}
					});
				}
			}).start();
		}
		image.setWidth(w);
		image.setHeight(h);
		image.setPosition(x, y);
		
		group = new Group();
		group.addActor(image);
	}

	public Group getImage()
	{
		return group;
	}
}
