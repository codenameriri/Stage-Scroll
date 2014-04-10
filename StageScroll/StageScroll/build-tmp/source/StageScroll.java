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

public void setup() {
  println("setup()");
  // setup basic sketch settings
  size(1024, 768, P3D);
  // start the stage
  myStage = new ScrollingStage(width/2,height*.6f,200,550);
}

public void draw() {
	// clear out the background
	background(0);
	// update the stage
	myStage.updateStage();
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

  // store RGB color vals
  // int[] rArr = [4];
  // int[] gArr = [4];
  // int[] bArr = [4];

  // Constructor
  ScrollingStage(float x, float y, float w, float h) {
    // set init positioning
    locX        = x;
    locY        = y;
    stageWidth  = w;
    stageHeight = h;

    // set color values


   // setup the stage bg rect
   this.drawStage();
  }

  public void drawStage(){
    rectMode(CENTER);
    rotateX(radians(stagePerspective));
    fill(255,255,255, 50);
    rect( locX, locY, stageWidth, stageHeight );
  }

  public void drawHitzones(){
    rectMode(CENTER);
    //rotateX(radians(stagePerspective));
    for( int i=0; i<4; i++ ){
      fill(255,34,184, 50);
      rect( 0, 0, 100, 100);
    }
  }

  public void updateStage(){
    // TODO: draw the animated background
    // drawBackground();
    // redraw the stage background
    drawStage();
    // draw the hit zones for the notes
    drawHitzones();
    // TODO: redraw the notes
    // drawNotes();

  }

  // void drawHitZone(int column){
  //   // draw rect
  //   rect(column*100-300, 320, 80, 80);
  // }

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
