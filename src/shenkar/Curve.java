package shenkar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Curve extends Shape {
	//public void printCurve(int x1,int y1,int x2,int y2,int x3,int y3,int x4,int y4){
		
	public Curve(Point2D p1, Point2D p2,Point2D p3, Point2D p4){	
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
		S_P1 = p1;
		S_P2 = p2;
		S_P3 = p3;
		S_P4 = p4;
	}
	public Curve(double x1,double y1,double x2 , double y2,
			double x3,double y3,double x4 , double y4){
		
		this(new Point2D.Double(x1,y1),new Point2D.Double(x2,y2),
				new Point2D.Double(x3,y3),new Point2D.Double(x4,y4));
	}
	
	public void setShape(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;			
	}
 
	public String WhoAmI() {
		// TODO Auto-generated method stub
		return "Curve";
	}
	
	public void drawShape(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		double t,xt,yt;
	

		for (t=0.0; t<1.0; t+=0.0005) //Drawing the curve between the 4 points. 
		{
			xt = Math.pow (1-t, 3) * p1.getX() + 3 * t * Math.pow (1-t, 2) * p3.getX() + 3 * Math.pow (t, 2) * (1-t) * p3.getX() + Math.pow (t, 3) * p4.getX(); 
			yt = Math.pow (1-t, 3) * p1.getY() + 3 * t * Math.pow (1-t, 2) * p2.getY() + 3 * Math.pow (t, 2) * (1-t) * p3.getY() + Math.pow (t, 3) * p4.getY();

			g2D.draw(new Rectangle2D.Double(xt,yt, 1,1));
		}

		//Drawing only the 4 points. 
		/*g2D.draw(new Rectangle2D.Double( p1.getX(), p1.getY(), 1,1));
		g2D.draw(new Rectangle2D.Double(p2.getX(), p2.getY(), 1,1));
		g2D.draw(new Rectangle2D.Double(p3.getX(), p3.getY(), 1,1));
		g2D.draw(new Rectangle2D.Double(p4.getX(), p4.getY(), 1,1));*/

	}
	@Override
	public void setShape(Point2D p1, Point2D p2, boolean R) {
		// TODO Auto-generated method stub
		
	}


	
	public Point2D getCenterPoint() {
		 return new Point2D.Double((p1.getX() + p4.getY())/2,(p1.getX() + p4.getY())/2);
	}
}
