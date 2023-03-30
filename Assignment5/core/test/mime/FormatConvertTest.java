package mime;


import static org.junit.Assert.assertEquals;

import mime.model.MoreImageProcessor;

/**
 * This is a test class to generate other formats of source file cat.ppm.
 */
public class FormatConvertTest {

  /**
   * Compares two images pixel by pixel.
   *
   * @param imgA the first image.
   * @param imgB the second image.
   * @return whether the images are both the same or not.
   */
  private void compareImage(MoreImageProcessor model, String imgA, String imgB) {

    // The images must be the same size and maxi-value.
    int[] infoA = model.getInfo(imgA);
    int[] infoB = model.getInfo(imgB);
    for (int i = 0; i < infoA.length; i++) {
      assertEquals(infoA[i], infoB[i]);
    }

    int width = infoA[0];
    int height = infoA[1];

    int[][][] imgAContent = model.getImage(imgA);
    int[][][] imgBContent = model.getImage(imgB);

    // Loop over every pixel.
    for (int y = 0; y < height; y++) {
      try {
        for (int x = 0; x < width; x++) {
          for (int rgb = 0; rgb < 3; rgb++) {
            // Compare the pixels for equality.
            if (imgAContent[x][y][rgb] != imgBContent[x][y][rgb]) {
              assertEquals(imgAContent[x][y][rgb], imgBContent[x][y][rgb]);
            }
          }
        }
      } catch (AssertionError e) {
        System.out.println("Comparing: " + imgA + " " + imgB);
        throw e;
      }
    }
  }

//  @Test
//  public void Test() throws IOException {
//    String[] type = {"png", "bmp", "ppm"};
//
//    MoreImageProcessor model = null;
//    MoreImageCommand commandLoad = null;
//    MoreImageCommand commandSave = null;
//    for (int j = 0; j < type.length; j++) {
//      for (int i = 0; i < type.length; i++) {
//        model = new MoreImageProcessorImpl();
//        commandLoad = new LoadInputStream("res/format/cat." + type[i], "cat-" + type[i]);
//        commandLoad.execute(model);
//
//        commandSave = new SaveOutStream("res/mime/cat-" + type[i] + "." + type[j],
//            "cat-" + type[i]);
//        commandSave.execute(model);
//
//        commandLoad = new LoadInputStream("res/mime/cat-" + type[i] + "." + type[j],
//            "cat-" + type[j]);
//        commandLoad.execute(model);
//
//        compareImage(model, "cat-" + type[i], "cat-" + type[j]);
//      }
//    }
//  }


}
