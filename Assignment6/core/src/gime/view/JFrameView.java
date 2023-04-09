package gime.view;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import gime.control.Features;
import gime.model.ReadOnlyImageProcessor;
import gime.model.ReadOnlyImageProcessorImpl;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFrameView extends JFrame implements IView {

  private final ReadOnlyImageProcessor processor;
  private Map<String, String> commandsMap = new LinkedHashMap<>();
  private JPanel topbar;
  private JPanel namep;
  private JPanel btnp;
  private JPanel histogramp;

  private JScrollPane scrollImage;
  private JLabel sImageLabel;

  private JButton addBtn;
  private JButton saveBtn;
  private JButton horizontalBtn;
  private JButton verticalBtn;

  private JButton splitBtn;
  private JButton combineBtn;

  private JComboBox<String> commandDD;
  private JComboBox<String> imagenameDD;
  private JComboBox<String> imagenameEffectDD;

  private Map<String, ArrayList<String>> processedImgNames;

  private void initCommandsMap() {
    commandsMap.put("Color Transform - Red Component", "colorTrans red-component");
    commandsMap.put("Color Transform - Green Component", "colorTrans green-component");
    commandsMap.put("Color Transform - Blue Component", "colorTrans blue-component");
    commandsMap.put("Color Transform - Value", "colorTrans value-component");
    commandsMap.put("Color Transform - Intensity", "colorTrans intensity-component");
    commandsMap.put("Color Transform - Luma", "colorTrans luma-component");
    commandsMap.put("Color Transform - Greyscale", "colorTrans greyscale");
    commandsMap.put("Color Transform - Sepia", "colorTrans sepia");
    commandsMap.put("Brighten", "brighten");
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


    commandDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    for (String key : commandsMap.keySet()) {
      commandDD.addItem(key);
    }
    commandDD.setSelectedIndex(-1);
    topbar.add(commandDD);

    namep = new JPanel();
    namep.setLayout(new BoxLayout(namep, BoxLayout.X_AXIS));
    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));

    imagenameDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    namep.add(imagenameDD);

    imagenameEffectDD = new JComboBox<>() {
      protected void fireActionEvent() {
        if (this.hasFocus()) {
          super.fireActionEvent();
        }
      }
    };
    namep.add(imagenameEffectDD);

    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));
    topbar.add(namep);

    btnp = new JPanel();
    btnp.setLayout(new BoxLayout(btnp, BoxLayout.X_AXIS));
    btnp.add(Box.createHorizontalGlue());

    splitBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/arrows.png"));
      splitBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    splitBtn.setOpaque(false);
    splitBtn.setFocusPainted(false);
    splitBtn.setBorderPainted(false);
    splitBtn.setContentAreaFilled(false);
    splitBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    splitBtn.setActionCommand("rgb-split");
    btnp.add(splitBtn);

    Dimension boxFiller = new Dimension(20, 30);
    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    combineBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/intersection.png"));
      combineBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    combineBtn.setOpaque(false);
    combineBtn.setFocusPainted(false);
    combineBtn.setBorderPainted(false);
    combineBtn.setContentAreaFilled(false);
    combineBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    combineBtn.setActionCommand("rgb-combine");
    btnp.add(combineBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    horizontalBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/horizontal-flip.png"));
      horizontalBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    horizontalBtn.setOpaque(false);
    horizontalBtn.setFocusPainted(false);
    horizontalBtn.setBorderPainted(false);
    horizontalBtn.setContentAreaFilled(false);
    horizontalBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(horizontalBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    verticalBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/vertical-flip.png"));
      verticalBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    verticalBtn.setOpaque(false);
    verticalBtn.setFocusPainted(false);
    verticalBtn.setBorderPainted(false);
    verticalBtn.setContentAreaFilled(false);
    verticalBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(verticalBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    addBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/add-new-50.png"));
      addBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    addBtn.setOpaque(false);
    addBtn.setFocusPainted(false);
    addBtn.setBorderPainted(false);
    addBtn.setContentAreaFilled(false);
    addBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(addBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));

    saveBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/save-50.png"));
      saveBtn.setIcon(new ImageIcon(img.getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      showDialog(JOptionPane.ERROR_MESSAGE, ex.getMessage());
    }
    saveBtn.setOpaque(false);
    saveBtn.setFocusPainted(false);
    saveBtn.setBorderPainted(false);
    saveBtn.setContentAreaFilled(false);
    saveBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    btnp.add(saveBtn);

    btnp.add(new Box.Filler(boxFiller, boxFiller, boxFiller));
    topbar.add(btnp);

    topbar.setPreferredSize(new Dimension(this.getWidth(), 75));

    sImageLabel = new JLabel();
    scrollImage = new JScrollPane(sImageLabel);
    scrollImage.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 75));

    histogramp = new JPanel();
    histogramp.add(new JLabel("Histogram Panel"));
    histogramp.setPreferredSize(new Dimension(this.getWidth() / 2, this.getHeight() - 75));

    this.add(topbar, BorderLayout.NORTH);
    this.add(scrollImage, BorderLayout.WEST);
    this.add(histogramp, BorderLayout.EAST);

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
        String green = (String) JOptionPane.showInputDialog(this, "Choose GREEN layer", "Combine Image",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        String blue = (String) JOptionPane.showInputDialog(this, "Choose BLUE layer", "Combine Image",
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        System.out.println(name + " " + green + " " + blue);
        features.rgbCombine(name, prefix + "-" + green, prefix + "-" + blue);
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      }
    });


    commandDD.addActionListener(evt -> {
      try {
        commandDispather(features);
      } catch (NullPointerException ope) {
        showDialog(JOptionPane.ERROR_MESSAGE, "Please load image before selecting an effect");
      } catch (Exception msg) {
        showDialog(JOptionPane.ERROR_MESSAGE, msg.getMessage());
      }
    });

    imagenameDD.addActionListener(evt -> {
      ArrayList<String> names = processedImgNames.get(imagenameDD.getSelectedItem().toString());
      if (names.contains(imagenameEffectDD.getSelectedItem().toString())) {
        showImage(getImgName());
      } else {
        showImage(imagenameDD.getSelectedItem().toString() + "-raw");
      }
    });

    imagenameEffectDD.addActionListener(evt -> showImage(getImgName()));

  }

  private String getImgName() {
    return imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem().toString();
  }

  private void commandDispather(Features features) {
//    System.out.println("Selected command: " + commandDD.getSelectedItem() + " " + imagenameDD.getSelectedItem().toString() + "-" + imagenameEffectDD.getSelectedItem().toString());
    System.out.println(commandDD.getSelectedItem().toString().split(" - ")[0]);

    String[] splited = commandDD.getSelectedItem().toString().split(" - ");


  }


  public void showImage(String name) {
    System.out.println(name);
    updateNameList();
    try {
      int[][][] imgList = this.processor.getImage(name);
      int[] imgInfo = this.processor.getInfo(name);
      BufferedImage convertedImg = convertImgToBufferImage(imgInfo, imgList);
      sImageLabel.setIcon(new ImageIcon(convertedImg));

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

  private void updateNameList() {
    String[] original = processor.getNameList();
    processedImgNames = new LinkedHashMap<>();
    for (String name : original) {
      String[] splited = name.split("-", 2);
      if (processedImgNames.containsKey(splited[0])) {
        ArrayList<String> tmp = processedImgNames.get(splited[0]);
        tmp.add(splited[1]);
      } else {
        ArrayList<String> tmp = new ArrayList<>();
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
    String name = (String) JOptionPane.showInputDialog(this, "Which image you want to see?", "Split Image",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
    showImage(name);
  }

}
