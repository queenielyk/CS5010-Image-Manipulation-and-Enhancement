package gime.control;

import gime.view.IView;
import gime.view.JFrameView;
import ime.model.ImageProcessor;
import mime.control.MoreImageController;

public class GraphicImageController extends MoreImageController implements IGraphicController,Features{
  private IView view;

  /**
   * Builder a {@link MoreImageController} and pass with In and Output stream.
   *
   * @param in  Input object
   * @param out Output object
   */
  public GraphicImageController(Readable in, Appendable out) {
    super(in, out);
  }


  public void runGUI(IView view,ImageProcessor model){
    this.model=model;
    this.view=view;
    view.addFeatures(this);
  }



}
