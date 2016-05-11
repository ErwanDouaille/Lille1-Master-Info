PImage mapImage;

float minX, maxX;
float minY, maxY;
int totalCount; // total number of places
float minPopulation, maxPopulation;
float minSurface, maxSurface;
float minAltitude, maxAltitude;
float minPopulationToDisplay = 1000;
int X = 1;
int Y = 2;
float x[];
float y[];  
ArrayList<City> cityList = new ArrayList<City>();
City selectedCity;

// Objects can be added to an ArrayList with add()

void setup() {
  size(800, 800);
  //mapImage = loadImage("map.jpg");
  readData();

}

void draw() {
  background(255);
  
  String s = "Afficher les populations supérieures à " + minPopulationToDisplay;
  fill(0);
  text(s, 10, 10, 500, 80); 
 
  color black = color(0);
  for (int i = 0; i < cityList.size(); i++) {
      if(cityList.get(i).population > minPopulationToDisplay)
        cityList.get(i).display();
  }
    
}

void keyPressed() {
  if (key == CODED) {
    if (keyCode == UP) {
        minPopulationToDisplay*=1.1;
    } else if (keyCode == DOWN) {
        minPopulationToDisplay*=0.9;
    } 
  } 
  redraw();
}

void mouseMoved() {
  //println("X: " + mouseX + " Y: " + mouseY );
 
  if(this.selectedCity!=null) {
    this.selectedCity.isSelected = false;
    this.selectedCity.isOver = false;
  }
  this.selectedCity = pick(mouseX, mouseY);
  if(this.selectedCity!=null) {
    this.selectedCity.isSelected = true;
    this.selectedCity.isOver = true;
  }
  redraw();
}

void mouseClicked() {
  //nothing
  redraw();
}

City pick(int px, int py) {
  for (int i = cityList.size()-1; i >0; i--) {
      if(cityList.get(i).population > minPopulationToDisplay) {
        if(cityList.get(i).contains(px, py)) 
          return cityList.get(i);
      }
  }
  return null;  
}

float mapX(float x) {
  return map(x, minX, maxX, 0, 800);
}

float mapY(float y) {
 return map(y, minY, maxY, 800, 0);
}

void readData() {
   String[] lines = loadStrings("../../villes.tsv");
  parseInfo(lines[0]); // read the header line
  x = new float[totalCount];
  y = new float[totalCount];
  for (int i = 2 ; i < totalCount ; ++i) {
    String[] columns = split(lines[i], TAB);
    x[i-2] = float (columns[1]);
    y[i-2] = float (columns[2]);
    cityList.add(new City(int(columns[0]), columns[4], mapX(float (columns[1])), mapY(float (columns[2])), float (columns[5]), float (columns[5])/float (columns[6]), float (columns[7])));

    
  }
}

void parseInfo(String line) {
  String infoString = line.substring(2); // remove the #
  String[] infoPieces = split(infoString, ',');
  totalCount = int(infoPieces[0]);
  minX = float(infoPieces[1]);
  maxX = float(infoPieces[2]);
  minY = float(infoPieces[3]);
  maxY = float(infoPieces[4]);
  minPopulation = float(infoPieces[5]);
  maxPopulation = float(infoPieces[6]);
  minSurface = float(infoPieces[7]);
  maxSurface = float(infoPieces[8]);
  minAltitude = float(infoPieces[9]);
  maxAltitude = float(infoPieces[10]); 
}
