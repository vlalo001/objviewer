package com.dim.objviewer;

public class Vert3 {
	private double a,b,c;
	
	

	public Vert3(double a, double b,double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public double getVertA(){
		return a;
	}
	
	public double getVertB(){
		return b;
	}
	
	public double getVertC(){
		return c;
	}
	
	public double[] getVert(){
		double[] vt = {a,b,c};
		return vt;
	}
	
	public void setVertA(double a) {
		this.a = a;
	}

	public void setVertB(double b) {
		this.b = b;
	}

	public void setVertC(double c) {
		this.c = c;
	}
	
	public void setVert(double a,double b,double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	

}