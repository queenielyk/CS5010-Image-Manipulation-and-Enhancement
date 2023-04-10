package gime.view;

import gime.control.Features;
import gime.model.ReadOnlyImageProcessor;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFrameView extends JFrame implements IView {

  private final ReadOnlyImageProcessor processor;
  private Map<String, String> commandsMap = new LinkedHashMap<>();
  private JPanel topbar;
  private JPanel namep;
  private JPanel btnp;
  private JPanel histoPanel;

  private Histogram histogram;

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

  public JFrameView(String caption, ReadOnlyImageProcessor processor) {
    super(caption);
    this.processor = processor;
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

    sImageLabel = new JLabel();
    imgp = new JPanel(new GridBagLayout());
    imgp.add(sImageLabel);
    scrollImage = new JScrollPane(imgp);
    scrollImage.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 75));

    // ----------------------------------Histogram---------------------
    histoPanel = new JPanel();
    histoPanel.setLayout(new BorderLayout());
    histogram = new Histogram();
    histoPanel.setPreferredSize(new Dimension(this.getWidth() / 2-50, this.getHeight() - 75));
    histoPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(30,30,30,30),"Histogram"));

    histoPanel.add(histogram, BorderLayout.CENTER);

    this.add(topbar, BorderLayout.NORTH);
    this.add(scrollImage, BorderLayout.WEST);
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
            "Combine Image",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String blue = (String) JOptionPane.showInputDialog(this, "Choose BLUE layer",
            "Combine Image",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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
        showImage(getImgName());
      } else {
        showImage(imagenameDD.getSelectedItem().toString() + "-raw");
      }
    });

    imagenameEffectDD.addActionListener(evt -> showImage(getImgName()));

  }

  private String getImgName() {
    return imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem()
        .toString();
  }


  public void showImage(String name) {
    updateNameList();
    try {
      int[][][] imgList = this.processor.getImage(name);
      int[] imgInfo = this.processor.getInfo(name);
      BufferedImage convertedImg = convertImgToBufferImage(imgInfo, imgList);
      sImageLabel.setIcon(new ImageIcon(convertedImg));
      histogram.showHistogram(name);
      String beforeName = imagenameDD.getSelectedItem().toString();
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

  private class Histogram extends JPanel {

    int[][][] image;
    int[] info;

    public void showHistogram(String imgName) {
      image = processor.getImage(imgName);
      info = processor.getInfo(imgName);
      repaint();
    }

    private int[] getCount(String mode) {
      int[] count = new int[256];
      switch (mode) {
        case "R":
          for (int row = 0; row < info[1]; row++) {
            for (int col = 0; col < info[0]; col++) {
              count[image[row][col][0]]++;
            }
          }
          break;
        case "G":
          for (int row = 0; row < info[1]; row++) {
            for (int col = 0; col < info[0]; col++) {
              count[image[row][col][1]]++;
            }
          }
          break;
        case "B":
          for (int row = 0; row < info[1]; row++) {
            for (int col = 0; col < info[0]; col++) {
              count[image[row][col][2]]++;
            }
          }
          break;
        case "I":
          int intensity = -1;
          for (int row = 0; row < info[1]; row++) {
            for (int col = 0; col < info[0]; col++) {
              if (image[row][col][0] == image[row][col][1]
                  && image[row][col][1] == image[row][col][2]) {
                intensity = image[row][col][0];
              } else {
                intensity = (int) (image[row][col][0] * 0.2126 + image[row][col][1] * 0.7152+image[row][col][2] * 0.0722);
              }
              count[intensity]++;
            }
          }
          break;
      }

      return count;
    }

    private void drawLine(int[] count){

    }
    @Override
    protected void paintComponent(Graphics g) {

      if (image == null || info == null) {
        return;
      }

      super.paintComponent(g);
      int boarder=50;
      int w = getWidth();
      int h = getHeight();
      int redCount[] = getCount("R");
      int greenCount[] = getCount("G");
      int blueCount[] = getCount("B");
      int intensityCount[] = getCount("I");

      //getMaxHeight
      int maxCount = 0;
      for (int i = 0; i <= 255; i++) {
        maxCount = Math.max(redCount[i], maxCount);
        maxCount = Math.max(greenCount[i], maxCount);
        maxCount = Math.max(blueCount[i], maxCount);
        maxCount = Math.max(intensityCount[i], maxCount);
      }

      int interval = (int) (w / 255.0);

      for (int i = 0; i < 255; i++) {
        //Draw RedLine
        g.setColor(Color.RED);
        g.drawLine(i * interval, (int) (h - (double) redCount[i] / (double) maxCount * h),
            (i + 1) * interval,
            (int) (h - (double) redCount[i + 1] / (double) maxCount * h));

        //Draw GreenLine
        g.setColor(Color.GREEN);
        g.drawLine(i * interval, (int) (h - (double) greenCount[i] / (double) maxCount * h),
            (i + 1) * interval,
            (int) (h - (double) greenCount[i + 1] / (double) maxCount * h));

        //Draw BlueLine
        g.setColor(Color.blue);
        g.drawLine(i * interval, (int) (h - (double) blueCount[i] / (double) maxCount * h),
            (i + 1) * interval,
            (int) (h - (double) blueCount[i + 1] / (double) maxCount * h));

        //Draw Intensity Line
        g.setColor(Color.black);
        g.drawLine(i * interval, (int) (h - (double) intensityCount[i] / (double) maxCount * h),
            (i + 1) * interval,
            (int) (h - (double) intensityCount[i + 1] / (double) maxCount * h));

        System.out.println("looping");
      }
      System.out.println("doneDrawing");

    }


  }

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

  private BufferedImage convertImgToBufferImage(int[] info, int[][][] image) {
    BufferedImage buffImage = new BufferedImage(info[0], info[1], BufferedImage.TYPE_INT_RGB);
    int[] rgb;
    for (int row = 0; row < info[1]; row++) {
      for (int col = 0; col < info[0]; col++) {
        rgb = image[row][col];
        Color c = new Color(rgb[0], rgb[1], rgb[2]);
        buffImage.setRGB(col, row, c.getRGB());
      }
    }
    return buffImage;
  }

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

  public void dialogAskImgAfterSplit(String[] options) {
    String name = (String) JOptionPane.showInputDialog(this, "Which image you want to see?",
        "Split Image",
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    showImage(name);
  }


}
