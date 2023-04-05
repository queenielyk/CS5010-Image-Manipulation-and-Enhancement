package ime.control;

import ime.model.ImageProcessor;
import java.io.IOException;

/**
 * This interface represent a Command can be preformed on ImageProcessor Model.
 */
public interface ImageCommand {

  /**
   * delegate command to model's different method.
   *
   * @param model model to work on
   * @throws IOException if IO error occurs in model's method(ex. load can not find file)
   */
  void execute(ImageProcessor model) throws IOException;
}
