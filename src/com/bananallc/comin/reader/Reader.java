package com.bananallc.comin.reader;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.bananallc.comin.store.StoreScreen;

public class Reader extends Game {
	
	public static final String TAG = "Reader";
	public static final boolean DEBUG = false;
	
	@Override
	public void create() {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		Gdx.app.log(TAG, "Start App");
		Texture.setEnforcePotImages(false);
		
		//setScreen(new ReaderScreen(this));
		setScreen(new StoreScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {	
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
