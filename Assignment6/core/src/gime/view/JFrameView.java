package gime.view;

import gime.control.Features;
import gime.model.ReadOnlyImageProcessor;
import mime.model.BufferImageConverter;
import mime.model.BufferImageConverterImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * A class to implement the View (GUI) by using Swing.
 * Also implement methods indicated at interface IView to allow Controller invoke a new view.
 * This GUI provides effects, e.g. Color Transform, GreyScale, Blur, Sharpen, Dither, etc.
 * User is able to load image save the image if the format is supported: bmp, jpg, jpeg, png, ppm.
 * The View also shows the histogram along with the image.
 */
public class JFrameView extends JFrame implements IView {

  private final ReadOnlyImageProcessor processor;
  private Map<String, String> commandsMap = new LinkedHashMap<>();
  private JPanel topbar;
  private JPanel namep;
  private JPanel btnp;
  private JPanel histoPanel;

  private Histogram histogram;

  private JPanel imageGap;
  private JPanel imgp;
  private JScrollPane scrollImage;
  private JLabel sImageLabel;

  private JButton addBtn;
  private JButton saveBtn;
  private JButton horizontalBtn;
  private JButton verticalBtn;

  private JButton splitBtn;
  private JButton combineBtn;
  private JButton brightnessBtn;

  private JComboBox<String> commandDD;
  private JComboBox<String> imagenameDD;
  private JComboBox<String> imagenameEffectDD;

  private Map<String, List<String>> processedImgNames;

  private BufferImageConverter biConverter;

  /**
   * A private helper method to initial the mapping between each dropdown option and actual command.
   */
  private void initCommandsMap() {
    commandsMap.put("Color Transform - Red Component", "colorTrans red-component");
    commandsMap.put("Color Transform - Green Component", "colorTrans green-component");
    commandsMap.put("Color Transform - Blue Component", "colorTrans blue-component");
    commandsMap.put("Color Transform - Value", "colorTrans value-component");
    commandsMap.put("Color Transform - Intensity", "colorTrans intensity-component");
    commandsMap.put("Color Transform - Luma", "colorTrans luma-component");
    commandsMap.put("Color Transform - Greyscale", "colorTrans luma-component");
    commandsMap.put("Color Transform - Sepia", "sepia");
    commandsMap.put("Blur", "blur");
    commandsMap.put("Sharpen", "sharpen");
    commandsMap.put("Dither", "dither");
  }

