package control;

import java.io.FileNotFoundException;
import java.io.IOException;

import IME.model.ImageProcessor;

/**
 * This interface represent a control.ImageCommand can be preformed on ImageProcessor Model
 */
public interface ImageCommand {
  void go(ImageProcessor model) throws FileNotFoundException, IOException;
}
