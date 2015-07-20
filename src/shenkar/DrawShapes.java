package shenkar;



import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.JColorChooser;
import javax.swing.BorderFactory;
import javax.swing.border.*;


@SuppressWarnings("serial")
public class DrawShapes extends JFrame implements ItemListener {
  
  MyCanvas 				myCanvas;
  JMenuItem				unduMenu;
  JButton				Circle,Curve,Line;
  JSlider  				sl_ScaleX,sl_RotateX,sl_RotateY,sl_RotateTheta,sl_ShearX,sl_ShearY;
  ColorSelectListener	colorListener;		
  ShapeSelectListener	shapeListener;

  double 		translate_X=0.0,translate_Y=0.0,rotate_X=0.0,rotate_Y=0.0,rotate_Theta=0.0,shear_X=0.0,shear_Y=0.0;
  double 		scale_X = 1.0,scale_Y=1.0;
  int			undo = 0;
  String 		action="None";
  File 			openFile = null;
  Color			colorBrush=Color.black;
  int			typeOfShape=0;
  float 		width = 0.5f;//initialize width

  public DrawShapes() {
    super();  
    
	JMenuBar menuBar = new JMenuBar();
	menuBar.setBorder(new MatteBorder(0, 0, 1, 0, Color.black));//border of the Menu
	final JMenu fileMenu =  new JMenu("File");
	fileMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenu editMenu = new JMenu("Edit");
	editMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenu helpMenu = new JMenu("Help");
	helpMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenuItem openMenu = new JMenuItem("Open File");
	openMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenuItem exitMenu = new JMenuItem("Exit");
	exitMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenuItem clearMenu = new JMenuItem("Clear");
	clearMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	unduMenu= new JMenuItem("Undo");
	unduMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	JMenuItem guideMenu = new JMenuItem("Guide");
	guideMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));
	unduMenu.setEnabled(false);
	
	/*-----------Action listeners of the Menu---------------*/
	openMenu.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	          JFileChooser fileChooser = new JFileChooser();
	          int returnValue = fileChooser.showOpenDialog(null);
	          if (returnValue == JFileChooser.APPROVE_OPTION) {
	          openFile = fileChooser.getSelectedFile();
	          myCanvas.readFromFile();
	          
	          }}});
	exitMenu.addActionListener(new ActionListener()
    {
       public void actionPerformed(ActionEvent arg0) {
         //  exit program
         System.exit(0);
         }
       }
    );
	clearMenu.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent ae) {
	    	  myCanvas.shapesVec.removeAllElements();
	    	  myCanvas.pointsVec.removeAllElements();
	    	  colorBrush=Color.black;
	    	  typeOfShape=0;
	    	  action="None";
	    	  initSliders();
	    	  myCanvas.repaint();
	          }});
	unduMenu.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		
			unduMenu.setEnabled(false);
			switch (action){
			case "RotateTheta":
				sl_RotateTheta.setValue(undo);
				rotate_Theta = undo * Math.PI / 180;
				break;
			case "RotateX":
				sl_RotateX.setValue(undo);
				rotate_X= undo;
				break;
			case "RotateY":
				sl_RotateY.setValue(undo);
				rotate_Y= undo;				
				break;
			case "ScaleX":
				sl_ScaleX.setValue(undo);	
				scale_X= undo/100.0; 			
				break;
			case "ShearX":
				sl_ShearX.setValue(undo);
				shear_X= undo/100.0;				
				break;
			case "ShearY":
				sl_ShearY.setValue(undo);
				shear_Y= undo/100.0;				
				break;
		
			}
			myCanvas.repaint();
		}});
	guideMenu.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	        Guidedialog ad = new Guidedialog();
	        ad.setVisible(true);
	      }
	    });
	
	fileMenu.add(openMenu);
	fileMenu.add(exitMenu);
	editMenu.add(clearMenu);
	editMenu.add(unduMenu);
	helpMenu.add(guideMenu);
	menuBar.add(fileMenu);
	menuBar.add(editMenu);
	menuBar.add(helpMenu);
	setJMenuBar(menuBar);
	
    

    
	/*-------------The side tool bar---------------*/   
    JPanel sideToolBar = new JPanel();
    sideToolBar.setSize(new Dimension(85,400));
    sideToolBar.setPreferredSize(new Dimension(85,400));
    sideToolBar.setBorder(new MatteBorder(0, 0, 1, 2, Color.black));//border of the side tool bar
    add(sideToolBar, BorderLayout.WEST);
    
    
    /*-----------sub tool bar of shapes-----------*/
    JLabel Lshape = new JLabel("Select Shape:",JLabel.LEFT);
    Lshape.setForeground(Color.blue);
    sideToolBar.add(Lshape);
    JToolBar subShapeBar = new JToolBar();
    add(subShapeBar,BorderLayout.LINE_START);
    subShapeBar.setLayout(new GridLayout(3,1,3,3));
    Line =  new JButton(new ImageIcon("line.gif"));
    Line.setCursor(new Cursor(Cursor.HAND_CURSOR));
    Circle= new JButton(new ImageIcon("circle.gif"));
    Circle.setCursor(new Cursor(Cursor.HAND_CURSOR));
    Curve =  new JButton(new ImageIcon("curve.gif"));
    Curve.setCursor(new Cursor(Cursor.HAND_CURSOR));
    Circle.setPreferredSize(new Dimension(40,40));
    Curve.setPreferredSize(new Dimension(40,40));
    Line.setPreferredSize(new Dimension(40,40));
    shapeListener = new ShapeSelectListener();
    Line.addActionListener(shapeListener);
    Circle.addActionListener(shapeListener);
    Curve.addActionListener(shapeListener);
    subShapeBar.add(Line);
    subShapeBar.add(Circle);
    subShapeBar.add(Curve);
    
    sideToolBar.add(subShapeBar); 
    add(sideToolBar);
    validate();
    
    /*------------Color Picker--------------*/
    JLabel Lcolor = new JLabel("Select color:",JLabel.LEFT);
    Lcolor.setForeground(Color.blue);
    final JButton colorButton= new JButton(new ImageIcon("colors.gif"));
    colorButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    colorButton.setForeground(Color.blue);
    colorListener = new ColorSelectListener();
    colorButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {	
			colorBrush = JColorChooser.showDialog(null, "Select Color", colorBrush);
		    colorButton.addActionListener(colorListener);
		    myCanvas.repaint();
		      
		}
    });
    
	/*----------------Width control-------------*/
    JLabel Lwidth = new JLabel("Select Width:",JLabel.LEFT);
    Lwidth.setForeground(Color.blue);
    Lwidth.setLayout(new GridLayout(3,2));
    final JTextField sizeofWidth=new JTextField("     0.5"); //default width 0.5
    sizeofWidth.setPreferredSize(new Dimension(50,20));
    sizeofWidth.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
        	String str = e.getActionCommand();
        	width=Float.parseFloat(str);
        	if(width>10||width<0.5)//Warning message
        		JOptionPane.showMessageDialog(sizeofWidth, "Width is between 0.5-10", "Warning", JOptionPane.ERROR_MESSAGE);
                	else
                		myCanvas.repaint();
    }});
    /*-----borders line of the buttons in the side tool bar---------*/
    colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK , 1));
    Line.setBorder(BorderFactory.createLineBorder(Color.BLACK , 1));
    Circle.setBorder(BorderFactory.createLineBorder(Color.BLACK , 1));
    Curve.setBorder(BorderFactory.createLineBorder(Color.BLACK , 1));
    colorButton.setBorder(BorderFactory.createLineBorder(Color.BLACK , 1));
    
    
    sideToolBar.add(Lcolor);
    sideToolBar.add(colorButton);
    sideToolBar.add(Lwidth);
    sideToolBar.add(sizeofWidth);

    add(sideToolBar, BorderLayout.LINE_START);
 
    
    /*------------The main tool bar-------------------*/
    JToolBar controlPanel = new JToolBar();
	controlPanel.setLayout(new GridLayout(1,1,0,0));
    add(controlPanel, BorderLayout.NORTH);
    controlPanel.setPreferredSize(new Dimension(600, 170));
    JPanel subPanel = new JPanel();
    subPanel.setPreferredSize(new Dimension(100, 100));
    subPanel.setSize(100, 100);
    subPanel.setBorder(new MatteBorder(0, 1, 0, 1, Color.black));
    JPanel subPanel2 = new JPanel();
    subPanel2.setPreferredSize(new Dimension(100, 100));
    subPanel2.setSize(100, 100);
    subPanel2.setBorder(new MatteBorder(0, 1, 0, 1, Color.black));
    JPanel subPanel3 = new JPanel();
    subPanel3.setPreferredSize(new Dimension(100, 100));
    subPanel3.setSize(100, 100);
    subPanel3.setBorder(new MatteBorder(0, 1, 0, 1, Color.black));
    JPanel subPanel4 = new JPanel();
    subPanel4.setPreferredSize(new Dimension(100, 100));
    subPanel4.setSize(100, 100);
    subPanel4.setBorder(new MatteBorder(0, 1, 0, 1, Color.black));
    controlPanel.setBorder(new MatteBorder(0, 0, 2, 0, Color.black));
  
    /*----------------- Rotate control------------------*/
    JLabel Rotate=new JLabel("Rotate: Theta     Axis-X       Axis-Y",JLabel.LEFT);
    subPanel.add(Rotate);	
    Rotate.setForeground(Color.blue);
	sl_RotateTheta = setSlider( JSlider.VERTICAL, 0, 360, 0, 90, 45);
	sl_RotateX = setSlider( JSlider.VERTICAL,-300, 300 ,0,100, 25);
    sl_RotateY = setSlider( JSlider.VERTICAL,-300, 300 ,0, 100, 25);
    sl_RotateTheta.setPreferredSize(new Dimension(55,130));
    sl_RotateX.setPreferredSize(new Dimension(55,130));
    sl_RotateY.setPreferredSize(new Dimension(55,130));
    sl_RotateTheta.setName("RotateTheta");
    sl_RotateTheta.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sl_RotateTheta.setForeground(Color.red);
    sl_RotateX.setName("RotateX");
    sl_RotateX.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sl_RotateX.setForeground(Color.red);
    sl_RotateY.setName("RotateY");
    sl_RotateY.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sl_RotateY.setForeground(Color.red);
    subPanel.add(sl_RotateTheta);
    subPanel.add(sl_RotateX);
    subPanel.add(sl_RotateY);
    controlPanel.add(subPanel);
    controlPanel.add(subPanel2);
    controlPanel.add(subPanel3);
    controlPanel.add(subPanel4);
    /*---------------------------Scaling control-------------------------*/

    JLabel Scale=new JLabel("                           Scale X:                            ",JLabel.LEFT);
    subPanel2.add(Scale);	
    Scale.setForeground(Color.blue);
    sl_ScaleX = setSlider( JSlider.VERTICAL, 0, 400, 100, 100,25);
    sl_ScaleX.setPreferredSize(new Dimension(55,130));
    sl_ScaleX.setForeground(Color.red);
    sl_ScaleX.setName("ScaleX");
    sl_ScaleX.setCursor(new Cursor(Cursor.HAND_CURSOR));
    subPanel2.add(sl_ScaleX);
    
    
   /*--------------------------Shear Control---------------------------*/
    JLabel Shear=new JLabel("Shear:    ShearX    ShearY                ",JLabel.LEFT);
    subPanel3.add(Shear);	
    Shear.setForeground(Color.blue);  
    sl_ShearX = setSlider( JSlider.VERTICAL, -300, 300,0,100, 25);
    sl_ShearY = setSlider( JSlider.VERTICAL, -300, 300,0,100, 25);
    sl_ShearX.setPreferredSize(new Dimension(55,130));
    sl_ShearY.setPreferredSize(new Dimension(55,130));
    sl_ShearX.setName("ShearX");
    sl_ShearX.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sl_ShearX.setForeground(Color.red);
    sl_ShearY.setName("ShearY");
    sl_ShearY.setCursor(new Cursor(Cursor.HAND_CURSOR));
    sl_ShearY.setForeground(Color.red);
    subPanel3.add(sl_ShearX);
    subPanel3.add(sl_ShearY);
    
    
    /*-----------------------Mirror Control----------------------------*/
    JLabel Mirror=new JLabel("               MirrorX                  ",JLabel.LEFT);
    subPanel4.add(Mirror);	
    Mirror.setForeground(Color.blue);
    JButton mirrorX = new JButton("Mirror via Axis X");
    mirrorX.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
    mirrorX.setCursor(new Cursor(Cursor.HAND_CURSOR));
    JLabel Mirror2=new JLabel("              MirrorY                ",JLabel.LEFT);
    Mirror2.setForeground(Color.blue);
    JButton mirrorY = new JButton("Mirror via Axis Y");
    mirrorY.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
    mirrorY.setCursor(new Cursor(Cursor.HAND_CURSOR));
    mirrorX.setForeground(Color.red);
    mirrorY.setForeground(Color.red);
    mirrorX.setPreferredSize(new Dimension(100,40));
    mirrorY.setPreferredSize(new Dimension(100,40));
    subPanel4.add(mirrorX);
    subPanel4.add(Mirror2);
    subPanel4.add(mirrorY);
    
    mirrorX.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {	
			action = e.getActionCommand();
			myCanvas.repaint();
		}
    });
    mirrorY.addActionListener(new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent e) {	
			action = e.getActionCommand();
			myCanvas.repaint();
		}
    });


    
    myCanvas = new MyCanvas();
    myCanvas.readFromFile();
    myCanvas.setBackground(Color.GRAY);
    setTitle("Ilan Oksenbrg");
    add(myCanvas, "Center");
  
    
  }
  
 public void initSliders(){
	 	sl_RotateTheta.setValue(0);
	 	sl_RotateX.setValue(0);			
		sl_RotateY.setValue(0);						
		sl_ScaleX.setValue(100);											
		sl_ShearX.setValue(0);
		sl_ShearY.setValue(0);
		}

  public JSlider setSlider( int orientation, int minimumValue,
		  int maximumValue, int initValue, int majorTickSpacing,int minorTickSpacing) {
	
	  JSlider slider = new JSlider(orientation, minimumValue, maximumValue,initValue);	 
	  slider.setPaintTicks(true);
	  slider.setMajorTickSpacing(majorTickSpacing);
	  slider.setMinorTickSpacing(minorTickSpacing);
	  slider.setPaintLabels(true);
	  slider.addChangeListener(new ChangeListener() {
	  public void stateChanged(ChangeEvent e) {
		
	        JSlider tempSlider = (JSlider) e.getSource();
	    
	        if (tempSlider.equals(sl_ScaleX)) {
	            if (sl_ScaleX.getValue() != 0.0) {
	            	scale_X = sl_ScaleX.getValue()/100.0 ;
	              	myCanvas.repaint();}
	        } 
	          else if (tempSlider.equals(sl_RotateTheta)) {
	              	rotate_Theta = sl_RotateTheta.getValue() * Math.PI / 180;	    
	              	myCanvas.repaint();
	        } else if (tempSlider.equals(sl_RotateX)) {
	        
	              	rotate_X = sl_RotateX.getValue();
	              	myCanvas.repaint();
	        } else if (tempSlider.equals(sl_RotateY)) {
	  
	              	rotate_Y = sl_RotateY.getValue();
	              	myCanvas.repaint();            
	        } else if (tempSlider.equals(sl_ShearX)) {
	            if (sl_ShearX.getValue() != 0.0) {	            	
	            	shear_X = sl_ShearX.getValue()/100.0 ;
	            	myCanvas.repaint();}
		    } else if (tempSlider.equals(sl_ShearY)) {
		        if (sl_ShearY.getValue() != 0.0) {		      
		        	shear_Y = sl_ShearY.getValue()/100.0;
		        	myCanvas.repaint();}}}});
	  slider.addMouseListener(new MouseInputListener(){
		@Override
		public void mousePressed(MouseEvent e) {
			 JSlider tempSlider = (JSlider) e.getSource();
			 unduMenu.setEnabled(true);
			 action = tempSlider.getName();
			  
			  switch (action){
				case "RotateTheta":
					undo = sl_RotateTheta.getValue();
					break;
				case "RotateX":
					undo =sl_RotateX.getValue();
					break;
				case "RotateY":
					undo =sl_RotateY.getValue();
					break;
				case "ScaleX":
					undo =sl_ScaleX.getValue();
					break;
				case "ShearX":
					undo =sl_ShearX.getValue();
					break;
				case "ShearY":
					undo =sl_ShearY.getValue();
					break;
				}
			System.out.println(undo);  
		}
		@Override
		public void mouseClicked(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mouseDragged(MouseEvent e) {}
		@Override
		public void mouseMoved(MouseEvent e){}
		});
	 
	  return slider;
	  }
  /*-------------explanation about this application----------------*/
  class Guidedialog extends JDialog {
	  public Guidedialog() {
	    setTitle("Guide");
	    setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

	    add(Box.createRigidArea(new Dimension(0, 10)));
	   
	    JLabel Instructions = new JLabel("<html><p style='Font-weight:bold;font-size:15px'> Instructions:<br><br></p>"+"<p style='text-decoration:underline'>Adding Shapes:</p>"+""
	    		+ "<ul><li>You can click one of the 3 types of images in the left tool bar, then click on the grey area 2 times for circle and line and 4 for curve."+""
	    		+ "<li>You can choose color by clicking the image colors and you can choose the width of the line by entering number between 0.5-10.</ul>"+""
	    		+ "<br><p style='text-decoration:underline'>Transformation:<br><br> </p>"+""
	    		+ "<ul><li>You can control each transformation by using the sliders or the buttons from the main tool bar on different shapes that you drew or uploaded.</ul>"+""
	    		+ "<p style='text-decoration:underline'>Uploading file:<br><br></p>"+""
	    		+ "<ul><li>Go to the Menu and click on File, then choose the open file button for searching your file.txt, After you open the file the application will draws an image accordingly to your coordinates.<li>Now you can use the tranformation sliders and buttons on this image.</ul>"+""
	    		+ "</HTML>");
	    add(Instructions);
	    add(Box.createRigidArea(new Dimension(0,85)));
		setSize(500, 500);
		setLocationRelativeTo(null);
		setResizable(false);
	  }
	}
  private class ColorSelectListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton tempButton = (JButton)e.getSource();
		colorBrush = tempButton.getBackground();
		}
	}
    
  private class ShapeSelectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JButton clickedButton = (JButton)e.getSource();
		
			if (clickedButton == Circle){typeOfShape=1;}else
			if (clickedButton == Line){typeOfShape=2;}else
			if (clickedButton == Curve){typeOfShape=3;}}
		}
