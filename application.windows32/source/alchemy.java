import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class alchemy extends PApplet {

int window_width, window_height;
int cell_width, cell_height;
int padding_horizontal, padding_vertical;
int padding_top;
int nrows, ncols;

int move_frames;
ArrayList<Mover> movers = new ArrayList<Mover>();

int[][] grid;
boolean toSpawn;

int stage;
int starting_element;
int min_element;

public void setup() {
  
  frameRate(30);
  window_width = 800;
  window_height = 800;

  padding_horizontal = padding_vertical = 5;
  padding_top = 120;

  nrows = ncols = 2;
  stage = 1;
  starting_element = 1;
  min_element = 1;

  calculateDimensions();
  grid = new int[nrows][ncols];
  initialSpawn();

  textAlign(CENTER, CENTER);
}

public void draw() {
  background(getBackgroundColor(getMax()));
  fill(getTextColor(getMax()));
  textSize(40);
  text("Alchemy", window_width / 2, padding_top / 2);
  textSize(20);
  text("Developed by ChanRT | Fork me at GitHub", window_width / 2, window_height + 130);
  
  textSize(min(cell_width / 2, cell_height / 2));

  if (toSpawn && stoppedMoving()) {
    spawn();
  }

  drawEmpty();
  drawCells();
}

public void drawEmpty() {
  boolean filled = false;
  for (int row = 0; row < nrows; row++) {
    for (int col = 0; col < ncols; col++) {

      filled = false;
      float x = getX(col);
      float y = getY(row);

      for (Mover mover : movers) {
        if (mover.isMoving(row, col)) {
          fill(0);

          filled = true;
          break;
        }
      }
      if (!filled) {
        fill(getBackgroundColor(grid[row][col]));
      }
      rect(x, y, cell_width, cell_height);
    }
  }
}

public void drawCells() {
  for (int row = 0; row < nrows; row++) {
    for (int col = 0; col < ncols; col++) {
      boolean found = false;
      for (Mover mover : movers) {
        if (mover.isMoving(row, col)) {
          mover.move();
          found = true;
        }
      }
      if (!found) {
        float x = getX(col);
        float y = getY(row);

        if (grid[row][col] != 0) {
          fill(getTextColor(grid[row][col]));
          text(getElement(grid[row][col]), x + cell_width / 2, y + cell_height / 2);
        }
      }
    }
  }
}

public void checkStage() {
  int max = getMax();

  if (stage == 1 && max >= 2) {
    stage++;
    min_element = 2;
    transferGrid(nrows + 1, ncols + 1);
  } 
  if (stage == 2 && max >= 6) {
    stage++;
    min_element = 5;
    transferGrid(nrows + 1, ncols);
  }
  if (stage == 3 && max >= 8) {
    stage++;
    min_element = 7;
    transferGrid(nrows, ncols + 1);
  }
  if (stage == 4 && max >= 10) {
    stage++;
    min_element = 9;
    transferGrid(nrows + 1, ncols + 1);
  }
  if (stage == 5 && max >= 14) {
    stage++;
    min_element = 13;
    transferGrid(nrows + 1, ncols);
  }
}

public void transferGrid(int new_rows, int new_cols) {
  int[][] new_grid = new int[new_rows][new_cols];

  for (int row = 0; row < nrows; row++) {
    for (int col = 0; col < ncols; col++) {
      new_grid[row][col] = grid[row][col];
    }
  }

  grid = new_grid;
  nrows = new_rows;
  ncols = new_cols;

  calculateDimensions();
}

public void keyPressed() {

  if (key == 'x') {
    for (Mover mover : movers) {
      mover.display();
    }
  }
  if (keyCode == UP || key == 'w' || key == 'W') {
    for (int row = 1; row < nrows; row++) {
      for (int col = 0; col < ncols; col++) {
        if (grid[row][col] != 0) {
          for (int i = 0; i < row; i++) {
            if (isColEmpty(col, row, i)) {
              if (grid[i][col] == 0) {
                grid[i][col] = grid[row][col];
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, i, col));
                break;
              } else if (isColEmpty(col, row, i) && grid[i][col] == grid[row][col]) {
                grid[i][col]++;
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, i, col));
                break;
              }
            }
          }
        }
      }
    }
  } else if (keyCode == DOWN || key == 's' || key == 'S') {
    for (int row = nrows - 2; row > -1; row--) {
      for (int col = 0; col < ncols; col++) {
        if (grid[row][col] != 0) {
          for (int i = nrows - 1; i > row; i--) {
            if (isColEmpty(col, row, i)) {
              if (grid[i][col] == 0) {
                grid[i][col] = grid[row][col];
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, i, col));
                break;
              } else if (grid[i][col] == grid[row][col]) {
                grid[i][col]++;
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, i, col));
                break;
              }
            }
          }
        }
      }
    }
  } else if (keyCode == RIGHT || key == 'd' || key == 'D') {
    for (int col = ncols - 2; col > -1; col--) {
      for (int row = 0; row < nrows; row++) {
        if (grid[row][col] != 0) {
          for (int j = ncols - 1; j > col; j--) {
            if (isRowEmpty(row, col, j)) {
              if (grid[row][j] == 0) {
                grid[row][j] = grid[row][col];
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, row, j));
                break;
              } else if (grid[row][j] == grid[row][col]) {
                grid[row][j]++;
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, row, j));
                break;
              }
            }
          }
        }
      }
    }
  } else if (keyCode == LEFT || key == 'a' || key == 'A') {
    for (int col = 1; col < ncols; col++) {
      for (int row = 0; row < nrows; row++) {
        if (grid[row][col] != 0) {
          for (int j = 0; j < col; j++) {
            if (isRowEmpty(row, col, j)) {
              if (grid[row][j] == 0) {
                grid[row][j] = grid[row][col];
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, row, j));
                break;
              } else if (grid[row][j] == grid[row][col]) {
                grid[row][j]++;
                grid[row][col] = 0;
                toSpawn = true;
                movers.add(new Mover(row, col, row, j));
                break;
              }
            }
          }
        }
      }
    }
  }
  checkStage();
  displayGrid();
}

