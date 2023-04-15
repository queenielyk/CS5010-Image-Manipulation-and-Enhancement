package imageprocessing.model;

import java.util.List;

/**
 * Interface that represents an 8 bit image.
 * An Image is represented as a List of Pixels.
 * Contains operations that you can perform on an image.
 */
public interface IImageModel {

  /**
   * Executes the "brighten" command.
   * Brightens/Darkens an image based on the intensity provided.
   *
   * @param increment - increment the brightness of the image by this amount
   * @return - a new brightened image object
   */
  IImageModel brightenImage(int increment);

  /**
   * Executes the "greyscale" command.
   * Converts a colour image into a greyscale image.
   *
   * @param component - the component that it uses as a reference. Can be
   *                  red/green/blue/value/intensity/luma
   * @return - a new greyscale image
   * @throws IllegalArgumentException - thrown if the component provided does not exist
   */
  IImageModel greyscale(String component)
          throws IllegalArgumentException;

  /**
   * Executes the "horizontalflip" command.
   * Flips an image horizontally.
   *
   * @return - a new horizontally flipped image
   */
  IImageModel horizontalFlip();

  /**
   * Executes the "verticalflip" command.
   * Flips an image vertically.
   *
   * @return - a new vertically flipped image
   */
  IImageModel verticalFlip();

  /**
   * Executes the "combine" command.
   * Combines 3 greyscale images into 1 colour image.
   *
   * @param sourceGreenModel - the greyscale image which provides the green channel
   * @param sourceBlueModel  - the greyscale image which provides the blue channel
   * @return - a new combined image
   * @throws IllegalArgumentException - thrown if the source images provided are invalid
   */
  IImageModel combineRGB(IImageModel sourceGreenModel, IImageModel sourceBlueModel)
          throws IllegalArgumentException;

  /**
   * Executes any command that requires a filter operation to be performed on the image.
   *
   * @param action - the type of filter that needs to be done (blur/sharpen)
   * @return - a new filtered image
   */
  IImageModel filter(String action);

  /**
   * Executes the dither command.
   * Converts an image into a dithered image.
   *
   * @return - a new dithered image
   */
  IImageModel dither();

  /**
   * Executes the sepia command and greyscale for luma.
   * Converts an image into sepia color.
   * Converts an image into greyscale image.
   *
   * @return - a new transformed image
   */
  IImageModel colorTransformations(String typeOfTransformation);

  /**
   * Getter - to get the height of an image object.
   *
   * @return int - the height of the image
   */
  int getHeight();

  /**
   * Getter - to get the width of an image object.
   *
   * @return - the width of the image
   */
  int getWidth();

  /**
   * Getter - to get the maximum pixel value of an image object.
   *
   * @return - the maximum pixel value of the image
   */
  int getMaxValue();

  /**
   * Getter - to get the image of an image object.
   *
   * @return - List of IPixel i.e. an image
   */
  List<IPixel> getImage();
}
