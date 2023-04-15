package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Transformation class helps in transforming an image based on the transformation action provided.
 * It calls the respective "transformation" method of the model.
 */
public class Transformation extends ImageCommand {
  private final String action;

  /**
   * The constructor is used to initialise the member variables of the "Transformation" class. It
   * is created when the user passes the transformation commands.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   * @param action               - the type of filter
   */
  public Transformation(String sourceImageName, String destinationImageName, String action) {
    super(sourceImageName, destinationImageName);
    this.action = action;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).colorTransformations(action);
  }
}
