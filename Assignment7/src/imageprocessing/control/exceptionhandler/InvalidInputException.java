package imageprocessing.control.exceptionhandler;

/**
 * Throws exception when the user inputs an invalid command.
 */
public class InvalidInputException extends Exception {
  /**
   * Exception handler that shows a message
   * to the user stating what the error is.
   *
   * @param message - Exception message
   */
  public InvalidInputException(String message) {
    super(message);
  }
}
