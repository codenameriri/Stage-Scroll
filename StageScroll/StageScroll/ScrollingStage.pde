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

  void drawStage(){
    rectMode(CENTER);
    rotateX(radians(stagePerspective));
    fill(255,255,255, 50);
    rect( locX, locY, stageWidth, stageHeight );
  }

  void drawHitzones(){
    rectMode(CENTER);
    //rotateX(radians(stagePerspective));
    for( int i=0; i<4; i++ ){
      fill(255,34,184, 50);
      rect( 0, 0, 100, 100);
    }
  }

  void updateStage(){
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