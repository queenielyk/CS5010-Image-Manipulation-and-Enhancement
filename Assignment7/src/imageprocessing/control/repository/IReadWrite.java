package imageprocessing.control.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import imageprocessing.control.IUserCommands;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;

/**
 * This interface is only used to read and write data to a repository.
 */
public interface IReadWrite extends IUserCommands {
  /**
   * Method to read data from a file.
   *
   * @return - an image model converted from the file
   * @throws IOException          - thrown if the file is not found at the specified path
   * @throws InvalidFileException - thrown if the file extension provided is wrong
   */
  IImageModel readFromFile()
          throws IOException, InvalidFileException;

  /**
   * Method to save/write the content to a file.
   *
   * @param sourceImageModel - the image that needs to be saved to a file
   * @return - an image model of the saved image
   * @throws IOException          - thrown in case there is an error while writing to the file
   * @throws InvalidFileException - thrown if the file extension provided is wrong
   */
  IImageModel saveToFile(IImageModel sourceImageModel)
          throws IOException, InvalidFileException;

  /**
   * Reads the entire file line by line.
   *
   * @return - a list of all the lines (string) in the file
   * @throws FileNotFoundException - thrown in case there is an error while writing to the file
   * @throws InvalidFileException  - thrown if the file extension provided is wrong
   */
  List<String> runFile()
          throws FileNotFoundException, InvalidFileException;
}
