package gime.control;

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
      view.showErrorDialog(error.getMessage());
    }
    view.showImage(imgName);
    view.updateNameList(imgName);
  }

  @Override
  public void save(String SavingPath, String ImgName) {
    try {
      MoreImageCommand cmd = new SaveOutStream(SavingPath, ImgName);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void rgbSplit(String from, String r, String g, String b) {
    try {
      ImageCommand cmd = new RgbSplit(from, r, g, b);
      cmd.execute(model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void rgbCombine(String to, String r, String g, String b) {
    try {
      ImageCommand cmd = new RgbCombine(to, r, g, b);
      cmd.execute(model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void brighten(int level, String from, String to) {
    try {
      ImageCommand cmd = new Brighten(level, from, to);
      cmd.execute(model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void greyscale(String mode, String from, String to) {
    try {
      MoreImageCommand cmd = new ColorTrans(mode, from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void vflip(String from, String to) {
    try {
      ImageCommand cmd = new Vflip(from, to);
      cmd.execute(model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void hflip(String from, String to) {
    try {
      ImageCommand cmd = new Hflip(from, to);
      cmd.execute(model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void sepia(String from, String to) {
    try {
      MoreImageCommand cmd = new ColorTrans("sepia", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void blur(String from, String to) {
    try {
      MoreImageCommand cmd = new Filter("blur", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void sharpen(String from, String to) {
    try {
      MoreImageCommand cmd = new Filter("sharpen", from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }

  @Override
  public void dither(String from, String to) {
    try {
      MoreImageCommand cmd = new Dither(from, to);
      cmd.execute((MoreImageProcessor) model);
    } catch (Exception error) {
      view.showErrorDialog(error.getMessage());
    }
  }
}
