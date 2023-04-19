package imageprocessing.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import imageprocessing.control.exceptionhandler.ImageNotFoundException;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.exceptionhandler.InvalidInputException;
import imageprocessing.control.imagecommands.Brighten;
import imageprocessing.control.imagecommands.Combine;
import imageprocessing.control.imagecommands.Dither;
import imageprocessing.control.imagecommands.Filter;
import imageprocessing.control.imagecommands.Greyscale;
import imageprocessing.control.imagecommands.HorizontalFlip;
import imageprocessing.control.imagecommands.Mosaic;
import imageprocessing.control.imagecommands.Transformation;
import imageprocessing.control.imagecommands.VerticalFlip;
import imageprocessing.control.repository.ReadWriteImage;
import imageprocessing.control.repository.ReadWritePPM;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;

/**
 * This class represents LogicController. It is responsible for
 * parsing the user inputs and delegating work to model.
 */

public class LogicController {
  private final HashMap<String, IImageModel> images;
  private final Set<String> imageNames;

  LogicController() {
    images = new HashMap<>();
    imageNames = new HashSet<>();
  }

  void executeImageCommands(List<List<IUserCommands>> imageCommands)
          throws IOException, InvalidFileException, IllegalArgumentException {
    List<String> sourceImageNames;

    for (List<IUserCommands> commands : imageCommands) {
      for (IUserCommands command : commands) {
        List<IImageModel> sourceImageModels = new ArrayList<>();
        sourceImageNames = command.getSourceImageName();
        for (String sourceImageName : sourceImageNames) {
          sourceImageModels.add(images.get(sourceImageName));
        }
        images.put(command.getDestinationImageName(),
                command.executeCommand(sourceImageModels));
      }
    }
  }

  IImageModel getImageModel(String imageName) {
    imageName = imageName.toLowerCase();
    List<IPixel> tempImage = new ArrayList<>(images.get(imageName).getImage());
    int height = images.get(imageName).getHeight();
    int width = images.get(imageName).getWidth();
    int maxValue = images.get(imageName).getMaxValue();

    return new ImageModel(height, width, maxValue, tempImage);
  }

  List<IUserCommands> parseCommand(String command)
          throws IllegalArgumentException, InvalidInputException, ImageNotFoundException {
    String[] commandWords;
    command = command.toLowerCase();

    if (command.substring(0, command.indexOf(' ')).equalsIgnoreCase("load") ||
            command.substring(0, command.indexOf(' ')).equalsIgnoreCase("save")) {
      commandWords = new String[3];

      commandWords[0] = command.substring(0, command.indexOf(' '));
      commandWords[1] = command.substring(command.indexOf(' ') + 1, command.lastIndexOf(' '));
      commandWords[2] = command.substring(command.lastIndexOf(' ') + 1, command.length());
    } else {
      commandWords = command.split(" ");
    }

    return checkCommandType(commandWords);
  }

  private List<IUserCommands> checkCommandType(String[] commandWords)
          throws IllegalArgumentException, ImageNotFoundException, InvalidInputException {
    List<IUserCommands> commandType = new ArrayList<>();

    switch (commandWords[0]) {
      case "load":
        //load sample.ppm koala
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        imageNames.add(commandWords[2]);

        if (commandWords[1].contains(".ppm")) {
          commandType.add(new ReadWritePPM(commandWords[2], commandWords[1], commandWords[0]));
        } else {
          commandType.add(new ReadWriteImage(commandWords[2], commandWords[1], commandWords[0]));
        }

        break;
      case "save":
        //save koala.ppm koala
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[2])) {
          throw new ImageNotFoundException(commandWords[2] + " image does not exist.");
        }

        if (commandWords[1].contains(".ppm")) {
          commandType.add(new ReadWritePPM(commandWords[2], commandWords[1], commandWords[0]));
        } else {
          commandType.add(new ReadWriteImage(commandWords[2], commandWords[1], commandWords[0]));
        }

