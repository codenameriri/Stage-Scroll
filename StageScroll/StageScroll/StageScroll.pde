// var
int starCount = 500;

// objects
ScrollingStage myStage;
Star[] starCollection;

void setup() {
  // sketch settings
  size(1024, 768, P3D);
  
  // instantiation
  myStage        = new ScrollingStage(1300); // pass in the sketch width to the stage
  starCollection = new Star[starCount];
  
  for( int i=0; i<starCount; i++ ){
  	starCollection[i] = new Star();
  }
}

void draw() {
	// clear out the background
	background(0);
	noStroke();

  // update data
  for( Star i:starCollection ){
    i.update();
  }
  myStage.update();

  // draw data
  for( Star i:starCollection ){
    i.draw();
  }
  myStage.draw();
}
