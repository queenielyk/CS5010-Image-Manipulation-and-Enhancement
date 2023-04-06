package gime.model;

public interface ImageProcessor extends ReadOnlyImageProcessor{
  /**
   * A method to load image from an ImageReader object and store it.
   *
   * @param info  info of the image, int[width, height, maxi-value]
   * @param image image in form of 3D-Array, int[Height][Width][RGB]
   * @param name  the name of image
   */
  void loadImage(int[] info, int[][][] image, String name);

  /**
   * A method to convert specified image to greyscale based on specified mode. Modes to convert
   * image to greyscale: - red-component - green-component - blue-component - value-component -
   * intensity-component - luma-component
   *
   * @param mode mode to be adopted to convert image to greyscale
   * @param from name of image to be converted
   * @param to   new image's name
   * @throws IllegalArgumentException greyscale mode is not an option
   */
  void colorTransform(String mode, String from, String to) throws IllegalArgumentException;

  /**
   * A method to flip the specified image horizontally, and named it.
   *
   * @param from name of image to be converted
   * @param to   new image's name
   */
  void horizontalFlip(String from, String to);

  /**
   * A method to flip the specified image vertically, and named it.
   *
   * @param from name of image to be converted
   * @param to   new image's name
   */
  void verticalFlip(String from, String to);


  /**
   * A method to adjust the brightness of specified image, and name it. The brightness value of the
   * image clamp to it's maximum value defined at the prototype, 0 as the minimum value as well.
   *
   * @param from name of the image to be adjusted
   * @param add  the brightness level to be added to the current level
   * @param to   new image's name
   */
  void adjustBrightness(String from, int add, String to);


  /**
   * A method to combine three channel-based greyscale images into one image, and named it.
   *
   * @param redName   the name of red-based greyscale image
   * @param greenName the name of green-based greyscale image
   * @param blueName  the name of blue-based greyscale image
   * @param to        new image's name
   * @throws IllegalStateException Either image info is different to others
   */
  void combines(String redName, String greenName, String blueName, String to)
          throws IllegalStateException;

  /**
   * A method to apply a filter to the specified image. There are two filtering mode: - Blur -
   * Sharpen
   *
   * @param mode filtering mode to be apply to the image
   * @param from name of image to be applied
   * @param to   new image's name
   * @throws IllegalStateException filtering mode is not supported
   */
  void filter(String mode, String from, String to) throws IllegalStateException;


  /**
   * A method to dither an image, and name it.
   *
   * @param from name of image to be dithered
   * @param to   new image's name
   */
  void dithering(String from, String to);
}
