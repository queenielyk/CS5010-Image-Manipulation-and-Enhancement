package imageprocessing.control;

import java.io.IOException;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;

/**
 * Represents all the commands a user can enter, either Image Processing commands or
 * Load and Save commands.
 */
public interface IUserCommands {
  /**
   * Retrieves the source image name on which the new updated image functions on.
   *
   * @return - a list of all source images that the new image works on.
   */
  List<String> getSourceImageName();

  /**
   * Retrieves the destination image name, i.e. the name of the newly created image.
   *
   * @return - a string that is the name of the latest processed image
   */
  String getDestinationImageName();

  /**
   * Executes the image command entered by the user by calling the respective
   * command's method in the image model.
   *
   * @param sourceImageModels - the list of source images that the command needs to work on to
   *                          update the latest image.
   * @return - an instance of IImageModel i.e. an image.
   * @throws IOException          - throws an exception while executing the save command,
   *                              which writes to a file
   * @throws InvalidFileException - throws an exception if it is not of accepted file format
   *                              or if it is not in RGB format.
   */
  IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws InvalidFileException, IOException;
}
