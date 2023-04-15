package imageprocessing.control.imagecommands;

import java.io.IOException;
import java.util.List;

import imageprocessing.control.IUserCommands;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;

/**
 * The interface represents the operations of an image command controller.
 */
public interface IImageCommand extends IUserCommands {
  /**
   * returns names of source images in the command.
   *
   * @return List of Strings - list of anmes of the source images.
   */
  @Override
  List<String> getSourceImageName();

  /**
   * returns name of the new image.
   *
   * @return String name of the new image.
   */
  @Override
  String getDestinationImageName();

  /**
   * returns an object of the model by calling the respective method for the command.
   *
   * @param sourceImageModels list of ImageModel objects that need to be operated on.
   * @return String name of the new image.
   * @throws IOException          - thrown in case there is an error while writing to the file
   * @throws InvalidFileException - thrown if the file extension provided is wrong
   */
  @Override
  IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws IOException, InvalidFileException;
}
