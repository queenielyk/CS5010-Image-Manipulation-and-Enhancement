package gime;

import static org.junit.Assert.assertEquals;

import gime.control.Features;
import gime.control.GraphicImageController;
import gime.control.IGraphicController;
import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;
import gime.view.IView;
import java.util.Arrays;
import mime.MoreImageControllerTest.MoreMockModel;
import mime.model.MoreImageProcessor;
import org.junit.Before;
import org.junit.Test;


/**
 * This is a test class for {@link GraphicImageController}
 */
public class GraphicImageControllerTest {

  private StringBuilder modelLog ;
  private StringBuilder viewLog;
  private IGraphicController ctrl;
  private MoreImageProcessor processor;
  private IView view;

  @Before
  public void init() {
    modelLog = new StringBuilder();
    viewLog = new StringBuilder();
    ctrl = new GraphicImageController();
    processor = new MoreMockModel(modelLog);
    view = new MockView(viewLog, "GIME APP", new ReadOnlyImageProcessorImpl(processor));
  }


  private static class  MockView implements IView {

    protected final StringBuilder log;

    private MockView(StringBuilder log, String caption, ReadOnlyImageProcessor model) {
      this.log = log;
    }

    @Override
    public void addFeatures(Features features) {
      log.append("Controller:" + features + "\n");
    }

    @Override
    public void updateShowing(String name) {
      log.append("name:" + name + "\n");
    }

    @Override
    public void showDialog(int type, String msg) {
      log.append("type:" + type + " " + "msg:" + msg + "\n");
    }

    @Override
    public void dialogAskImgAfterSplit(String[] options) {
      log.append("options:" + Arrays.deepToString(options) + "\n");
    }
  }

  @Test
  public void loadImageTestValid() {
    ctrl.runGUI(view, processor);
    ctrl.loadImage("res/cat.ppm", "cat");
    assertEquals("Controller:" + ctrl + "\n" + "name:cat-raw\n", viewLog.toString());
    assertEquals("Info:[3, 3, 255] "
        + "Image:[[[234, 232, 236], [209, 194, 193], [168, 150, 148]], "
        + "[[234, 230, 231], [194, 184, 187], [116, 99, 101]], "
        + "[[211, 203, 206], [170, 150, 150], [70, 42, 43]]] "
        + "name:cat-raw\n",modelLog.toString());
  }

  @Test
  public void loadImageTestInvalid() {
    ctrl.runGUI(view, processor);
    ctrl.loadImage("res/cat.xxx", "cat");

    assertEquals("Controller:" + ctrl + "\n"
        + "type:0 msg:Handler do not support:xxxformat\n"
        , viewLog.toString());
    assertEquals("",modelLog.toString());
  }

  @Test
  public void saveImageTest(){
    ctrl.runGUI(view, processor);
    ctrl.save("res/cat.ppm", "cat");
    assertEquals("Controller:" + ctrl + "\n"
            + "type:1 msg:Completed!\n"
        , viewLog.toString());
    assertEquals("Name: cat\n"
        + "Name: cat\n",modelLog.toString());
  }


  @Test
  public void rgbSplitTestValid(){
    ctrl.runGUI(view, processor);
    ctrl.loadImage("res/cat.ppm", "cat");
    ctrl.rgbSplit("cat-raw");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:cat-raw\n"
            + "options:[cat-raw-r, cat-raw-g, cat-raw-b]\n"
        , viewLog.toString());
    assertEquals("Info:[3, 3, 255] "
        + "Image:[[[234, 232, 236], [209, 194, 193], [168, 150, 148]], "
        + "[[234, 230, 231], [194, 184, 187], [116, 99, 101]], "
        + "[[211, 203, 206], [170, 150, 150], [70, 42, 43]]] "
        + "name:cat-raw\n"
        + "Mode:red-component From:cat-raw To:cat-raw-r\n"
        + "Mode:green-component From:cat-raw To:cat-raw-g\n"
        + "Mode:blue-component From:cat-raw To:cat-raw-b\n",modelLog.toString());
  }

  @Test
  public void rgbCombineTest(){
    ctrl.runGUI(view, processor);
    ctrl.rgbCombine("red","green","blue");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:red-combine\n"
        , viewLog.toString());
    assertEquals("R:red G:green B:blue To:red-combine\n",modelLog.toString());
  }

  @Test
  public void brightenTest(){
    ctrl.runGUI(view, processor);
    ctrl.brighten(50,"cat");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:cat-brighten\n"
        , viewLog.toString());
    assertEquals("From:cat Add:50 To:cat-brighten\n",modelLog.toString());
  }

  @Test
  public void vflipTest(){
    ctrl.runGUI(view, processor);
    ctrl.vflip("cat");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:cat-vflip\n"
        , viewLog.toString());
    assertEquals("From:cat To:cat-vflip\n",modelLog.toString());
  }

  @Test
  public void hflipTest(){
    ctrl.runGUI(view, processor);
    ctrl.hflip("cat");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:cat-hflip\n"
        , viewLog.toString());
    assertEquals("From:cat To:cat-hflip\n",modelLog.toString());
  }

  @Test
  public void commandTest(){
    ctrl.runGUI(view, processor);
    ctrl.commandDispatcher("sepia","cat");
    assertEquals("Controller:" + ctrl + "\n"
            + "name:cat-sepia\n"
        , viewLog.toString());
    assertEquals("Mode:sepia From:cat To:cat-sepia\n",modelLog.toString());
  }

}