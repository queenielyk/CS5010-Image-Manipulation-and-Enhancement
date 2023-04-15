package imageprocessing.control.imagecommands;

import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Dither class helps in dithering an image.
 * It calls the respective "dither" method of the model.
 */
public class Dither extends ImageCommand {

  /**
   * The constructor is used to initialise the member variables of the "dither" class. It
   * is created when the user passes the "Dither" command.
   *
   * @param sourceImageName      - name of the source image
   * @param destinationImageName - name of the destination image
   */
  public Dither(String sourceImageName, String destinationImageName) {
    super(sourceImageName, destinationImageName);
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) {
    return sourceImageModels.get(0).greyscale("luma").dither();
  }
}
