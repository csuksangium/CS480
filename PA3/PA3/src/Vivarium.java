
//Chanan Suksangium CS480 
//Programming Assignment 3
//csuksan@bu.edu 11/15/16

import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium
{
  private Tank tank;
  public Sharky shark;
  public Fishy fishy;
  public Food food;
  public boolean drawFood, gone;
 
  public Vivarium()
  {
    tank = new Tank( 4.2f, 4.2f, 4.2f );
    fishy = new Fishy(-0.3f, 0.5f, 0.3f, 1f, this);
    shark = new Sharky(0f, -0.9f, -0.7f,  0.8f, this);
	food = new Food(this);
	drawFood = false;
	gone = false;
    //1 1 0.5 -0.3 -0.5 -0.7
	//-0.3 0.5 0.3 0 -0.9 -0.7
  }

  public void VivariumObjects () {
	  
  }
  
  public void init( GL2 gl )
  {
	food.init(gl);
    tank.init( gl );
    fishy.init( gl );
    shark.init(gl);
    fishy.addHunter(shark);
    shark.addPrey(fishy);
    fishy.addFood(food);
  }

  public void update( GL2 gl )
  {
	if(drawFood){
		if(gone == false){
			food.update(gl);
	}
	}
    tank.update( gl );
    shark.update(gl);
    if(fishy.eaten == false){
    	fishy.update( gl );
    }

  }

  public void draw( GL2 gl )
  {
	if(drawFood){
		if(gone == false){
			food.draw(gl);
		}
	  }
    tank.draw( gl );
    if(fishy.eaten == false){
    	fishy.draw( gl );
    }
    shark.draw(gl);
  }
 
}
