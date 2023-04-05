package mime.model;

import ime.model.ImageProcessor;

/**
 * This interface extends interface ImageProcessor.
 * Override methods various to previous version, e.g. adding exceptions
 * It provides more operations other than the operations mentioned in
 * interface ImageProcessor.
 * New operations:
 * - Filter
 * - Blur
 * - Sharpen
 * - Dithering
 */
public interface MoreImageProcessor extends ImageProcessor {

  /**
   * A method to load image from an ImageReader object and store it.
   *
   * @param info  info of the image, int[width, height, maxi-value]
   * @param image image in form of 3D-Array, int[Height][Width][RGB]
   * @param name  the name of image
   */

  void loadImage(int[] info, int[][][] image, String name);

  /**
   * A method to combine three channel-based greyscale images into one image, and named it.
   *
   * @param redName   the name of red-based greyscale image
   * @param greenName the name of green-based greyscale image
   * @param blueName  the name of blue-based greyscale image
   * @param to        new image's name
   * @throws IllegalStateException Either image info is different to others
   */
  @Override
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

  /**
   * A method to return an image.
   *
   * @param name name of image
   * @return image in form of int[][][]
   */
  int[][][] getImage(String name);

  /**
   * A method to return info of image.
   *
   * @param name name of image
   * @return info in form of int[]
   */
  int[] getInfo(String name);


}
