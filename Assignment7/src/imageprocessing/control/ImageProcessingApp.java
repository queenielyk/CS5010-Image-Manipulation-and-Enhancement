package imageprocessing.control;

import java.io.IOException;

import imageprocessing.control.exceptionhandler.ImageNotFoundException;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.exceptionhandler.InvalidInputException;
import imageprocessing.view.IImageGUIView;
import imageprocessing.view.IImageView;
import imageprocessing.view.ImageGUIView;
import imageprocessing.view.ImageView;

/**
 * This Class executes an Image processing application.
 */
public class ImageProcessingApp {

  /**
   * main method where the application execution starts.
   */
  public static void main(String[] args)
          throws InvalidInputException, InvalidFileException, IOException, ImageNotFoundException {
    IImageView view;
    IImageGUIView guiView;
    IController controller;

    if (args.length == 1 && args[0].equalsIgnoreCase("-text")) {
      view = new ImageView(System.out);
      controller = new Controller(view, System.in);
    } else if (args.length > 0) {
      view = new ImageView(System.out);
      controller = new Controller(view, args);
    } else {
      guiView = new ImageGUIView("Image Processing App");
      controller = new GUIController(guiView);
    }

    controller.goController();
  }
}
