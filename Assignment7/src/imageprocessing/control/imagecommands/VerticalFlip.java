package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Vertical Flip class that helps in flipping an image vertically.
 * It calls the respective "Vertical-flip" method of the model.
 */
public class VerticalFlip extends ImageCommand {

  /**
   * The constructor is used to initialise the member variables of the "VerticalFlip" class. It is
   * used when the user enters the "Vertical-flip" command.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   */
  public VerticalFlip(String sourceImageName, String destinationImageName) {
    super(sourceImageName, destinationImageName);
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).verticalFlip();
  }
}
