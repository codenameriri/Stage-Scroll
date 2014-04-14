class Hitzone
{
  // props   
   int colNum;
   int x;
   int y;
   int w;
   int h;
   float stagePerspective; // degrees the stage is tilted so we can match it
  
   color zoneColor; // where we store the RGB of this hitzone

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
   
   
  void updateHitzone()
  {
    // if something is within bounds of the hitzone, change its color
    if( pointRect( mouseX, mouseY, this.x-430, this.y-100, this.w, this.h ) ){
      println("hitting colNum:" + colNum);
    }

  }

  void drawHitzone()
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
boolean pointRect(int px, int py, int rx, int ry, int rw, int rh) {
  
  // test for collision
  if (px >= rx-rw/2 && px <= rx+rw/2 && py >= ry-rh/2 && py <= ry+rh/2) {
    return true;    // if a hit, return true
  }
  else {            // if not, return false
    return false;
  }
}



}