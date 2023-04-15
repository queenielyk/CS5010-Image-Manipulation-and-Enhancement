package imageprocessing.control;

import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import imageprocessing.control.exceptionhandler.ImageNotFoundException;
import imageprocessing.control.exceptionhandler.InvalidFileException;
import imageprocessing.control.exceptionhandler.InvalidInputException;
import imageprocessing.model.IImageModel;
import imageprocessing.model.IPixel;
import imageprocessing.model.ImageModel;
import imageprocessing.model.Pixel;
import imageprocessing.view.IImageView;

import static org.junit.Assert.assertEquals;

/**
 * tests Controller and Model.
 */
public class ControllerModelTest {
  private IController getInputs(IImageView view, InputStream in
          , String input, StringBuilder mocklog) {
    view = new MockImageView(System.out, mocklog);
    in = new ByteArrayInputStream(input.getBytes());
    return new Controller(view, in);
  }

  /**
   * Mocks the view.
   */
  public static class MockImageView implements IImageView {
    StringBuilder mockLog;
    private final PrintStream out;

    MockImageView(PrintStream out, StringBuilder mockLog) {
      this.mockLog = mockLog;
      this.out = out;
    }

    @Override
    public void showUserInputMessage() {
      //Overriding the method of IImageView interface for mocking
    }

    @Override
    public void showOutput(String message) {
      this.mockLog.append(message);
    }
  }

  private List<String> getParsedFile(String filePath) throws FileNotFoundException {
    Scanner scan;
    try {
      scan = new Scanner(new FileInputStream(filePath));
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException("File " + filePath + " not found!");
    }

    List<String> lines = new ArrayList<>();

    while (scan.hasNextLine()) {
      String s = scan.nextLine().trim();
      if (!s.equals("") && s.charAt(0) != '#') {
        lines.add(s);
      }
    }
    scan.close();

    return lines;
  }

  private IImageModel getImage(String filePath) throws IOException {
    int height;
    int width;
    int maxValue;
    int max;
    int red;
    int green;
    int blue;
    BufferedImage img;
    List<IPixel> image = new ArrayList<>();

    img = ImageIO.read(new File(filePath));

    height = img.getHeight();
    width = img.getWidth();
    maxValue = 0;

    for (int i = 0; i < height; ++i) {
      for (int j = 0; j < width; ++j) {
        Color c = new Color(img.getRGB(j, i));
        red = c.getRed();
        green = c.getGreen();
        blue = c.getBlue();

        max = Math.max(red, Math.max(green, blue));

        if (maxValue < max) {
          maxValue = max;
        }

        image.add(new Pixel(red, green, blue));
      }
    }
    return new ImageModel(height, width, maxValue, image);
  }