        break;
      case "brighten":
        //brighten 10 koala koala-brighter
        if (commandWords.length != 4) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[2])) {
          throw new ImageNotFoundException(commandWords[2] + " image does not exist.");
        }
        imageNames.add(commandWords[3]);

        commandType.add(new Brighten(commandWords[2], commandWords[3], commandWords[1]));

        break;
      case "greyscale":
        if (commandWords.length == 4) {
          //greyscale value-component koala koala-greyscale
          if (!imageNames.contains(commandWords[2])) {
            throw new ImageNotFoundException(commandWords[2] + " image does not exist.");
          }
          imageNames.add(commandWords[3]);

          commandType.add(new Greyscale(commandWords[2], commandWords[3],
                  commandWords[1].split("-")[0]));
        } else if (commandWords.length == 3) {
          //greyscale koala koala-greyscale
          if (!imageNames.contains(commandWords[1])) {
            throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
          }
          imageNames.add(commandWords[2]);

          commandType.add(new Transformation(commandWords[1], commandWords[2],
                  "luma"));
        } else {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }
        break;
      case "mosaic":
        if(commandWords.length != 4){
          //mosaic num-seeds source-image-name dest-image-name.
          throw new InvalidInputException(commandWords[0]+" command entered is wrong.");
        }
        if (!imageNames.contains(commandWords[2])) {
          throw new ImageNotFoundException(commandWords[2] + " image does not exist.");
        }
        imageNames.add(commandWords[3]);
        commandType.add(new Mosaic(commandWords[2],commandWords[3],Integer.valueOf(commandWords[1])));
        break;
      case "rgb-split":
        //rgb-split koala koala-red koala-green koala-blue
        if (commandWords.length != 5) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);
        imageNames.add(commandWords[3]);
        imageNames.add(commandWords[4]);

        commandType.add(new Greyscale(commandWords[1], commandWords[2],
                "red"));
        commandType.add(new Greyscale(commandWords[1], commandWords[3],
                "green"));
        commandType.add(new Greyscale(commandWords[1], commandWords[4],
                "blue"));

        break;
      case "horizontal-flip":
        //horizontal-flip koala koala-horizontal
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);

        commandType.add(new HorizontalFlip(commandWords[1], commandWords[2]));

        break;
      case "vertical-flip":
        //vertical-flip koala koala-vertical
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);

        commandType.add(new VerticalFlip(commandWords[1], commandWords[2]));

        break;
      case "rgb-combine":
        //rgb-combine koala-red-tint koala-red koala-green koala-blue
        if (commandWords.length != 5) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[2])) {
          throw new ImageNotFoundException(commandWords[2] + " image does not exist.");
        }
        if (!imageNames.contains(commandWords[3])) {
          throw new ImageNotFoundException(commandWords[3] + " image does not exist.");
        }
        if (!imageNames.contains(commandWords[4])) {
          throw new ImageNotFoundException(commandWords[4] + " image does not exist.");
        }
        imageNames.add(commandWords[1]);

        commandType.add(new Combine(commandWords[2], commandWords[3], commandWords[4],
                commandWords[1]));

        break;
      case "blur":
      case "sharpen":
        //blur koala koala-blurred -- sharpen koala koala-sharpened
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);

        commandType.add(new Filter(commandWords[1], commandWords[2], commandWords[0]));

        break;
      case "dither":
        //dither koala koala-dither
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);

        commandType.add(new Dither(commandWords[1], commandWords[2]));

        break;
      case "sepia":
        //sharpen koala koala-sepia
        if (commandWords.length != 3) {
          throw new InvalidInputException(commandWords[0] + " command entered is wrong.");
        }

        if (!imageNames.contains(commandWords[1])) {
          throw new ImageNotFoundException(commandWords[1] + " image does not exist.");
        }
        imageNames.add(commandWords[2]);

        commandType.add(new Transformation(commandWords[1], commandWords[2], "sepia"));

        break;
      default:
        throw new IllegalArgumentException("The command - " + commandWords[0] + " is Invalid.");
    }

    return commandType;
  }
}
