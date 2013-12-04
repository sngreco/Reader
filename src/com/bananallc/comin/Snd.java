package com.bananallc.comin;

public class Snd {
	
	public String input;
	public int id, androidId, volume, repeat;
	public int type = TYPE_FILE;
	public int priority = 1;
	
	public final static int TYPE_ASSET = 0;
	public final static int TYPE_FILE = 1;
	
	public boolean checkTypeAsset()
	{
		if (type == TYPE_ASSET)
			return true;
		else
			return false;
	}
	
	public boolean checkTypeFile()
	{
		if (type == TYPE_FILE)
			return true;
		else
			return false;
	}
}
