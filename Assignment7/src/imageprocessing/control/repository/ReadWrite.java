package imageprocessing.control.repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.model.IImageModel;

/**
 * This class implements IReadWrite. It is responsible for reading txt files.
 */

public class ReadWrite implements IReadWrite {
  protected final String fileOperations;
  protected final String filePath;
  private final String imageName;

  /**
   * Creates an object of readwrite, and instantiates its member variables.
   *
   * @param fileOperations - operation to be done on the image, i.e. either load or save
   * @param filePath       - path of the image file
   * @param imageName      - name of the image
   */
  ReadWrite(String fileOperations, String filePath, String imageName) {
    this.fileOperations = fileOperations;
    this.filePath = filePath;
    this.imageName = imageName;
  }

  /**
   * Creates an object of readwrite, and instantiates its member variables. Required for Run
   * command which does not have an image name.
   *
   * @param fileOperations - operation to be done on the image, i.e. either load or save
   * @param filePath       - path of the image file
   */
  public ReadWrite(String fileOperations, String filePath) {
    this.fileOperations = fileOperations;
    this.filePath = filePath;
    this.imageName = null;
  }

  @Override
  public List<String> getSourceImageName() {
    if (fileOperations.equalsIgnoreCase("load")) {
      return new ArrayList<>();
    }
    List<String> sourceImageNames = new ArrayList<>();
    sourceImageNames.add(this.imageName);

    return sourceImageNames;
  }

  @Override
  public String getDestinationImageName() {
    return this.imageName;
  }

  @Override
  public IImageModel executeCommand(List<IImageModel> sourceImageModels)
          throws IOException, InvalidFileException {
    if (fileOperations.equalsIgnoreCase("load")) {
      return this.readFromFile();
    } else {
      return this.saveToFile(sourceImageModels.get(0));
    }
  }

  @Override
  public IImageModel readFromFile()
          throws IOException, InvalidFileException {
    return null;
  }

  @Override
  public IImageModel saveToFile(IImageModel sourceImageModel)
          throws IOException, InvalidFileException {
    return null;
  }

  @Override
  public List<String> runFile() throws FileNotFoundException {
    List<String> lines = new ArrayList<>();
    Scanner scan;
    try {
      scan = new Scanner(new FileInputStream(filePath));
    }
    catch (FileNotFoundException e) {
      throw new FileNotFoundException("File " + filePath + " not found!");
    }

    while (scan.hasNextLine()) {
      String s = scan.nextLine().trim();
      if (!s.equals("") && s.charAt(0) != '#') {
        lines.add(s);
      }
    }

    scan.close();
    return lines;
  }
}
