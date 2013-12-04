package com.bananallc.utilities;

import java.io.File;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Timer;
import com.bananallc.comin.Msc;
import com.bananallc.comin.reader.Reader;

public class MusicHandler implements Disposable{
	
	private List<Msc> musics;
	private Music music;
	private boolean enabled = true;
	private int musicId, volume;
	private File sndDir;
	private Timer timer;

	private final static int VOLUME_MAX = 100;
	private final static int VOLUME_MIN = 0;
	
	private final static String TAG = "MusicHandler";
	
	public MusicHandler(List<Msc> musics, File sndDir)
	{
		timer = new Timer();
		this.musics = musics;
		this.sndDir = sndDir;
		if (musics != null)
			Gdx.app.log(TAG, musics.size() + " Files loaded from:" + sndDir);
		else
			Gdx.app.log(TAG, "No files loaded from:" + sndDir);
	}
	
	public boolean play(int id, int volume)
	{
		Msc msc = getMsc(id);
		
		if (!enabled || msc==null)
		{
			Gdx.app.log(TAG, "Music disabled or msc not found");
			return false;
		}
		
		if (musicId != id)
			stop();
		
		if (music == null)
			music = Gdx.audio.newMusic(new FileHandle(sndDir + File.separator + msc.input));
		
		music.setLooping(msc.loop);
		setVolume(volume);
		music.play();
		musicId = id;

		return true;
	}
	
	public boolean fade(final int id, final int startVol, final int endVol, final float duration)
	{
		if (!play(id, startVol))
		{
			Gdx.app.log(TAG, "Fade failed because Play failed");
			return false;
		}
		
		final int fadeCounter = endVol - startVol; //Example: 100 - 0 = 100 (increasing) OR 0 - 100 = -100 (decreasing)
		final float fadeIncrement = Math.abs(duration / fadeCounter);

		for (int a = 0; a < Math.abs(fadeCounter); a++)
		{
			timer.scheduleTask(new Timer.Task() {
				
				@Override
				public void run() {	
					if (fadeCounter > 0)
						setVolume(volume + 1);
					else
						setVolume(volume - 1);
					
					if (volume == 0)
						stop();
					else if (volume == endVol)
						timer.clear();
				}
			}, fadeIncrement * a);
		}
		
		
		return true;
	}
	
	private Msc getMsc(int id)
	{
		Msc msc = null;
		if (musics != null)
		{
			for (int a = 0; a < musics.size(); a++)
			{
				if (musics.get(a).id == id)
				{
					msc = musics.get(a);
					break;
				}
			}
		}
		
		if (musics == null)
		{
			Gdx.app.log(TAG, "No Music Attached To Comin");
		}
		else if (msc == null)
		{
			Gdx.app.log(TAG, "ID:" + id + " not found");
		}
		
		return msc;
	}
	
	public void setVolume(int v)
	{
		if (music != null)
		{
			
			if (v > VOLUME_MAX)
				v = VOLUME_MAX;
			else if (v < VOLUME_MIN)
				v = VOLUME_MIN;
			
			volume = v;
			
			float vol = v/100f;
			if (Reader.DEBUG) Gdx.app.log(TAG, "setVolume intVol:" + v + ", floatVol:" + vol);
			
			music.setVolume(v/100f);
		}
	}
	
	public void pause(float fadeDur)
	{
		if (music != null)
			music.pause();
	}
	
	public void stop()
	{
		if (music != null)
		{
			timer.clear();
			music.stop();
			music.dispose();			
			Gdx.app.log(TAG, "Music Stopped");
		}
	}
	
	public void enable()
	{
		enabled = true;
		Gdx.app.log(TAG, "Music Enabled");
	}
	
	public void disable()
	{
		enabled = false;
		stop();
		Gdx.app.log(TAG, "Music Disabled");
	}

	@Override
	public void dispose() {
		stop();
	}

}
