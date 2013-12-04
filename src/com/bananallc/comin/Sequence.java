package com.bananallc.comin;

public class Sequence {
	
	private final static int OUT_OF_BOUNDS = -1;
	
	private int id;
	private int position = OUT_OF_BOUNDS;
	private int[] actionIds;
	
	public int getId()
	{
		return id;
	}
	
	public int next()
	{
		int actionId = OUT_OF_BOUNDS;
		
		position += 1;
		if (actionIds != null && actionIds.length > position)
			actionId = actionIds[position];
		else
			position -= 1;
		
		return actionId;
	}
	
	public int previous()
	{
		int actionId = OUT_OF_BOUNDS;
		
		position -= 1;
		if (actionIds != null && actionIds.length > position)
			actionId = position;
		else
			position += 1;
		
		return actionId;
	}
	
	public void reset()
	{
		position = OUT_OF_BOUNDS;
	}
}
