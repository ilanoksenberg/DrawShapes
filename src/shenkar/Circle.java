package shenkar;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;


public class Circle extends Shape {
	
	double x=0.0;
	double radius;
	
	public Circle(Point2D p1,Point2D p2)
	{
		this.p1=p1;
		this.p2=p2;
		S_P1=p1;
		S_P2=p2;
		radius = Radius(p1, p2);
	}
	public Circle(double x1,double y1,double x2 , double y2) {
		this(new Point2D.Double(x1,y1),new Point2D.Double(x2,y2));
	}
	
	public double Radius(Point2D p1,Point2D p2){
		 // Calculate the Radius by the function squrt((x2-x1)^2+(y2-y1)^2)
		return this.radius=Math.sqrt(Math.pow(Math.abs((p2.getX()-p1.getX())),2.0)+Math.pow(Math.abs((p2.getY()-p1.getY())),2.0));
	
	}
	
	public void setShape(Point2D p1,Point2D p2,boolean R){
		this.p1 = p1;
		this.p2 = p2;
		if(R){
			this.radius=Radius(p1,p2);
		}
		
	}

	public void drawShape(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		double x=0.0;
		double y=radius;
		
		double p = 3 - (2*y);
	
		while ( x < y ) 
			{
			g2D.draw(new Rectangle2D.Double( p1.getX()-x, p1.getY()-y,1,1));//upper left left
			g2D.draw(new Rectangle2D.Double(p1.getX()-y,  p1.getY()-x,1,1));//upper upper left
			g2D.draw(new Rectangle2D.Double(p1.getX()+y, p1.getY()-x,1,1));//upper upper right
			g2D.draw(new Rectangle2D.Double( p1.getX()+x, p1.getY()-y,1,1));//upper right right
			g2D.draw(new Rectangle2D.Double(p1.getX()-x, p1.getY()+y,1,1));//lower left left
			g2D.draw(new Rectangle2D.Double(p1.getX()-y,p1.getY()+x,1,1));//lower lower left
			g2D.draw(new Rectangle2D.Double(p1.getX()+y, p1.getY()+x,1,1));//lower lower right
			g2D.draw(new Rectangle2D.Double(p1.getX()+x,p1.getY()+y,1,1));//lower right right
			
					
		if (x != y)
			if (p < 0) p += 4*x++ + 6; else p += 4*(x++ - y--) + 10;  	
	}
		
	}



	@Override
	public String WhoAmI() {
		// TODO Auto-generated method stub
		return "Circle";
	}
	 
	
	@Override
	public void setShape(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
		// TODO Auto-generated method stub
		
	}
	
	public Point2D getCenterPoint() {
		// TODO Auto-generated method stub
		return p2;
	}
	
}

