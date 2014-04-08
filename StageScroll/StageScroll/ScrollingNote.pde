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

  void drawStageBG(){
    rectMode(CENTER);
    translate(512, 384, 0);
    rotateX(PI/3);
    fill(255, 255, 255, 100);
    rect(0, 0, 512, 750);
  }
  
  void drawHitZone(int column){
    
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

  int getCount() {
    return count;
  }

  // overriding default "start()"
  void start() {
    running = true;
    println("ScrollingNote::"+this.id+ " has started. executing every " + wait + "ms");
    // do whatever you want start to do, usually setup stuff 


    // call this last
    super.start();
  }

  void run() {
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
  void quit() {
    System.out.println( "Quitting.." );
    running = false;
    interrupt();
  }
}

