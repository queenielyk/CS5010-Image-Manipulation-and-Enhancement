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

public class GraphicImageController implements IGraphicController, Features {

  private ImageProcessor model;
  private IView view;


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
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
    view.showImage(imgName);
  }

  @Override
  public void save(String SavingPath, String ImgName) {
    try {
      MoreImageCommand cmd = new SaveOutStream(SavingPath, ImgName);
      System.out.println();
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
      ImageCommand cmd = new RgbCombine(r + "-combine", r, g, b);
      cmd.execute(model);
      view.showImage(r + "-combine");
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void brighten(int level, String from, String to) {
    try {
      ImageCommand cmd = new Brighten(level, from, to);
      cmd.execute(model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void greyscale(String mode, String from, String to) {
    try {
      MoreImageCommand cmd = new ColorTrans(mode, from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void vflip(String from) {
    try {
      ImageCommand cmd = new Vflip(from, from + "-vflip");
      cmd.execute(model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
    view.showImage(from + "-vflip");
  }

  @Override
  public void hflip(String from) {
    try {
      ImageCommand cmd = new Hflip(from, from + "-hflip");
      cmd.execute(model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
    view.showImage(from + "-hflip");
  }

  @Override
  public void sepia(String from, String to) {
    try {
      MoreImageCommand cmd = new ColorTrans("sepia", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void blur(String from, String to) {
    try {
      MoreImageCommand cmd = new Filter("blur", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void sharpen(String from, String to) {
    try {
      MoreImageCommand cmd = new Filter("sharpen", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }

  @Override
  public void dither(String from, String to) {
    try {
      MoreImageCommand cmd = new Dither(from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showDialog(JOptionPane.ERROR_MESSAGE, error.getMessage());
    }
  }
}
