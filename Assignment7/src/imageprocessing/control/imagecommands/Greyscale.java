package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Greyscale class that helps in converting a color image to a greyscale image.
 * It calls the respective "greyscale" method of the model.
 */
public class Greyscale extends ImageCommand {
  private final String component;

  /**
   * The constructor is used to initialise the member variables of the "Greyscale" class. It
   * is created when the user passes the "Greyscale" command.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   * @param component            - name of the component/channel that is converted to greyscale
   */
  public Greyscale(String sourceImageName, String destinationImageName, String component) {
    super(sourceImageName, destinationImageName);
    this.component = component;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws IllegalArgumentException {
    return sourceImageModels.get(0).greyscale(this.component);
  }
}
