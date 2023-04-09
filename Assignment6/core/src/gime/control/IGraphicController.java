package gime.control;

import gime.view.IView;
import gime.view.JFrameView;
import ime.control.IController;
import ime.model.ImageProcessor;

public interface IGraphicController {
  void runGUI(IView view, ImageProcessor model);
}
