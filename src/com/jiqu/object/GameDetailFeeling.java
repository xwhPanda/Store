package com.jiqu.object;

public class GameDetailFeeling {
	private float f1;
	private float f2;
	private float f3;
	private float f4;
	private float f5;
	public synchronized float getF1() {
		return f1;
	}
	public synchronized void setF1(float f1) {
		this.f1 = f1;
	}
	public synchronized float getF2() {
		return f2;
	}
	public synchronized void setF2(float f2) {
		this.f2 = f2;
	}
	public synchronized float getF3() {
		return f3;
	}
	public synchronized void setF3(float f3) {
		this.f3 = f3;
	}
	public synchronized float getF4() {
		return f4;
	}
	public synchronized void setF4(float f4) {
		this.f4 = f4;
	}
	public synchronized float getF5() {
		return f5;
	}
	public synchronized void setF5(float f5) {
		this.f5 = f5;
	}
	@Override
	public String toString() {
		return "GameDetailFeeling [f1=" + f1 + ", f2=" + f2 + ", f3=" + f3 + ", f4=" + f4 + ", f5=" + f5 + "]";
	}
}
