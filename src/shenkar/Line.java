package shenkar;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Line extends Shape {

		public Line(Point2D p1, Point2D p2){	
			this.p1 = p1;
			this.p2 = p2;
			S_P1 = p1;
			S_P2 = p2;
		}
		public Line(double x1,double y1,double x2 , double y2){
			this(new Point2D.Double(x1,y1),new Point2D.Double(x2,y2));
		}
		
		public void setShape(Point2D p1,Point2D p2,boolean R){
			if (S_P1==null && S_P2==null){
				S_P1 = p1;
				S_P2 = p2;
			}
			this.p1 = p1;
			this.p2 = p2;
		}
		
		public void drawShape(Graphics g){
			Graphics2D g2D = (Graphics2D) g;
			double dx=p2.getX() - p1.getX(),dy=p2.getY() - p1.getY(),steps,k;
			double xincrement,yincrement,x=p1.getX(),y=p1.getY();
			
			if(Math.abs(dx)>Math.abs(dy)) steps=Math.abs(dx); else steps=Math.abs(dy); //According to the gradient we take the absolut delta.

			xincrement=dx/steps; //We divide the delta X with the number of steps to get the number of pixels we need to draw.
			yincrement=dy/steps; //We divide the delta Y with the number of steps to get the number of pixels we need to draw.
	
			g2D.draw(new Rectangle2D.Double( Math.round(x),Math.round(y),1,1)); //We draw the first pixel of the line
	
			for(k=0;k<steps;k++)//Loop to draw all of the line pixels.
			{
				x+=xincrement;
				y+=yincrement;
				g2D.draw(new Rectangle2D.Double(Math.round(x),Math.round(y),1,1));       
			}
		
		}
		@Override
		public String WhoAmI() {
			// TODO Auto-generated method stub
			return "Line";
		}



		@Override
		public void setShape(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
			// TODO Auto-generated method stub
			
		}

		
		public Point2D getCenterPoint() {
			
			return new Point2D.Double((p1.getX() + p2.getX())/2,(p1.getY() + p2.getY())/2);
		}
}
