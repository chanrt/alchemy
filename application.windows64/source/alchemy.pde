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

void setup() {
  size(800, 1000);
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

void draw() {
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

void drawEmpty() {
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

void drawCells() {
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

void checkStage() {
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

void transferGrid(int new_rows, int new_cols) {
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

void keyPressed() {

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
void initialSpawn() {
  randomSeed(int(random(0, 10000)));
  int row = int(random(nrows));
  int col = int(random(ncols));

  grid[row][col] = starting_element;

  do {
    row = int(random(nrows));
    col = int(random(ncols));
  } while (grid[row][col] == starting_element);

  grid[row][col] = starting_element;
}

void spawn() {
  int row, col;
  do {
    row = int(random(nrows));
    col = int(random(ncols));
  } while (grid[row][col] != 0);

  grid[row][col] = getSpawnNumber();

  toSpawn = false;
}
