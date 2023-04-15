package imageprocessing.control;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import imageprocessing.control.exceptionhandler.ImageNotFoundException;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.exceptionhandler.InvalidInputException;
import imageprocessing.control.repository.IReadWrite;
import imageprocessing.control.repository.ReadWrite;
import imageprocessing.view.IImageView;

/**
 * The Controller class is where the program execution begins.
 * An instance of this class is created when we start the program.
 * It communicates with the model and view and accordingly
 * sends and receives the data.
 */
public class Controller implements IController {
  private final IImageView imageView;
  private final Scanner scan;
  private final List<List<IUserCommands>> userCommands;
  private final boolean isCommandLineArg;
  private final LogicController logicController;

  /**
   * Constructor to initialize the parameters of the controller.
   * Invoked if the user provides keyboard input.
   *
   * @param imageView - an object of the view class
   * @param in        - inputStream passed to the view to read the user input
   */
  public Controller(IImageView imageView, InputStream in) {
    this.imageView = imageView;
    this.scan = new Scanner(in);
    this.userCommands = new ArrayList<>();
    this.isCommandLineArg = false;
    this.logicController = new LogicController();
  }

  /**
   * Constructor to initialize the parameters of the controller.
   * Invoked if the user provides command line arguments.
   *
   * @param imageView - an object of the view class
   * @param args      - commandline arguments passed to the main method
   */
  public Controller(IImageView imageView, String[] args) throws InvalidInputException {
    if (args.length != 2 || !args[0].equals("-file")) {
      throw new InvalidInputException("Please enter the command line arguments in this format - "
              + "-file file_Name.txt");
    }

    this.imageView = imageView;
    this.scan = new Scanner(new ByteArrayInputStream(args[1].getBytes()));
    this.userCommands = new ArrayList<>();
    this.isCommandLineArg = true;
    this.logicController = new LogicController();
  }

  @Override
  public void goController() {
    try {
      if (this.isCommandLineArg) {
        runCommand("run " + scan.nextLine());
      } else {
        boolean takeInput = true;
        String userInput;

        while (takeInput) {
          this.imageView.showUserInputMessage();
          userInput = scan.nextLine();

          if (userInput.equalsIgnoreCase("q")) {
            takeInput = false;
          } else if (userInput.substring(0, 3).equalsIgnoreCase("run")) {
            runCommand(userInput);
          } else {
            processCommands(userInput);
          }
        }
      }

      if (userCommands.size() > 0) {
        logicController.executeImageCommands(userCommands);
      }

      this.imageView.showOutput("Successfully processed commands.");
      scan.close();
    } catch (ImageNotFoundException e) {
      imageView.showOutput(e.getMessage());
    } catch (InvalidInputException e) {
      imageView.showOutput(e.getMessage());
    } catch (InvalidFileException e) {
      imageView.showOutput(e.getMessage());
    } catch (FileNotFoundException e) {
      imageView.showOutput(e.getMessage());
    } catch (IOException e) {
      imageView.showOutput(e.getMessage());
    }
  }

  private void processCommands(String userInput) {
    try {
      userCommands.add(logicController.parseCommand(userInput));
    } catch (ImageNotFoundException e) {
      imageView.showOutput(e.getMessage());
    } catch (InvalidInputException e) {
      imageView.showOutput(e.getMessage());
    } catch (IllegalArgumentException e) {
      imageView.showOutput(e.getMessage());
    }
  }

  private void runCommand(String command)
          throws ImageNotFoundException, InvalidInputException,
          InvalidFileException, FileNotFoundException {
    try {
      String[] commandWords = command.split(" ");
      if (commandWords.length != 2) {
        throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
      }

      IReadWrite run = new ReadWrite(commandWords[0], commandWords[1]);
      List<String> commands = run.runFile();

      for (String c : commands) {
        if (c.substring(0, 3).equalsIgnoreCase("run")) {
          runCommand(c);
        } else {
          processCommands(c);
        }
      }
    } catch (InvalidInputException | FileNotFoundException e) {
      imageView.showOutput(e.getMessage());
    }
  }
}
