package com.bananallc.comin.store;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bananallc.comin.AnimList.Anim;
import com.bananallc.comin.Clr;
import com.bananallc.comin.Comin;
import com.bananallc.comin.Show;
import com.bananallc.comin.reader.Reader;
import com.bananallc.comin.reader.ReaderScreen;
import com.bananallc.utilities.DownloadHandler;
import com.bananallc.views.View;
import com.google.gson.Gson;

public class StoreScreen implements Screen{

	private final static String TAG = "ReaderScreen";
	
	private final static Clr DETAILS_BG_CLR = new Clr(107, 107, 107, 255);
	private final static int DETAILS_MARGIN = 15;
	private final static int DETAILS_PAD = 5;
	private final static int DETAILS_FONT_SIZE_LARGE = 8;
	private final static int DETAILS_FONT_SIZE_MEDIUM = 4;
	//private final static Clr DETAILS_FONT_CLR_DARK = new Clr(47, 47, 47, 255);
	private final static Clr DETAILS_FONT_CLR_LIGHT = new Clr(200, 200, 200, 255);
	//private final static Clr DETAILS_FONT_CLR_MEDIUM = new Clr(54, 54, 54, 255);

	private final static int LIST_L = 0;
	private final static int LIST_B = 12;
	private final static int LIST_R = 0;
	private final static int LIST_T = 12;
	
	private final static Clr BAR_BORDER_CLR = new Clr(189, 189, 189, 255);
	private final static Clr BAR_CLR = new Clr(20, 20, 20, 255);
	private final static Clr BG_CLR_TOP = new Clr(39, 39, 39, 255);
	private final static Clr BG_CLR_BOTTOM = new Clr(80, 80, 80, 255);
	public final static Clr THUMB_BORDER_CLR = new Clr(204, 204, 204, 255);
	private final static float BAR_MARGIN = 4;
	
	private final static Anim MOVE_IN = new Anim(Anim.MOVE_TO, new float[]{2, 0}, .5f, 0, 0, Anim.INT_LINEAR);
	private final static Anim FADE_OUT = new Anim(Anim.ALPHA, new float[]{0}, 1, 0, 0, Anim.INT_LINEAR);
	private final static Anim FADE_IN = new Anim(Anim.ALPHA, new float[]{100}, .5f, 0, 0, Anim.INT_LINEAR);
	
	private Stage stage;
	private SpriteBatch batch;
	private ShapeRenderer sr;
	private int width, height, rows, cols;
	private ScrollPane list;
	private Group filter;
	private Reader reader;
	
	public StoreScreen(Reader reader)
	{
		this.reader = reader;
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		stage = new Stage();
		sr = new ShapeRenderer();
		Gdx.input.setInputProcessor(stage);	
	}

	@Override
	public void resize(int width, int height) {
		if (this.width == width && this.height == height)
			return;
		
		Gdx.app.log(TAG, "Set Viewport: w:" + width + ", h:" + height);
		stage.clear();
		this.width = width;
		this.height = height;
		
		stage.setViewport(width, height, true);
		loadStore();
	}
	
	private void loadStore()
	{
		List<Comin> comins = downloadComins("http://www.bananallc.com/comins/comins.json");
		loadList(comins);
		loadFilter();
		//resetDetails();
		
		stage.addActor(rectangle(width, height, 0, 0, 0, 0, new Color[]{BG_CLR_BOTTOM.get(), BG_CLR_BOTTOM.get(), BG_CLR_TOP.get(), BG_CLR_TOP.get()}));
		stage.addActor(bar(0, 5, 0, 88));
		stage.addActor(bar(0, 88, 0, 5));
		stage.addActor(list);
		stage.addActor(filter);
		//stage.addActor(details);

		filter.addAction(FADE_OUT.getAnim(width, height));
	}
	
	private void loadList(final List<Comin> comins)
	{
    	final int count = comins.size();
		cols = 4;
		rows = count / cols;
		if (count % cols > 0)
			rows += 1;
		
		final float tPad = width / 200;
		final float tWidth = (width - (tPad*2)) / cols;
		final float tHeight = (tWidth * 9) / 16;
		
		final float tX = tPad;
		final float tY = tPad;
		final float tW = tWidth - (tPad*2);
		final float tH = (tW * 9) / 16;
		
		Gdx.app.log(TAG, "count:" + count + ", cols:" + cols + ", rows:" + rows + ", tWidth:" + tWidth + ", tHeight:" + tHeight + ", tPad:" + tPad);
		
		final Table table = new Table();
		table.pad(tPad);
		
		for (int a1 = 0; a1 < rows; a1++)
		{
			for (int a2 = 0; a2 < cols; a2++)
			{
				final int position = (a1*cols) + a2;
				
				if (position >= comins.size())
					break;
				else
				{
					final Group thumb = comins.get(position).getWebThumb(tX, tY, tW, tH);
					thumb.addListener(new InputListener() {
						public boolean touchDown(InputEvent event, float x, float y,
								int pointer, int button) {
							return true;
						}

						public void touchUp(InputEvent event, float x, float y,
								int pointer, int button) {
							Gdx.app.log(TAG, "touchUp");
							//loadDetails(comins.get(position));
							//details.addAction(MOVE_IN.getAnim(width, height));
							View view = new View();
							thumb.addActor(new View());
							view.setDimensions(0, 0, 10, 10, 0, 0);
							Item item = new Item(comins.get(position), width, height*.85f);
							stage.addActor(item);
							filter.addAction(FADE_IN.getAnim(width, height));
						}
					});
					table.add(thumb).size(tWidth, tHeight);
				}
			}
			table.row();
		}
		
		final float x = Show.convertX(width, LIST_L);
		final float y = Show.convertY(height, LIST_B);
		final float w = Show.convertW(width, LIST_R, x);
		final float h = Show.convertH(height, LIST_T, y);
		
		list = new ScrollPane(table);
		list.setFadeScrollBars(true);
		list.setWidth(w);
		list.setHeight(h);
		list.setPosition(x, y);
	}
	
