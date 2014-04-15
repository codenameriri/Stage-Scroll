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
  // myStage = new ScrollingStage(width/2,height*.6,200,550);
  myStage = new ScrollingStage(width/2,height/2,200,600);


  // spawn the starfield
  for( int i=0; i<starCount; i++ ){
  	starCollection[i] = new Star();
  }
}

public void draw() {
	pushMatrix();

	// clear out the background
	background(0);
	noStroke();


	// update the stars
	for(int q=0; q<starCount; q++){
		starCollection[q].update();
		starCollection[q].display();
	}

  	rectMode(CENTER);
  	//rotateX(radians(50));		
	myStage.updateStage();
	popMatrix();
}
class Hitzone
{
  // props   
   int colNum;
   int x;
   int y;
   int w;
   int h;
   // hitzone props
   int hitzoneColor;     // where we store the RGB of this hitzone
   PShape hitzoneShape;    // graphic object

   // stage props
   float stagePerspective; // degrees the stage is tilted so we can match it

  // Constructor 
  Hitzone( int column, float persp, float stageWidth )
  {   
    colNum = column;
    this.w = 30;
    this.h = 30;
    this.x = stageWidth / colNum + this.w;
    this.y = height-120;    
    this.stagePerspective = persp;
    this.stageWidth = stageWidth;

    // set the hitzone color dependant on colNum
    switch(colNum){
      case 0: hitzoneColor = color(255, 34, 184); break; // pink
      case 1: hitzoneColor = color(240, 133, 46); break; // orange 
      case 2: hitzoneColor = color(255, 220, 73); break; // yellow
      case 3: hitzoneColor = color(16, 255, 160); break; // green
      case 4: hitzoneColor = color(13, 255, 234); break; // blue
    }

    println("Column#: " + colNum + "| X: " + this.x + " | Y: " + this.y);

    // create the shape and set its fill
    hitzoneShape = createShape(RECT, this.x, this.y, this.w, this.h);
    hitzoneShape.setFill(hitzoneColor);
  }
   
   
  public void updateHitzone()
  {
    this.display();

    int mx = mouseX;
    int my = mouseY;
    int bufferSpace = 10;

    if( (mx >= this.x-this.w/2+bufferSpace) && 
        (mx <= this.x+this.w/2+bufferSpace) && 
        (my >= this.y-this.h/2+bufferSpace) && 
        (my <= this.y+this.h/2+bufferSpace) ){
      println("within bounds of colNum:" + colNum);
      // change fill color
      hitzoneShape.setFill(0xffFFFFFF);
    }else{
      //restore hitzone color
      hitzoneShape.setFill(hitzoneColor);
    }
  }

  // checks for collisions between 2 shapes 
  public boolean checkCollisions(PShape s1, PShape s2){
    // TODO
    return true;
  }

  public void display(){
    pushMatrix();
    translate(0,0,1);
    rectMode(CORNER);
    shape( hitzoneShape );
    popMatrix();
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

    // Spawn the hitzone objects, pass in column, store in hitzone collection.
    for(int i=0; i<numHitzones; i++){
      hitzoneCollection[i] = new Hitzone(i, stagePerspective, stageWidth);
    }
  }

  public void drawStage(){
    pushMatrix();
    fill(100);
    rect( locX, locY, stageWidth, stageHeight );
    popMatrix();
  }


  public void updateStage(){
    // redraw the stage background
    drawStage();
    // draw the hit zones for the notes
    for( Hitzone i : hitzoneCollection ){
      i.updateHitzone();
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
