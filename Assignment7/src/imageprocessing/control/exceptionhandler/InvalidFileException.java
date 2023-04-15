package imageprocessing.control.exceptionhandler;

/**
 * Throws an exception when the file to be parsed is of an invalid type.
 */
public class InvalidFileException extends Exception {
  /**
   * Exception handler that shows a message
   * to the user stating what the error is.
   *
   * @param message - Exception message
   */
  public InvalidFileException(String message) {
    super(message);
  }
}
