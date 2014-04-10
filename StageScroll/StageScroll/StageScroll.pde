ScrollingStage myStage;

void setup() {
  println("setup()");
  // setup basic sketch settings
  size(1024, 768, P3D);
  // start the stage
  myStage = new ScrollingStage(width/2,height*.6,200,550);
}

void draw() {
	// clear out the background
	background(0);
	// update the stage
	myStage.updateStage();
}
