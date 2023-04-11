package gime.control;

import gime.view.IView;
import mime.model.MoreImageProcessor;

/**
 * An interface to indicate an IGraphicController actions.
 */
public interface IGraphicController {

  /**
   * A method to execute the whole program.
   *
   * @param view  a View
   * @param model a read-only model
   */
  void runGUI(IView view, MoreImageProcessor model);
}
