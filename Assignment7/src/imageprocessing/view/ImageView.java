package imageprocessing.view;

import java.io.PrintStream;

/**
 * Class that represents a VIEW. Displays message to the user.
 */
public class ImageView implements IImageView {
  private final PrintStream out;

  /**
   * create an object of ImageView.
   *
   * @param out an object of PrintStream
   */
  public ImageView(PrintStream out) {
    this.out = out;
  }

  @Override
  public void showUserInputMessage() {
    out.println("Enter image command (to execute and quit the program enter q): ");
  }

  @Override
  public void showOutput(String message) {
    out.println(message);
  }
}
