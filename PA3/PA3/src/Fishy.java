
//Chanan Suksangium CS480 
//Programming Assignment 3
//csuksan@bu.edu 11/15/16

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class Fishy
{	
  private GLUT glut = new GLUT();
  private int fish_obj, body;
  private int tail1, tail2;
  public float x, y, z, lastX, lastY, lastZ;
  
  private int fin1, fin2, boundSphere;
  private float tailAngle;
  private float tailDirection; //sign for angle
  private float scale;
  private float angle;
  private float speed;
  private float dir_x, dir_y, dir_z;
  private float predX, predY, predZ;
  public float sum_x, sum_y, sum_z;
  //E
  private double potentialE = java.lang.Math.E;
  
  private Sharky predator = null;
  private Food foody = null;
  
  public float sharkX, sharkY, sharkZ;
  public float xx, yy, zz;
  
  public static double diameter;
  public boolean eaten;
  
  private Vivarium v;

  public Fishy(float _x, float _y, float _z, float _scale, Vivarium viv)
  {
	  fish_obj = 0;
	  dir_x=-1;
	  dir_y=-1;
	  dir_z=-1;
	  predX = dir_x;
	  predY = dir_y;
	  predZ = dir_z;
	  
	  v = viv;
	
	  eaten = false;
	  
	  xx = _x;
	  yy = _y;
	  zz = _z;
	  
	  x = lastX = _x;
	  y = lastY = _y;
	  z = lastZ = _z;
	  scale = _scale;
	    
	  tailAngle = 0;
	  tailDirection = 1;
	  angle = 0;
	  speed = 0.01f;
  }

  public void init( GL2 gl )
  {
	  create_body( gl );
	  create_fin_1( gl );
	  create_fin_2( gl );
	  create_tail_1( gl );
	  create_tail_2( gl );
	  //createBoundingSphere(gl);

	  fish_obj = gl.glGenLists(1);

	  gl.glNewList( fish_obj, GL2.GL_COMPILE );
	  construct_disp_list( gl ); 
	  gl.glEndList();
  }
  
  private void construct_disp_list( GL2 gl)
  { 
	 gl.glPushMatrix();

	 gl.glTranslatef(x, y, z);
	 gl.glScalef(scale, scale, scale);
		
	 // Find a rotation matrix that points object in direction of movement
	 float dx = lastX;
	 float dy = lastY;
	 float dz = lastZ;

	 float mag = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
	 float[] v = new float[3];
		
	 v[0] = dx / mag;
	 v[1] = dy / mag;
	 v[2] = dz / mag;

	 // up vector
	 float[] up = { 0.0f, -1.0f, 0.0f };
		
	 float[] temp = {v[1] * up[2] - up[1] * v[2], v[2] * up[0] - v[0] * up[2], v[0] * up[1] - v[1] * up[0]};
				
	 // normalize

	 float[] rotationMatrix = { v[0], v[1], v[2], 0.0f, up[0],
				up[1], up[2], 0.0f, temp[0], temp[1], temp[2], 0.0f, x, y, z,
				1.0f };
		
	  gl.glMultMatrixf(rotationMatrix, 0);

		
	  gl.glColor3f(0f, 1f, 0.5f); //grey
	  gl.glRotated(angle, 1, 1, 0);
	  gl.glRotated(tailAngle + 90, 0, 1, 0);
	  
	  //body
	  gl.glPushMatrix();
	  gl.glCallList( body );
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
	    
	  //tail2
	  gl.glPushMatrix();
	  gl.glRotated(tailAngle, 0.0, 1.0, 0.0 );
	  gl.glCallList(tail2);
		       
	  gl.glPopMatrix();
	  gl.glPopMatrix();
	  
	  //Bounding Sphere
	  gl.glPushMatrix();
	  gl.glCallList(boundSphere);
	  gl.glPopMatrix();
	  
	  gl.glPopMatrix();
	  
	 
  }

  public void update( GL2 gl )
  {
	
	gl.glNewList(fish_obj, GL2.GL_COMPILE );
	collision(x, y, z, predator.xx, predator.yy, predator.zz);
	if(v.drawFood){
		eaten(x, y, z, foody.fx, foody.fy, foody.fz);
	}
    flap();
    swim();
    construct_disp_list( gl ); 
    gl.glEndList();
    
    }
  //Tail Movement
  private void flap(){
	  tailAngle += 0.6 * tailDirection;
	  if(tailAngle > 20 || tailAngle < -20){
		  tailDirection = -tailDirection;
	  }
  }
  //Movement Function
  private void swim(){
	  
	  //Predator negative potential function
	  predX = (float) (0.05 * -potentiaX(predator.xx, predator.yy, predator.zz));
	  predY = (float) (0.05 * -potentiaY(predator.xx, predator.yy, predator.zz));
	  predZ = (float) (0.05 * -potentiaZ(predator.xx, predator.yy, predator.zz));
	  float foodX = (float) (0.05 * potentiaX(foody.fx, foody.fy, foody.fz));
	  float foodY = (float) (0.05 * potentiaY(foody.fx, foody.fy, foody.fz));
	  float foodZ = (float) (0.05 * potentiaZ(foody.fx, foody.fy, foody.fz));
	  
	  //Normalize food vector
	  float[] potenNorm = new float[3];
	  float fMag = (float) Math.sqrt(foodX * foodX + foodY * foodZ + foodZ * foodZ);
	  potenNorm[0] = foodX / fMag;
	  potenNorm[1] = foodY / fMag;
	  potenNorm[2] = foodZ / fMag;
	  
	  Random rand = new Random();
	  float[] po = new float[3];
	  po[0] = predX;
	  po[1] = predY;
	  po[2] = predZ;
	  //Random movement
	  float min = -2.0f;
	  float max = 2.0f;
		
	  float rand_x = min + rand.nextFloat() * (max - min);
	  float rand_y = min + rand.nextFloat() * (max - min);
	  float rand_z = min + rand.nextFloat() * (max - min);
	  
	  float[] ra = new float[3];
	  ra[0] = (float) (0.3 * potentiaX(rand_x, rand_y, rand_z));
	  ra[1] = (float) (0.3 * potentiaY(rand_x, rand_y, rand_z));
	  ra[2] = (float) (0.3 * potentiaZ(rand_x, rand_y, rand_z));
	  
	  //Gradient sum if food exist
	  if(v.drawFood){
		  sum_x = (float) (0.01 * (0.5 * rand_x + po[0] + potenNorm[0]));
		  sum_y = (float) (0.01 * (0.5 * rand_x + po[1] + potenNorm[1]));
		  sum_z = (float) (0.01 * (0.5 * rand_x + po[2] + potenNorm[2]));
	  }
	  //Gradient sum without food
	  else{
		  sum_x = (float) (0.01 * (0.5 * rand_x + po[0]));
		  sum_y = (float) (0.01 * (0.5 * rand_x + po[1]));
		  sum_z = (float) (0.01 * (0.5 * rand_x + po[2]));		  
	  }
	  //Direction Update
	  dir_x += sum_x;
	  dir_y += sum_y;
	  dir_z += sum_z;
	  
	  lastX = dir_x;
	  lastY = dir_y;
	  lastZ = dir_z;
	  
	  //Boundary Check and movement
	  if (z > (0.9) || z < (-0.9)){
		  dir_z = -dir_z;
	  }
	  z += speed * dir_z;
	  
	  if (x > (0.9) || x < (-0.9)){
		  dir_x = -dir_x;
	  }
	  x += speed * dir_x;
	  
	  if (y > (0.9) || y < (-0.9)){
		  dir_y = -dir_y;
	  }
	  y += speed * dir_y;
  }
    	
  //Draw creature
  public void draw( GL2 gl )
  {
	  gl.glPushMatrix();
	  gl.glCallList( fish_obj );
	  gl.glPopMatrix();
	   
  }
  //Body Creation
  private void create_body(GL2 gl){
	  
	  body = gl.glGenLists(1);
	  gl.glNewList( body, GL2.GL_COMPILE );
	  gl.glPushMatrix();
		    
	  gl.glTranslated(0, 0, 0.2 * 1.3f);
	  gl.glScaled(.3f, .5f, 1.3f);
	  glut.glutSolidSphere(0.2, 36, 18);
		    
	  gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Fin 1 Creation
  private void create_fin_1(GL2 gl){
	  
	  fin1 = gl.glGenLists(1);
	  gl.glNewList(fin1, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    
	  gl.glTranslated(0.05, 0, 0.3);
	  gl.glScaled(.3f, .3f, .1f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Fin 2 Creation
  private void create_fin_2(GL2 gl){
	  
	  fin2 = gl.glGenLists(1);
	  gl.glNewList(fin2, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    
	  gl.glTranslated(-0.05, 0, 0.3);
	  gl.glScaled(.3f, .3f, .1f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
	  
  }
  //Tail 1 Creation
  private void create_tail_1(GL2 gl){
	  tail1 = gl.glGenLists(1);
	  gl.glNewList( tail1, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    
	  gl.glTranslated(0, 0, -0.1 * 0.3f);
	  gl.glScaled(.1f, .5f, .2f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
	    
  }
  //Tail 2 Creation
  private void create_tail_2(GL2 gl){
	  
	  tail2 = gl.glGenLists(1);
	  gl.glNewList( tail2, GL2.GL_COMPILE );
	  gl.glPushMatrix();
	    	
	  gl.glTranslated(0, 0, -0.1 * 0.8f);
	  gl.glScaled(.1f, .5f, .2f);
	  glut.glutSolidSphere(0.2, 36, 18);
	    
	  gl.glPopMatrix();
	  gl.glEndList();
	    
  }
  //Potential function for X
  private double potentiaX(float px, float py, float pz){
	  
	  sharkX = px;
	  sharkY = py;
	  sharkZ = pz;
	  
	  double potentX = -(x - sharkX);
	  double potentY = (y - sharkY);
	  double potentZ = (z - sharkZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialX = -2 * (x - sharkX) * potent;
	  return potentialX;
  }
  //Potential function for Y
  private double potentiaY(float px, float py, float pz){
	  
	  sharkX = px;
	  sharkY = py;
	  sharkZ = pz;
	  
	  double potentX = -(x - sharkX);
	  double potentY = (y - sharkY);
	  double potentZ = (z - sharkZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialY = -2 * (y - sharkY) * potent;
	  return potentialY;
  }
  //Potential function for Z
  private double potentiaZ(float px, float py, float pz){
	  
	  sharkX = px;
	  sharkY = py;
	  sharkZ = pz;
	  
	  double potentX = -(x - sharkX);
	  double potentY = (y - sharkY);
	  double potentZ = (z - sharkZ);
	  double potent = -Math.pow(potentX, 2) + Math.pow(potentY, 2) + Math.pow(potentZ, 2);
	  potent = Math.pow(potentialE, potent);
	  double potentialZ = -2 * (z - sharkZ) * potent;
	  return potentialZ;
  }
  //Add Shark to know location
  public void addHunter(Sharky s) {
	  predator = s;
	}
  //Collision Function
  public boolean collision(float px, float py, float pz, float prx, float pry, float prz) {
		
	  float distance = (float) Math.sqrt(Math.pow(px - prx, 2) + Math.pow(py - pry, 2) + Math.pow(pz - prz, 2));
	  if (distance < predator.sphereRadius) {
		  
		return eaten = true;
		
	  }

		return eaten = false; 
	}
  //Food Eating Function
  private void eaten(float px, float py, float pz, float prx, float pry, float prz) {
		
	  float distance = (float) Math.sqrt(Math.pow(px - prx, 2) + Math.pow(py - pry, 2) + Math.pow(pz - prz, 2));
	  if (distance < 0.32) {
		  v.gone = true;
		  v.drawFood = false;
		  }
	  else{ 
		  v.gone = false; 
		  v.drawFood = true;
		}
  }
	
	  public void addFood(Food p) {
		  foody = p;
		}
   //Bounding Sphere Creation
   private void createBoundingSphere(GL2 gl) {
	  boundSphere = gl.glGenLists(1);
	  gl.glNewList(boundSphere, GL2.GL_COMPILE);
		
	  gl.glPushMatrix();
	  gl.glTranslated(0, 0, 0.2f);
	  glut.glutWireSphere(0.32, 36, 24);
	  gl.glPopMatrix();
		
	  gl.glEndList();
	}
	
}
