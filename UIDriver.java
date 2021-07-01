import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UIDriver
{
  static JFileChooser chooser = new JFileChooser();
  static FileNameExtensionFilter filter;

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      SwingUtilities.updateComponentTreeUI(chooser); } catch (Exception localException) {
    }
    JOptionPane.showMessageDialog(null, "Choose image file");
    BufferedImage read = getImage();

    switch (JOptionPane.showOptionDialog(null, "Encrypt or decrypt?", null, 0, 3, null, 
      new String[] { "Encrypt", "Decrypt" }, Integer.valueOf(0))) {
    case 0:
      JOptionPane.showMessageDialog(null, "Choose text file");
      try {
        new Steganographer(read, getText().toCharArray()).encrypt();
      } catch (NullPointerException e) {
        System.exit(0);
      }
    case 1:
      try
      {
        new Steganographer(read, null).decrypt();
      } catch (NullPointerException e) {
        System.exit(0);
      }
    }
  }

  public static String getText()
  {
    chooser.setFileFilter(new FileNameExtensionFilter("Txt text files", new String[] { "txt" }));
    File text = null;
    if (chooser.showOpenDialog(null) == 0)
      text = chooser.getSelectedFile();
    else
      System.exit(0);
    FileReader reader = null;
    try {
      reader = new FileReader(text);
    } catch (Exception e1) {
      JOptionPane.showMessageDialog(null, "File could not be found.", null, 0);
    }

    JFrame frame = new JFrame("Processing");
    JProgressBar progress = new JProgressBar();
    progress.setIndeterminate(true);
    frame.add(new JPanel().add(progress));
    frame.pack();
    frame.setSize(250, 80);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    String textString = "";
    try
    {
      int i;
      while ((i = reader.read()) != -1)
      {
        int i;
        textString = textString + (char)i;
      }frame.dispose();
      JOptionPane.showMessageDialog(frame, "Text processing complete");
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Error occured reading text file", null, 0);
    }
    return textString;
  }

  public static BufferedImage getImage() {
    chooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", new String[] { "jpg", "png" }));
    BufferedImage image = null;
    if (chooser.showOpenDialog(null) == 0)
      try {
        image = ImageIO.read(chooser.getSelectedFile());
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "File could not be found.", null, 0);
      }
    else
      System.exit(0);
    return image;
  }

  public static File getFile() {
    chooser.setFileFilter(new FileNameExtensionFilter("File", new String[] { "jpg", "png", "txt" }));
    if (chooser.showOpenDialog(null) == 0)
      return chooser.getSelectedFile();
    return null;
  }
}
