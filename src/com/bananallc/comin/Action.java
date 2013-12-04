package com.bananallc.comin;

import java.util.List;

import com.badlogic.gdx.Gdx;

public class Action
{
	private int id; //id of action - used to enable or disable action
	private int type; //what does the action do
	private int[] targets; // targets[0] -> actionId, showId, branchId, soundId, endingId >> targets[1] -> sceneId, animListId >> targets[...] actionIds
	private float[] values; // values[0] -> endVolume >> values[1] -> startVolume >> values[2] -> fadeDuration
	private int repeat = 0; //times to repeat, -1 = continuously
	private float delay = 0; // delay for action to trigger
	private boolean enabled = true; //handler will check if cancelled before running
	
	//Assignments
	private boolean onStart = false;
	private boolean onEnd = false;
	private List<Click> onClick;
	private List<Rotate> onRotate;

	public final static int REPEAT_CONTINUOUSLY = -1;
	private final static String TAG = "Action";

	//Comin Control
	public final static int TYPE_CHOOSE_SCENE = 0;
	public final static int TYPE_CHOOSE_ENDING = 1;
	public final static int TYPE_END_COMIN = 2;
	//Action Control
	public final static int TYPE_ENABLE_ACTION = 5;
	public final static int TYPE_DISABLE_ACTION = 6;
	public final static int TYPE_START_ACTIONS = 7;
	public final static int TYPE_NEXT_STEP_SEQUENCE = 8;
	public final static int TYPE_PREVIOUS_STEP_SEQUENCE = 9;
	//Music Control
	public final static int TYPE_PLAY_MUSIC = 10;
	public final static int TYPE_PAUSE_MUSIC = 11;
	public final static int TYPE_STOP_MUSIC = 12;
	public final static int TYPE_SET_MUSIC_VOLUME = 13;
	//Sound Control
	public final static int TYPE_PLAY_SOUND = 20;
	public final static int TYPE_PAUSE_SOUND = 21;
	public final static int TYPE_PAUSE_ALL_SOUNDS = 22;
	//Show Control
	public final static int TYPE_START_ANIMATION = 30;
	public final static int TYPE_STOP_ANIMATION = 31;
	
	//Target Types
	public final static int TARGET_BRANCH = 1;
	public final static int TARGET_SCENE = 2;
	public final static int TARGET_ENDING = 3;
	public final static int TARGET_MUSIC = 4;
	public final static int TARGET_SOUND = 5;
	public final static int TARGET_SHOW = 6;
	public final static int TARGET_ANIM_LIST = 7;
	public final static int TARGET_SEQUENCE = 8;
	
	//Value Types
	public final static int VALUE_END_VOLUME = 1;
	public final static int VALUE_START_VOLUME = 2;
	public final static int VALUE_FADE_DURATION = 3;
	
	public int getId()
	{
		return id;
	}
	
	public int getType()
	{
		return type;
	}
	
	public int getTarget(int targetType)
	{
		int position = 0;
		if (targetType == TARGET_SCENE || targetType == TARGET_ANIM_LIST)
			position = 1;
		
		int target = 0;
		if (targets != null && targets.length >= position+1)
			target = targets[position];
		
		return target;
	}
	
	public float getValue(int valueType)
	{
		int position = 0;
		if (valueType == VALUE_END_VOLUME)
			position = 0;
		if (valueType == VALUE_START_VOLUME)
			position = 1;
		else if (valueType == VALUE_FADE_DURATION)
			position = 2;
		
		float value = 0;
		if (values != null && values.length >= position+1)
			value = values[position];
		
		return value;
	}
	
	public int[] getTargets()
	{
		return targets;
	}
	
	public int getRepeat()
	{
		return repeat;
	}
	
	public float getDelay()
	{
		return delay;
	}
	
	public String getLog()
	{
		String log = "id:" + id + ", type:";
		if (type == TYPE_PLAY_MUSIC)
			log = log + "play music";
		else if (type == TYPE_PAUSE_MUSIC)
			log = log + "pause music";
		else if (type == TYPE_PLAY_SOUND)
			log = log + "play sound";
		else if (type == TYPE_START_ANIMATION)
			log = log + "animate, show:" + getTarget(TARGET_SHOW);
		else if (type == TYPE_CHOOSE_SCENE)
			log = log + "choose scene";
		else if (type == TYPE_CHOOSE_ENDING)
			log = log + "choose ending";
		else if (type == TYPE_SET_MUSIC_VOLUME)
			log = log + "set music volume";
		
		log = log + ", delay:" + delay + ", repeat:" + repeat;
		
		return log;
	}
	
	public boolean checkEnabled()
	{
		return enabled;
	}
	
	public boolean checkOnSceneStart()
	{
		boolean check = false;
		if (onStart)
		{
			check = true;
		}
		return check;
	}
	
	public boolean checkOnSceneEnd()
	{
		boolean check = false;
		if (onEnd)
		{
			check = true;
		}
		return check;
	}
	
	public boolean checkOnClick(int showId)
	{
		boolean check = false;
		if (onClick != null)
		{
			for (int a = 0; a < onClick.size(); a++)
			{
				if (showId == onClick.get(a).showId)
				{
					check = true;
					break;
				}
			}
		}
		return check;
	}
	
	public boolean checkOnRotate()
	{
		boolean check = false;
		if (onRotate != null)
			check = true;
		return check;
	}
	
	public boolean checkInsideRotateRange(int degree)
	{
		boolean check = false;
		
		for (int a = 0; a < onRotate.size(); a++)
		{
			Gdx.app.log(TAG, "checkInsideRotateRange, degree:" + degree + ", start:" + onRotate.get(a).startRange + ", end:" + onRotate.get(a).endRange);
			if (onRotate.get(a).startRange > onRotate.get(a).endRange)
			{
				if ((degree >= onRotate.get(a).startRange && degree <= 360) || (degree >= 0 && degree <= onRotate.get(a).endRange))
				{
					check = true;
					break;
				}
			}
			else if (degree >= onRotate.get(a).startRange && degree <= onRotate.get(a).endRange)
			{
				check = true;
				break;
			}
		}
		
		
		return check;
	}
	
	//Assignment Class
	public static class Click
	{
		int showId;
	}

	//Assignment Class
	public static class Rotate
	{
		float startRange; //value must be greater than or equal to
		float endRange; //value must be less than or equal to
	}
}