  /**
   * A constructor to construct a View, setting the title as the caption and have a read-only model.
   *
   * @param caption   the title of this GUI
   * @param processor a read-only model
   */
  public JFrameView(String caption, ReadOnlyImageProcessor processor) {
    super(caption);
    this.processor = processor;
    this.biConverter = new BufferImageConverterImpl();
    initCommandsMap();

    setPreferredSize(new Dimension(1750, 900));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    pack();

    this.setLayout(new BorderLayout());

    topbar = new JPanel();
    topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));

    commandDD = new CustomJComboBox();
    for (String key : commandsMap.keySet()) {
      commandDD.addItem(key);
    }
    commandDD.setSelectedIndex(-1);
    topbar.add(commandDD);

    namep = new JPanel();
    namep.setLayout(new BoxLayout(namep, BoxLayout.X_AXIS));
    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));

    imagenameDD = new CustomJComboBox();
    namep.add(imagenameDD);

    imagenameEffectDD = new CustomJComboBox();
    namep.add(imagenameEffectDD);

    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));
    topbar.add(namep);

    btnp = new JPanel();
    btnp.setLayout(new BoxLayout(btnp, BoxLayout.X_AXIS));
    btnp.add(Box.createHorizontalGlue());

    try {
      brightnessBtn = new CustomJButton("resources/brightness.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(brightnessBtn);

    Dimension boxFiller = new Dimension(20, 30);
    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      splitBtn = new CustomJButton("resources/arrows.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(splitBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      combineBtn = new CustomJButton("resources/intersection.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(combineBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      horizontalBtn = new CustomJButton("resources/horizontal-flip.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(horizontalBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      verticalBtn = new CustomJButton("resources/vertical-flip.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(verticalBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      addBtn = new CustomJButton("resources/add-new-50.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(addBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    try {
      saveBtn = new CustomJButton("resources/save-50.png");
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    btnp.add(saveBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));
    topbar.add(btnp);

    topbar.setPreferredSize(new Dimension(this.getWidth(), 75));

    imageGap = new JPanel();
    imageGap.setPreferredSize(new Dimension(20, this.getHeight() - 75));

    sImageLabel = new JLabel();
    imgp = new JPanel(new GridBagLayout());
    imgp.add(sImageLabel);
    scrollImage = new JScrollPane(imgp);
    scrollImage.setPreferredSize(new Dimension((this.getWidth() - 20) / 2, this.getHeight() - 75));
    scrollImage.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

    // ----------------------------------Histogram---------------------
    histoPanel = new JPanel();
    histoPanel.setLayout(new BorderLayout());
    histogram = new Histogram();
    histoPanel.setPreferredSize(new Dimension(this.getWidth() / 2 - 20, this.getHeight() - 75));
    histoPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(30, 30, 30, 30), "Histogram"));

    histoPanel.add(histogram, BorderLayout.CENTER);

    this.add(topbar, BorderLayout.NORTH);
    this.add(imageGap, BorderLayout.WEST);
    this.add(scrollImage, BorderLayout.CENTER);
    this.add(histoPanel, BorderLayout.EAST);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }


  @Override
  public void addFeatures(Features features) {

    horizontalBtn.addActionListener(evt -> {
      try {
        features.hflip(getImgName());
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      } catch (Exception msg) {
        showDialog(JOptionPane.ERROR_MESSAGE, msg.getMessage());

      }
    });

    verticalBtn.addActionListener(evt -> {
      try {
        features.vflip(getImgName());
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      } catch (Exception msg) {
        showDialog(JOptionPane.ERROR_MESSAGE, msg.getMessage());
      }
    });

    addBtn.addActionListener(evt -> {
      JFileChooser addChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
              "BMP, JPG, JPEG, PNG, PPM Images", "jpg", "jpeg", "bmp", "png", "ppm");
      addChooser.setFileFilter(filter);
      int selected = addChooser.showOpenDialog(JFrameView.this);
      if (selected == JFileChooser.APPROVE_OPTION) {
        File selectedFile = addChooser.getSelectedFile();
        features.loadImage(selectedFile.getAbsolutePath(), selectedFile.getName());
      }
    });

    saveBtn.addActionListener(evt -> {
      try {
        String name = getImgName();
        JFileChooser saveChooser = new JFileChooser(".");
        int selected = saveChooser.showSaveDialog(JFrameView.this);
        if (selected == JFileChooser.APPROVE_OPTION) {
          File selectedFile = saveChooser.getSelectedFile();
          features.save(selectedFile.getAbsolutePath(), name);
        }
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      }
    });

    splitBtn.addActionListener(evt -> {
      try {
        String name = getImgName();
        features.rgbSplit(name);
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      }
    });

    combineBtn.addActionListener(evt -> {
      try {
        String name = getImgName();
        String prefix = imagenameDD.getSelectedItem().toString();
        String[] options = processedImgNames.get(prefix).toArray(new String[0]);
        String green = (String) JOptionPane.showInputDialog(this, "Choose GREEN layer",
                "Combine Image", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String blue = (String) JOptionPane.showInputDialog(this, "Choose BLUE layer",
                "Combine Image", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        features.rgbCombine(name, prefix + "-" + green, prefix + "-" + blue);
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      }
    });

    brightnessBtn.addActionListener(evt -> {
      try {
        String name = getImgName();
        String response = (String) JOptionPane.showInputDialog(this,
                "Brightness/Darkness level (-255 ~ 255)", "Brighten/Darken Image",
                JOptionPane.QUESTION_MESSAGE, null, null, '0');

        int degree = Integer.parseInt(response);
        if (degree < -255 || degree > 255) {
          showDialog(JOptionPane.ERROR_MESSAGE, "Input level not in range");
          return;
        }
        features.brighten(degree, name);
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      }
    });

    commandDD.addActionListener(evt -> {
      try {
        features.commandDispatcher(commandsMap.get(commandDD.getSelectedItem().toString()),
                getImgName());
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
        commandDD.setSelectedIndex(-1);
      } catch (Exception msg) {
        showDialog(JOptionPane.ERROR_MESSAGE, msg.getMessage());
        commandDD.setSelectedIndex(-1);
      }
    });

    imagenameDD.addActionListener(evt -> {
      List<String> names = processedImgNames.get(imagenameDD.getSelectedItem().toString());
      if (names.contains(imagenameEffectDD.getSelectedItem().toString())) {
        updateShowing(getImgName());
      } else {
        updateShowing(imagenameDD.getSelectedItem().toString() + "-raw");
      }
    });

    imagenameEffectDD.addActionListener(evt -> updateShowing(getImgName()));

  }

  /**
   * A private helper method to get the name of image to be shown.
   *
   * @return name of image to be shown
   */
  private String getImgName() {
    return imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem()
            .toString();
  }

  @Override
  public void updateShowing(String name) {
    showImage(name);
    histogram.showHistogram(name, processor);
  }


  private void showImage(String name) {
    updateNameList();
    try {
      int[][][] imgList = this.processor.getImage(name);
      int[] imgInfo = this.processor.getInfo(name);
      BufferedImage convertedImg = biConverter.convertToBufferImg(imgInfo, imgList);
      sImageLabel.setIcon(new ImageIcon(convertedImg));
      String[] splited = name.split("-", 2);
      imagenameDD.setSelectedItem(splited[0]);

      imagenameEffectDD.removeAllItems();
      for (String effectname : processedImgNames.get(splited[0])) {
        imagenameEffectDD.addItem(effectname);
        if (effectname.equals(splited[1])) {
          imagenameEffectDD.setSelectedItem(effectname);
        }
      }

    } catch (NoSuchElementException nse) {
      showDialog(JOptionPane.ERROR_MESSAGE, nse.getMessage());
    }
  }


  /**
   * A private helper method to update the name list so the dropdown options are up-to-date.
   */
  private void updateNameList() {
    String[] original = processor.getNameList();
    processedImgNames = new LinkedHashMap<>();
    for (String name : original) {
      String[] splited = name.split("-", 2);
      if (processedImgNames.containsKey(splited[0])) {
        List<String> tmp = processedImgNames.get(splited[0]);
        tmp.add(splited[1]);
      } else {
        List<String> tmp = new ArrayList<>();
        tmp.add(splited[1]);
        processedImgNames.put(splited[0], tmp);
      }
    }

    imagenameDD.removeAllItems();
    for (String name : processedImgNames.keySet()) {
      imagenameDD.addItem(name);
    }
  }

  @Override
  public void showDialog(int type, String msg) {
    String title = "";
    switch (type) {
      case JOptionPane.INFORMATION_MESSAGE:
        title = "Action Info";
        break;
      case JOptionPane.ERROR_MESSAGE:
        title = "Action Incomplete";
        break;
    }
    JOptionPane.showMessageDialog(this, msg, title, type);
  }

  @Override
  public void dialogAskImgAfterSplit(String[] options) {
    String name = (String) JOptionPane.showInputDialog(this, "Which image you want to see?",
            "Split Image", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    updateShowing(name);
  }


}
