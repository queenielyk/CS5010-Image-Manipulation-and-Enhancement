package control;

import java.io.IOException;

import IME.model.ImageProcessor;

/**
 * This interface represent a Command can be preformed on ImageProcessor Model
 */
public interface ImageCommand {
  void go(ImageProcessor model) throws IOException;
}
