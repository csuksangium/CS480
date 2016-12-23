//Chanan Suksangium CS480 
//Programming Assignment 3
//csuksan@bu.edu 11/15/16

import java.util.Random;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.*;

public class Food {
	
	private GLUT glut;
	private int food_obj;
	public float fx, fy, fz;
	public boolean add, gone;
	private float speed;
	private Vivarium v;
	
	public Food(Vivarium viv) {
		
		Random rand = new Random();
		float min = -2.0f;
		float max = 2.0f;
			
		fx = min + rand.nextFloat() * (max - min);
		fz = min + rand.nextFloat() * (max - min);
		fy = 2;
		
		glut = new GLUT();
		v = viv;
		speed = 0.01f; 
		
	}
	
	public void init(GL2 gl) {
		
		food_obj = gl.glGenLists(1);
		gl.glNewList(food_obj, GL2.GL_COMPILE);
		gl.glTranslatef(fx, 0, fz);
		glut.glutSolidSphere(0.1, 36, 24);
		gl.glEndList();
		
	}
	

	public void update(GL2 gl) {
        
		if (fy > -1.99) {
			fy -= speed;
		}

	}
	
	public void draw(GL2 gl) {
		gl.glPushMatrix();
	    gl.glPushAttrib( GL2.GL_CURRENT_BIT );
	    gl.glTranslatef(0, fy, 0);
	    gl.glColor3f( 0.94f, 0.94f, 1f); //white
	    gl.glCallList( food_obj );
	    gl.glPopAttrib();
	    gl.glPopMatrix();
		
	}
	
}
