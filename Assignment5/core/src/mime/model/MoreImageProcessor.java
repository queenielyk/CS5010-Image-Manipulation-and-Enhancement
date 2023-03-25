package mime.model;

import java.awt.image.BufferedImage;

import ime.model.ImageProcessor;

public interface MoreImageProcessor extends ImageProcessor {
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


  void dithering(String from, String to);
}
