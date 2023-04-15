package imageprocessing.view;

/**
 * The Main View of the application that shows the user various outputs.
 */
public interface IImageView {
  /**
   * Tells the user to enter the appropriate type of input.
   */
  void showUserInputMessage();

  /**
   * Displays a message to the user once the program execution is completed.
   */
  void showOutput(String message);
}
