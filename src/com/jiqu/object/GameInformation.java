package com.jiqu.object;

import java.io.Serializable;

import android.R.integer;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class GameInformation implements Serializable {
	public String name;
	public String time;
	public Drawable nameIcon;
	public String des;
	public long size;
	public String downloadUrl;
	public Drawable subscript;
	public int score;
	
	public String sortName;
	public int adapterType = -1;
}
