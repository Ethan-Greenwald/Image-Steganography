/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JFileChooser;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JProgressBar;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.filechooser.FileNameExtensionFilter;
/*     */ 
/*     */ public class UIDriver
/*     */ {
/*  17 */   static JFileChooser chooser = new JFileChooser();
/*     */   static FileNameExtensionFilter filter;
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  22 */       UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
/*  23 */       SwingUtilities.updateComponentTreeUI(chooser); } catch (Exception localException) {
/*     */     }
/*  25 */     JOptionPane.showMessageDialog(null, "Choose image file");
/*  26 */     BufferedImage read = getImage();
/*     */ 
/*  28 */     switch (JOptionPane.showOptionDialog(null, "Encrypt or decrypt?", null, 0, 3, null, 
/*  29 */       new String[] { "Encrypt", "Decrypt" }, Integer.valueOf(0))) {
/*     */     case 0:
/*  31 */       JOptionPane.showMessageDialog(null, "Choose text file");
/*     */       try {
/*  33 */         new Steganographer(read, getText().toCharArray()).encrypt();
/*     */       } catch (NullPointerException e) {
/*  35 */         System.exit(0);
/*     */       }
/*     */     case 1:
/*     */       try
/*     */       {
/*  40 */         new Steganographer(read, null).decrypt();
/*     */       } catch (NullPointerException e) {
/*  42 */         System.exit(0);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String getText()
/*     */   {
/*  49 */     chooser.setFileFilter(new FileNameExtensionFilter("Txt text files", new String[] { "txt" }));
/*  50 */     File text = null;
/*  51 */     if (chooser.showOpenDialog(null) == 0)
/*  52 */       text = chooser.getSelectedFile();
/*     */     else
/*  54 */       System.exit(0);
/*  55 */     FileReader reader = null;
/*     */     try {
/*  57 */       reader = new FileReader(text);
/*     */     } catch (Exception e1) {
/*  59 */       JOptionPane.showMessageDialog(null, "File could not be found.", null, 0);
/*     */     }
/*     */ 
/*  62 */     JFrame frame = new JFrame("Processing");
/*  63 */     JProgressBar progress = new JProgressBar();
/*  64 */     progress.setIndeterminate(true);
/*  65 */     frame.add(new JPanel().add(progress));
/*  66 */     frame.pack();
/*  67 */     frame.setSize(250, 80);
/*  68 */     frame.setLocationRelativeTo(null);
/*  69 */     frame.setVisible(true);
/*  70 */     String textString = "";
/*     */     try
/*     */     {
/*     */       int i;
/*  72 */       while ((i = reader.read()) != -1)
/*     */       {
/*     */         int i;
/*  73 */         textString = textString + (char)i;
/*  74 */       }frame.dispose();
/*  75 */       JOptionPane.showMessageDialog(frame, "Text processing complete");
/*     */     } catch (IOException e) {
/*  77 */       JOptionPane.showMessageDialog(null, "Error occured reading text file", null, 0);
/*     */     }
/*  79 */     return textString;
/*     */   }
/*     */ 
/*     */   public static BufferedImage getImage() {
/*  83 */     chooser.setFileFilter(new FileNameExtensionFilter("JPG & PNG Images", new String[] { "jpg", "png" }));
/*  84 */     BufferedImage image = null;
/*  85 */     if (chooser.showOpenDialog(null) == 0)
/*     */       try {
/*  87 */         image = ImageIO.read(chooser.getSelectedFile());
/*     */       } catch (IOException e) {
/*  89 */         JOptionPane.showMessageDialog(null, "File could not be found.", null, 0);
/*     */       }
/*     */     else
/*  92 */       System.exit(0);
/*  93 */     return image;
/*     */   }
/*     */ 
/*     */   public static File getFile() {
/*  97 */     chooser.setFileFilter(new FileNameExtensionFilter("File", new String[] { "jpg", "png", "txt" }));
/*  98 */     if (chooser.showOpenDialog(null) == 0)
/*  99 */       return chooser.getSelectedFile();
/* 100 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Users\etgre\OneDrive\Desktop\Steganography v2.0p.jar
 * Qualified Name:     UIDriver
 * JD-Core Version:    0.6.2
 */