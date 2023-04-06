package ime.control;

import ime.model.ImageProcessor;
import java.io.IOException;

/**
 * The controller interface for the ImageProcess program. The functions here have been designed to
 * give control to the controller, and the primary operation for the controller to function (process
 * an image process command).
 */
public interface IController {

  /**
   * Process a given string command line and return status or error as a string.
   *
   * @param command the command given, including any parameters (e.g. "load images/koala.ppm
   *                koala")
   * @return status or error message
   * @throws IOException if script file can not be found or error occurs in ImageCommand.go()
   */
  String processCommand(String command) throws IOException;

  /**
   * Start the program, pass in the model and hand over the control.
   *
   * @param model Model to work with
   * @throws IOException if output is not appendable or got from processCommand
   */
  void run(ImageProcessor model) throws IOException;
}



