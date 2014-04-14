ScrollingStage myStage;

int starCount = 500;
Star[] starCollection;

void setup() {
  size(1024, 768, P3D);

  starCollection = new Star[starCount];

  // start the stage
  myStage = new ScrollingStage(width/2,height*.6,200,550);

  // spawn the starfield
  for( int i=0; i<starCount; i++ ){
  	starCollection[i] = new Star();
  }
}

void draw() {
	// clear out the background
	background(0);
	noStroke();

	// update the stars
	for(int q=0; q<starCount; q++){
		starCollection[q].update();
		starCollection[q].display();
	}

  	// drawing settings
  	rectMode(CENTER);
  	rotateX(radians(50));		
	// update the stage
	myStage.updateStage();	
}