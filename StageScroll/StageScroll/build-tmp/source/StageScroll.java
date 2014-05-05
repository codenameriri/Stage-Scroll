import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.looksgood.ani.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class StageScroll extends PApplet {

// required for @Mia's text animaitons

// set to whatever the pixel height of @Brennan's widgets
final int WIDGET_HEIGHT = 40;
// define our stage
ScrollingStage myStage;

public void setup() {
  size(1024, 768, P3D);
  frameRate(30);
  smooth();
  // required for @Mia's text animations
  Ani.init(this);

  // instantiate our stage. x, y, width, height. drawing done from
  // center origin of images.
  myStage = new ScrollingStage(width/2, height/2-WIDGET_HEIGHT, width, height);
}



public void draw() {
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
public void mouseReleased(){
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
public void keyReleased(){
  if( myStage.doneLoading ){
    if( myStage.activeHitzone >= 5 ){
      myStage.setActiveHitzone(1);
    }else{
      myStage.setActiveHitzone( myStage.activeHitzone+1 );
    }
  }
}
class Feedback{

  float xPos, yPos;        // Text's position
  String type;             // Text's type
  float easing;            // Easing for the Text
  float scale;             // Scaling the text
  float currentAlpha;      // Changing opacity of the Text

  PImage[] textImages = new PImage[7];    // Array to hold all the images
  String[] fileNames = {"RELAX_GURU", "FOCUS_MASTER", "THE_EEGJ", "AWESOME", "CRAZY", "GREAT", "SWEET"};

  PImage image;            // Seleted image from the textImages array
  float imageW, imageH;    // Selected image's size
  int imageIndex;          // Image Text's index

  /*------------------------------------------------------
  *  _type           -  "relax", "focus", "random"
  *  _xPox, _yPos    -  position for placing the Image Text
  --------------------------------------------------------*/
  public Feedback(String _type, float _xPos, float _yPos){
    type = _type;
    xPos = _xPos;
    yPos = _yPos;

    // Store the loaded text images in the PImage array
    for(int i =0; i < fileNames.length; i++){
      textImages[i] = loadImage("textImages/" + fileNames[i] + ".png");
    }

    // Determine the text index according to the type
    if(type.equals("relax")){
      imageIndex = 0;
    }else if(type.equals("focus")){
      imageIndex = 1;
    }else if(type.equals("random")){
      imageIndex = PApplet.parseInt(random(2,7));
    }
    image = textImages[imageIndex];
    imageW = image.width/8;
    imageH = image.height/8;

    easing = 0.10f;
    currentAlpha = 255;
  }

  public void draw(){
    smooth();
    pushMatrix();
    translate(xPos, yPos);

    // for changing the opacity of the text
    float targetAlpha = 0;
    float dA = targetAlpha - currentAlpha;

    // for changing the y position of the text
    float targetY = height/15;
    float dY = targetY - yPos;
    if( yPos < targetY ){
      yPos += dY * easing;

      // for changing the scale of the text
      if(scale < 5){
        scale += easing;
      }
      if( yPos > (targetY * 0.95f) ){
        currentAlpha += dA * 0.25f;
      }
     }
     tint(255, currentAlpha);
     scale(scale);
     Ani.to(this, 1, "yPos", yPos, Ani.SINE_IN);

     imageMode(CENTER);
     image(image, 0, yPos, imageW, imageH);
     popMatrix();
     tint(255, 255);
  }//END draw()

}
class LoaderThread extends Thread {
  // THREAD SETTINGS
  ScrollingStage parent;
  boolean active;
  boolean loadingCompleted;
  int resourceIndex;
  String statusText;

  // IMAGE CONSTANTS
  final int GRID_FRAME_CNT    = 10;
  final int HITZONE_FRAME_CNT = 5;
  final int STAGE_FRAME_CNT   = 6;
  final int COLSEQ_FRAME_CNT  = 10;
  final String EXTN            = ".png";
  final String GRID_DIR       = "grid/";
  final String VIGNETTE_DIR   = "vignette/";
  final String STAGE_DIR      = "stage/";
  final String FOCAL_DIR      = "focal/";
  final String NOTES_DIR      = "notes/";
  final String HITZONE_DIR    = "hitzone/";


  // IMAGE VARIABLES
  PImage[] stageSequence;
  PImage[] gridSequence;
  PImage[] col1Sequence;
  PImage[] col2Sequence;
  PImage[] col3Sequence;
  PImage[] col4Sequence;
  PImage[] col5Sequence;
  PImage[] hitzoneSequence;
  PImage stageBG;
  PImage focal;
  PImage focusVignette;
  PImage relaxVignette;

  // COLLECTIONS
  Star[] starCollection;

  LoaderThread( ScrollingStage p ){
    parent           = p;
    active           = false;
    loadingCompleted = false;
    resourceIndex    = 0;
    statusText       = "Initializing..";
  }

  public void start(){
    println("LoaderThread::start();");
    statusText = "Loader running..";
    active = true;
    super.start();
  }

  public void run(){
    while( true ){
      if( active ){
        println("LoaderThread::current resource index: " + resourceIndex);
        loadResources(resourceIndex);
        resourceIndex++;
      }else{
        println("LoaderThread::Loading completed!");
        statusText = "All assets loaded successfully.";
        parent.finishedLoading(true);
        break;
      }
    }
  }



  public void loadResources( int whichResource ){
    println("LoaderThread::loadResources()");
    int parentW = round(parent.w);
    int parentH = round(parent.h);
    int gridH   = round(parent.h+parent.GRID_H_PADDING);
    int seqH    = round(parent.h+parent.SEQ_H_PADDING);

    switch (whichResource) {
      // load the stage sequence
      case 0:
        statusText = "Loading stage sequence..";
        stageSequence = new PImage[STAGE_FRAME_CNT];
        for( int ss=0; ss<stageSequence.length; ss++ ){
          stageSequence[ss] = loadImage(STAGE_DIR+"stage"+ss+EXTN);
          stageSequence[ss].resize( parentW, parentH );
        }
      break;
      // load the grid sequence
      case 1:
        statusText = "Loading grid sequence..";
        gridSequence = new PImage[GRID_FRAME_CNT];
        for( int gs=0; gs<gridSequence.length; gs++ ){
          gridSequence[gs] = loadImage(GRID_DIR+"grid"+gs+EXTN);
          gridSequence[gs].resize(parentW, gridH);
        }
      break;
      // load the col1 sequence
      case 2:
        statusText = "Loading column 1 note sequences..";
        col1Sequence = new PImage[COLSEQ_FRAME_CNT];
        for( int c1s=0; c1s<col1Sequence.length; c1s++ ){
          col1Sequence[c1s] = loadImage(NOTES_DIR+"col_1/notes_"+c1s+EXTN);
          col1Sequence[c1s].resize(parentW, seqH);
        }
      break;
      // load the col2 sequence
      case 3:
        statusText = "Loading column 2 note sequences..";
        col2Sequence = new PImage[COLSEQ_FRAME_CNT];
        for( int c2s=0; c2s<col2Sequence.length; c2s++ ){
          col2Sequence[c2s] = loadImage(NOTES_DIR+"col_2/notes_"+c2s+EXTN);
          col2Sequence[c2s].resize(parentW, seqH);
        }
      break;
      // load the col3 sequence
      case 4:
        statusText = "Loading column 3 note sequences..";
        col3Sequence = new PImage[COLSEQ_FRAME_CNT];
        for( int c3s=0; c3s<col3Sequence.length; c3s++ ){
          col3Sequence[c3s] = loadImage(NOTES_DIR+"col_3/notes_"+c3s+EXTN);
          col3Sequence[c3s].resize(parentW, seqH);
        }
      break;
      // load the col4 sequence
      case 5:
        statusText = "Loading column 4 note sequences..";
        col4Sequence = new PImage[COLSEQ_FRAME_CNT];
        for( int c4s=0; c4s<col4Sequence.length; c4s++ ){
          col4Sequence[c4s] = loadImage(NOTES_DIR+"col_4/notes_"+c4s+EXTN);
          col4Sequence[c4s].resize(parentW, seqH);
        }
      break;
      // load the col5 sequence
      case 6:
        statusText = "Loading column 5 note sequences..";
        col5Sequence = new PImage[COLSEQ_FRAME_CNT];
        for( int c5s=0; c5s<col5Sequence.length; c5s++ ){
          col5Sequence[c5s] = loadImage(NOTES_DIR+"col_5/notes_"+c5s+EXTN);
          col5Sequence[c5s].resize(parentW, seqH);
        }
      break;
      // load the hitzone sequence
      case 7:
        statusText = "Loading note hitzones..";
        hitzoneSequence = new PImage[HITZONE_FRAME_CNT];
        for( int hs=0; hs<hitzoneSequence.length; hs++ ){
          hitzoneSequence[hs] = loadImage(HITZONE_DIR+"hitzone"+hs+EXTN);
          hitzoneSequence[hs].resize(parentW, seqH);
        }
      break;
      // load the stage background
      case 8:
        statusText = "Loading background elements..";
        stageBG = loadImage(STAGE_DIR+"bg"+EXTN);
        stageBG.resize(parentW, parentH);
      break;
      // load the focal point
      case 9:
        statusText = "Loading focal..";
        focal = loadImage(FOCAL_DIR+"focal_point_black"+EXTN);
        focal.resize(parentW, parentH);
      break;
      // load the focus vignette
      case 10:
        statusText = "Loading vignette (focus)..";
        focusVignette = loadImage(VIGNETTE_DIR+"focus_vignette"+EXTN);
        // TODO: focusVignette.resize();
      break;

      // load the relaxed vignette
      case 11:
        statusText = "Loading vignette (relaxed)..";
        relaxVignette = loadImage(VIGNETTE_DIR+"relax_vignette"+EXTN);
        // TODO: relaxVignette.resize();
      break;

      // generate the star objects
      case 12:
        statusText = "Generating starfield..";
        starCollection = new Star[parent.starCount];
        for( int sc=0; sc<starCollection.length; sc++ ){
          statusText = "Generating Star #: " + sc;
          starCollection[sc] = new Star();
        }
      break;

      // pass the images off to the parent
      case 13:
        statusText = "Handing off assets to stage..";
        handOff();
      break;

      // past 12? finish the thread. :)
      default:
      println("LoaderThread::loadResources() - final resource loaded");
      statusText = "Resource load complete.";
      quit();
      break;
    }
  }


  // hands off the loaded image referenes to the parent stage
  public void handOff(){
    println("LoaderThread::handoff()");
    parent.stageSequence   = this.stageSequence;
    parent.gridSequence    = this.gridSequence;
    parent.col1Sequence    = this.col1Sequence;
    parent.col2Sequence    = this.col2Sequence;
    parent.col3Sequence    = this.col3Sequence;
    parent.col4Sequence    = this.col4Sequence;
    parent.col5Sequence    = this.col5Sequence;
    parent.hitzoneSequence = this.hitzoneSequence;
    parent.stageBG         = this.stageBG;
    parent.focal           = this.focal;
    parent.focusVignette   = this.focusVignette;
    parent.relaxVignette   = this.relaxVignette;
    parent.starCollection  = this.starCollection;
  }



  public boolean isActive(){
    return active;
  }



  public void quit(){
    println("LoaderThread::quit()");
    active = false;
    interrupt();
  }


}
/*
 * @author      Tom Conroy
 * @version     1.0
 * @since       1.0
 */

class ScrollingStage {

  /*********************
   * Object Properties *
   ********************/

  // Mia's text stuff
  int imageIndex;
  Feedback textFeed;
  boolean showSuccessTxt;

  // CONSTANTS
  final String GRID_DIR       = "grid/";
  final String VIGNETTE_DIR   = "vignette/";
  final String STAGE_DIR      = "stage/";
  final String FOCAL_DIR      = "focal/";
  final String NOTES_DIR      = "notes/";
  final String HITZONE_DIR    = "hitzone/";
  final String MISC_DIR       = "misc/";
  final int BG_Y_PADDING      = -100;
  final int FOCAL_Y_PADDING   = 150;
  final int GRID_Y_PADDING    = -100;
  final int GRID_H_PADDING    = 200;
  final int SEQ_Y_PADDING     = -100;
  final int SEQ_H_PADDING     = 200;
  final int NOTE_FRAME_COUNT  = 9;

  // PUBLIC
  float x;
  float y;
  float w;
  float h;
  int starCount;
  int currentNoteFrame;
  int activeNote;
  int score;
  PImage stageBG;
  PImage focal;
  PImage focusVignette;
  PImage relaxVignette;
  PImage[] hitzoneSequence;
  PImage[] stageSequence;
  PImage[] gridSequence;
  PImage[] col1Sequence;
  PImage[] col2Sequence;
  PImage[] col3Sequence;
  PImage[] col4Sequence;
  PImage[] col5Sequence;
  Star[] starCollection;
  boolean doneLoading;

  // PRIVATE
  private LoaderThread loader;
  private PImage logoImage;
  private PFont gothamBold;
  private PFont gothamThin;
  private int activeHitzone;
  private int stageIndex;
  private int gridIndex;
  private int activeVignette;

  // settings for the 2 vignette
  private int focusVignAlpha;
  private int relaxVignAlpha;
  private int focusAlphaMax;
  private int relaxAlphaMax;


  /*********************
   * Object Constructor *
   ********************/

  ScrollingStage( float xLoc, float yLoc, float imgWidth, float imgHeight ){
    println("class::ScrollingStage insance created.");

    // setup initial stage properties.
    logoImage        = loadImage( MISC_DIR+"logo.png" );
    gothamBold       = createFont("fonts/Gotham-Bold.ttf", 48, false);
    gothamThin       = createFont("fonts/Gotham-Thin.ttf", 48, false);
    loader           = new LoaderThread(this);
    x                = xLoc;
    y                = yLoc;
    w                = imgWidth;
    h                = imgHeight;
    doneLoading      = false;
    showSuccessTxt   = false;
    starCount        = 500;
    currentNoteFrame = -1;
    activeNote       = 0;
    activeHitzone    = 0;
    activeVignette   = 0;
    stageIndex       = 0;
    gridIndex        = 0;
    score            = 0;

    // start the asset loader.
    loader.start();
  }


  /*********************
   * Setup Methods     *
   ********************/

  public void finishedLoading( boolean loadedStatus ){
    doneLoading = loadedStatus;
  }

  /*********************
   * Draw Methods      *
   ********************/

  public void draw(){
    if( this.doneLoading == false ){
      drawLoadingScreen(); // show loading screen
    }else{
      drawStageElements(); // done loading, draw the stage elements
    }
  } // end draw();

  public void drawStageElements(){
    imageMode(CENTER);
    drawStageBackground();
    drawStageStars();
    drawStageForeground();
    drawStageGrid();
    drawStageNotes();
    drawStageHitzone();
    drawStageFocal();
    drawStageVignette();
    imageMode(CORNER);
    if( showSuccessTxt ){
      textFeed.draw();
    }
    drawScoreText();
  }

  public void drawLoadingScreen(){
    imageMode(CENTER);
    textAlign(CENTER);
    image( logoImage, this.w/2, this.h/2, this.w/2, this.h/2 );
    textFont(gothamBold, 26);
    text("SETTING UP, PLEASE WAIT..", this.w/2, this.h/2-this.h/5);
    textFont(gothamBold, 18);
    text( loader.statusText, this.w/2, this.h/2+this.h/5 );
    imageMode(CORNER);
  }

  public void drawStageBackground(){
    tint(255,230);
    image( stageBG, this.x, this.y+BG_Y_PADDING, this.w, this.h );
    tint(255,255);
  }

  public void drawStageStars(){
    for(Star i:starCollection){
      i.draw();
    }
  }

  public void drawStageVignette(){
    imageMode(CORNER);
    tint(255, focusVignAlpha);
    image(focusVignette, 0, 0);
    tint(255, relaxVignAlpha);
    image(relaxVignette, 0, this.w-1165);
    tint(255,255);
    imageMode(CENTER);
  }

  public void drawStageForeground(){
    image(stageSequence[stageIndex], this.x, this.y, this.w, this.h);
  }

  public void drawStageGrid(){
    image(gridSequence[gridIndex],
            this.x, this.y+GRID_Y_PADDING, this.w, this.h+GRID_H_PADDING);
  }

  public void drawStageNotes(){
    // draw the active note frame
    float xVal = this.x;
    float yVal = this.y + SEQ_Y_PADDING;
    float wVal = this.w;
    float hVal = this.h + SEQ_H_PADDING;

    switch (activeNote) {
      case 1: image(col1Sequence[currentNoteFrame],xVal,yVal,wVal,hVal); break;
      case 2: image(col2Sequence[currentNoteFrame],xVal,yVal,wVal,hVal); break;
      case 3: image(col3Sequence[currentNoteFrame],xVal,yVal,wVal,hVal); break;
      case 4: image(col4Sequence[currentNoteFrame],xVal,yVal,wVal,hVal); break;
      case 5: image(col5Sequence[currentNoteFrame],xVal,yVal,wVal,hVal); break;
      case 0: default: break;
    }

  } // end drawStageNotes();

  public void drawStageHitzone(){
    // draw the hitzone
    if( activeHitzone > 0 ){
      image(hitzoneSequence[activeHitzone-1], this.x, this.y+GRID_Y_PADDING, this.w, this.h+GRID_H_PADDING);
    }
  }

  public void drawStageFocal(){
    image(focal, this.x, 100, this.w, this.h);
  }

  public void drawScoreText(){
    String scoreStr = String.format("%04d", score);
    textFont(gothamBold, 30);
    textAlign(RIGHT);
    fill(255, 255, 255);
    text("SCORE", this.w-10, 30);
    textAlign(RIGHT);
    textFont(gothamThin, 35);
    text(scoreStr, this.w-10, 60);
  }


  /*********************
   * Update Methods    *
   ********************/

  public void update(){
    if( this.doneLoading == true ){
      updateStage();
      updateGrid();
      updateStars();
      updateVignette();
    }
  }

  public void updateStars(){
    // update all our stars
    for( Star i:starCollection ){
      i.update();
    }
  }

  public void updateStage(){
    // increment stage animation sequence
    if( stageIndex >= stageSequence.length-1 ){
      stageIndex = 0;
    }else{
      stageIndex++;
    }
  }

  public void updateGrid(){
    // increment grid animation sequence
    if( gridIndex >= gridSequence.length-1 ){
      gridIndex = 0;
    }else{
      gridIndex++;
    }
  }

  public void updateVignette(){
    // calculate the maxAlpha value for each vignette, based on active hitzone.
    switch (activeHitzone) {
      case 1:
        // max focused
        focusAlphaMax = 255;
        relaxAlphaMax = 0;
      break;
      case 2:
        // 80% focused, 20% relaxed
        focusAlphaMax = 204;
        relaxAlphaMax = 51;
      break;
      case 3:
        // 10% focused, 10% relaxed
        focusAlphaMax = 25;
        relaxAlphaMax = 25;
      break;
      case 4:
      // 80% relaxed, 20% focused
        focusAlphaMax = 51;
        relaxAlphaMax = 204;
      break;
      case 5:
        // maxed relaxed
        focusAlphaMax = 0;
        relaxAlphaMax = 255;
      break;
      case 0:
      default:
        focusAlphaMax = 0;
        relaxAlphaMax = 0;
      break;
    }
    // increment vignetteAlpha for each, up or down, depending on active hitzone
    if( focusVignAlpha < focusAlphaMax ){
      focusVignAlpha += 2;
    }else{
      focusVignAlpha -= 4;
    }

    if(relaxVignAlpha < relaxAlphaMax){
      relaxVignAlpha += 4;
    }else{
      relaxVignAlpha -= 6;
    }
  }



  /*********************
   * Note Methods      *
   ********************/

  // spawns a note in colNum (1-5)
  public void spawnNote(int colNum){
    activeNote       = colNum;
    currentNoteFrame = 0;
  }

  // increment note animation by numToInc
  public void incrementNote( int numToInc ){
    currentNoteFrame += numToInc;
    if( currentNoteFrame > NOTE_FRAME_COUNT){
      calcScore(activeNote);
      terminateNote();
    }
  }

  // disables the active note (kill method)
  public void terminateNote(){
    currentNoteFrame = -1;
    activeNote       = 0;
  }

  // called when note reaches end of its column
  public void calcScore(int completedNote){
    if( notesMatchHitzone() ){
      incrementScore(completedNote);
      String textType;
      switch( completedNote ){
        case 1:  textType = "focus";  break;
        case 5:  textType = "relax";  break;
        default: textType = "random"; break;
      }
      textFeed       = new Feedback(textType, this.w/2, -25);
      showSuccessTxt = true;
    }else{
      println("Missed!");
    }
  }

  // returns true if the hitzone and note matched, false if they didn't.
  public boolean notesMatchHitzone(){
    boolean retbool = false;
    if( this.activeNote == this.activeHitzone ){
      retbool = true;
    }
    return retbool;
  }

  // increments the score. 150 points for focused or relaxed, 100 for inbetween
  public void incrementScore(int completedNote){
    int retPt;
    switch (completedNote) {
      case 1:
      case 5:
      retPt = 150;
      break;
      default:
      retPt = 100;
    }
    score += retPt;
  }


  /*********************
   * Hitzone Methods  *
   ********************/
   // sets the active hitzone (1-5)
   public void setActiveHitzone(int colNum){
    if( colNum > -1 && colNum < 6 ){
        this.activeHitzone = colNum;
    }
   }



  /*********************
   * Vignette Methods  *
   ********************/

   public void setActiveVignette(int which) {
    if( which == 1 || which == 2 || which == 0 ){
      activeVignette = which;
    }
   }

}
class Star {



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
  int star_color;



  // Constructor
  Star(){
    randomizePosition(true);
    float alpha_val = 200;
    // set the star to a random color
    switch( round(random(5)) ){
        case 0: star_color = color( 0,   0,   0,   alpha_val ); break;
        case 1: star_color = color( 255, 51,  220, alpha_val ); break;
        case 2: star_color = color( 239, 130, 47,  alpha_val ); break;
        case 3: star_color = color( 255, 218, 90,  alpha_val ); break;
        case 4: star_color = color( 192, 255, 74,  alpha_val ); break;
        case 5: star_color = color( 13,  255, 232, alpha_val ); break;
    }
  }



  public void randomizePosition(boolean randomizeZ){
    //orig: x = random(-width * 2, width * 2);
    x = random( -width, width );
    //orig: y = random(-height * 2, height * 2);
    // y = random(-height, height);
    y = random( -500, 0 );

    if(randomizeZ){
      z = random(100, 1000);
    }
    else{
      z = 1000;
    }

    velocity  = 3; //random(0.5, 5);
    star_size = random(2, 10);
  }



  public void update(){
    /*if(mousePressed){
      z -= velocity * 10;
    }
    else{
      z -= velocity;
    }*/

    z -= velocity;

    screen_x        = x / z * 100 + width/2;
    screen_y        = y / z * 100 + height/2;
    screen_diameter = star_size / z * 100;

    if( screen_x < 0      ||
        screen_x > width  ||
        screen_y < 0      ||
        screen_y > height ||
        z < 1)
    {
      randomizePosition(false);
    }
  }



  public void draw(){
    //float star_color = 255 - z * 255 / 1000;
    fill(star_color);
    ellipse(screen_x, screen_y, screen_diameter, screen_diameter);
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
