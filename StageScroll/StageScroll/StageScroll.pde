ScrollingStage myStage;

void setup() {
  println("setup()");
  
  // setup basic sketch settings
  size(1024, 768, P3D);
  background(0);
  
  // instantiate + start the ScrollingStage
  myStage = new ScrollingStage("myStage", 100);
  myStage.start();
}

void draw() {

  int a = myStage.getCount();
  println("Tick: " + a);
}







