package com.bananallc.comin.reader;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.bananallc.comin.Action;
import com.bananallc.comin.Comin;
import com.bananallc.comin.Scene;
import com.bananallc.comin.Show;
import com.bananallc.utilities.MusicHandler;
import com.bananallc.utilities.SoundHandler;

public class ReaderScreen implements Screen {

	private final static String TAG = "ReaderScreen";

	//private Reader reader;
	private Comin comin;
	private Scene scene;
	private MusicHandler musicHandler;
	private SoundHandler soundHandler;
	
	//private final static int[] ORIENTATION_LANDSCAPE_ONE_RANGE = {315, 45};
	//private final static int[] ORIENTATION_PORTRAIT_ONE_RANGE = {45, 135};
	//private final static int[] ORIENTATION_LANDSCAPE_TWO_RANGE = {135, 225};
	//private final static int[] ORIENTATION_PORTRAIT_TWO_RANGE = {225, 315};
	
	public final static int ORIENTATION_LANDSCAPE_ONE = 0;
	public final static int ORIENTATION_PORTRAIT_ONE = 1;
	public final static int ORIENTATION_LANDSCAPE_TWO = 2;
	public final static int ORIENTATION_PORTRAIT_TWO = 3;

	private Stage stage;
	private Timer timer;
	private SpriteBatch batch;
	private Color background;
	//private FPSLogger logger;
	private int width, height;
	private int orientation; //identifies which quadrant the rotation is set to
	
	public ReaderScreen(Reader reader, Comin comin)
	{
		//this.reader = reader;
		batch = new SpriteBatch();
		timer = new Timer();
		stage = new Stage();
		orientation = ORIENTATION_LANDSCAPE_ONE;
		//logger = new FPSLogger();
		loadComin(comin.id);
	}

	@Override
	public void show() {
		//batch = new SpriteBatch();
		//timer = new Timer();
		//stage = new Stage();
		//orientation = ORIENTATION_LANDSCAPE_ONE;
		//logger = new FPSLogger();
		//loadComin(30);
		Gdx.input.setInputProcessor(stage);	
	}

	@Override
	public void resize(int width, int height) {
		if (this.width == width && this.height == height)
			return;
		
		Gdx.app.log(TAG, "Set Viewport: w:" + width + ", h:" + height);
		this.width = width;
		this.height = height;
		stage.setViewport(width, height, true);
		loadScene();
	}

	private void loadComin(int id) {
		if (comin != null)
			return;
			
		//Comin.download(id);
		comin = Comin.deserialize(Comin.getJsonPath(id));
		musicHandler = new MusicHandler(comin.getMusics(), Comin.getSoundDir(id));
		soundHandler = new SoundHandler(comin.getSounds(), Comin.getSoundDir(id));
	}

