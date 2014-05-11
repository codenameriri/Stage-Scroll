/**
 * class for generating star field particle effect
 * modified from source found on www.openprocessing.org
 * by Thomas Conroy
 */
class Star {

  /**
   * Properties
   */
  float x;
  float y;
  float z;
  float velocity;
  float star_size;
  float screen_x;
  float screen_y;
  float screen_diameter;
  float old_screen_x;
  float old_screen_y;
  color star_color;

  /**
   * Star() Constructor
   */
  Star(){
    randomizePosition(true);
    float alpha_val = 200;
    // set the star to a random color
    switch( round(random(5)) ){
        case 0: star_color = color( 0,   0,   0,   alpha_val ); break;
        case 1: star_color = color( 255, 51,  220, alpha_val ); break;
        case 2: star_color = color( 239, 130, 47,  alpha_val ); break;
        case 3: star_color = color( 255, 218, 90,  alpha_val ); break;
        case 4: star_color = color( 192, 255, 74,  alpha_val ); break;
        case 5: star_color = color( 13,  255, 232, alpha_val ); break;
    }
  }

  /**
   * randomizes the position of the particle effects.
   * If randomizeZ == true, z value is randomized.
   */
  void randomizePosition(boolean randomizeZ){
    x = random( -width, width );
    y = random( -500, 0 );
    if(randomizeZ){
      z = random(100, 1000);
    }
    else{
      z = 1000;
    }
    velocity  = 3;
    star_size = random(2, 10);
  }

  /**
   * Updates the star settings
   */
  void update(){
    z -= velocity;
    screen_x        = x / z * 100 + width/2;
    screen_y        = y / z * 100 + height/2;
    screen_diameter = star_size / z * 100;
    if( screen_x < 0      ||
        screen_x > width  ||
        screen_y < 0      ||
        screen_y > height ||
        z < 1){
      randomizePosition(false);
    }
  }

  /**
   * Draws the star using the updated settings. Also sets to random color
   */
  void draw(){
    fill(star_color);
    ellipse(screen_x, screen_y, screen_diameter, screen_diameter);
  }
}