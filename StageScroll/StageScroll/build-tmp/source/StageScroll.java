import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class StageScroll extends PApplet {

ScrollingStage myStage;

int starCount = 500;
Star[] starCollection;

public void setup() {
  size(1024, 768, P3D);

  starCollection = new Star[starCount];

  // start the stage
  myStage = new ScrollingStage(width/2,height*.6f,200,550);

  // spawn the starfield
  for( int i=0; i<starCount; i++ ){
  	starCollection[i] = new Star();
  }
}

public void draw() {
	// clear out the background
	background(0);
	noStroke();

	// update the stars
	for(int q=0; q<starCount; q++){
		starCollection[q].update();
		starCollection[q].display();
	}

  	// drawing settings
  	rectMode(CENTER);
  	rotateX(radians(50));		
	// update the stage
	myStage.updateStage();	
}
class Hitzone
{
  // props   
   int colNum;
   int x;
   int y;
   int w;
   int h;
   float stagePerspective; // degrees the stage is tilted so we can match it
  
   int zoneColor; // where we store the RGB of this hitzone

  // Constructor 
  Hitzone( int column, float persp )
  {   
    // store the column of this hitzone -- 0-4
    colNum = column;
    this.x = 430 + (colNum * 40);
    this.y = height - 100;
    this.w = 30;
    this.h = 30;
    this.stagePerspective = persp;

    // set the hitzone color dependant on 
    switch(colNum){
      case 0: zoneColor = color(255, 34, 184); break; // pink
      case 1: zoneColor = color(240, 133, 46); break; // orange 
      case 2: zoneColor = color(255, 220, 73); break; // yellow
      case 3: zoneColor = color(16, 255, 160); break; // green
      case 4: zoneColor = color(13, 255, 234); break; // blue
    }

    println("Column#: " + colNum + "| X: " + this.x + " | Y: " + this.y);
  }
   
   
  public void updateHitzone()
  {
    // if something is within bounds of the hitzone, change its color
    if( pointRect( mouseX, mouseY, this.x-430, this.y-100, this.w, this.h ) ){
      println("hitting colNum:" + colNum);
    }

  }

  public void drawHitzone()
  {
    fill( zoneColor );
    rect(this.x, this.y, this.h, this.w);
  }


/* 
POINT/RECT COLLISION FUNCTION
Takes 6 arguments:
  + x,y position of point 1 - in this case "you"
  + x,y position of point 2 - in this case the static rectangle
  + width and height of rectangle
*/
public boolean pointRect(int px, int py, int rx, int ry, int rw, int rh) {
  
  // test for collision
  if (px >= rx-rw/2 && px <= rx+rw/2 && py >= ry-rh/2 && py <= ry+rh/2) {
    return true;    // if a hit, return true
  }
  else {            // if not, return false
    return false;
  }
}



}
/*
 * @author      Tom Conroy
 * @version     1.0 
 * @since       1.0
 */
class ScrollingNote extends Thread {

  boolean running;
  int wait;
  String id;
  int count;

  // Constructor
  ScrollingNote(String s, int w) {
    running = false; // disable running by default
    id      = s;     // name of the instance
    wait    = w;     // how long to wait before repeating thread loop
    count   = 0;     // the number of times we've looped through the thread
        
    // setup the stage bg rect
    drawStageBG();

    // setup the smaller note hit points
    for(int i=1; i<=5; i++){
     drawHitZone(i);
    }

  }

  public void drawStageBG(){
    rectMode(CENTER);
    translate(512, 384, 0);
    rotateX(PI/3);
    fill(255, 255, 255, 100);
    rect(0, 0, 512, 750);
  }
  
  public void drawHitZone(int column){
    
    // setup stroke
    stroke(255,255,255,100);   
    strokeWeight(3);
    // setup fill
    switch(column){
      case 1:
        fill(255,51,220, 99);
        break;
      case 2:
        fill(239,130,47, 99);
        break;
      case 3:
        fill(255,218,90, 99);
        break;
      case 4:
        fill(192,255,74, 99);
        break;
      case 5:
        fill(13,255,232,99);
        break;
    }
    // draw rect
    rect(column*100-300, 320, 80, 80);
  }

  public int getCount() {
    return count;
  }

  // overriding default "start()"
  public void start() {
    running = true;
    println("ScrollingNote::"+this.id+ " has started. executing every " + wait + "ms");
    // do whatever you want start to do, usually setup stuff 


    // call this last
    super.start();
  }

  public void run() {
    while (running) {
      // do whatever you want while going through this particular loop
      count++;
      try {
        sleep((long)(wait));
      } 
      catch(Exception e) {
        // todo: handle exceptions
      }
    }
    System.out.println("ScrollingNote " + id + " Thread is done.");
  }

  // quits the thread
  public void quit() {
    System.out.println( "Quitting.." );
    running = false;
    interrupt();
  }
}

/*
 * @author      Tom Conroy
 * @version     1.0 
 * @since       1.0
 */
class ScrollingStage {

  // properties
  float locX;
  float locY;
  float stageWidth;
  float stageHeight;
  float stageBPM;
  float stagePerspective = 50; // perspective to tilt the stage at

  // properties for hitzones 
  int numHitzones = 5;
  Hitzone[] hitzoneCollection;

  // Constructor
  ScrollingStage(float x, float y, float w, float h) {
    // set init positioning
    locX        = x;
    locY        = y;
    stageWidth  = w;
    stageHeight = h;

    // setup hitzone container
    hitzoneCollection = new Hitzone[numHitzones];

   // setup the stage bg rect
   this.drawStage();

    // Spawn the hitzone objects, pass in their column, store in the hitzone collection.
    // also, draw the hitzone 
    for(int i=0; i<numHitzones; i++){
      hitzoneCollection[i] = new Hitzone(i, stagePerspective);
    }
  }

  public void drawStage(){
    fill(100);
    rect( locX, locY, stageWidth, stageHeight );
  }


  public void updateStage(){
    // redraw the stage background
    drawStage();
    // draw the hit zones for the notes
    for( Hitzone i : hitzoneCollection ){
      i.updateHitzone();
      i.drawHitzone();
    }
  }

}
class Star
{
  float x;
  float y;
  float z;
  float velocity;
  float star_size;
   
  float screen_x;
  float screen_y;
  float screen_diameter;
   
  float old_screen_x;
  float old_screen_y;
   
   
  // Constructor 
  Star()
  {   
    randomizePosition(true);
  }
   
  public void randomizePosition(boolean randomizeZ)
  {
    x = random(-width * 2, width * 2);
    y = random(-height * 2, height * 2);
     
    if(randomizeZ)
      z = random(100, 1000);
    else
      z = 1000;
       
    velocity = 3; //random(0.5, 5);
    star_size = random(2, 10);
  }
   
  public void update()
  {
     
    if(mousePressed)
      z -= velocity * 10;
    else
      z -= velocity;
     
    screen_x = x / z * 100 + width/2;
    screen_y = y / z * 100 + height/2;
    screen_diameter = star_size / z * 100;
     
    if(screen_x < 0 || screen_x > width || screen_y < 0 || screen_y > height || z < 1)
    {
      randomizePosition(false);
    }
  }
   
  public void display()
  {
    float star_color = 255 - z * 255 / 1000;
    fill(star_color);
    rotateX(radians(myStage.stagePerspective));
    ellipse(screen_x, screen_y, screen_diameter, screen_diameter);  
  	rotateX(radians(myStage.stagePerspective*-1));
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "StageScroll" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
