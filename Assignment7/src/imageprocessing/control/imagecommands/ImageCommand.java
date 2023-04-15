package imageprocessing.control.imagecommands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;


/**
 * This is an abstract class that implements IImageCommand.
 * It represents an ImageCommand Controller of the Command Design Pattern.
 */
public abstract class ImageCommand implements IImageCommand {

  protected final String sourceImageName;
  private final String destinationImageName;

  ImageCommand(String sourceImageName, String destinationImageName) {
    this.sourceImageName = sourceImageName;
    this.destinationImageName = destinationImageName;
  }

  @Override
  public List<String> getSourceImageName() {
    List<String> sourceImageNames = new ArrayList<>();
    sourceImageNames.add(this.sourceImageName);

    return sourceImageNames;
  }

  @Override
  public String getDestinationImageName() {
    return this.destinationImageName;
  }

  @Override
  public abstract IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws IOException, InvalidFileException;
}
