package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Horizontal Flip class that helps in flipping an image horizontally.
 * It calls the respective "horizontal-flip" method of the model.
 */
public class HorizontalFlip extends ImageCommand {

  /**
   * The constructor is used to initialise the member variables of the "HorizontalFlip" class. It
   * is created when the user passes the "Horizontal-flip" command.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   */
  public HorizontalFlip(String sourceImageName, String destinationImageName) {
    super(sourceImageName, destinationImageName);
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).horizontalFlip();
  }
}
