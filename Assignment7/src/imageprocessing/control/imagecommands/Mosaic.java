package imageprocessing.control.imagecommands;

import java.io.IOException;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;

public class Mosaic extends ImageCommand {
  private final int seed;

  /**
   * Build a mosaic command .
   *
   * @param sourceImageName      source image to process
   * @param destinationImageName new image new after process
   * @param seed                 the seed to apply the algorithm
   */
  public Mosaic(String sourceImageName, String destinationImageName, int seed) {
    super(sourceImageName, destinationImageName);
    this.seed = seed;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels) throws IOException, InvalidFileException {
    return sourceImageModels.get(0).mosaic(this.seed);
  }
}