  @Test
  public void testInvalidCommand() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "invalid test.ppm test" + System.lineSeparator()
            + "Q";
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals("The command - invalid is Invalid.Successfully processed commands."
            , logs.toString());
  }

  @Test
  public void testFileWithInvalidCommand() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "run testCommandFiles/testWithBadCommand.txt"
            + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals("The command - brjsfub is Invalid.Successfully processed commands."
            , logs.toString());
  }

  @Test
  public void testImageNotFound() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "brighten 10 testInvalid test-brighter"
            + System.lineSeparator() + "q";
    IImageView view = null;
    InputStream in = null;
    IController c = getInputs(view, in, input, logs);
    c.goController();
    assertEquals("testinvalid image does not exist.Successfully processed commands."
            , logs.toString());
  }

  @Test
  public void testLoadPPM() throws InvalidInputException, InvalidFileException, IOException
          , ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test" + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "10", "12", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testLoadSameFile() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "load testPPMfolder/test.ppm testTwo" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm testTwo"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "10", "12", "13"));

    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testSaveTwice() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "brighten 10 test test-brighter" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-brighter" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-brighter"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testBrighten() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "brighten 10 test test-brighter" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-brighter"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testBrightenTwo() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "brighten -10 test test-brighter" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-brighter"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "0", "2", "3"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscale() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale red-component test test-red" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-red"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "10", "10", "10"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscaleGreen() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale green-component test test-green" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-green"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "12"
            , "12", "12"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscaleBlue() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale blue-component test test-blue" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-blue"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "13"
            , "13", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscaleValue() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale value-component test test-value" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-value"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "13"
            , "13", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscaleLuma() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale luma-component test test-luma" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-luma"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "11"
            , "11", "11"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testGreyscaleLIntensity() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "greyscale intensity-component test test-intensity" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-intensity"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "11"
            , "11", "11"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testHorizontalFlip() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/testHorizontalFlip.ppm test"
            + System.lineSeparator()
            + "horizontal-flip test test-h" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-h"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "2 1", "255", "15", "16", "17"
            , "10", "12", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testVerticalFlip() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/testVerticalFlip.ppm test"
            + System.lineSeparator()
            + "vertical-flip test test-v" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-v"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 2", "255", "15", "16", "17"
            , "10", "12", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testHorizontalVerticalFlip() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/testVerticalFlip.ppm test"
            + System.lineSeparator() + "horizontal-flip test test-h"
            + System.lineSeparator()
            + "vertical-flip test-h test-h-v" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm test-h-v"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 2", "255", "15", "16", "17"
            , "10", "12", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testSplit() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test"
            + System.lineSeparator() + "rgb-split test test-red test-green test-blue"
            + System.lineSeparator()
            + "save resultPPMfolder/test-red.ppm test-red" + System.lineSeparator()
            + "save resultPPMfolder/test-red.ppm test-red" + System.lineSeparator()
            + "save resultPPMfolder/test-green.ppm test-green" + System.lineSeparator()
            + "save resultPPMfolder/test-blue.ppm test-blue"
            + System.lineSeparator() + "Q";
    List<String> redResultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "10", "10", "10"));
    List<String> greenResultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "12", "12", "12"));
    List<String> blueResultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "13", "13", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(redResultFile, getParsedFile("resultPPMfolder/test-red.ppm"));
    assertEquals(greenResultFile, getParsedFile("resultPPMfolder/test-green.ppm"));
    assertEquals(blueResultFile, getParsedFile("resultPPMfolder/test-blue.ppm"));
  }

  @Test
  public void testCombine() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test-blue.ppm test-blue"
            + System.lineSeparator() + "load testPPMfolder/test-red.ppm test-red"
            + System.lineSeparator() + "load testPPMfolder/test-green.ppm test-green"
            + System.lineSeparator() + "rgb-combine result test-red test-green test-blue"
            + System.lineSeparator()
            + "save resultPPMfolder/result.ppm result"
            + System.lineSeparator() + "Q";
    List<String> result = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "10", "12", "13"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(result, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testRunFile() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "run testCommandFiles/commandsForTest.txt"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testRunTwice() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "run testCommandFiles/commandsForTest.txt"
            + System.lineSeparator() + "run testCommandFiles/commandsForTest.txt"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testCommandAndRun() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test"
            + System.lineSeparator() + "run testCommandFiles/commandsForTest.txt"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255"
            , "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testLoadPNG() throws InvalidInputException, InvalidFileException, IOException
          , ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPNGfolder/test.png test" + System.lineSeparator()
            + "save resultPNGfolder/result.png test" + System.lineSeparator() + "Q";

    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultPNGfolder/result.png");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(10, 12, 13));
    IImageModel expectedImage = new ImageModel(1, 1, 255, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadPNGAndSaveInPPM() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPNGfolder/test.png test" + System.lineSeparator()
            + "sepia test testsepiaed" + System.lineSeparator()
            + "save resultPPMfolder/result.ppm testsepiaed" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    List<String> expectedImage = new ArrayList<>(Arrays.asList(new String[]{"P3", "1 1", "13", "15"
            , "13", "10"}));
    assertEquals(expectedImage, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testLoadJPGAndSaveInPNG() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testJPGfolder/test.jpg test" + System.lineSeparator()
            + "dither test testDithered" + System.lineSeparator()
            + "save resultPNGfolder/result.png testDithered" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultPNGfolder/result.png");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(0, 0, 0));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadBMPAndSaveInJPG() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testBMPfolder/test.bmp test" + System.lineSeparator()
            + "blur test testBlurred" + System.lineSeparator()
            + "save resultJPGfolder/result.jpg testBlurred" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultJPGfolder/result.jpg");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(2, 4, 3));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadPPMAndSaveInBMP() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPPMfolder/test.ppm test" + System.lineSeparator()
            + "sharpen test testSharpen" + System.lineSeparator()
            + "save resultBMPfolder/result.bmp testSharpen" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultBMPfolder/result.bmp");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(10, 12, 13));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadJPGAndSaveInJPG() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testJPGfolder/test.jpg test" + System.lineSeparator()
            + "greyscale test testGreyed" + System.lineSeparator()
            + "save resultJPGfolder/result.jpg testGreyed" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultJPGfolder/result.jpg");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(11, 11, 11));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadPNGAndSaveInPNG() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testPNGfolder/test.png test" + System.lineSeparator()
            + "brighten 10 test testBrightened" + System.lineSeparator()
            + "save resultPNGfolder/result.png testBrightened" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultPNGfolder/result.png");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(20, 22, 23));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testLoadBMPAndSaveInBMP() throws InvalidInputException, InvalidFileException
          , IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "load testBMPfolder/test.bmp test" + System.lineSeparator()
            + "greyscale red-component test testRed" + System.lineSeparator()
            + "save resultBMPfolder/result.bmp testRed" + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    IController controller = getInputs(view, in, input, logs);
    controller.goController();
    IImageModel actualImage = getImage("resultBMPfolder/result.bmp");
    List<IPixel> expectedPixels = new ArrayList<>();
    expectedPixels.add(new Pixel(10, 10, 10));
    IImageModel expectedImage = new ImageModel(1, 1, 14, expectedPixels);
    assertEquals(true, actualImage.equals(expectedImage));
  }

  @Test
  public void testRunFileWithRun() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "run testCommandFiles/testWithRun.txt"
            + System.lineSeparator() + "Q";
    List<String> resultFile = new ArrayList<>(Arrays.asList("P3", "1 1", "255", "20", "22", "23"));
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals(resultFile, getParsedFile("resultPPMfolder/result.ppm"));
  }

  @Test
  public void testWrongCommandSyntax() throws InvalidInputException
          , InvalidFileException, IOException, ImageNotFoundException {
    StringBuilder logs = new StringBuilder();
    String input = "brighten test testbrighter"
            + System.lineSeparator() + "Q";
    IImageView view = null;
    InputStream in = null;
    getInputs(view, in, input, logs).goController();
    assertEquals("brighten command entered is wrong.Successfully processed commands."
            , logs.toString());
  }
}