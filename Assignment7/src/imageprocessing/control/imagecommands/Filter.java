package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Filter class helps in filtering an image based on the filter provided.
 * It calls the respective "filter" method of the model.
 */
public class Filter extends ImageCommand {
  private final String action;

  /**
   * The constructor is used to initialise the member variables of the "Filter" class. It
   * is created when the user passes the transformation commands.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   * @param action               - the type of filter
   */
  public Filter(String sourceImageName, String destinationImageName, String action) {
    super(sourceImageName, destinationImageName);
    this.action = action;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).filter(action);
  }
}
