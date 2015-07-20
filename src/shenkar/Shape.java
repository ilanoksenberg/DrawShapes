package shenkar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;


abstract class Shape {
	Point2D p1;
	Point2D p2;
	Point2D p3;
	Point2D p4;
	
	Point2D S_P1=null;
	Point2D S_P2=null;
	Point2D S_P3=null;
	Point2D S_P4=null;
	
	Color	c = Color.black;
	
	public abstract String WhoAmI();
	public abstract void drawShape(Graphics g);
	public abstract void setShape(Point2D p1,Point2D p2,boolean R);
	public abstract void setShape(Point2D p1,Point2D p2,Point2D p3,Point2D p4);
	public abstract Point2D getCenterPoint();
	
	

}
