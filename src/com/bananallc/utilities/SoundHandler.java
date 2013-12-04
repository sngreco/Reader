package com.bananallc.utilities;

import java.io.File;
import java.util.List;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.bananallc.comin.Snd;
import com.bananallc.utilities.LRUCache.CacheEntryRemovedListener;

public class SoundHandler implements CacheEntryRemovedListener<Snd, Sound>{
	
	private final static String TAG = "SoundHandler";
	private final static int MAX_LOADED_SOUNDS = 10;
	
	private float volume = 1f;
	private boolean enabled = true;
	private final LRUCache<Snd, Sound> soundCache;
	private File sndDir;
	private List<Snd> sounds;
	
	private final static int VOLUME_MAX_INT = 100;
	private final static int VOLUME_MIN_INT = 0;
	//private final static float VOLUME_MAX_FLOAT = 1;
	//private final static float VOLUME_FLOAT = 0;
	
	public SoundHandler(List<Snd> sounds, File sndDir)
	{
		soundCache = new LRUCache<Snd, Sound> (MAX_LOADED_SOUNDS);
		soundCache.setEntryRemovedListener(this);
		this.sndDir = sndDir;
		this.sounds = sounds;
		if (sounds != null) preLoad();
	}
	
	private void preLoad()
	{
		for (int a = 0; a < sounds.size() && a < MAX_LOADED_SOUNDS; a++)
		{
			FileHandle file = new FileHandle(sndDir + File.separator + sounds.get(a).input);
			Gdx.app.log(TAG, "Pre-Loading id:" + sounds.get(a).id + ", path:" + file.path());
			soundCache.add(sounds.get(a), Gdx.audio.newSound(file));
		}
	}
	
	public void play(int id)
	{
		Snd snd = getSound(id);
		if (enabled && snd != null)
		{
			Sound sound = soundCache.get(snd);
			if (sound == null)
			{
				FileHandle file = new FileHandle(sndDir + File.separator + snd.input);
				sound = Gdx.audio.newSound(file);
				soundCache.add(snd, sound);
			}
			sound.play(volume);
		}
	}
	
	private Snd getSound(int id)
	{
		Snd snd = null;
		for (int a =0; a < sounds.size(); a++)
		{
			if (id == sounds.get(a).id)
			{
				snd = sounds.get(a);
				break;
			}
		}
		return snd;
	}
	
	public void setVolume(int v)
	{
		if (v <= VOLUME_MAX_INT && v >= VOLUME_MIN_INT)
			volume = v/100f;
		this.volume = v;
	}
	
	public void enable()
	{
		enabled = true;
		Gdx.app.log(TAG, "Sound Enabled");
	}
	
	public void disable()
	{
		enabled = false;
		stop();
		Gdx.app.log(TAG, "Sound Disabled");
	}
	
	public void stop()
	{
		for (Sound sound : soundCache.retrieveAll()){
			sound.stop();
			sound.dispose();
		}
	}

	@Override
	public void notifyEntryRemoved(Snd snd, Sound sound) {
		Gdx.app.log(TAG, "Disposing sound:" + snd.id);
		sound.dispose();
	}

}
