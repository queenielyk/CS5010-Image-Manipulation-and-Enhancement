package imageprocessing.control.exceptionhandler;

/**
 * Throws an exception when the image is not found.
 */
public class ImageNotFoundException extends Exception {
  /**
   * Exception handler that shows a message
   * to the user stating what the error is.
   *
   * @param message - Exception message
   */
  public ImageNotFoundException(String message) {
    super(message);
  }
}
