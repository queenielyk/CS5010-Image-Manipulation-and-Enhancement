package gime.view;

import gime.control.Features;
import gime.control.GraphicImageController;
import javax.swing.JFrame;

public class JFrameView extends JFrame implements IView{

  public JFrameView(String caption) {
    super(caption);
  }


  @Override
  public void addFeatures(Features features) {

  }
}
