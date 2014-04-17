/*
class Hitzone
{
  // props   
   int colNum;
   int x;
   int y;
   int w;
   int h;
   // hitzone props
   color hitzoneColor;     // where we store the RGB of this hitzone
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
   
   
  void updateHitzone()
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
      hitzoneShape.setFill(#FFFFFF);
    }else{
      //restore hitzone color
      hitzoneShape.setFill(hitzoneColor);
    }
  }

  // checks for collisions between 2 shapes 
  boolean checkCollisions(PShape s1, PShape s2){
    // TODO
    return true;
  }

  void display(){
    pushMatrix();
    translate(0,0,1);
    rectMode(CORNER);
    shape( hitzoneShape );
    popMatrix();
  }
}
*/
