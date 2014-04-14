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

  void drawStage(){
    fill(100);
    rect( locX, locY, stageWidth, stageHeight );
  }


  void updateStage(){
    // redraw the stage background
    drawStage();
    // draw the hit zones for the notes
    for( Hitzone i : hitzoneCollection ){
      i.updateHitzone();
      i.drawHitzone();
    }
  }

}