	private void loadScene() {		
		clearScene();
		scene = comin.getCurrentScene();
		Gdx.app.log(TAG, "Load Scene:" + scene.getId());
		setOrientation();
		scene.load(width, height, Comin.getImageDir(comin.id).getAbsolutePath(), Comin.getFontDir(comin.id).getAbsolutePath());
		background = scene.getColor();

		for (int a = 0; a < scene.getShows().size(); a++) {
			Gdx.app.log(TAG, "Load Child:" + a);
			final Show show = scene.getShows().get(a);
			final Group group = show.getGroup();
			group.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, "Show:" + show.id + ", Touch Down");
					return true;
				}

				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					Gdx.app.log(TAG, "Show:" + show.id + ", Touch Up");
					performActions(scene.getClickActions(show.id), "click, id:"
							+ show.id);
				}
			});
			stage.addActor(group);
		}
		
		performActions(scene.getStartActions(), "loadScene id:" + scene.getId());
		//showToc();
	}
	
	private void setOrientation()
	{
		int newOrientation = scene.getOrientation();
		int rotation = (orientation - newOrientation) * 90;
		Gdx.app.log(TAG, "rotateScreen, oldOrientation:" + orientation + ", newOrientation:" + newOrientation + ", rotation:" + rotation);
		orientation = newOrientation;
		
		if (orientation == ORIENTATION_PORTRAIT_ONE || orientation == ORIENTATION_PORTRAIT_TWO)
		{
			width = Gdx.graphics.getHeight();
			height = Gdx.graphics.getWidth();
		}
		else
		{
			width = Gdx.graphics.getWidth();
			height = Gdx.graphics.getHeight();
		}

		//if (Gdx.app.getType() == ApplicationType.Desktop)
		//{
			//Gdx.graphics.setDisplayMode(width, height, false);
		//}
		//else
		//{
			stage.getCamera().rotate(rotation, 0, 0, 1);	
			stage.getCamera().position.set(width/2, height/2, 0);
		//}
	}
	
	/*private void showToc()
	{
		Table table = new Table();
		String path = Comin.getImageDir(comin.id) + File.separator + "000";
		for (int b = 0; b < 3; b++)
		{
			for (int a = 0; a < 9; a++)
			{
				Texture texture = new Texture(path + a + ".jpg");
				Image image = new Image(texture);
				image.setWidth(200);
				image.setHeight(100);
				table.add(image).size(200, 100);
			}
			table.row();
		}
		//ScrollPaneStyle sps = new ScrollPaneStyle();
		ScrollPane sp = new ScrollPane(table);
		sp.setFadeScrollBars(true);
		sp.setWidth(Gdx.graphics.getWidth());
		sp.setHeight(Gdx.graphics.getHeight());
		
		Gdx.app.log(TAG, "tWidth:" + table.getWidth() + ", tHeight:" + table.getHeight() + ", spWidth:" + sp.getWidth() + ", spHeight:" + sp.getHeight());
		stage.addActor(sp);
	}*/

	private float calcActionsDuration(final List<Action> actions) {
		float duration = 0;

		for (int a = 0; a < actions.size(); a++) {
			final Action action = actions.get(a);
			float temp = action.getDelay();

			if (action.getType() == Action.TYPE_PLAY_MUSIC || action.getType() == Action.TYPE_PAUSE_MUSIC)
				temp += action.getValue(Action.VALUE_FADE_DURATION);
			else if (action.getType() == Action.TYPE_START_ANIMATION)
				temp += scene.getAnimList(action.getTarget(Action.TARGET_ANIM_LIST)).calcDuration();

			if (temp > duration)
				duration = temp;
		}

		Gdx.app.log(TAG, "Actions Duration:" + duration);
		return duration;
	}

	private void clearScene() {
		int children = 0;
		if (stage != null)
		{
			stage.clear();
			children = stage.getActors().size;
		}
		timer.clear();
		if (scene != null)
			scene.clear();
		Gdx.app.log(TAG, "Scene Cleared, Children Remain:" + children);
	}

	private void performActions(List<Action> actions, String caller) {
		final int size = actions.size();
		Gdx.app.log(TAG, "performActions, caller:" + caller + ", size:" + size);
		for (int a = 0; a < size; a++)
			performAction(actions.get(a), caller, 0);
	}

	private void endScene(final int branchId, final int sceneId) {
		Gdx.app.log(TAG, "endScene, chosen branch:" + branchId
				+ ", chosen scene:" + sceneId);

		final int endingSceneId = scene.getId();
		final List<Action> actions = scene.getEndActions();

		performActions(actions, "endScene id:" + endingSceneId);

		timer.scheduleTask(new Task() {
			@Override
			public void run() {
				Gdx.app.log(TAG, "***END SCENE " + endingSceneId + "***");
				comin.setScene(branchId, sceneId);
				loadScene();
			}
		}, calcActionsDuration(actions));
	}

	private void performAction(final Action action, final String caller, final int count) {
		if (action == null) {
			Gdx.app.log(TAG, "performAction, " + caller + " ACTION == NULL");
			return;
		}
		
		Gdx.app.log(TAG,
				"performAction, " + caller + " start:" + action.getLog());
		if (action.getType() == Action.TYPE_PLAY_MUSIC)
			musicHandler.fade(action.getTarget(Action.TARGET_MUSIC),
					(int) action.getValue(Action.VALUE_START_VOLUME),
					(int) action.getValue(Action.VALUE_END_VOLUME),
					action.getValue(Action.VALUE_FADE_DURATION));
		else if (action.getType() == Action.TYPE_PAUSE_MUSIC)
			musicHandler.fade(action.getTarget(Action.TARGET_MUSIC),
					(int) action.getValue(Action.VALUE_START_VOLUME),
					(int) action.getValue(Action.VALUE_END_VOLUME),
					action.getValue(Action.VALUE_FADE_DURATION));
		else if (action.getType() == Action.TYPE_SET_MUSIC_VOLUME)
			musicHandler.setVolume((int) action
					.getValue(Action.VALUE_END_VOLUME));
		else if (action.getType() == Action.TYPE_PLAY_SOUND)
			soundHandler.play(action.getTarget(Action.TARGET_SOUND));
		else if (action.getType() == Action.TYPE_START_ANIMATION)
			scene.addAnim(action.getTarget(Action.TARGET_SHOW), action.getTarget(Action.TARGET_ANIM_LIST), width, height);
		else if (action.getType() == Action.TYPE_CHOOSE_SCENE)
			endScene(action.getTarget(Action.TARGET_BRANCH),
					action.getTarget(Action.TARGET_SCENE));
		else if (action.getType() == Action.TYPE_START_ACTIONS)
			performActions(scene.getActions(action.getTargets()),
					"performAction Start Actions");
		else if (action.getType() == Action.TYPE_NEXT_STEP_SEQUENCE)
			performAction(
					scene.getAction(scene.getSequence(
							action.getTarget(Action.TARGET_SEQUENCE)).next()),
					"performAction Next Step Sequence", 0);
		else if (action.getType() == Action.TYPE_NEXT_STEP_SEQUENCE)
			performAction(
					scene.getAction(scene.getSequence(
							action.getTarget(Action.TARGET_SEQUENCE)).previous()),
					"performAction Previous Step Sequence", 0);
		
		if (action.getRepeat() > count)
			performAction(action, "performAction repeat, count:" + count, count+1);

		/*
		 * else if (action.getType() == Action.TYPE_END_COMIN) dialogEnding();
		 */
		// else if (action.getType() == Action.TYPE_STOP_CLICK)
		// stopClick(action.value);

		/*
		 * if (count < action.getRepeat() || action.getRepeat() ==
		 * Action.REPEAT_CONTINUOUSLY) performAction(actionId, count+1,
		 * "performAction");
		 */
	}

	@Override
	public void render(float arg0) {
		Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		checkRotation();
		//logger.log();

		batch.begin();
		stage.draw();
		batch.end();
	}
	
	private void checkRotation()
	{
		if (!Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer))
			return;
		
		int rotation = convertDegree(Gdx.input.getAccelerometerX(), Gdx.input.getAccelerometerY());

		List<Action> actions = scene.getRotateActions();
		for (int a = 0; a < actions.size(); a++)
		{
			if (actions.get(a).checkInsideRotateRange(rotation))
				performAction(actions.get(a), "checkRotation", 0);
		}
	}
	
	private int convertDegree(float x, float y)
	{
		int value = 0;
		
		if (y > 0 && x > 0)
			value = (int) (90 * y / 10);
		else if (y > 0 && x == 0)
			value = 90;
		else if (y > 0 && x < 0)
			value = (int) (180 - (90 * y / 10));
		else if (y == 0 && x < 0)
			value = 180;
		else if (y < 0 && x < 0)
			value = (int) (180 + (90 * -y / 10));
		else if (y < 0 && x == 0)
			value = 270;
		else if (y < 0 && x > 0)
			value = (int) (360 + (90 * y / 10));
		
		return value;
	}

	@Override
	public void dispose() {
		clearScene();
		batch.dispose();
		stage.dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
