/*
 * @author      Tom Conroy
 * @version     1.0 
 * @since       1.0
 */
class ScrollingStage {

  // properties
  float x;
  float y;
  float w;
  float h;
  float bpm;
  
  // image settings
  PImage stageImg;
  PImage overlayImg;

  // Constructor
  ScrollingStage(float w) {
    this.w = w - 200;

    // calculate the width to display the image
    this.h = this.w * 0.58; //0.5827;    
    
    /* load images */
    // stageBG
    stageImg = loadImage("stage.png");
    // stage overlaay
    overlayImg = loadImage("focal_point.png");
    
  }

  void draw(){
    // draw the stage background 
    image(stageImg, -40, 10, this.w, this.h);
    
    // draw the stage overlay
    blendMode(MULTIPLY);
    image(overlayImg, 50, -450, 1777/2, 1777/2);
    blendMode(BLEND);
    
    // draw the hitzones
  }


  void update(){

  }

}
