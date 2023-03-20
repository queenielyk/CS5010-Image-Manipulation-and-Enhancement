package ime.model;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * An interface to represent a processor to process an image. A processor read the original image
 * and able to process each component to produce a new image. A processor could export image to
 * local as well.
 */
public interface ImageProcessor {

  /**
   * A method to read image from specified path, name it to given name.
   *
   * @param path relative path of image, in class dependent format
   * @param name name of image to be called throughout the command line
   * @throws FileNotFoundException the provided file is not exist
   * @throws IllegalStateException the magic number of the image is not P3
   */
  public void loadImage(String path, String name)
      throws FileNotFoundException, IllegalStateException;

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
  public void greyscale(String mode, String from, String to) throws IllegalArgumentException;

  /**
   * A method to flip the specified image horizontally, and named it.
   *
   * @param from name of image to be converted
   * @param to   new image's name
   */
  public void horizontalFlip(String from, String to);

  /**
   * A method to flip the specified image vertically, and named it.
   *
   * @param from name of image to be converted
   * @param to   new image's name
   */
  public void verticalFlip(String from, String to);


  /**
   * A method to adjust the brightness of specified image, and name it. The brightness value of the
   * image clamp to it's maximum value defined at the prototype, 0 as the minimum value as well.
   *
   * @param from name of the image to be adjusted
   * @param add  the brightness level to be added to the current level
   * @param to   new image's name
   */
  public void adjustBrightness(String from, int add, String to);


  /**
   * A method to combine three channel-based greyscale images into one image, and named it.
   *
   * @param redName   the name of red-based greyscale image
   * @param greenName the name of green-based greyscale image
   * @param blueName  the name of blue-based greyscale image
   * @param to        new image's name
   */
  public void combines(String redName, String greenName, String blueName, String to);

  /**
   * A method to export image to class dependent format and save at local. This method will
   * overwrite the existing file if this path already exist.
   *
   * @param from name of the image to be export
   * @param path relative path to save the image at local
   * @throws IOException unable to write file
   */
  public void save(String from, String path) throws IOException;

}
