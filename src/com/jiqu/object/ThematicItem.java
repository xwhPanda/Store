package com.jiqu.object;

import java.io.Serializable;

public class ThematicItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private int click;
	private String focus;
	private long id;
	private String intrduce;
	private int num;
	private String title;
	
	public synchronized int getClick() {
		return click;
	}
	public synchronized void setClick(int click) {
		this.click = click;
	}
	public synchronized String getFocus() {
		return focus;
	}
	public synchronized void setFocus(String focus) {
		this.focus = focus;
	}
	public synchronized long getId() {
		return id;
	}
	public synchronized void setId(long id) {
		this.id = id;
	}
	public synchronized String getIntrduce() {
		return intrduce;
	}
	public synchronized void setIntrduce(String intrduce) {
		this.intrduce = intrduce;
	}
	public synchronized int getNum() {
		return num;
	}
	public synchronized void setNum(int num) {
		this.num = num;
	}
	public synchronized String getTitle() {
		return title;
	}
	public synchronized void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return "ThematicItem [click=" + click + ", focus=" + focus + ", id=" + id + ", intrduce=" + intrduce + ", num=" + num + ", title=" + title + "]";
	}
}
