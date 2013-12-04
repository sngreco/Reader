package com.bananallc.comin;


public class Background 
{
	String[] colors;
	String borderColor;
	int gradientType, gradientOrientation;
	double borderWidth, borderDashLength, borderDashGap, cornerRadius; //percent of screen
	
	public final static int ORIENTATION_BL_TR = 0;
	public final static int ORIENTATION_BOTTOM_TOP = 1;
	public final static int ORIENTATION_BR_TL = 2;
	public final static int ORIENTATION_LEFT_RIGHT = 3;
	public final static int ORIENTATION_RIGHT_LEFT = 4;
	public final static int ORIENTATION_TL_BR = 5;
	public final static int ORIENTATION_TOP_BOTTOM = 6;
	public final static int ORIENTATION_TR_BL = 7;
	
	public final static int TYPE_LINEAR = 0;
	public final static int TYPE_RADIAL = 1;
	public final static int TYPE_SWEEP = 2;
	
	//private final static String TAG = "Background";
	
	/*public GradientDrawable generate(int screenWidth)
	{
    	GradientDrawable background = new GradientDrawable();
    	
    	//set colors
    	if (colors.length > 1)
    		background = new GradientDrawable(getOrientation(), getColors());
    	else if (colors.length == 1)
    		background.setColor(getColors()[0]);
    	
    	//set border
    	if (borderWidth > 0)
    	{
    		int thickness = (int) (borderWidth * screenWidth / 100);
        	background.setStroke(thickness, getColor(borderColor));
    	}
    	
    	//set rounded corners
    	if (cornerRadius > 0)
    	{
    		cornerRadius = cornerRadius * screenWidth / 100;
        	background.setCornerRadius((float) cornerRadius);
    	}
    	
		return background;
	}
	
	private int[] getColors()
	{
		int[] temp = new int[colors.length];
		for (int a = 0; a < colors.length; a++)
		{
			temp[a] = getColor(colors[a]);
		}
		return temp;
	}
	
	private int getColor(String color)
	{
		int temp = Color.WHITE;
		if (color != null && color != "")
			temp = Color.parseColor(color);
		Log.i(TAG, "getColor, string:" + color + ", int:" + temp);
		return temp;
	}
	
	private GradientDrawable.Orientation getOrientation()
	{
		if (gradientOrientation == ORIENTATION_BL_TR)
			return GradientDrawable.Orientation.BL_TR;
		else if (gradientOrientation == ORIENTATION_BOTTOM_TOP)
			return GradientDrawable.Orientation.BOTTOM_TOP;
		else if (gradientOrientation == ORIENTATION_BR_TL)
			return GradientDrawable.Orientation.BR_TL;
		else if (gradientOrientation == ORIENTATION_LEFT_RIGHT)
			return GradientDrawable.Orientation.LEFT_RIGHT;
		else if (gradientOrientation == ORIENTATION_RIGHT_LEFT)
			return GradientDrawable.Orientation.RIGHT_LEFT;
		else if (gradientOrientation == ORIENTATION_TL_BR)
			return GradientDrawable.Orientation.TL_BR;
		else if (gradientOrientation == ORIENTATION_TOP_BOTTOM)
			return GradientDrawable.Orientation.TOP_BOTTOM;
		else
			return GradientDrawable.Orientation.TR_BL;
	}*/
}
