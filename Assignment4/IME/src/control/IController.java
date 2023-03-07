package control;

import java.io.IOException;

import IME.model.ImageProcessor;

/**
 * The controller interface for the ImageProcess program. The functions here have been designed to
 * give control to the controller, and the primary operation for the controller to function (process
 * a image process control.command)
 */
public interface IController {

  /**
   * Process a given string control.command and return status or error as a string
   *
   * @param command the control.command given, including any parameters (e.g. "load images/koala.ppm
   *                koala")
   * @return status or error message
   */
  String processCommand(String command) throws IOException;

  /**
   * Start the program, i.e. give control to the controller
   */
  void go(ImageProcessor model) throws IOException;
}



