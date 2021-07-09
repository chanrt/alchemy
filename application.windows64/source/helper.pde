boolean stoppedMoving() {  
  for (Mover mover : movers) {
    if (mover.isActive()) {
      return false;
    }
  }
  return true;
}

// Calculators
void calculateDimensions() {
  cell_width = (window_width - (ncols + 1) * padding_horizontal) / ncols;
  cell_height = (window_height - (nrows + 1) * padding_vertical) / nrows;
}

float getX(int j) {
  return padding_horizontal + (padding_horizontal + cell_width) * j;
}

float getY(int i) {
  return padding_top + padding_vertical + (padding_vertical + cell_height) * i;
}

// Debuggers
void displayGrid() {
  for (int i = 0; i < nrows; i++) {
    for (int j = 0; j < ncols; j++) {
      print(grid[i][j], " ");
    }
    print("\n");
  }
}

// Empty checkers
boolean isRowEmpty(int row, int col1, int col2) {
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

boolean isColEmpty(int col, int row1, int row2) {
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
