class City { 
  int postalcode; 
  String name; 
  float x; 
  float y; 
  float population; 
  float density; 
  float altitude; 
  boolean isSelected;
  boolean isOver;

  City(int postalCode, String name, float x, float y, float population, float density, float altitude) {
    this.postalcode= postalCode;
    this.name = name;
    this.x = x;
    this.y = y;
    this.population = population;
    this.density = density;
    this.altitude = altitude;  
    this.isSelected = false;  
    this.isOver = false;  
  }
  
  void display() {
    ellipseMode(RADIUS);  // Set ellipseMode to RADIUS
    color c1 = color(255,255,204);
    color c2 = color(34, 94, 168);
    float value_normalized = map(altitude, 0, maxAltitude, 0, 1);
    fill( lerpColor(c1, c2, value_normalized));  // Set fill to white
   
    if(isOver)
      fill(color(255,0,0));     
    ellipse(x, y, density/500, density/500); 

    if(isSelected) {
      String s = this.name;
      fill(250);
      rect(this.x+20, this.y,textWidth(s)+10, 15);
      fill(50);
      text(s, this.x+24, this.y, 500, 80); 
    }

  }
  
  boolean contains(int px, int py) {
    return dist(x, y, px, py) <= density/500 + 1;
  }

  String toString() {
     return this.name + " " + x + ":" + y + " " + population + " " + density;
  }


}
