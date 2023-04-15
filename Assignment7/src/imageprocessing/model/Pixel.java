package imageprocessing.model;

/**
 * Pixel implements IPixel. It represents a Pixel of the form (red, green, blue).
 */
public class Pixel implements IPixel {
  private final int red;
  private final int green;
  private final int blue;

  /**
   * Constructs an object of Pixel class.
   * Each pixel has a Red, Green and Blue channel.
   *
   * @param red   - the red component of the pixel
   * @param green - the green component of the pixel
   * @param blue  - the blue component of the pixel
   */
  public Pixel(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  @Override
  public int getRed() {
    return this.red;
  }

  @Override
  public int getGreen() {
    return this.green;
  }

  @Override
  public int getBlue() {
    return this.blue;
  }

  /**
   * Overriding the equals method such that 2 pixels that have the same red, green, blue values
   * are equal to each other.
   *
   * @param o - an instance of IPixel type
   * @return - true if the pixels are equal, else false
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof IPixel) {
      IPixel pixel = (IPixel) o;

      return this.getRed() == pixel.getRed() && this.getBlue() == pixel.getBlue()
              && this.getGreen() == pixel.getGreen();
    } else {
      return false;
    }
  }

  /**
   * Overriding the hashcode method such that 2 pixels that have the same red, green, blue values
   * have the same hashcode value.
   *
   * @return - the hashcode of the pixel object
   */
  @Override
  public int hashCode() {
    StringBuilder hash = new StringBuilder();
    hash.append("r" + this.getRed() + "g" + this.getGreen() + "b" + this.getBlue());

    return Double.hashCode(hash.hashCode());
  }
}
