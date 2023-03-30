package mime.control.command;

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
   * @throws IOException If IO error occurs in model’s method(ex. load can not find file)
   */
  void execute(MoreImageProcessor model) throws IOException;
}
