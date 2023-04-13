package mime.model;

/**
 * An Enum class to define all ColorTransform 2D matrix.
 */
public enum ColorTransMatrix {
  RED(new float[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}}),
  GREEN(new float[][]{{0, 1, 0}, {0, 1, 0}, {0, 1, 0}}),
  BLUE(new float[][]{{0, 0, 1}, {0, 0, 1}, {0, 0, 1}}),
  VALUE(new float[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}}),
  INTENSITY(new float[][]{{1 / 3F, 1 / 3F, 1 / 3F}, {1 / 3F, 1 / 3F, 1 / 3F},
      {1 / 3F, 1 / 3F, 1 / 3F}}),
  LUMA(new float[][]{{0.2126F, 0.7152F, 0.0722F}, {0.2126F, 0.7152F, 0.0722F},
      {0.2126F, 0.7152F, 0.0722F}}),
  SEPIA(new float[][]{{0.393F, 0.769F, 0.189F}, {0.349F, 0.686F, 0.168F},
      {0.272F, 0.534F, 0.131F}});

  private final float[][] floats;

  /**
   * A private constructor to construct an Enum.
   *
   * @param floats the 2D matrix to hold
   */
  private ColorTransMatrix(float[][] floats) {
    this.floats = floats;
  }

  /**
   * A method to return the ColorTrans matrix.
   *
   * @return a 2D matrix
   */
  public float[][] getFloats() {
    return floats;
  }
}
