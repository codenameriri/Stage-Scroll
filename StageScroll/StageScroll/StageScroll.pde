// required for @Mia's text animaitons
import de.looksgood.ani.*;
// set to whatever the pixel height of @Brennan's widgets
final int WIDGET_HEIGHT = 40;
// define our stage
ScrollingStage myStage;

void setup() {
  size(1024, 768, P3D);
  frameRate(30);
  smooth();
  // required for @Mia's text animations
  Ani.init(this);

  // instantiate our stage. x, y, width, height. drawing done from
  // center origin of images.
  myStage = new ScrollingStage(width/2, height/2-WIDGET_HEIGHT, width, height);
}



void draw() {
  // reset frame
	background(0);
	smooth();
  noStroke();

  // each draw loop, you must call the update() then the draw() for the stage.
  myStage.update();
  myStage.draw();
}


/*
  in this example we're using mouse clicks to spawn/increment the note, but in
  the project we'll be calling this from inside @Ben's timekeeper once every
  beat. simply call myStage.spawnNote( columnNum ) to spawn a note in
  columnNum (1-5).
*/
void mouseReleased(){
  // this prevents spawning notes before the stage is ready.
  if( myStage.doneLoading ){
    // activeNote = 0 means there is no currently active note, so it's safe to spawn a new one.
    if( myStage.activeNote == 0 ){
      myStage.spawnNote( round(random(1,5)) );
    }else{
      myStage.incrementNote(1);
    }
  }
}

/*
  here we're using key presses to cycle through the hitzones. In the app, we'll
  be linking this to brainwave data. set with 1-5.
 */
void keyReleased(){
  if( myStage.doneLoading ){
    if( myStage.activeHitzone >= 5 ){
      myStage.setActiveHitzone(1);
    }else{
      myStage.setActiveHitzone( myStage.activeHitzone+1 );
    }
  }
}