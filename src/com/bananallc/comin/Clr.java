package com.bananallc.comin;

import com.badlogic.gdx.graphics.Color;

public class Clr{
	//private final static String TAG = "Clr";
	
	private float r;
	private float g;
	private float b;
	private float a = 255;
	
	public Clr()
	{
		
	}
	
	public Clr(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color get()
	{
		float rF = getRFloat();
		float gF = getGFloat();
		float bF = getBFloat();
		float aF = getAFloat();
		
		//Gdx.app.log(TAG, "r:" + rF + ", g:" + gF + ", b:" + bF + ", a:" + aF);
		
		return new Color(rF,gF,bF,aF);
	}
	
	public float getRFloat()
	{
		return r/255;
	}
	
	public float getGFloat()
	{
		return g/255;
	}
	
	public float getBFloat()
	{
		return b/255;
	}
	
	public float getAFloat()
	{
		return a/255;
	}
}
