package imageprocessing.control.imagecommands;

import java.util.ArrayList;
import java.util.List;

import imageprocessing.model.IImageModel;

/**
 * Combine class represents Combine command class. It has destinationImageName, sourceRedImageName,
 * sourceGreenImageName, sourceBlueImageName.
 */
public class Combine extends ImageCommand {
  private final String sourceGreenImageName;
  private final String sourceBlueImageName;

  /**
   * The constructor is used to initialise the member variables of the "Combine" class. It
   * is created when the user passes the "Combine" command.
   *
   * @param sourceRedImageName   - name of the red image
   * @param sourceGreenImageName - name of the green image
   * @param sourceBlueImageName  - name of the blue image
   * @param destinationImageName - destination image, i.e. image name of the newly created image
   */
  public Combine(String sourceRedImageName, String sourceGreenImageName,
                 String sourceBlueImageName, String destinationImageName) {
    super(sourceRedImageName, destinationImageName);
    this.sourceGreenImageName = sourceGreenImageName;
    this.sourceBlueImageName = sourceBlueImageName;
  }

  @Override
  public List<String> getSourceImageName() {
    List<String> sourceImageNames = new ArrayList<>();
    sourceImageNames.add(this.sourceImageName);
    sourceImageNames.add(this.sourceGreenImageName);
    sourceImageNames.add(this.sourceBlueImageName);

    return sourceImageNames;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws IllegalArgumentException {
    return sourceImageModels.get(0).combineRGB(sourceImageModels.get(1), sourceImageModels.get(2));
  }
}
