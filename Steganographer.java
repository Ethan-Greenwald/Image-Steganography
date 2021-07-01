/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ 
/*     */ public class Steganographer
/*     */ {
/*     */   private BufferedImage image;
/*     */   private char[] text;
/*     */   private String decryptedText;
/*     */   private int height;
/*     */   private int width;
/*     */   private JProgressBar progress;
/*     */   private JFrame frame;
/*     */   private JPanel panel;
/*     */   private JButton start;
/*     */   private JButton cancel;
/*     */   public boolean decrypting;
/*     */   public boolean encrypting;
/*     */ 
/*     */   Steganographer(BufferedImage image, char[] text)
/*     */   {
/*  30 */     this.text = text;
/*  31 */     this.image = image;
/*  32 */     this.height = image.getHeight();
/*  33 */     this.width = image.getWidth();
/*     */   }
/*     */ 
/*     */   public int[] getColors(int x, int y) {
/*  37 */     int rgb = this.image.getRGB(x, y);
/*  38 */     int red = (rgb & 0xFF0000) >> 16;
/*  39 */     int green = (rgb & 0xFF00) >> 8;
/*  40 */     int blue = rgb & 0xFF;
/*  41 */     return new int[] { red, green, blue };
/*     */   }
/*     */ 
/*     */   private void encryptText() {
/*  45 */     String newColor = "";
/*  46 */     int curChar = 0;
/*     */ 
/*  49 */     String binary = ""; String curBits = "";
/*  50 */     for (int row = 0; row < this.height; row++)
/*  51 */       for (int col = 0; col < this.width; col++) {
/*  52 */         int[] colors = getColors(col, row);
/*  53 */         if (curChar == this.text.length)
/*  54 */           return;
/*  55 */         if (binary.contentEquals(""))
/*  56 */           binary = toBin(this.text[(curChar++)]);
/*  57 */         newColor = toBin(colors[0]);
/*  58 */         curBits = binary.substring(0, 2);
/*  59 */         newColor = newColor.substring(0, 6) + curBits;
/*  60 */         int red = Integer.parseInt(newColor, 2);
/*  61 */         binary = binary.substring(2);
/*  62 */         if (curChar == this.text.length)
/*  63 */           return;
/*  64 */         if (binary.contentEquals(""))
/*  65 */           binary = toBin(this.text[(curChar++)]);
/*  66 */         newColor = toBin(colors[1]);
/*  67 */         curBits = binary.substring(0, 2);
/*  68 */         newColor = newColor.substring(0, 6) + curBits;
/*  69 */         int green = Integer.parseInt(newColor, 2);
/*  70 */         binary = binary.substring(2);
/*  71 */         if (curChar == this.text.length)
/*  72 */           return;
/*  73 */         if (binary.contentEquals(""))
/*  74 */           binary = toBin(this.text[(curChar++)]);
/*  75 */         newColor = toBin(colors[2]);
/*  76 */         curBits = binary.substring(0, 2);
/*  77 */         newColor = newColor.substring(0, 6) + curBits;
/*  78 */         int blue = Integer.parseInt(newColor, 2);
/*  79 */         binary = binary.substring(2);
/*  80 */         this.image.setRGB(col, row, toRGB(red, green, blue));
/*  81 */         this.progress.setValue(this.progress.getValue() + 1);
/*     */       }
/*     */   }
/*     */ 
/*     */   private void decryptText()
/*     */   {
/*  87 */     int length = this.height * this.width;
/*  88 */     String text = ""; String temp = "";
/*  89 */     int count = 0;
/*     */ 
/*  91 */     for (int row = 0; row < this.height; row++) {
/*  92 */       for (int col = 0; col < this.width; col++) {
/*  93 */         int[] rgb = getColors(col, row);
/*     */ 
/*  95 */         String red = toBin(rgb[0]);
/*  96 */         temp = temp + red.substring(6);
/*  97 */         if (temp.length() == 8) {
/*  98 */           text = text + (char)Integer.parseInt(temp, 2);
/*  99 */           temp = "";
/*     */         }
/* 101 */         if ((!this.decrypting) || (count++ == length)) {
/* 102 */           this.decryptedText = text;
/* 103 */           return;
/*     */         }
/*     */ 
/* 106 */         String green = toBin(rgb[1]);
/* 107 */         temp = temp + green.substring(6);
/* 108 */         if (temp.length() == 8) {
/* 109 */           text = text + (char)Integer.parseInt(temp, 2);
/* 110 */           temp = "";
/*     */         }
/* 112 */         if ((!this.decrypting) || (count++ == length)) {
/* 113 */           this.decryptedText = text;
/* 114 */           return;
/*     */         }
/*     */ 
/* 117 */         String blue = toBin(rgb[2]);
/* 118 */         temp = temp + blue.substring(6);
/* 119 */         if (temp.length() == 8) {
/* 120 */           text = text + (char)Integer.parseInt(temp, 2);
/* 121 */           temp = "";
/*     */         }
/* 123 */         if ((!this.decrypting) || (count++ == length)) {
/* 124 */           this.decryptedText = text;
/* 125 */           return;
/*     */         }
/* 127 */         this.progress.setValue(this.progress.getValue() + 1);
/*     */       }
/*     */     }
/* 130 */     this.decryptedText = text;
/* 131 */     this.decrypting = false;
/*     */   }
/*     */ 
/*     */   public void encrypt() throws NullPointerException
/*     */   {
/* 136 */     this.encrypting = false;
/* 137 */     this.start = new JButton("Start");
/* 138 */     this.start.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 141 */         Steganographer.this.encrypting = true;
/*     */       }
/*     */     });
/* 144 */     this.cancel = new JButton("Cancel");
/* 145 */     this.cancel.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 148 */         JOptionPane.showMessageDialog(Steganographer.this.frame, "Encryption cancelled");
/* 149 */         System.exit(0);
/*     */       }
/*     */     });
/* 152 */     updateProgressFrame("Encrypting");
/* 153 */     this.frame.setVisible(true);
/* 154 */     boolean validWrite = false;
/* 155 */     String write = null;
/*     */     do
/*     */       try {
/* 158 */         System.out.println();
/* 159 */         if (this.encrypting) {
/* 160 */           encryptText();
/* 161 */           this.frame.dispose();
/* 162 */           JOptionPane.showMessageDialog(null, "Encryption complete. Choose image write path: ");
/* 163 */           File file = UIDriver.getFile();
/* 164 */           write = file.getAbsolutePath();
/* 165 */           writeImage(file);
/* 166 */           validWrite = true;
/*     */         }
/*     */       } catch (IOException e) {
/* 169 */         JOptionPane.showMessageDialog(null, "File could not be created.", null, 0);
/* 170 */         validWrite = false;
/*     */       }
/* 172 */     while (!validWrite);
/* 173 */     JOptionPane.showMessageDialog(null, "Encrypted image successfully written to \"" + write + "\"");
/*     */   }
/*     */ 
/*     */   public void decrypt() throws NullPointerException {
/* 177 */     this.decrypting = false;
/* 178 */     this.start = new JButton("Start");
/* 179 */     this.start.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 182 */         Steganographer.this.decrypting = true;
/*     */       }
/*     */     });
/* 185 */     this.cancel = new JButton("Cancel");
/* 186 */     this.cancel.addActionListener(new ActionListener()
/*     */     {
/*     */       public void actionPerformed(ActionEvent e) {
/* 189 */         Steganographer.this.decrypting = false;
/*     */       }
/*     */     });
/* 192 */     updateProgressFrame("Decrypting");
/* 193 */     String write = null;
/* 194 */     boolean validWrite = false;
/*     */     do
/*     */       try {
/* 197 */         System.out.println();
/* 198 */         if (this.decrypting) {
/* 199 */           decryptText();
/* 200 */           this.frame.dispose();
/*     */ 
/* 203 */           JOptionPane.showMessageDialog(null, "Decryption terminated. Choose text write path: ");
/* 204 */           write = writeText(UIDriver.getFile());
/* 205 */           validWrite = true;
/*     */         }
/*     */       } catch (IOException e) { JOptionPane.showMessageDialog(null, "File could not be created.", null, 0);
/* 208 */         validWrite = false;
/*     */       } catch (NullPointerException e) {
/* 210 */         JOptionPane.showMessageDialog(null, "File was not created.", null, 1);
/* 211 */         System.exit(0);
/*     */       }
/* 213 */     while (!validWrite);
/* 214 */     JOptionPane.showMessageDialog(null, "Decrypted text successfully written to \"" + write + "\"");
/*     */   }
/*     */ 
/*     */   private void updateProgressFrame(String title) {
/* 218 */     this.panel = new JPanel();
/* 219 */     this.progress = new JProgressBar(0, this.height * this.width);
/* 220 */     this.progress.setStringPainted(true);
/* 221 */     this.frame = new JFrame(title);
/* 222 */     this.panel.add(this.progress);
/* 223 */     this.panel.add(this.start);
/* 224 */     this.panel.add(this.cancel);
/* 225 */     this.frame.add(this.panel);
/* 226 */     this.frame.setLocationRelativeTo(null);
/* 227 */     this.frame.pack();
/* 228 */     this.frame.setVisible(true);
/*     */   }
/*     */ 
/*     */   private String writeText(File file) throws IOException, NullPointerException {
/* 232 */     String path = file.getAbsolutePath();
/* 233 */     BufferedWriter writer = new BufferedWriter(new FileWriter(path));
/* 234 */     writer.write(this.decryptedText);
/* 235 */     writer.close();
/* 236 */     return path;
/*     */   }
/*     */ 
/*     */   private void writeImage(File file) throws IOException {
/* 240 */     ImageIO.write(this.image, "png", file);
/*     */   }
/*     */ 
/*     */   private int toRGB(int red, int green, int blue) {
/* 244 */     return red * 65536 + green * 256 + blue;
/*     */   }
/*     */ 
/*     */   private String toBin(int i) {
/* 248 */     return String.format("%8s", new Object[] { Integer.toBinaryString(i) }).replace(' ', '0');
/*     */   }
/*     */ }

/* Location:           C:\Users\etgre\OneDrive\Desktop\Steganography v2.0p.jar
 * Qualified Name:     Steganographer
 * JD-Core Version:    0.6.2
 */