// Spawners
public void initialSpawn() {
  randomSeed(PApplet.parseInt(random(0, 10000)));
  int row = PApplet.parseInt(random(nrows));
  int col = PApplet.parseInt(random(ncols));

  grid[row][col] = starting_element;

  do {
    row = PApplet.parseInt(random(nrows));
    col = PApplet.parseInt(random(ncols));
  } while (grid[row][col] == starting_element);

  grid[row][col] = starting_element;
}

public void spawn() {
  int row, col;
  do {
    row = PApplet.parseInt(random(nrows));
    col = PApplet.parseInt(random(ncols));
  } while (grid[row][col] != 0);

  grid[row][col] = getSpawnNumber();

  toSpawn = false;
}
class Mover {
  int final_row, final_col;
  float current_x, current_y;
  float speed_x, speed_y;
  float final_x, final_y;
  boolean active;
  int num_frames = 20;
  Mover(int row1, int col1, int row2, int col2) {
    println(grid[row2][col2], " moving from ", row1, col1, " to ", row2, col2);
    final_row = row2;
    final_col = col2;

    current_x = getX(col1);
    current_y = getY(row1);

    final_x = getX(col2);
    final_y = getY(row2);

    speed_x = (final_x - current_x) / num_frames; 
    speed_y = (final_y - current_y) / num_frames;

    active = true;
  }
  public boolean isMoving(int row, int col) {
    return (active && row == final_row && col == final_col);
  }
  public boolean isActive() {
    return active;
  }
  public void move() {
    if ((speed_x > 0 && current_x > final_x) || (speed_x < 0 && current_x < final_x)) {
      current_x = final_x;
      speed_x = 0;
      active = false;
    }
    if ((speed_y > 0 && current_y > final_y) || (speed_y < 0 && current_y < final_y)) {
      current_y = final_y;
      speed_y = 0;
      active = false;
    }

    if (current_x != final_x || current_y != final_y) {
      current_x += speed_x;
      current_y += speed_y;
    } else if (current_x == final_x && current_y == final_y) {
      speed_x = 0;
      speed_y = 0;
      active = false;
    }

    if (current_x < padding_horizontal) {
      speed_x = 0;
      active = false;
      current_x = padding_horizontal;
    } else if (current_y < padding_vertical) {
      speed_y = 0;
      active = false;
      current_y = padding_vertical;
    } else if (current_x > window_width - padding_horizontal) {
      speed_x = 0;
      active = false;
      current_x = window_width - padding_horizontal;
    } else if (current_y > window_height - padding_vertical) {
      speed_y = 0;
      active = false;
      current_y = window_height - padding_vertical;
    }

    fill(getBackgroundColor(grid[final_row][final_col]));
    rect(current_x, current_y, cell_width, cell_height);
    if (grid[final_row][final_col] != 0) {
      fill(getTextColor(grid[final_row][final_col]));
      text(getElement(grid[final_row][final_col]), current_x + cell_width / 2, current_y + cell_height / 2);
    }
  }
  public void display() {
    print(active, grid[final_row][final_col], current_x, current_y, final_x, final_y, "\n");
  }
}
public int getMax() {
  int max = 0;
  for (int row = 0; row < nrows; row++) {
    for (int col = 0; col < ncols; col++) {
      if (grid[row][col] > max) {
        max = grid[row][col];
      }
    }
  }
  return max;
}

