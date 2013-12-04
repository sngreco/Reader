package com.bananallc.comin;

import java.io.File;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.bananallc.comin.Show.Txt.Rect;


public class Show 
{
	private final static String TAG = "Show";
	
	public int id;
	private float l, t, r, b;
	
	public Txt txt;
	public Img img;
	public Rect rect;
	public Background background;
	
	public Group group;
	
	public void load(final float width, final float height, final String imageDir, final String fontDir)
	{						
		float x = width * l / 100;
		float y = height * b / 100;
		float w = width - ((width * r / 100) + x);
		float h = height - ((height * t / 100) + y);
		
		group = new Group();
		group.setWidth(w);
		group.setHeight(h);
		group.setPosition(x, y);
		group.setOrigin(group.getWidth()/2, group.getHeight()/2);
		//group.setVisible(visible);
		
		Gdx.app.log(TAG, "id:" + id + ", x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);

		if (rect != null)
			group.addActor(rect.load(id, w, h));
		if (checkImg())
			group.addActor(img.load(id, w, h, imageDir));
		if (checkTxt())
			group.addActor(txt.load(id, w, h, fontDir));
	}
	
	public void clear()
	{
		group.clearActions();
		group.clear();
		if (checkImg())
			img.clear();
	}
	
	public Group getGroup()
	{
		return group;
	}
	
	public boolean checkImg()
	{
		boolean check = false;
		if (img != null)
			check = true;
		return check;
	}
	
	public boolean checkTxt()
	{
		boolean check = false;
		if (txt!= null)
			check = true;
		return check;
	}

	public class Img {
		private final static String TAG = "Img";
		
		private String input;
		private int l, t, r, b;
		private Texture texture;
		
		public Image load(int id, final float width, final float height, final String imageDir)
		{
			final float x = width * l / 100;
			final float y = height * b / 100;
			final float w = width - ((width * r / 100) + x);
			final float h = height - ((height * t / 100) + y);
			
			final String path = imageDir + File.separator + input;
			FileHandle file = new FileHandle(path);
			if (!file.exists())
				return null;
			texture = new Texture(file);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			Image image = new Image(texture);
			image.setWidth(w);
			image.setHeight(h);
			image.setPosition(x, y);
			
			Gdx.app.log(TAG, "id:" + id + ", x:" + x + ", y:" + y + ", w:" + w + ", h:" + h + ", path:" + path);
			
			return image;
		}
		
		public void clear()
		{
			texture.dispose();
		}
	}
	
	public static class Txt 
	{
		public String font, input;
		private int l, t, r, b;
		private Clr color;
		public int[] alignment;
		
		public final static int ALIGN_CENTER = 0;
		public final static int ALIGN_CENTER_HORIZONTAL = 1;
		public final static int ALIGN_LEFT = 2;
		public final static int ALIGN_TOP = 3;
		public final static int ALIGN_RIGHT = 4;
		public final static int ALIGN_BOTTOM = 5;
		public final static int ALIGN_CENTER_VERTICAL = 6;
		public final static int FILL = 7;
		public final static int FILL_HORIZONTAL = 8;
		public final static int FILL_VERTICAL = 9;

	    public static final int MIN_FONT_SIZE = 1;
	    public static final int MAX_FONT_SIZE = 100;
	    public static final int MIN_INCREMENT = 1;
		
		public Label load(int id, final float width, final float height, final String fontDir)
		{
			final float x = width * l / 100;
			final float y = height * b / 100;
			final float w = width - ((width * r / 100) + x);
			final float h = height - ((height * t / 100) + y);
			
			Label label = null;
			LabelStyle style = null;
			BitmapFont f = null;
			int fontSize = MAX_FONT_SIZE;
			int increment = fontSize;
			float textHeight = 0;
			boolean processing = true;
			
			Color c = new Color(Color.BLACK);
			if (color != null)
				c = color.get();
			
			while (processing)
			{
				f = new FreeTypeFontGenerator(new FileHandle(fontDir + File.separator + font)).generateFont(fontSize);
				style = new LabelStyle(f, c);
				label = new Label(input, style);
				label.setWidth(w);
				label.setHeight(h);
				label.setWrap(true);
				
				textHeight = label.getTextBounds().height;	
				
				if (increment > MIN_INCREMENT)
					increment = increment / 2;

				if (textHeight > h)
					fontSize -= increment;
				else if (textHeight < h && increment > MIN_INCREMENT)
					fontSize += increment;
				else
					processing = false;
				
				Gdx.app.log(TAG, "fontSize:" + fontSize + ", textHeight:" + textHeight + ", maxHeight:" + h + ", increment:" + increment);
			}	
			label.setPosition(x, y);
			
			Gdx.app.log(TAG, "id:" + id + ", x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
			return label;
		}
		
		public class Rect{
			private final static String TAG = "Rect";

			private int l, t, r, b;
			private List<Clr> colors;
			private ShapeRenderer sr;
			
			public Actor load(int id, final float width, final float height)
			{
				final float x = width * l / 100;
				final float y = height * b / 100;
				final float w = width - ((width * r / 100) + x);
				final float h = height - ((height * t / 100) + y);

				sr = new ShapeRenderer();
				
				Actor rect = new Actor(){
					@Override
					public void draw(SpriteBatch batch, float arg1){
						batch.end();
						sr.setProjectionMatrix(batch.getProjectionMatrix());
						sr.setTransformMatrix(batch.getTransformMatrix());
						sr.begin(ShapeType.FilledRectangle);
						
						if (colors == null)
						{
							sr.setColor(Color.BLACK);
							sr.filledRect(x, y, w, h);
						}
						else if (colors!= null && colors.size() == 4)
						{
							sr.filledRect(x, y, w, h, colors.get(0).get(), colors.get(1).get(), colors.get(2).get(), colors.get(3).get());
						}
						else
						{
							sr.setColor(colors.get(0).get());
							sr.filledRect(x, y, w, h);
						}
						sr.end();
						batch.begin();
					}
				};
				rect.setWidth(w);
				rect.setHeight(h);
				rect.setPosition(x, y);
				
				Gdx.app.log(TAG, "id:" + id + ", x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
			
				return rect;
			}
		}
	}
	
	public static float calcRatioWidth(float calcH, float aspectW, float aspectH)
	{
		return (calcH * aspectW) / aspectH;
	}
	public static float calcRatioHeight(float calcW, float aspectW, float aspectH)
	{
		return (calcW * aspectH) / aspectW;
	}
	
	public static float convertX(float width, float l)
	{
		return width * l / 100;
	}
	
	public static float convertY(float height, float b)
	{
		return height * b / 100;
	}
	
	public static float convertW(float width, float r, float x)
	{
		return width - ((width * r / 100) + x);
	}
	
	public static float convertH(float height, float t, float y)
	{
		return height - ((height * t / 100) + y);
	}
}
