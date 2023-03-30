package mime.model;

import ime.model.ImageProcessor;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A new interface provides more operations other than the operations mentioned in
 * interface ImageProcessor.
 * New operations:
 * - Filter
 * - Blur
 * - Sharpen
 * - Dithering
 * Some operations mentioned in interface ImageProcessor are no longer supported in this version,
 * they are marked as @Deprecated and throw IllegalStateException.
 */
public interface MoreImageProcessor extends ImageProcessor {

  /**
   * A method to load image from an ImageReader object and store it.
   *
   * @param info info of the image, int[width, height, maxi-value]
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
   * A method to export image to specified and acceptable image format then save at local This
   * method will overwrite the existing file if this path already exist.
   *
   * @param from   name of the image to be export
   * @param stream a output stream
   * @param format the format of image
   * @throws IOException           unable to write file
   * @throws IllegalStateException the image format is not an option
   */
  void save(String from, OutputStream stream, String format)
          throws IOException, IllegalStateException;
}
