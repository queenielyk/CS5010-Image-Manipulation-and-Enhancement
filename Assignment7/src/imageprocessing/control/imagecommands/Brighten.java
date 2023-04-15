package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Brighten class represents brighten command class. It has increment.
 */
public class Brighten extends ImageCommand {
  private final int increment;

  /**
   * The constructor is used to initialise the member variables of the "Brighten" class. It
   * is created when the user passes the "Brighten" command.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   * @param increment            - increment/decrement pixel value
   */
  public Brighten(String sourceImageName, String destinationImageName, String increment) {
    super(sourceImageName, destinationImageName);
    this.increment = Integer.parseInt(increment);
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).brightenImage(this.increment);
  }
}
