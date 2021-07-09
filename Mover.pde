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
  boolean isMoving(int row, int col) {
    return (active && row == final_row && col == final_col);
  }
  boolean isActive() {
    return active;
  }
  void move() {
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
  void display() {
    print(active, grid[final_row][final_col], current_x, current_y, final_x, final_y, "\n");
  }
}