public int getSpawnNumber() {
  int sum = 0, num = 0;

  for (int row = 0; row < nrows; row++) {
    for (int col = 0; col < ncols; col++) {
      if (grid[row][col] != 0) {
        sum += grid[row][col];
        num++;
      }
    }
  }

  if (min_element < sum/num) {
    return PApplet.parseInt(random(min_element, sum/num));
  } else {
    return PApplet.parseInt(random(sum/num, min_element));
  }
}

public int getBackgroundColor(int num) {
  switch(num) {
  case 0:
    return 0xff000000;
  case 1:
    return 0xffadd8e6;
  case 2:
    return 0xff00008b;
  case 3:
    return 0xff6c7a86;
  case 4:
    return 0xff964b00;
  case 5:
    return 0xff8b0000;
  case 6:
    return 0xff79b47c;
  case 7:
    return 0xffffff00;
  case 8:
    return 0xff000080;
  case 9:
    return 0xffd9381e;
  case 10:
    return 0xff1f51ff;
  case 11:
    return 0xffafafaf;
  case 12:
    return 0xff2a802a;
  case 13:
    return 0xff848789;
  case 14:
    return 0xff9599a5;
  case 15:
    return 0xffffa500;
  case 16:
    return 0xff2c320a;
  case 17:
    return 0xff0cafff;
  case 18:
    return 0xffff6700;
  default:
    return 0xff000000;
  }
}

public int getTextColor(int num) {
  switch(num) {
  case 1:
  case 7:
    return 0xff000000;
  default:
    return 0xffffffff;
  }
}

public String getElement(int num) {
  switch(num) {
  case 0:
    return "";
  case 1:
    return "H";
  case 2:
    return "He";
  case 3:
    return "Li";
  case 4:
    return "Be";
  case 5:
    return "B";
  case 6:
    return "C";
  case 7:
    return "N";
  case 8:
    return "O";
  case 9:
    return "F";
  case 10:
    return "Ne";
  case 11:
    return "Na";
  case 12:
    return "Mg";
  case 13:
    return "Al";
  case 14:
    return "Si";
  case 15:
    return "P";
  case 16:
    return "S";
  case 17:
    return "Cl";
  case 18:
    return "Ar";
  default:
    return "";
  }
}
public boolean stoppedMoving() {  
  for (Mover mover : movers) {
    if (mover.isActive()) {
      return false;
    }
  }
  return true;
}

// Calculators
public void calculateDimensions() {
  cell_width = (window_width - (ncols + 1) * padding_horizontal) / ncols;
  cell_height = (window_height - (nrows + 1) * padding_vertical) / nrows;
}

public float getX(int j) {
  return padding_horizontal + (padding_horizontal + cell_width) * j;
}

public float getY(int i) {
  return padding_top + padding_vertical + (padding_vertical + cell_height) * i;
}

// Debuggers
public void displayGrid() {
  for (int i = 0; i < nrows; i++) {
    for (int j = 0; j < ncols; j++) {
      print(grid[i][j], " ");
    }
    print("\n");
  }
}

// Empty checkers
public boolean isRowEmpty(int row, int col1, int col2) {
  if (col1 > col2) {
    int t = col1;
    col1 = col2;
    col2 = t;
  }
  for (int j = col1 + 1; j < col2; j++) {
    if (grid[row][j] != 0) {
      return false;
    }
  }
  return true;
}

public boolean isColEmpty(int col, int row1, int row2) {
  if (row1 > row2) {
    int t = row1;
    row1 = row2;
    row2 = t;
  }
  for (int i = row1 + 1; i < row2; i++) {
    if (grid[i][col] != 0) {
      return false;
    }
  }
  return true;
}
  public void settings() {  size(800, 1000); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "alchemy" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
