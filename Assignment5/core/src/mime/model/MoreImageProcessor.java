package mime.model;

import java.awt.image.BufferedImage;
import java.io.IOException;

import ime.model.ImageProcessor;

public interface MoreImageProcessor extends ImageProcessor {

  /**
   * A method to verify whether accepting them image format.
   * Accepted image format includes:
   * - ppm
   * - jpg
   * - png
   * - bmp
   *
   * @param pathname pathname of incoming image
   * @return true if acceptable; else otherwise
   */
  boolean verifyFormat(String pathname);

  void loadImage(BufferedImage image, String name);

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
   * A method to apply a filter to the specified image.
   * There are two filtering mode:
   * - Blur
   * - Sharpening
   *
   * @param mode filtering mode to be apply to the image
   * @param from name of image to be applied
   * @param to   new image's name
   */
  void filter(String mode, String from, String to);


  /**
   * A method to dither an image, and name it.
   *
   * @param from name of image to be dithered
   * @param to   new image's name
   */
  void dithering(String from, String to);


  /**
   * A method to export image to specified and acceptable image format then save at local
   * This method will overwrite the existing file if this path already exist.
   *
   * @param from name of the image to be export
   * @param path relative path to save the image at local
   * @throws IOException           unable to write file
   * @throws IllegalStateException the image format is not an option
   */
  @Override
  void save(String from, String path) throws IOException, IllegalStateException;
}
