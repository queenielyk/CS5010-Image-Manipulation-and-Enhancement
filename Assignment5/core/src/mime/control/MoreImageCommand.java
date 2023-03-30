package mime.control;

import java.io.IOException;

import mime.model.MoreImageProcessor;

/**
 * This interface represent a Command can be preformed on MoreImageProcessor Model.
 */
public interface MoreImageCommand {

  /**
   * delegate command to model's different method.
   *
   * @param model model to work on
   * @throws IOException If IO error occurs when reading/writing the image
   */
  void execute(MoreImageProcessor model) throws IOException;
}
