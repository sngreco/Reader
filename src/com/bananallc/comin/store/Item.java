package com.bananallc.comin.store;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.bananallc.comin.Clr;
import com.bananallc.comin.Comin;
import com.bananallc.comin.Rectangle;
import com.bananallc.comin.Show;

public class Item extends Group{
	
	private float w, h, x, y;
	private Group description, thumb;
	private ScrollPane scroller;
	private Comin comin;
	
	private final static int W = 100;
	private final static int H = 70;
	private final static int X = 0;
	private final static int Y = 15;
	private final static int X_PAD = 0;
	
	public Item(Comin comin, float width, float height)
	{
		super();
		this.comin = comin;
		
		clear();
		
		setWidth(width);
		setHeight(height);
		setPosition(0, 0);
		setOrigin(getWidth()/2, getHeight()/2);
		
		addActor(new Thumb(getHeight()));
	}
	
	public class Thumb extends Group{
		
		private final static int W = 16;
		private final static int H = 9;
		
		public Thumb(float h)
		{
			setWidth(Show.calcRatioWidth(h, W, H));
			setHeight(h);
			//setWidth(1920/2);
			//setHeight(1080);
			setPosition(0, 0);

			addActor(comin.getWebThumb(0, 0, getWidth(), getHeight()));
		}
	}
	
	/*public class Description extends Group{
		
		private final static int W = 1; //Aspect Ratio
		private final static int H = 2; //Aspect Ratio
		private final static int PAD = 5; //Percentage Width
		
		private final Clr BG_CLR = new Clr(107, 107, 107, 255);
		
		public Description(float x, float y)
		{
			setWidth(Show.calcRatioWidth(h, W, H));
			setHeight(h);
			setPosition(x, y);
			
			//Details Background
			addActor(new Rectangle(0, 0, getWidth(), getHeight(), new Color[]{BG_CLR.get()}));

			//Details Text
			final float rX = tW;
			final float rW = w - tW;
			final float pad = rW * DETAILS_PAD / 100;

			//Comin Title
			int fontSize = (int) (h * DETAILS_FONT_SIZE_LARGE / 100);
			BitmapFont f = new FreeTypeFontGenerator(Gdx.files.internal("data/Comic_Book.ttf")).generateFont(fontSize);
			Color c = DETAILS_FONT_CLR_LIGHT.get();
			LabelStyle style = new LabelStyle(f, c);
			Label title = new Label(comin.title, style);
			
			final float hW = rW - (pad*2);
			final float hX = rX + pad;
			float hY = h - pad - fontSize;
			title.setWidth(hW);
			title.setPosition(hX, hY);
			details.addActor(title);
			
			//Comin Series
			fontSize = (int) (h * DETAILS_FONT_SIZE_MEDIUM / 100);
			f = new FreeTypeFontGenerator(Gdx.files.internal("data/Comic_Book.ttf")).generateFont(fontSize);
			style = new LabelStyle(f, c);
			Label series = new Label("Series", style);
			hY -= fontSize;
			series.setWidth(hW);
			series.setPosition(hX, hY);
			details.addActor(series);
			
			//Comin Publisher
			Label publisher = new Label("Publisher", style);
			hY -= fontSize;
			publisher.setWidth(hW);
			publisher.setPosition(hX, hY);
			details.addActor(publisher);
			
			//Comin Description
			final float dW = hW - (pad*2);
			final float dH = hY - (pad*2);
			hY -= dH;
			
			c = DETAILS_FONT_CLR_LIGHT.get();
			style = new LabelStyle(f, c);
			Label text = new Label(comin.description + comin.description + comin.description, style);
			text.setWidth(dW);
			text.setWrap(true);
			text.setPosition(0, 0);
			
			ScrollPane description = new ScrollPane(text);
			description.setFadeScrollBars(true);
			description.setWidth(hW);
			description.setHeight(dH);
			description.setPosition(hX, hY);
			details.addActor(description);
		}
	}
	
	private void clr()
	{
		if (thumb == null)
			thumb = new Group();
		else
			thumb.clear();
		
		if (description == null)
			description = new Group();
		else
			description.clear();
		
		if (details == null)
			details = new Group();
		else
			details.clear();
		
		if (scroller == null)
			scroller = new ScrollPane(details);
		
		//details.setPosition(Show.convertX(width, DETAILS_L), Show.convertY(height, DETAILS_B));
	}*/

	private float calcDimension(final float dimension, final int percent)
	{
		return dimension * percent / 100;
	}
	
	private void loadThumb()
	{
		
	}
	
	/*private void load(Comin comin)
	{
		reset();
		
		//Details Group
		details.setWidth(w);
		details.setHeight(h);
		details.setOrigin(details.getWidth()/2, details.getHeight()/2);

		
	}*/
}
