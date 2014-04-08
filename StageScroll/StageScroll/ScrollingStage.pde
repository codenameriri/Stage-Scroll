/*
 * @author      Tom Conroy
 * @version     1.0 
 * @since       1.0
 */
class ScrollingStage extends Thread {

  boolean running;
  int wait;
  String id;
  int count;

  // Constructor
  ScrollingStage(String s, int w) {
    running = false; // disable running by default
    id      = s;     // name of the instance
    wait    = w;     // how long to wait before repeating thread loop
    count   = 0;     // the number of times we've looped through the thread
        
    // setup the stage bg rect
    drawStageBG();

    // setup the smaller note hit points
    for(int i=1; i<6; i++){
     drawHitZone(i);
    }

  }


  void drawStageBG(){
    rectMode(CENTER);
    translate(512, 384, 0);
    rotateX(PI/3);
    fill(255);
    rect(0, 0, 512, 750);
  }
  
  void drawHitZone(int column){
    
    //translate(column*100, 0, 0);
//    rotateX(PI/8);
    fill(50);
    rect(column*100-300, 320, 80, 80);
    
    /* TODO: position each column seperately 
    switch(column){
      case 1:
        
      break;
      
      case 2:
      break;
      
      case 3:
      break;
      
      case 4:
      break;
    }*/
  }

  int getCount() {
    return count;
  }

  // overriding default "start()"
  void start() {
    running = true;
    println("ScrollingStage::"+this.id+ " has started. executing every " + wait + "ms");
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
    System.out.println("ScrollingStage " + id + " Thread is done.");
  }

  // quits the thread
  void quit() {
    System.out.println( "Quitting.." );
    running = false;
    interrupt();
  }
}

