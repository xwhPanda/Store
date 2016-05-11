package com.jiqu.object;

import java.io.Serializable;
import java.util.Arrays;

public class ThematicInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ThematicItem[] data1;
	private ThematicItem[] data2;
	
	public synchronized ThematicItem[] getData1() {
		return data1;
	}
	public synchronized void setData1(ThematicItem[] data1) {
		this.data1 = data1;
	}
	public synchronized ThematicItem[] getData2() {
		return data2;
	}
	public synchronized void setData2(ThematicItem[] data2) {
		this.data2 = data2;
	}
	
	@Override
	public String toString() {
		return "ThematicInfo [data1=" + Arrays.toString(data1) + ", data2=" + Arrays.toString(data2) + "]";
	}
}
