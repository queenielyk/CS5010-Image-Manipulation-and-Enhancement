package imageprocessing.model;

/**
 * The Interface represents a Pixel and its properties
 * A Pixel is represented by R(red) G(green) B(blue) values.
 * Overrides the equals and hashcode methods respectively.
 */
public interface IPixel {
  /**
   * Getter - to get the red channel of the pixel.
   *
   * @return - the red component
   */
  int getRed();

  /**
   * Getter - to get the green channel of the pixel.
   *
   * @return - the green component
   */
  int getGreen();

  /**
   * Getter - to get the blue channel of the pixel.
   *
   * @return - the blue component
   */
  int getBlue();
}
