package com.bananallc.comin;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;

public class Scene 
{
	private static final String TAG = "Scene";
	
	private static final String DEFAULT_BACKGROUND = "#000000"; //Black
	
	private int id;
	private int orientation;
	private String background = DEFAULT_BACKGROUND;
	private List<Show> shows;
	private List<Action> actions;
	private List<AnimList> animLists;
	private List<Sequence> sequences;
	private Clr color;
	
	private boolean preview = false;
	
	public void load(final float width, final float height, final String imageDir, final String fontDir)
	{
		Gdx.app.log(TAG, "Begin Preparing Shows");
		for (int a = 0; a < shows.size(); a++)
		{
			shows.get(a).load(width, height, imageDir, fontDir);
		}
		Gdx.app.log(TAG, "End Preparing Shows");
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getOrientation()
	{
		return orientation;
	}
	
	public Color getColor()
	{
		if (color == null)
			return Color.BLACK;
		else
			return color.get();
	}
	
	public boolean getPreview()
	{
		return preview;
	}
	
	public String getBackground()
	{
		return background;
	}
	
	public List<Show> getShows()
	{
		return shows;
	}
	
	public Sequence getSequence(int id)
	{
		Sequence sequence = null;
		for (int a = 0; a < sequences.size(); a++)
		{
			if (sequences.get(a).getId() == id)
			{
				sequence = sequences.get(a);
				break;
			}
		}
		return sequence;
	}
	
	public Action getAction(int id)
	{
		Action action = null;
		for (int a = 0; a < actions.size(); a++)
		{
			if (actions.get(a).getId() == id)
			{
				action = actions.get(a);
				break;
			}
		}
		return action;
	}
	
	public List<Action> getActions(int[] ids)
	{
		List<Action> acts = new LinkedList<Action>();
		
		if (ids == null)
			return acts;
		
		for (int a =0; a < ids.length; a++)
		{
			Action action = getAction(ids[a]);
			if (action != null)
				acts.add(action);
		}
		
		return acts;
	}
	
	public List<Action> getClickActions(int showId)
	{
		List<Action> temp = new LinkedList<Action>();
		if (actions != null)
		{
			for (int a = 0; a < actions.size(); a++)
	    	{
	    		if (actions.get(a).checkOnClick(showId) && actions.get(a).checkEnabled())
	    			temp.add(actions.get(a));
	    	}
		}
		return temp;
	}
	
	public List<Action> getStartActions()
	{
		List<Action> temp = new LinkedList<Action>();
		if (actions != null)
		{
			for (int a = 0; a < actions.size(); a++)
	    	{
	    		if (actions.get(a).checkOnSceneStart())
	    			temp.add(actions.get(a));
	    	}
		}
		return temp;
	}
	
	public List<Action> getEndActions()
	{
		List<Action> temp = new LinkedList<Action>();
		if (actions != null)
		{
			for (int a = 0; a < actions.size(); a++)
	    	{
	    		if (actions.get(a).checkOnSceneEnd())
	    			temp.add(actions.get(a));
	    	}
		}
		return temp;
	}
	
	public List<Action> getRotateActions()
	{
		List<Action> temp = new LinkedList<Action>();
		if (actions != null)
		{
			for (int a = 0; a < actions.size(); a++)
	    	{
	    		if (actions.get(a).checkOnRotate())
	    			temp.add(actions.get(a));
	    	}
		}
		return temp;
	}
	
	public Show getShow(int showId)
	{
		Show show = null;
		for (int a = 0; a < shows.size(); a++)
		{
			if (shows.get(a).id == showId)
			{
				show = shows.get(a);
				break;
			}
		}
		return show;
	}
	
	public Group getGroup(int showId)
	{
		Group group = null;
		for (int a = 0; a < shows.size(); a++)
		{
			if (shows.get(a).id == showId)
			{
				group = shows.get(a).group;
				break;
			}
		}
		return group;
	}
	
	public AnimList getAnimList(int id)
	{
		AnimList animList = null;
		for (int a = 0; a < animLists.size(); a++)
		{
			if (animLists.get(a).getId() == id)
			{
				animList = animLists.get(a);
				break;
			}
		}
		return animList;
	}
	
	public void clear()
	{
		for (int a = 0; a < shows.size(); a++)
		{
			shows.get(a).clear();
		}
		
		if (sequences == null) return;
		
		for (int a = 0; a < sequences.size(); a++)
		{
			sequences.get(a).reset();
		}
	}
	
	public void addAnim(int showId, int animId, int width, int height)
	{
		AnimList anim = getAnimList(animId);
		Group group = getGroup(showId);
		if (anim != null && group != null)
			group.addAction(anim.getAnims(width, height));
	}
	
	/*public boolean checkOrientationLandscape()
	{
		boolean landscape = false;
		if (orientation == ORIENTATION_LANDSCAPE)
			landscape = true;
		return landscape;
	}
	
	public boolean checkOrientationPortrait()
	{
		boolean portrait = false;
		if (orientation == ORIENTATION_PORTRAIT)
			portrait = true;
		return portrait;
	}
	
	public boolean checkOrientationAuto()
	{
		boolean auto = false;
		if (orientation == ORIENTATION_AUTO)
			auto = true;
		return auto;
	}*/
}
