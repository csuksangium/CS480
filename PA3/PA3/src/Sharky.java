
//Chanan Suksangium CS480 
//Programming Assignment 3
//csuksan@bu.edu 11/15/16

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Sharky
{  
  private GLUT glut = new GLUT();
  private int shark_obj, body;
  private int tail1, tail2, topFin, bottomFin;
  public float xx, yy, zz, lastx, lasty, lastz;
  
  private double potentialE = java.lang.Math.E;
  
  private int fin1, fin2, boundSphere;
  private float tailAngle;
  private float tailDirection; //sign for angle
  private float scale;
  private float angle;
  private float speed;
  
  private float dir_x, dir_y, dir_z;
  
  public float sphereRadius;
  private Vivarium v;
  private Fishy prey = null;
  

  public Sharky(float _x, float _y, float _z, float _scale, Vivarium viv)
  {
	  shark_obj = 0;
	  dir_x=1;
	  dir_y=0;
	  dir_z=0;
	  
	  v = viv;

	  xx = _x;
	  yy = _y;
	  zz = _z;
	  
	  scale = _scale;
	  
	  sphereRadius = 0.67f * scale;
	  
	  tailAngle = 0;
	  tailDirection = 1;
	  angle = 0;
	  speed = 0.008f;
  }

  public void init( GL2 gl )
  {
	  create_body( gl );
	  create_fin_1( gl );
	  create_fin_2( gl );
	  create_tail_1( gl );
	  create_tail_2( gl );
	  create_top_fin( gl );
	  create_bottom_fin( gl );
	  //createBoundingSphere(gl);

	  shark_obj = gl.glGenLists(1);

	  gl.glNewList( shark_obj, GL2.GL_COMPILE );
	  construct_disp_list( gl ); 
	  gl.glEndList();
	    
  }
  
  private void construct_disp_list( GL2 gl)
  { 
	  gl.glPushMatrix();
	  
	  gl.glTranslatef(xx, yy, zz);
	  gl.glScalef(scale, scale, scale);
	  
	  // Find a rotation matrix that points object in direction of movement
	  float dx = lastx;
	  float dy = lasty;
	  float dz = lastz;

	  float mag = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	  float[] v = new float[3];
			
	  v[0] = dx / mag;
	  v[1] = dy / mag;
	  v[2] = dz / mag;

	  // up vector
	  float[] up = { 0.0f, 1.0f, 0.0f };
			
	  float[] temp = {v[1] * up[2] - up[1] * v[2], v[2] * up[0] - v[0] * up[2], v[0] * up[1] - v[1] * up[0]};
					
	  // normalize

	  float[] rotationMatrix = { v[0], v[1], v[2], 0.0f, up[0], up[1], up[2], 0.0f, temp[0], temp[1], temp[2], 0.0f, xx, yy, zz, 1.0f };
			
	  gl.glMultMatrixf(rotationMatrix, 0);	  
	  
	  
	  gl.glColor3f(0f, 0.75f, 1f); //blue
	  
	  gl.glRotated(angle, 1, 1, 0);
	  gl.glRotated(tailAngle + 90, 0, 1, 0);
	  
	  //body
	  gl.glPushMatrix();
	  gl.glCallList( body );
	  gl.glPopMatrix();
	  //Fin
	  gl.glPushMatrix();
	  gl.glCallList(topFin);
	  gl.glPopMatrix();
	    
	  gl.glPushMatrix();
	  gl.glCallList(bottomFin);
	  gl.glPopMatrix();
	  
	  //fin1
	  gl.glPushMatrix();
	  gl.glCallList(fin1);
	  gl.glPopMatrix();
	    
	  //fin2
	  gl.glPushMatrix();
	  gl.glCallList(fin2);
	  gl.glPopMatrix();
	  
	  //tail1
	  gl.glPushMatrix();
	  gl.glRotated( tailAngle, 0, 1, 0 );
	  gl.glCallList(tail1);
	  gl.glPopMatrix();
	    
	  //tail2
	  gl.glPushMatrix();
	  gl.glRotated(tailAngle, 0.0, 1.0, 0.0 );
	  gl.glCallList(tail2);    
	  gl.glPopMatrix();
	  
	  //Bounding Sphere
	  gl.glPushMatrix();
	  gl.glCallList(boundSphere);
	  gl.glPopMatrix();
	  
	  gl.glPopMatrix();
  }

  public void update( GL2 gl )
  {
	  gl.glNewList(shark_obj, GL2.GL_COMPILE );
      flap();
      swim();
      construct_disp_list( gl ); 
      gl.glEndList();
    
    }
  
  //tail movement
  private void flap(){
	  
	  tailAngle += 0.6 * tailDirection;
	  if(tailAngle > 15 || tailAngle < -15){
		  tailDirection = -tailDirection;
	  }
  }
  
  //movement function
  private void swim(){
	  
	  //normalize potential function
	  
	  float[] tempp = new float[3];
	  
	  //Potential Function for Gradient Sum
	  float preyx = (float) (0.005 * potentiaX(prey.x, prey.y, prey.z));
	  float preyy = (float) (0.005 * potentiaY(prey.x, prey.y, prey.z));
	  float preyz = (float) (0.005 * potentiaZ(prey.x, prey.y, prey.z));
	  float pMag = (float) Math.sqrt(preyx * preyx + preyy * preyy + preyz * preyz);
	  //Normalize
	  tempp[0] = preyx / pMag;
	  tempp[1] = preyy / pMag;
	  tempp[2] = preyz / pMag;
	  
	  float min = -2.0f;
	  float max = 2.0f;
	  //Random Movement
	  Random rand = new Random();
	  float rand_x = min + rand.nextFloat() * (max - min);
	  float rand_y = min + rand.nextFloat() * (max - min);
	  float rand_z = min + rand.nextFloat() * (max - min);
	  //Coordinates for rotation matrix
	  lastx = dir_x;
	  lasty = dir_y;
	  lastz = dir_z;
	  
	  //If fish alive, chase fish, otherwise random movement
	  if(prey.eaten == false){
		  dir_x += 0.03 * tempp[0];
		  dir_y += 0.03 * tempp[1];
		  dir_z += 0.03 * tempp[2];
	  }
	  
	  else{
		  dir_x += 0.005 * rand_x;
		  dir_y += 0.005 * rand_y;
		  dir_z += 0.005 * rand_z;
	  }

	//Boundary Check and movement
	  if (xx > (1f) || xx < -1f){
		  dir_x = -dir_x;

	  }
	  xx += speed * dir_x;

	  
	  if (zz > (1f) || zz < (-1f)){
		  dir_z = -dir_z;

	  }
	  zz += speed * dir_z;
	  
	  if (yy > (0.9f) || yy < (-0.9f)){
		  dir_y = -dir_y;

	  }
	  yy += speed * dir_y;
  }
    	
  
  public void draw( GL2 gl )
  {
	
	  gl.glPushMatrix();
	  gl.glCallList( shark_obj );
	  gl.glPopMatrix();
	   
  }
  //creation of body
  private void create_body(GL2 gl){
	  
	  body = gl.glGenLists(1);
	  gl.glNewList( body, GL2.GL_COMPILE );
		    
	  gl.glPushMatrix();
		    
      gl.glTranslated(0, 0, 0.2 * 1.3f);
      gl.glScaled(.6f, 1f, 2.6f);
      glut.glutSolidSphere(0.2, 36, 18);
		    
      gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Fin 1
  private void create_fin_1(GL2 gl){
	  fin1 = gl.glGenLists(1);
	  gl.glNewList(fin1, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    
	  gl.glTranslated(0.1, 0, 0.3);
	  gl.glScaled(.6f, .6f, .2f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Fin 2
  private void create_fin_2(GL2 gl){
	  fin2 = gl.glGenLists(1);
	  gl.glNewList(fin2, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    
	  gl.glTranslated(-0.1, 0, 0.3);
	  gl.glScaled(.6f, .6f, .2f);
	  glut.glutSolidSphere(0.2, 36, 18);

	  gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Tail 1
  private void create_tail_1(GL2 gl){
	  tail1 = gl.glGenLists(1);
	  gl.glNewList( tail1, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	  gl.glRotatef(-35, 1, 0, 0);
	  gl.glTranslated(0, 0.35f, -0.2 * 1f);
	  gl.glScaled(.2f, 1f, .4f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
  }
  //Tail 2
  private void create_tail_2(GL2 gl){
	   tail2 = gl.glGenLists(1);
	   gl.glNewList( tail2, GL2.GL_COMPILE );
	   gl.glPushMatrix();
	    
	   gl.glRotatef(40, 1, 0, 0);	
	   gl.glTranslated(0, -0.35f, -0.2 * 1f);
	   gl.glScaled(.2f, 1f, .4f);
	   glut.glutSolidSphere(0.2, 36, 18);
	    
	   gl.glPopMatrix();
	   gl.glEndList();
	    
  }
  //Top fin
  private void create_top_fin(GL2 gl){
	   topFin = gl.glGenLists(1);
	   gl.glNewList(topFin, GL2.GL_COMPILE );
	    
	   gl.glPushMatrix();
	   gl.glRotatef(90, 0, 1, 0);
	   gl.glRotatef(-90, 1, 0, 0);
	   gl.glTranslated(-0.25, 0, 0);
	   gl.glScaled(.6f, 0.1f, 1f);
	   glut.glutSolidCone(0.2, 0.5, 36, 18);
	    
	   gl.glPopMatrix();
	   gl.glEndList();
  }
  //Bottom fin
  private void create_bottom_fin(GL2 gl){
	   bottomFin = gl.glGenLists(1); 
	   gl.glNewList(bottomFin, GL2.GL_COMPILE );
	    
	   gl.glPushMatrix();
	   gl.glRotatef(90, 0, 1, 0);
	   gl.glRotatef(90, 1, 0, 0);
	   gl.glTranslated(-0.2, 0, 0);
	   gl.glScaled(.6f, 0.1f, 1f);
	   glut.glutSolidCone(0.2, 0.5, 36, 18);
	    
	   gl.glPopMatrix();
	   gl.glEndList();
  }
  //X potential function
  private double potentiaX(float px, float py, float pz){
	  float preyX = px;
	  
	  float preyY = py;
	  
	  float preyZ = pz;
	  
	  double potentX = -(xx - preyX);
	  double potentY = (yy - preyY);
	  double potentZ = (zz - preyZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialX = -2 * (xx - preyX) * potent;
	  return potentialX;
  }
  //Y potential function
  private double potentiaY(float px, float py, float pz){
	  float preyX = px;
	  
	  float preyY = py;
	  
	  float preyZ = pz;
	  
	  double potentX = -(xx - preyX);
	  double potentY = (yy - preyY);
	  double potentZ = (zz - preyZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialY = -2 * (yy - preyY) * potent;
	  return potentialY;
  }
  //Z potential function
  private double potentiaZ(float px, float py, float pz){
	  float preyX = px;
	  
	  float preyY = py;
	  
	  float preyZ = pz;
	  
	  double potentX = -(xx - preyX);
	  double potentY = (yy - preyY);
	  double potentZ = (zz - preyZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialZ = -2 * (zz - preyZ) * potent;
	  return potentialZ;
  }
  
  //Add prey to shark to get location
  public void addPrey(Fishy f) {
		prey = f;
	}
  //Bounding sphere	
  private void createBoundingSphere(GL2 gl) {
		boundSphere = gl.glGenLists(1);
		gl.glNewList(boundSphere, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glTranslated(0, 0, 0.1f);
		glut.glutWireSphere(0.67, 36, 24);
		gl.glPopMatrix();
		gl.glEndList();
	}
	

	
}