/*
 * Canvas Class 
 * */  
   class MyCanvas extends Canvas implements MouseInputListener  {
	  Vector<Shape>		shapesVec;
	  Vector<Point2D>	pointsVec;
	  
	  private Point2D	centerPoint = null;
	  int     			typeclicked = 0;
	  Rectangle2D		lightPixel=null;
		  
	 public MyCanvas(){
		 addMouseListener(this);
		 shapesVec = new Vector<Shape>();
		 pointsVec  = new Vector<Point2D>();
		 centerPoint = new Point2D.Double();
		
	 }
	
	/*--------------- the canvas painting-----------------*/ 
    public void paint(Graphics g) {
      Graphics2D g2D = (Graphics2D) g;
     
      BasicStroke stroke = new BasicStroke(width);
      g2D.setStroke(stroke);
  
     
      /*------------ Performing transformations on each shape from the Vector---------*/ 
      for(Shape shape:shapesVec){

    	
    	if (action.equals("RotateTheta")||action.equals("RotateX")
    			||action.equals("RotateY")||action.equals("ScaleX")
    			||action.equals("ShearX")||action.equals("ShearY"))
    	{
    		if (shape.WhoAmI().equalsIgnoreCase("Curve")){	
    			shape.setShape(rotation(shape.S_P1), rotation(shape.S_P2), 
    					rotation(shape.S_P3), rotation(shape.S_P4));
    			shape.setShape(shearing(shape.p1), shearing(shape.p2), 
       					shearing(shape.p3), shearing(shape.p4));
    			shape.setShape(scaling(shape.p1), scaling(shape.p2), 
       					scaling(shape.p3), scaling(shape.p4));	
    		}
    		else
    		{	
    			shape.setShape(rotation(shape.S_P1), rotation(shape.S_P2),false);
    			shape.setShape(shearing(shape.p1), shearing(shape.p2),false);
    			if (shape.WhoAmI().equalsIgnoreCase("Circle"))
    			{
    			
    				if((action.equals("ScaleX")))
    					
    				   shape.setShape(scaling(shape.p1), scaling(shape.p2),true);
    				else
    				   shape.setShape(scaling(shape.p1), scaling(shape.p2),false);
    				
    			}
 			   else
 			    {
 				   	   shape.setShape(scaling(shape.p1), scaling(shape.p2),false);
 			    }
    		}
    	}	    	  
       else if (action.equals("Mirror via Axis Y")){
    	  
    	   if (shape.WhoAmI().equalsIgnoreCase("Curve"))	
      			shape.setShape(mirrorAxisY(shape.p1), mirrorAxisY(shape.p2), 
      					mirrorAxisY(shape.p3), mirrorAxisY(shape.p4));
      		   else
      			shape.setShape(mirrorAxisY(shape.p1), mirrorAxisY(shape.p2),false);
    	   }
       else if (action.equals("Mirror via Axis X")){
    	   if (shape.WhoAmI().equalsIgnoreCase("Curve"))	{
     			shape.setShape(mirrorAxisX(shape.p1), mirrorAxisX(shape.p2), 
     					mirrorAxisX(shape.p3), mirrorAxisX(shape.p4));
     			//System.out.print("mirrorAxisY_C");
    	   }
     		   else{
     			shape.setShape(mirrorAxisX(shape.p1), mirrorAxisX(shape.p2),false);
     			//System.out.print("mirrorAxisY_NC");
     		   }
    	   }
    		
    		g2D.setColor(shape.c);	
    		
    		shape.drawShape(g); }
     /*--------------------For lighting a single pixel---------------------*/
      if(lightPixel!=null){
    	  g2D.setColor(Color.BLACK);
    	  g2D.draw(lightPixel); 
      }}
    
    /*------------------Transformation functions-------------*/
    
    public Point2D translate(Point2D oldP){
    	//System.out.println("translate");
    	double matrix[][] = {{1,0,0},
    						{0,1,0},
    						{translate_X,translate_Y,1}};
    	
    	return multMatrix(oldP,matrix);
    }
    
    public Point2D multMatrix(Point2D oldP,double matrix[][]){
    	/*Multiply Matrix by Vector=(x,y,1)*/
    	//System.out.println("multMatrix");
    	double newPx = oldP.getX() * matrix[0][0] + oldP.getY() * matrix[1][0] + 1 * matrix[2][0];
    	double newPy = oldP.getX() * matrix[0][1] + oldP.getY() * matrix[1][1] + 1 * matrix[2][1];
    	
    	return new Point2D.Double(newPx, newPy);
    }
      
    public Point2D rotation(Point2D p){//fist translate, then rotate and translate again. 
    	//System.out.println("rotation");
    	translate_X = (centerPoint.getX())*(-1)+rotate_X; 
    	translate_Y = (centerPoint.getY())*(-1)+rotate_Y;
    	Point2D tr =  translate(p);
    	/*----rotation matrix---*/
	    double matrix[][] = {{Math.cos(rotate_Theta),Math.sin(rotate_Theta),0},
	    					{-Math.sin(rotate_Theta),Math.cos(rotate_Theta),0},
	    					{0,0,1}};
	    Point2D ro = multMatrix(tr,matrix);
    	translate_X = (centerPoint.getX())+rotate_X;
    	translate_Y = (centerPoint.getY())+rotate_Y;
    	return translate(ro);
    }
    
    public Point2D scaling(Point2D p){
    	//System.out.println("scaling");
    	double cX = centerPoint.getX();
    	double cY = centerPoint.getY();
    	/*----scaling matrix---*/
    	double mat[][]  = {{scale_X,0,0},{0,scale_X,0},{cX*(1-scale_X),cY*(1-scale_X),1}};
    	return multMatrix(p,mat);
    }
  
    public Point2D shearing(Point2D p){//fist translate, then shearing x,y and translate again.
    	//System.out.println("shearing");
    	translate_X = centerPoint.getX() * (-1); 
    	translate_Y = centerPoint.getY() * (-1);
    	
    	Point2D tr =  translate(p);
    	/*----shearing x matrix---*/
    	double matrix_Sx[][]  = {{1,0,0},
    							{shear_X,1,0},
    							{0,0,1}};
    	Point2D	 shX = multMatrix(tr,matrix_Sx);
    	/*----shearing y matrix---*/
        double matrix_Sy[][]  = {{1,shear_Y,0},
				        		{0,1,0},
				        		{0,0,1}};
    	Point2D	 shY = multMatrix(shX,matrix_Sy);
    	translate_X = centerPoint.getX(); 
    	translate_Y = centerPoint.getY();
    	
    	return translate(shY);		
    }
    
    public Point2D mirrorAxisY(Point2D p){//fist translate, then mirror y and translate again.
    	//System.out.println("mirrorAxisY");
    	System.out.println(p.getX()+","+p.getY() );
    	translate_X = centerPoint.getX() * (-1); 
    	translate_Y = centerPoint.getY() * (-1);
    	System.out.println(translate_X+","+ translate_Y);
    	//Point2D p2(centerPoint.getX()+,centerPoint.getY())
    	Point2D tr =  translate(p); 
    	/*----mirror AxisY matrix---*/
    	double matrixMirror[][]  = {{-1,0,0},
					    			{0,1,0},
					    			{0,0,1}};
    	Point2D mirror = multMatrix(tr,matrixMirror);
    	
    	translate_X =  centerPoint.getX();
    	translate_Y =  centerPoint.getY(); 
    	return  translate(mirror);	
    }
    public Point2D mirrorAxisX(Point2D p){//fist translate, then mirror x and translate again.
    	//System.out.println("mirrorAxisX");
    	translate_X = centerPoint.getX() * (-1); 
    	translate_Y = centerPoint.getY() * (-1); 	
    	Point2D tr =  translate(p); 
    	/*----mirror Axisx matrix---*/
    	double matrixMirror[][]  = {{1,0,0},
					    			{0,-1,0},
					    			{0,0,1}};
    	Point2D mirror = multMatrix(tr,matrixMirror);
    	translate_X =  centerPoint.getX(); 
    	translate_Y =  centerPoint.getY(); 
    	
    	return  translate(mirror);	
    }
    
    public Point2D  undoScaling(Point2D p){
    	//System.out.println("undoScaling");
    	/*----undoScaling matrix---*/
    	double matrix[][] ={{1/scale_X,0,0},
		    			{0,1/scale_X,0},
		    			{0,0,1}};
    	return multMatrix(p,matrix);
    }
    public Point2D  undoRotation(Point2D p){//fist translate, then rotate and translate again.
    	//System.out.println("undoRotation");
    	translate_X = (centerPoint.getX())*(-1)+rotate_X; 
    	translate_Y = (centerPoint.getY())*(-1)+rotate_Y;  
    	Point2D tr =  translate(p);
    	/*----undoRotation matrix---*/
	    double matrix[][] ={{Math.cos(rotate_Theta),-Math.sin(rotate_Theta),0},
	    				 {Math.sin(rotate_Theta),Math.cos(rotate_Theta),0},
	    				 {0,0,1}};
	    Point2D ro = multMatrix(tr,matrix);
    	translate_X = (centerPoint.getX())+rotate_X;
    	translate_Y = (centerPoint.getY())+rotate_Y;
    	return translate(ro);
    }
    
   
    
    /**----------------------Read From File---------------------------------**/
    @SuppressWarnings("resource")
	public void readFromFile() {
    	FileReader 			myFile = null;
    	BufferedReader  	input = null;
    	shapesVec.removeAllElements();
    	action="";
    	try {
    		myFile = new FileReader("points.txt");	
    		input = new BufferedReader(myFile);
			
			String line = input.readLine();
			
			while(line!=null)
			{			
				
				String[] splitLine  = line.split(",");
				if (splitLine.length != 5 && splitLine.length!=9 ){throw new Exception("Error :Invalid coordinates number");}
				double coords[]=new double[splitLine.length-1];
				for (int i=0;i<coords.length;++i){
					try {
						coords[i] = Double.parseDouble(splitLine[i+1]);
					} catch (NumberFormatException  e) {		
						e.printStackTrace();
					}
					
				}
				pointsVec.removeAllElements();
				for (int i=0;i<coords.length;i+=2)
					pointsVec.add(new Point2D.Double(coords[i],coords[i+1]));
		
				switch (splitLine[0]){
				case "circle":
					if (splitLine.length != 5){throw new Exception("Error :Invalid coordinates number");}
					shapesVec.add(new Circle(coords[0],coords[1],coords[2],coords[3])); 
					break;
				case "line":
					if (splitLine.length != 5){throw new Exception("Error :Invalid coordinates number");}
					shapesVec.add(new Line(coords[0],coords[1],coords[2],coords[3]));
					break;
				case "curve":
					if (splitLine.length != 9){throw new Exception("Error :Invalid coordinates number");}
					shapesVec.add(new Curve(coords[0],coords[1],coords[2],coords[3],
							coords[4],coords[5],coords[6],coords[7]));
					break;
					
			    default:
			    	throw new Exception("Error :Invalid shape");	
				
				}
				
				line = input.readLine();
			}
			findCenterPoint(); 
			initSliders();
			repaint();
				
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		} 
		
	} 
    /*--------the Center point of the all Shapes in the Canvas.--------------*/
    public void findCenterPoint(){
		
    	double maxPointx = pointsVec.get(0).getX();
    	double maxPointy = pointsVec.get(0).getY();
    	double minPointx = pointsVec.get(0).getX();
    	double minPointy = pointsVec.get(0).getY();
    	
    	 for(Point2D point:pointsVec){
    			if (point.getX() > maxPointx)
    			{maxPointx =point.getX();}
    			if (point.getY() > maxPointy)
    			{maxPointy =point.getY();}
    			
    			if (point.getX() < minPointx)
    			{minPointx =point.getX();}
    			if (point.getY() < minPointy)
    			{minPointy =point.getY();}	
    		 }
    	
    	centerPoint =  new Point2D.Double( (maxPointx+minPointx)/2,(maxPointy+minPointy)/2 );
    }


    /*---------------Creating the Shapes by the mouse click coordinates-------------------*/
	@Override
	public void mouseClicked(MouseEvent e) {
		
		action="";
		Point2D newP = new Point2D.Double(e.getX(), e.getY());
		lightPixel = new Rectangle2D.Double(newP.getX(),newP.getY(), 1, 1);
		System.out.println(newP.getX()+","+newP.getY());
		if(typeclicked < 4 && typeOfShape!=0 )
		{
			pointsVec.add(newP);
			int size = pointsVec.size();
			typeclicked++;	
			repaint();
		switch (typeOfShape){
		
		case 1:
			
			if (typeclicked == 2){
				
				 Shape s = new Circle(pointsVec.get(size-1),pointsVec.get(size-2));
				 s.c=colorBrush;
				 shapesVec.add(s);
				 findCenterPoint();
				 typeclicked = 0;
				 lightPixel = null;
				 repaint();
				 
			} 
			break;
		case 2:
			if (typeclicked == 2){
				 
				 Shape s = new Line(pointsVec.get(size-1),pointsVec.get(size-2));
				 s.c=colorBrush;
				 shapesVec.add(s);
				 findCenterPoint();
				 typeclicked = 0;
				 lightPixel = null;
				 repaint();
				 
			}
			break;
		case 3:
			if (typeclicked == 4){
				 
				 Shape s = new Curve(pointsVec.get(size-1),pointsVec.get(size-2),
						 pointsVec.get(size-3),pointsVec.get(size-4));
		
				 s.c=colorBrush;
				 shapesVec.add(s);
				 findCenterPoint();
				 typeclicked = 0;
				 lightPixel = null;
				 repaint();
			}	
			break;
		
		}}else  typeclicked = 0;
		
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseDragged(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
}

  public static void main(String[] a) {
	  Runnable run = new Runnable() 
		{ 
			public void run()
			{
				DrawShapes drawShapes  = new DrawShapes();
				drawShapes.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				drawShapes.setSize(800,650);
				drawShapes.setLocationRelativeTo(null);
				drawShapes.setResizable(false); 										
				drawShapes.setVisible(true);	
			}
		};
		
SwingUtilities.invokeLater(run);
  }

@Override
public void itemStateChanged(ItemEvent arg0) {
	// TODO Auto-generated method stub
	
}
}

           

