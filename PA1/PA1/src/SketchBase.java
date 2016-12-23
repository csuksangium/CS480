//****************************************************************************
// SketchBase.  

//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)
//  Some functions written by Chanan Suksangium
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	// draw a line segment
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
	{
		
	//Line DDA was given in lab
		
		int xStart = p1.x, xEnd = p2.x, yStart = p1.y, yEnd = p2.y;
		int dx = xEnd - xStart, dy = yEnd - yStart;
		int steps;
		float rStart = p1.c.r, rEnd = p2.c.r;
		float gStart = p1.c.g, gEnd = p2.c.g;
		float bStart = p1.c.b, bEnd = p2.c.b;
		ColorType col = new ColorType(rStart, gStart, bStart);
		float deltaR, deltaG, deltaB;
		float deltaX, deltaY;
		float x = xStart;
		float y = yStart;
		if(Math.abs(dx) > Math.abs(dy)){
			steps = Math.abs(dx);
		}
		else{
			steps = Math.abs(dy);
		}
		deltaX = (float)dx / steps;
		deltaY = (float)dy / steps;
		deltaR = (rEnd - rStart) / steps;
		deltaG = (gEnd - gStart) / steps;
		deltaB = (bEnd - bStart) / steps;
		drawPoint(buff, new Point2D(Math.round(x),Math.round(y),p1.c));
		
		for(int k = 0; k < steps; k++){
			x+= deltaX;
			y+= deltaY;
			col.r += deltaR;
			col.g += deltaG;
			col.b += deltaB;
			drawPoint(buff, new Point2D(Math.round(x), Math.round(y), col));
		}
	}
	
	// draw a triangle
	//Algorithms from http://www.sunshine2k.de/coding/java/TriangleRasterization/TriangleRasterization.html
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		ColorType c = p1.c;

		Point2D v1, v2, v3;
		
		//Defining order of vertices
		
		
		Point2D[] tempLst = sortVertices(p1, p2, p3);
		
		v1 = tempLst[0];
		v2 = tempLst[1];
		v3 = tempLst[2];

		//Flat Bottom Triangle
		if(v2.y == v3.y){
	
			fillBottom(buff, v1, v2, v3, c, do_smooth);

	}
		//Flat Top Triangle
		else if(v1.y == v2.y){
			
			fillTop(buff, v1, v2, v3, c, do_smooth);
			
			}
		
		//General case for triangles where no x and y are equal for p1, p2, p3
		
		else{
						
			int temp1 = v2.y;
			int temp2 = (int)((v1.x) + ((float)(v2.y-v1.y)/(float)(v3.y - v1.y)) * (v3.x - v1.x));
			Point2D v4 = new Point2D(temp2, temp1, c);
			Point2D temp = v4;
			ColorType p8 = Line(v1, v3, v4);
			if(do_smooth){
				v4.c.r = p8.r;
				v4.c.g = p8.g;
				v4.c.b = p8.b;
				fillBottom(buff, v1, v2, v4, c, do_smooth);
				
				
				
				fillTop(buff, v2, v4, v3, p8, do_smooth);
			}
			else{
			fillBottom(buff, v1, v2, v4, c, do_smooth);
			
			fillTop(buff, v2, v4, v3, c, do_smooth);
		
		}
	}
	}
	
	public static void fillBottom(BufferedImage buff, Point2D vrt1, Point2D vrt2, Point2D vrt3, ColorType c, boolean do_smooth){
		
		float iSlope1 = (float)(vrt2.x - vrt1.x) / (vrt2.y - vrt1.y);
		float iSlope2 = (float)(vrt3.x - vrt1.x) / (vrt3.y - vrt1.y);
		float flatx1 = vrt1.x;
		float flatx2 = vrt1.x;
		
		if(do_smooth){
			
			ColorType c1 = new ColorType(vrt1.c.r, vrt1.c.g, vrt1.c.b);
			ColorType c2 = new ColorType(vrt2.c.r, vrt2.c.g, vrt2.c.b);
			ColorType c3 = new ColorType(vrt3.c.r, vrt3.c.g, vrt3.c.b);
			
			for(int sY = vrt1.y; sY <= vrt2.y; sY++){
				
				float incR = sY - vrt1.y;
				float incZ = sY - vrt1.y;
		
				float cSteps = vrt2.y - vrt1.y;
				float dSteps = vrt3.y - vrt1.y;
				
				float deltaR = (c2.r - c1.r) / cSteps;
				float deltaG = (c2.g - c1.g) / cSteps;
				float deltaB = (c2.b - c1.b) / cSteps;
				
				float deltaRe = (c3.r - c1.r) / dSteps;
				float deltaGr = (c3.g - c1.g) / dSteps;
				float deltaBl = (c3.b - c1.b) / dSteps;
				

				float begR = deltaR * incR + c1.r;
				float begG = deltaG * incR + c1.g;
				float begB = deltaB * incR + c1.b;
				
				float endR = deltaRe * incZ + c1.r;
				float endG = deltaGr * incZ + c1.g;
				float endB = deltaBl * incZ + c1.b;
				
				ColorType begC = new ColorType(begR, begG, begB);
				ColorType endC = new ColorType(endR, endG, endB);
				
				drawLine(buff, new Point2D((Math.round(flatx1)), sY, begC), new Point2D(Math.round(flatx2), sY, endC));	
				c = endC;
				flatx1 += iSlope1;
				flatx2 += iSlope2;
			}
			
		}
		else{
		
		for(int scanY = vrt1.y; scanY <= vrt2.y; scanY++)
		{
			
			drawLine(buff, new Point2D((int)(flatx1), scanY, c), new Point2D((int)(flatx2), scanY, c));
			
			flatx1 += iSlope1;
			flatx2 += iSlope2;
		}
		}	
	}
	
	public static void fillTop(BufferedImage buff, Point2D vt1, Point2D vt2, Point2D vt3, ColorType c, boolean do_smooth){
		
		float tSlope1 = (float)(vt3.x - vt1.x) / (vt3.y - vt1.y);
		float tSlope2 = (float)(vt3.x - vt2.x) / (vt3.y - vt2.y);
		float tlatx1 = vt3.x;
		float tlatx2 = vt3.x;
		
		if(do_smooth){
			
			ColorType c1 = new ColorType(vt1.c.r, vt1.c.g, vt1.c.b);
			ColorType c2 = new ColorType(vt2.c.r, vt2.c.g, vt2.c.b);
			ColorType c3 = new ColorType(vt3.c.r, vt3.c.g, vt3.c.b);
			
			for(int scaY = vt3.y; scaY > vt1.y; scaY--){
				
				float incR = vt3.y - scaY;
				
				float cSteps = vt2.y - vt3.y;
				float dSteps = vt1.y - vt3.y;
				
				float deltaR = (c3.r - c2.r) / cSteps;
				float deltaG = (c3.g - c2.g) / cSteps;
				float deltaB = (c3.b - c2.b) / cSteps;
				
				float deltaRe = (c3.r - c1.r) / dSteps;
				float deltaGr = (c3.g - c1.g) / dSteps;
				float deltaBl = (c3.b - c1.b) / dSteps;
				
				float begR = deltaR * incR + c3.r;
				float begG = deltaG * incR + c3.g;
				float begB = deltaB * incR + c3.b;
				
				float endR = deltaRe * incR + c3.r;
				float endG = deltaGr * incR + c3.g;
				float endB = deltaBl * incR + c3.b;
				
				ColorType begC = new ColorType(begR, begG, begB);
				ColorType endC = new ColorType(endR, endG, endB);
				
				drawLine(buff, new Point2D((Math.round(tlatx1)), scaY, endC), new Point2D((int) tlatx2,scaY, begC));	
				
				tlatx1 -= tSlope1;
				tlatx2 -= tSlope2;
			}
		}
		else{
					
		for(int scaY = vt3.y; scaY > vt1.y; scaY--)
		{
			drawLine(buff, new Point2D((int)(tlatx1), scaY, c), new Point2D((int)(tlatx2), scaY, c));
			
			tlatx1 -= tSlope1;
			tlatx2 -= tSlope2;
			
			
		}
		}
	}
	
	public static Point2D[] sortVertices(Point2D v1, Point2D v2, Point2D v3) {
		Point2D vert1, vert2, vert3;
		//Sorting vertices
		if((v3.y > v2.y) && (v3.y > v1.y)) {
			if(v2.y > v1.y) {
				vert1 = v1;
				vert2 = v2;
				vert3 = v3;
				}
			else {
				vert1 = v2;
				vert2 = v1;
				vert3 = v3;
				}
		}
		else if((v2.y > v1.y) && (v2.y > v3.y)) {
			if(v3.y > v1.y) {
				vert1 = v1;
				vert2 = v3;
				vert3 = v2;
			}
			else {
				vert2 = v1;
				vert1 = v3;
				vert3 = v2;
				}
		}
		else {
			if(v3.y > v2.y) {
				vert1 = v2;
				vert2 = v3;
				vert3 = v1;
				}
			else {
				vert1 = v3;
				vert2 = v2;
				vert3 = v1;
				}
		}
		
		Point2D[] vertLst = {vert1, vert2, vert3};

		return vertLst;
	}
	public static ColorType Line(Point2D p1, Point2D p2, Point2D p4)
	{
		
	//Line DDA was given in lab
		
		int xStart = p1.x, xEnd = p4.x, yStart = p1.y, yEnd = p4.y;
		int dx = xEnd - xStart, dy = yEnd - yStart; 
		int steps;
		float rStart = p1.c.r, rEnd = p2.c.r;
		float gStart = p1.c.g, gEnd = p2.c.g;
		float bStart = p1.c.b, bEnd = p2.c.b;
		ColorType col = new ColorType(rStart, gStart, bStart);
		float deltaR, deltaG, deltaB;
		if(Math.abs(dx) > Math.abs(dy)){
			steps = Math.abs(dx);
		}
		else{
			steps = Math.abs(dy);
		}
		deltaR = (rEnd - rStart) / steps;
		deltaG = (gEnd - gStart) / steps;
		deltaB = (bEnd - bStart) / steps;
		
		for(int k = 0; k < steps; k++){

			col.r += deltaR;
			col.g += deltaG;
			col.b += deltaB;
		}
		return col;
	}
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawPoint(buff, p3);
	}
}