	private void loadFilter()
	{
		filter = new Group();
		
		Texture texture = new Texture("data/colors/black.png");
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Image image = new Image(texture);
		image.setWidth(width);
		image.setHeight(height);
		image.setPosition(0, 0);

		filter.addActor(image);
	}
	
	/*private void resetDetails()
	{
		if (details == null)
			details = new Group();
		else
			details.clear();
		
		//details.setPosition(Show.convertX(width, DETAILS_L), Show.convertY(height, DETAILS_B));
	}
	
	private void loadDetails(final Comin comin)
	{
		
		resetDetails();

		//stage.addActor(new Item(comin));

		//final float x = Show.convertX(width, DETAILS_L);
		//final float x = 1280;
		//final float y = Show.convertY(height, DETAILS_B);
		//final float w = Show.convertW(width, DETAILS_R, x);
		//final float cp = width * DETAILS_R / 100;
		//final float w =  width - (cp + 1280);
		//final float h = Show.convertH(height, DETAILS_T, y);
		
		final float w = width;
		final float h = height * .8f;
		
		//Details Group
		details.setWidth(w);
		details.setHeight(h);
		details.setOrigin(details.getWidth()/2, details.getHeight()/2);
		details.setPosition(0, 0);

		//Details Background
		details.addActor(rectangle(w, h, 0, 0, 0, 0, new Color[]{DETAILS_BG_CLR.get()}));
		final float tW = (h * 16) / 9;
		Group thumb = comin.getWebThumb(0, 0, tW, h);
		details.addActor(thumb);

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
		

		details.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (Comin.getJsonPath(comin.id).exists())
					reader.setScreen(new ReaderScreen(reader, comin));
				else
					Comin.download(comin.id);
			}
		});
	}*/
	
	private Group bar(float l, float t, float r, float b)
	{
		float x = width * l / 100;
		float y = height * b / 100;
		float w = width - ((width * r / 100) + x);
		float h = height - ((height * t / 100) + y);
		
		Group group = new Group();
		group.setWidth(w);
		group.setHeight(h);
		group.setPosition(x, y);
		group.setOrigin(group.getWidth()/2, group.getHeight()/2);
		
		Gdx.app.log(TAG, "Bar x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);

		group.addActor(rectangle(w, h, 0, 0, 0, 0, new Color[]{BAR_BORDER_CLR.get()}));
		group.addActor(rectangle(w, h, 0, BAR_MARGIN, 0, BAR_MARGIN, new Color[]{BAR_CLR.get()}));
		
		return group;
	}
	
	private Actor rectangle(float width, float height, float l, float t, float r, float b, final Color[] colors)
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
				if (colors.length == 1)
				{
					sr.setColor(colors[0]);
					sr.filledRect(x, y, w, h);
				}
				else if (colors.length == 4)
				{
					sr.filledRect(x, y, w, h, colors[0], colors[1], colors[2], colors[3]);
				}
				sr.end();
				batch.begin();
			}
		};
		rect.setWidth(w);
		rect.setHeight(h);
		rect.setPosition(x, y);
		
		Gdx.app.log(TAG, "Rectangle x:" + x + ", y:" + y + ", w:" + w + ", h:" + h);
	
		return rect;
	}

	private List<Comin> downloadComins(String url)
	{
		List<Comin> comins = new LinkedList<Comin>();
		try 
		{
			String json = DownloadHandler.download(new URL(url));
			if (json != null && json.length() > 0)
				comins = Arrays.asList(new Gson().fromJson(json, Comin[].class));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return comins;
	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClearColor(BG_CLR_TOP.getRFloat(), BG_CLR_TOP.getGFloat(), BG_CLR_TOP.getBFloat(), BG_CLR_TOP.getAFloat());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();

		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}

	@Override
	public void resume() {
	}

}
