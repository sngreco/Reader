package com.bananallc.comin;

import java.util.List;


public class Tag
{
	public int id, type;
	public String title, description, urlThumb;
	public List<Comin> comins; 
	public List<Link> links;
	public boolean selected = false;	
	
	public final static int TYPE_GENRE = 1;
	public final static int TYPE_DEVELOPER = 2;
	public final static int TYPE_SERIES = 3;
}
