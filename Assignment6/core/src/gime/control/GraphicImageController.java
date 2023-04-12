package gime.control;

import javax.swing.JOptionPane;

import gime.view.IView;
import ime.control.ImageCommand;
import ime.control.command.Brighten;
import ime.control.command.Hflip;
import ime.control.command.RgbCombine;
import ime.control.command.RgbSplit;
import ime.control.command.Vflip;
import ime.model.ImageProcessor;

import mime.control.MoreImageCommand;
import mime.control.command.ColorTrans;
import mime.control.command.Dither;
import mime.control.command.Filter;
import mime.control.command.LoadInputStream;
import mime.control.command.SaveOutStream;
import mime.model.MoreImageProcessor;

/**
 * A class to work as a Controller by implementing interface IGraphicController,
 * and interface Features.
 */
public class GraphicImageController implements IGraphicController, Features {

  private ImageProcessor model;
  private IView view;

  @Override
  public void runGUI(IView view, MoreImageProcessor model) {
    this.model = model;
    this.view = view;
    view.addFeatures(this);
  }


  //---------------------<<<<CALL BACK FUNCTIONS>>>>>>--------------------

  @Override
  public void loadImage(String path, String imgName) {
    imgName = imgName.replaceAll("-", "") + "-raw";
    try {
      MoreImageCommand cmd = new LoadInputStream(path, imgName);
      cmd.execute((MoreImageProcessor) model);
      view.updateShowing(imgName);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void save(String SavingPath, String ImgName) {
    try {
      MoreImageCommand cmd = new SaveOutStream(SavingPath, ImgName);
      cmd.execute((MoreImageProcessor) model);
      view.showDialog(JOptionPane.INFORMATION_MESSAGE, "Completed!");
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void rgbSplit(String from) {
    try {
      String[] rgb = new String[]{from + "-r", from + "-g", from + "-b"};
      ImageCommand cmd = new RgbSplit(from, rgb[0], rgb[1], rgb[2]);
      cmd.execute(model);
      view.dialogAskImgAfterSplit(rgb);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void rgbCombine(String r, String g, String b) {
    try {
      String name = r + "-combine";
      ImageCommand cmd = new RgbCombine(name, r, g, b);
      cmd.execute(model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void brighten(int level, String from) {
    try {
      String name = from + "-" + (level < 0 ? "darken" : "brighten");
      ImageCommand cmd = new Brighten(level, from, name);
      cmd.execute(model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  private void colorTrans(String mode, String from) {
    try {
      String name = from + "-" + mode.split("-")[0];
      MoreImageCommand cmd = new ColorTrans(mode, from, name);
      cmd.execute((MoreImageProcessor) model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void vflip(String from) {
    try {
      String name = from + "-vflip";
      ImageCommand cmd = new Vflip(from, name);
      cmd.execute(model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void hflip(String from) {
    try {
      String name = from + "-hflip";
      ImageCommand cmd = new Hflip(from, name);
      cmd.execute(model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  private void filter(String mode, String from) {
    try {
      String name = from + "-" + mode;
      MoreImageCommand cmd = new Filter(mode, from, name);
      cmd.execute((MoreImageProcessor) model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  private void dither(String from) {
    String name = from + "-dither";
    try {
      MoreImageCommand cmd = new Dither(from, name);
      cmd.execute((MoreImageProcessor) model);
      view.updateShowing(name);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void commandDispatcher(String command, String from) {
    switch (command.split(" ")[0]) {
      case "colorTrans":
        colorTrans(command.split(" ")[1], from);
        break;
      case "sepia":
        colorTrans(command, from);
        break;
      case "blur":
      case "sharpen":
        filter(command, from);
        break;
      case "dither":
        dither(from);
        break;
      default:
        throw new IllegalArgumentException("Action undefined");
    }
  }

}
