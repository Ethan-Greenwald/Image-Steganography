import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class Steganographer
{
  private BufferedImage image;
  private char[] text;
  private String decryptedText;
  private int height;
  private int width;
  private JProgressBar progress;
  private JFrame frame;
  private JPanel panel;
  private JButton start;
  private JButton cancel;
  public boolean decrypting;
  public boolean encrypting;

  Steganographer(BufferedImage image, char[] text)
  {
    this.text = text;
    this.image = image;
    this.height = image.getHeight();
    this.width = image.getWidth();
  }

  public int[] getColors(int x, int y) {
    int rgb = this.image.getRGB(x, y);
    int red = (rgb & 0xFF0000) >> 16;
    int green = (rgb & 0xFF00) >> 8;
    int blue = rgb & 0xFF;
    return new int[] { red, green, blue };
  }

  private void encryptText() {
    String newColor = "";
    int curChar = 0;

    String binary = ""; String curBits = "";
    for (int row = 0; row < this.height; row++)
      for (int col = 0; col < this.width; col++) {
        int[] colors = getColors(col, row);
        if (curChar == this.text.length)
          return;
        if (binary.contentEquals(""))
          binary = toBin(this.text[(curChar++)]);
        newColor = toBin(colors[0]);
        curBits = binary.substring(0, 2);
        newColor = newColor.substring(0, 6) + curBits;
        int red = Integer.parseInt(newColor, 2);
        binary = binary.substring(2);
        if (curChar == this.text.length)
          return;
        if (binary.contentEquals(""))
          binary = toBin(this.text[(curChar++)]);
        newColor = toBin(colors[1]);
        curBits = binary.substring(0, 2);
        newColor = newColor.substring(0, 6) + curBits;
        int green = Integer.parseInt(newColor, 2);
        binary = binary.substring(2);
        if (curChar == this.text.length)
          return;
        if (binary.contentEquals(""))
          binary = toBin(this.text[(curChar++)]);
        newColor = toBin(colors[2]);
        curBits = binary.substring(0, 2);
        newColor = newColor.substring(0, 6) + curBits;
        int blue = Integer.parseInt(newColor, 2);
        binary = binary.substring(2);
        this.image.setRGB(col, row, toRGB(red, green, blue));
        this.progress.setValue(this.progress.getValue() + 1);
      }
  }

  private void decryptText()
  {
    int length = this.height * this.width;
    String text = ""; String temp = "";
    int count = 0;

    for (int row = 0; row < this.height; row++) {
      for (int col = 0; col < this.width; col++) {
        int[] rgb = getColors(col, row);

        String red = toBin(rgb[0]);
        temp = temp + red.substring(6);
        if (temp.length() == 8) {
          text = text + (char)Integer.parseInt(temp, 2);
          temp = "";
        }
        if ((!this.decrypting) || (count++ == length)) {
          this.decryptedText = text;
          return;
        }

        String green = toBin(rgb[1]);
        temp = temp + green.substring(6);
        if (temp.length() == 8) {
          text = text + (char)Integer.parseInt(temp, 2);
          temp = "";
        }
        if ((!this.decrypting) || (count++ == length)) {
          this.decryptedText = text;
          return;
        }

        String blue = toBin(rgb[2]);
        temp = temp + blue.substring(6);
        if (temp.length() == 8) {
          text = text + (char)Integer.parseInt(temp, 2);
          temp = "";
        }
        if ((!this.decrypting) || (count++ == length)) {
          this.decryptedText = text;
          return;
        }
        this.progress.setValue(this.progress.getValue() + 1);
      }
    }
    this.decryptedText = text;
    this.decrypting = false;
  }

  public void encrypt() throws NullPointerException
  {
    this.encrypting = false;
    this.start = new JButton("Start");
    this.start.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        Steganographer.this.encrypting = true;
      }
    });
    this.cancel = new JButton("Cancel");
    this.cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(Steganographer.this.frame, "Encryption cancelled");
        System.exit(0);
      }
    });
    updateProgressFrame("Encrypting");
    this.frame.setVisible(true);
    boolean validWrite = false;
    String write = null;
    do
      try {
        System.out.println();
        if (this.encrypting) {
          encryptText();
          this.frame.dispose();
          JOptionPane.showMessageDialog(null, "Encryption complete. Choose image write path: ");
          File file = UIDriver.getFile();
          write = file.getAbsolutePath();
          writeImage(file);
          validWrite = true;
        }
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "File could not be created.", null, 0);
        validWrite = false;
      }
    while (!validWrite);
    JOptionPane.showMessageDialog(null, "Encrypted image successfully written to \"" + write + "\"");
  }

  public void decrypt() throws NullPointerException {
    this.decrypting = false;
    this.start = new JButton("Start");
    this.start.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        Steganographer.this.decrypting = true;
      }
    });
    this.cancel = new JButton("Cancel");
    this.cancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e) {
        Steganographer.this.decrypting = false;
      }
    });
    updateProgressFrame("Decrypting");
    String write = null;
    boolean validWrite = false;
    do
      try {
        System.out.println();
        if (this.decrypting) {
          decryptText();
          this.frame.dispose();

          JOptionPane.showMessageDialog(null, "Decryption terminated. Choose text write path: ");
          write = writeText(UIDriver.getFile());
          validWrite = true;
        }
      } catch (IOException e) { JOptionPane.showMessageDialog(null, "File could not be created.", null, 0);
        validWrite = false;
      } catch (NullPointerException e) {
        JOptionPane.showMessageDialog(null, "File was not created.", null, 1);
        System.exit(0);
      }
    while (!validWrite);
    JOptionPane.showMessageDialog(null, "Decrypted text successfully written to \"" + write + "\"");
  }

  private void updateProgressFrame(String title) {
    this.panel = new JPanel();
    this.progress = new JProgressBar(0, this.height * this.width);
    this.progress.setStringPainted(true);
    this.frame = new JFrame(title);
    this.panel.add(this.progress);
    this.panel.add(this.start);
    this.panel.add(this.cancel);
    this.frame.add(this.panel);
    this.frame.setLocationRelativeTo(null);
    this.frame.pack();
    this.frame.setVisible(true);
  }

  private String writeText(File file) throws IOException, NullPointerException {
    String path = file.getAbsolutePath();
    BufferedWriter writer = new BufferedWriter(new FileWriter(path));
    writer.write(this.decryptedText);
    writer.close();
    return path;
  }

  private void writeImage(File file) throws IOException {
    ImageIO.write(this.image, "png", file);
  }

  private int toRGB(int red, int green, int blue) {
    return red * 65536 + green * 256 + blue;
  }

  private String toBin(int i) {
    return String.format("%8s", new Object[] { Integer.toBinaryString(i) }).replace(' ', '0');
  }
}
