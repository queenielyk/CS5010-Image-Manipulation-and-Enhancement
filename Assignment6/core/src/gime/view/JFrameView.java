package gime.view;

import java.awt.*;
import java.awt.event.ActionEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import gime.control.Features;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JFrameView extends JFrame implements IView {

  private Map<String, String> commandsMap = new HashMap<>();
  private JPanel topbar;
  private JPanel namep;
  private JPanel btnp;
  private JPanel imagep;
  private JPanel histogramp;

  private JButton addBtn;
  private JButton saveBtn;

  private JComboBox<String> commandDD;
  private JComboBox<String> imagenameDD;
  private JComboBox<String> imagenameEffectDD;

  private void initCommandsMap() {
    commandsMap.put("Color Transform - Red Component", "colorTrans red-component");
    commandsMap.put("Color Transform - Green Component", "colorTrans green-component");
  }

  public JFrameView(String caption) {
    super(caption);
    initCommandsMap();

    setPreferredSize(new Dimension(1750, 900));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();

    this.setLayout(new BorderLayout());

    topbar = new JPanel();
    topbar.setLayout(new BoxLayout(topbar, BoxLayout.X_AXIS));


    commandDD = new JComboBox<>();
    for (String key : commandsMap.keySet()) {
      commandDD.addItem(key);
    }
    topbar.add(commandDD);

    namep = new JPanel();
    namep.setLayout(new BoxLayout(namep, BoxLayout.X_AXIS));
    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));

    imagenameDD = new JComboBox<>();
    imagenameDD.addItem("res/cat");
    namep.add(imagenameDD);

    imagenameEffectDD = new JComboBox<>();
    imagenameEffectDD.addItem("dithering");
    namep.add(imagenameEffectDD);

    namep.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));
    topbar.add(namep);

    btnp = new JPanel();
    btnp.setLayout(new BoxLayout(btnp, BoxLayout.X_AXIS));
    btnp.add(Box.createHorizontalGlue());


    addBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/add-new-50.png"));
      addBtn.setIcon(new ImageIcon(img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    addBtn.setOpaque(false);
    addBtn.setFocusPainted(false);
    addBtn.setBorderPainted(false);
    addBtn.setContentAreaFilled(false);
    addBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    addBtn.setActionCommand("addImage");
    btnp.add(addBtn);

    btnp.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));

    saveBtn = new JButton();
    try {
      Image img = ImageIO.read(getClass().getResource("resources/save-50.png"));
      saveBtn.setIcon(new ImageIcon(img.getScaledInstance(25, 25, java.awt.Image.SCALE_SMOOTH)));
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
    saveBtn.setOpaque(false);
    saveBtn.setFocusPainted(false);
    saveBtn.setBorderPainted(false);
    saveBtn.setContentAreaFilled(false);
    saveBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Especially important
    saveBtn.setActionCommand("saveImage");
    btnp.add(saveBtn);

    btnp.add(new Box.Filler(new Dimension(20, 25), new Dimension(20, 25), new Dimension(20, 25)));
    topbar.add(btnp);

    topbar.setBackground(Color.orange);
    topbar.setPreferredSize(new Dimension(this.getWidth(), 75));

    imagep = new JPanel();
    imagep.add(new JLabel("Image Panel"));
    imagep.setBackground(Color.pink);
    imagep.setPreferredSize(new Dimension(this.getWidth() / 2, 750 - 75));

    histogramp = new JPanel();
    histogramp.add(new JLabel("Histogram Panel"));
    histogramp.setBackground(Color.yellow);
    histogramp.setPreferredSize(new Dimension(this.getWidth() / 2, 750 - 75));

    this.add(topbar, BorderLayout.NORTH);
    this.add(imagep, BorderLayout.WEST);
    this.add(histogramp, BorderLayout.EAST);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }


  @Override
  public void addFeatures(Features features) {
    addBtn.addActionListener(evt -> {
      JFileChooser addChooser = new JFileChooser(".");
      FileNameExtensionFilter filter = new FileNameExtensionFilter(
              "BMP, JPG, JPEG, PNG, PPM Images", "jpg", "jpeg", "bmp", "png", "ppm");
      addChooser.setFileFilter(filter);
      int selected = addChooser.showOpenDialog(JFrameView.this);
      if (selected == JFileChooser.APPROVE_OPTION) {
        File selectedFile = addChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      }
    });

    saveBtn.addActionListener(evt -> {
      JFileChooser saveChooser = new JFileChooser(".");
      int selected = saveChooser.showSaveDialog(JFrameView.this);
      if (selected == JFileChooser.APPROVE_OPTION) {
        File selectedFile = saveChooser.getSelectedFile();
        System.out.println("Selected file: " + selectedFile.getAbsolutePath());
      }
    });


    commandDD.addActionListener(evt -> {
      System.out.println("Selected command: " + commandDD.getSelectedItem());
    });

    imagenameDD.addActionListener(evt -> {
      System.out.println("Selected command: " + imagenameDD.getSelectedItem());
    });

    imagenameEffectDD.addActionListener(evt -> {
      System.out.println("Selected command: " + imagenameEffectDD.getSelectedItem());
    });


  }
}
