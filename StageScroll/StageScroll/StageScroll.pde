import de.looksgood.ani.*;    // required for @Mia's text animaitons
final int WIDGET_HEIGHT = 40; // pixel height of @Brennan's widgets
ScrollingStage myStage;       // define our stage

/**
 * Main Stage Setup
 */
void setup() {
  // stage settings
  size(1024, 768, P3D);
  frameRate(30);
  smooth();
  Ani.init(this); // required for @Mia's text animations

  /* instantiate our stage. x, y, width, height. drawing done from
    center origin of images.*/
  myStage = new ScrollingStage(width/2, height/2-WIDGET_HEIGHT, width, height);
}


/*
  Main Stage Draw
 */
void draw() {
  // reset frame
	background(0);
	smooth();
  noStroke();

  /* must call stage update and draw each cycle of Main stage */
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
  // prevents spawning notes before the stage is ready.
  if( myStage.doneLoading ){
    // activeNote = 0 means no currently active note, safe to spawn a new one.
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