package gime.control;

/**
 * An interface to indicate methods as protocols for the View to interact with.
 */
public interface Features {

  /**
   * A method to load an image from file.
   *
   * @param path    the pathname of the image file
   * @param imgName the name of image in Model
   */
  void loadImage(String path, String imgName);

  /**
   * A method of save an image to file.
   *
   * @param savingPath the pathname to save the image file
   * @param imgName    the name of image in Model
   */
  void save(String savingPath, String imgName);

  /**
   * A method to split an image into R, G, B mode individually.
   *
   * @param from the name of image in Model
   */
  void rgbSplit(String from);

  /**
   * A method to combine three images into one, by taking the specified mode value.
   *
   * @param r the name of image in Model, take red value
   * @param g the name of image in Model, take green value
   * @param b the name of image in Model, take blue value
   */
  void rgbCombine(String r, String g, String b);

  /**
   * A method to adjust the brightness level of an image.
   *
   * @param level the brightness level to add to the current value, range: -255 ~ 255
   * @param from  the name of image in Model
   */
  void brighten(int level, String from);

  /**
   * A method to flip an image vertically.
   *
   * @param from the name of image in Model
   */
  void vflip(String from);

  /**
   * A method to flip an image horizontally.
   *
   * @param from the name of image in Model.
   */
  void hflip(String from);

  /**
   * A method to receive various command and do dynamic dispatch.
   * Commands accepted:
   * - colorTrans
   * - sepia
   * - blur
   * - sharpen
   * - dither
   *
   * @param command the command of effect
   * @param from    the name of image in Model
   * @throws IllegalArgumentException if command is undefined
   */
  void commandDispatcher(String command, String from) throws IllegalArgumentException;

}
