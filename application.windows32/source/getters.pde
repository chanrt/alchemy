int getMax() {
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

int getSpawnNumber() {
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
    return int(random(min_element, sum/num));
  } else {
    return int(random(sum/num, min_element));
  }
}

int getBackgroundColor(int num) {
  switch(num) {
  case 0:
    return #000000;
  case 1:
    return #add8e6;
  case 2:
    return #00008b;
  case 3:
    return #6c7a86;
  case 4:
    return #964b00;
  case 5:
    return #8b0000;
  case 6:
    return #79b47c;
  case 7:
    return #ffff00;
  case 8:
    return #000080;
  case 9:
    return #d9381e;
  case 10:
    return #1f51ff;
  case 11:
    return #afafaf;
  case 12:
    return #2a802a;
  case 13:
    return #848789;
  case 14:
    return #9599a5;
  case 15:
    return #ffa500;
  case 16:
    return #2c320a;
  case 17:
    return #0cafff;
  case 18:
    return #ff6700;
  default:
    return #000000;
  }
}

int getTextColor(int num) {
  switch(num) {
  case 1:
  case 7:
    return #000000;
  default:
    return #ffffff;
  }
}

String getElement(int num) {
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
