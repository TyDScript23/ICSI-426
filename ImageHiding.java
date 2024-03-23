import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import java.io.*;
import java.util.Objects;

import javax.imageio.ImageIO;

import javax.swing.*;

//create a JFrame and declare a listener for actions
public class ImageHiding extends JFrame implements ActionListener
{
 //ComboBox so we may select the operation
 JComboBox<String> operationSelector;

 BufferedImage hostImage;
 BufferedImage secretImage;

 JPanel controlPanel;
 JPanel imagePanel;

 JTextField encodeBitsText;
 JButton encodeBitsPlus;
 JButton encodeBitsMinus;

 JTextField nBitsText;
 JButton nBitsPlus;
 JButton nBitsMinus;

 ImageCanvas hostCanvas;
 ImageCanvas secretCanvas;

 Steganography s;

 // host image, if it exists is returned
 public BufferedImage getHostImage()
 {
  BufferedImage img = null;

  //try and catch to see if the image exists
  try
  {
   img = ImageIO.read(new File("host_image.jpg"));
  }
  catch (IOException ioe) { ioe.printStackTrace(); }

  return img;
 }


 // secret image, if it exists, is returned
 public BufferedImage getSecretImage()
 {

  BufferedImage img = null;

  //try and catch to see if the image exists
  try
  {
   img = ImageIO.read(new File("secret_image.jpg"));
  }
  catch (IOException ioe) { ioe.printStackTrace(); }

  return img;
 }

 //returns number from text input
 public int getBits()
 {
  return Integer.parseInt(encodeBitsText.getText());
 }


 public void actionPerformed(ActionEvent event)
 {
  Object source = event.getSource();

  //create a new Steganography object for the host image
  Steganography host = new Steganography(this.getHostImage());

  //we look for the operation selected in the combobox
  String selectedMethod = (String)operationSelector.getSelectedItem();

  //based of the method selected we run the corresponding encode function
  switch(Objects.requireNonNull(selectedMethod))
  {
   case "MSB of S to LSB of H":
    host.MSBtoLSB(this.getSecretImage(), this.getBits());
    break;
   case "LSB of S to LSB of H":
    host.LSBtoLSB(this.getSecretImage(), this.getBits());
    break;
   case "MSB of S to MSB of H":
    host.MSBtoMSB(this.getSecretImage(), this.getBits());
    break;
   case "LSB of S to MSB of H":
    host.LSBtoMSB(this.getSecretImage(), this.getBits());
    break;
  }

  //fetches the host image
  hostCanvas.setImage(host.getImage());

  Steganography secret = new Steganography(this.getSecretImage());

  //based of the selected method from the Combo Box we run the corresponding masking method
  switch(Objects.requireNonNull(selectedMethod))
  {
   case "MSB of S to LSB of H":
    secret.getMaskedMSBtoLSB(this.getBits());
    break;
   case "LSB of S to LSB of H":
    secret.getMaskedLSBtoLSB(this.getBits());
    break;
   case "MSB of S to MSB of H":
    secret.getMaskedMSBtoMSB(this.getBits());
    break;
   case "LSB of S to MSB of H":
    secret.getMaskedLSBtoMSB(this.getBits());
    break;
  }

  //fetches the secret image
  secretCanvas.setImage(secret.getImage());

  //when + button is tapped
  if (source == encodeBitsPlus)
  {
   //increments bits by 1 each time
   int bits = this.getBits() + 1;

   //bits is bounded below 8
   if (bits > 8) { bits = 8; }

   //updates the value of bits in the text field
   encodeBitsText.setText(Integer.toString(bits));

   //new Steganography object contained the host image
   s = new Steganography(this.getHostImage());

   //based of teh selcted method from the ComboBox we run the corresponding encoding method on
   //the host image
   switch(selectedMethod)
   {
    case "MSB of S to LSB of H":
     s.MSBtoLSB(this.getSecretImage(), bits);
     break;
    case "LSB of S to LSB of H":
     s.LSBtoLSB(this.getSecretImage(), bits);
     break;
    case "MSB of S to MSB of H":
     s.MSBtoMSB(this.getSecretImage(), bits);
     break;
    case "LSB of S to MSB of H":
     s.LSBtoMSB(this.getSecretImage(), bits);
     break;
   }

   //set the ImageCanvas for the host picture to left dude
   hostCanvas.setImage(s.getImage());

   //repaint the ImageCanvas for the host picture
   hostCanvas.repaint();

   //redefines the Steganography object to the secret image
   s = new Steganography(this.getSecretImage());

   //based on the selected option in the ComboBox we run the corresponding masking function on the secret image
   switch(selectedMethod)
   {
    case "MSB of S to LSB of H":
     s.getMaskedMSBtoLSB(bits);
     break;
    case "LSB of S to LSB of H":
     s.getMaskedLSBtoLSB(bits);
     break;
    case "MSB of S to MSB of H":
     s.getMaskedMSBtoMSB(bits);
     break;
    case "LSB of S to MSB of H":
     s.getMaskedLSBtoMSB(bits);
     break;
   }

   //the canvas for the secret image is set to the right dude
   secretCanvas.setImage(s.getImage());

   //repaints the secret canvas
   secretCanvas.repaint();
  }
  //if minus button is tapped
  else if (source == encodeBitsMinus)
  {

   //decrement the value of bits in the text box
   int bits = this.getBits() - 1;

   //makes sure that the value of bits does not go lower than 0
   if (bits < 0) { bits = 0; }

   //updates the value of bits in the text field
   encodeBitsText.setText(Integer.toString(bits));

   //creates a Steganography object with the left dude
   s = new Steganography(this.getHostImage());

   //based on the selected method from the ComboBox we run the corresponding encoding function on the host image
   switch(selectedMethod)
   {
    case "MSB of S to LSB of H":
     s.MSBtoLSB(this.getSecretImage(), bits);
     break;
    case "LSB of S to LSB of H":
     s.LSBtoLSB(this.getSecretImage(), bits);
     break;
    case "MSB of S to MSB of H":
     s.MSBtoMSB(this.getSecretImage(), bits);
     break;
    case "LSB of S to MSB of H":
     s.LSBtoMSB(this.getSecretImage(), bits);
     break;
   }

   //encode the secret image in to the host image, see comments on "encode"
   //s.MSBtoLSB(this.getSecretImage(), bits);

   //set the canvas meant for the host image to the host image
   hostCanvas.setImage(s.getImage());

   //repaint the canvas for the host image
   hostCanvas.repaint();

   //create a new steganography object using the secret image
   s = new Steganography(this.getSecretImage());

   //based on the selected option from the ComboBox we run the corresponding masking function on the secret image
   switch(selectedMethod)
   {
    case "MSB of S to LSB of H":
     s.getMaskedMSBtoLSB(bits);
     break;
    case "LSB of S to LSB of H":
     s.getMaskedLSBtoLSB(bits);
     break;
    case "MSB of S to MSB of H":
     s.getMaskedMSBtoMSB(bits);
     break;
    case "LSB of S to MSB of H":
     s.getMaskedLSBtoMSB(bits);
     break;
   }

   //sets the secretCanvas to the secret image (right dude)
   secretCanvas.setImage(s.getImage());

   //repaint the canvas meant for the secret image
   secretCanvas.repaint();
  }
 }

 public ImageHiding()
 {

  //declare the grid layout
  GridBagLayout layout = new GridBagLayout();

  //declare the grid constraints
  GridBagConstraints gbc = new GridBagConstraints();

  //sets the title of the current frame
  this.setTitle("Image Hiding Demo");

  //returns and stores the contentPane for this JFrame
  Container container = this.getContentPane();

  //sets the current grid layout to default
  this.setLayout(layout);

  //add a new JLabel to the Frame
  this.add(new JLabel("Bits to encode into host image:"));

  //declares a text field object that is not editable and contains the
  //value "0" by default
  encodeBitsText = new JTextField("0", 5);
  encodeBitsText.setEditable(false);

  //specifies how to distribute the extra horizontal space
  gbc.weightx = -1.0;

  //sets the constraints of the test field
  layout.setConstraints(encodeBitsText, gbc);

  //adds the text field box
  this.add(encodeBitsText);

  //declares the plus button w/ label
  encodeBitsPlus = new JButton("+");

  //adds a listener to the plus button
  encodeBitsPlus.addActionListener(this);

  //declares the minus button w/ label
  encodeBitsMinus = new JButton("-");

  //adds a listener to the minus button
  encodeBitsMinus.addActionListener(this);

  //redefine the weight of the grid constraint
  gbc.weightx = 1.0;

  //sets the constraint for the plus button
  layout.setConstraints(encodeBitsPlus, gbc);

  //add the plus button to the Frame
  this.add(encodeBitsPlus);

  //the component's display area will be from gridx to the last cell in the row
  gbc.gridwidth = GridBagConstraints.REMAINDER;

  //sets the constraints for the minus button to the default constraints
  layout.setConstraints(encodeBitsMinus, gbc);

  //add teh minus button
  this.add(encodeBitsMinus);

  //declare a new grid layout
  GridBagLayout imageGridbag = new GridBagLayout();

  //declare new grid constraints
  GridBagConstraints imageGBC = new GridBagConstraints();

  //create a JPanel
  imagePanel = new JPanel();

  //set the layout of the JPanel
  imagePanel.setLayout(imageGridbag);

  //declare label for host image
  JLabel hostImageLabel = new JLabel("Host image:");

  //declare label for secret image
  JLabel secretImageLabel = new JLabel("Secret image:");

  //add the host image label to the PANEL
  imagePanel.add(hostImageLabel);

  //the next component fills from widthx to end of container
  imageGBC.gridwidth = GridBagConstraints.REMAINDER;

  //set the constraints of the secret image label
  imageGridbag.setConstraints(secretImageLabel, imageGBC);

  //add the secret image label
  imagePanel.add(secretImageLabel);

  //declare the canvas for the host image
  hostCanvas = new ImageCanvas(this.getHostImage());

  //declare the canvas for the secret image
  secretCanvas = new ImageCanvas(this.getSecretImage());

  //adds the host image canvas to the panel for the images
  imagePanel.add(hostCanvas);

  //adds the secret image canvas to the panel for the images
  imagePanel.add(secretCanvas);

  //the next component fills from widthx to end of container
  gbc.gridwidth = GridBagConstraints.REMAINDER;

  //set the constraints of the image panel
  layout.setConstraints(imagePanel, gbc);

  //add the panel containing the images
  this.add(imagePanel);

  //sets the default operation to exit when the close button is pressed
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  //size the frame so that all of its contents are at or above their preferred sizes
  this.pack();

  //Drop down menu is added
  operationSelector = new JComboBox<>(new String[]{"MSB of S to LSB of H","LSB of S to LSB of H", "MSB of S to MSB of H", "LSB of S to MSB of H"});
  gbc.gridwidth = GridBagConstraints.REMAINDER;
  layout.setConstraints(operationSelector, gbc);
  operationSelector.setEditable(false);
  this.add(operationSelector);

  operationSelector.addActionListener(this);

  //set the Frame components to visible
  this.setVisible(true);
 }


 public static void main(String[] args)
 {
  //declare our JFrame
  ImageHiding frame = new ImageHiding();

  //set the JFrame components to visible
  frame.setVisible(true);
 }

 //declares an ImageCanvas on the JPanel
 public static class ImageCanvas extends JPanel
 {

  //image object
  Image img;

  //paint the image onto the current image canvas
  public void paintComponent(Graphics g)
  {
   g.drawImage(img, 0, 0, this);
  }

  //set the image to the image object passed in
  public void setImage(Image img)
  {
   this.img = img;
  }

  //constructor for ImageCanvas initializing the image and defining the canvas dimensions
  public ImageCanvas(Image img)
  {

   //sets the current image for the canvas
   this.img = img;

   //creates the image campus with the same dimensions as the current image
   this.setPreferredSize(new Dimension(img.getWidth(this), img.getHeight(this)));
  }
 }
}

class Steganography
{
 //our current image is declared
 BufferedImage image;

 public void getMaskedMSBtoLSB(int bits){

  System.out.println("We are masking MSB to LSB");
  //assign this array to the RGB values of the host image (left dude)
  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  //calculates the value to keep the most significant bits up bits
  int maskBits = (int)(Math.pow(2, bits)) - 1 << (8 - bits);

  //replicates pattern across color channels
  int mask = (maskBits << 24) | (maskBits << 16) | (maskBits << 8) | maskBits;

  //for the entire length of the host image
  for (int i = 0; i < imageRGB.length; i++)
  {
   //mask the RGB values
   imageRGB[i] = imageRGB[i] & mask;
  }

  //set the new RGB values on the image to the new values
  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }


 //embeds teh secret image "encodeimage" into HOST image
 public void MSBtoLSB(BufferedImage encodeImage, int encodeBits)
 {
  System.out.println("We are now in MSBtoLSB");

  //getting all the pixel RGB values for right dude and storing them
  int[] encodeRGB = encodeImage.getRGB(0, 0, encodeImage.getWidth(null), encodeImage.getHeight(null), null, 0, encodeImage.getWidth(null));

  //getting all the pixel RGB values for left dude and storing them
  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  // Calculate a mask to isolate the number of bits (encodeBits) from the secret image's pixels
  int encodeByteMask = (int)(Math.pow(2, encodeBits)) - 1 << (8 - encodeBits);
  int encodeMask = (encodeByteMask << 24) | (encodeByteMask << 16) | (encodeByteMask << 8) | encodeByteMask;

  // Calculate a complementary mask for the host image that will be used to clear out space
  int decodeByteMask = ~(encodeByteMask >>> (8 - encodeBits)) & 0xFF;
  int hostMask = (decodeByteMask << 24) | (decodeByteMask << 16) | (decodeByteMask << 8) | decodeByteMask;

  //for size of left dude image RGB values
  for (int i = 0; i < imageRGB.length; i++)
  {
   //encode the data and set the new RGB values in the RGB array
   int encodeData = (encodeRGB[i] & encodeMask) >>> (8 - encodeBits);
   imageRGB[i] = (imageRGB[i] & hostMask) | (encodeData & ~hostMask);
  }

  //set the new RGB values of the left dude image to those calculated
  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void getMaskedMSBtoMSB(int bits){

  System.out.println("We are masking MSB to MSB");

  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  int maskBits = (int) (Math.pow(2, bits)) - 1 << (8 - bits);
  int mask = (maskBits << 24) | (maskBits << 16) | (maskBits << 8) | maskBits;

  for (int i = 0; i < imageRGB.length; i++) {
   imageRGB[i] = imageRGB[i] & mask; // Mask the RGB values
  }

  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void MSBtoMSB(BufferedImage encodeImage, int encodeBits) {
  System.out.println("We are now in MSBtoMSB");

  //getting the original RGB values for the image and storing them in an array
  int[] encodeRGB = encodeImage.getRGB(0, 0, encodeImage.getWidth(null), encodeImage.getHeight(null), null, 0, encodeImage.getWidth(null));
  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  int encodeByteMask = (int) (Math.pow(2, encodeBits)) - 1 << (8 - encodeBits);
  int encodeMask = (encodeByteMask << 24) | (encodeByteMask << 16) | (encodeByteMask << 8) | encodeByteMask;// Mask for MSBs

  int decodeByteMask = ~(encodeByteMask) & 0xFF;
  int hostMask = (decodeByteMask << 24) | (decodeByteMask << 16) | (decodeByteMask << 8) | decodeByteMask; // Mask for clearing LSBs

  for (int i = 0; i < imageRGB.length; i++) {
   int encodeData = (encodeRGB[i] & encodeMask) >>> (8 - encodeBits);
   imageRGB[i] = (imageRGB[i] & hostMask) | (encodeData & ~hostMask);
  }

  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void getMaskedLSBtoLSB(int bits) {
  System.out.println("We are masking LSB to LSB");

  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));
  int maskBits = (1 << bits) - 1; // Mask with the specified number of LSBs set to 1
  int mask = (maskBits << 24) | (maskBits << 16) | (maskBits << 8) | maskBits;

  for (int i = 0; i < imageRGB.length; i++) {
   imageRGB[i] = imageRGB[i] & mask;
  }

  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void LSBtoLSB(BufferedImage encodeImage, int encodeBits) {
  System.out.println("We are now in LSBtoLSB");

  int[] encodeRGB = encodeImage.getRGB(0, 0, encodeImage.getWidth(null), encodeImage.getHeight(null), null, 0, encodeImage.getWidth(null));
  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));
  int encodeByteMask = (int) (Math.pow(2, encodeBits)) - 1;
  int encodeMask = (encodeByteMask << 24) | (encodeByteMask << 16) | (encodeByteMask << 8) | encodeByteMask;
  int decodeByteMask = ~(encodeByteMask >>> (8 - encodeBits)) & 0xFF;
  int hostMask = (decodeByteMask << 24) | (decodeByteMask << 16) | (decodeByteMask << 8) | decodeByteMask;
  for (int i = 0; i < imageRGB.length; i++) {
   //encode the data and set the new RGB values in the RGB array
   int encodeData = (encodeRGB[i] & encodeMask) >>> (8 - encodeBits);
   imageRGB[i] = (imageRGB[i] & hostMask) | (encodeData & ~hostMask);
  }
  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void getMaskedLSBtoMSB(int bits){
  System.out.println("We are masking LSB to MSB");

  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  int maskBits = (int) (Math.pow(2, bits) - 1); // Calculate mask to keep the least significant bits
  int mask = (maskBits << 24) | (maskBits << 16) | (maskBits << 8) | maskBits;

  for (int i = 0; i < imageRGB.length; i++) {
   imageRGB[i] = imageRGB[i] & mask;
  }

  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 public void LSBtoMSB(BufferedImage encodeImage, int encodeBits) {
  System.out.println("We are now in LSBtoMSB");

  int[] encodeRGB = encodeImage.getRGB(0, 0, encodeImage.getWidth(null), encodeImage.getHeight(null), null, 0, encodeImage.getWidth(null));
  int[] imageRGB = image.getRGB(0, 0, image.getWidth(null), image.getHeight(null), null, 0, image.getWidth(null));

  int encodeByteMask = (int) (Math.pow(2, encodeBits)) - 1;
  int encodeMask = (encodeByteMask << 24) | (encodeByteMask << 16) | (encodeByteMask << 8) | encodeByteMask;

  int decodeByteMask = ~(encodeByteMask) & 0xFF;
  int hostMask = (decodeByteMask << 24) | (decodeByteMask << 16) | (decodeByteMask << 8) | decodeByteMask;

  for (int i = 0; i < imageRGB.length; i++) {
   //encode the data and set the new RGB values in the RGB array
   int encodeData = (encodeRGB[i] & encodeMask) >>> (8 - encodeBits);
   imageRGB[i] = (imageRGB[i] & hostMask) | (encodeData & ~hostMask);
  }

  image.setRGB(0, 0, image.getWidth(null), image.getHeight(null), imageRGB, 0, image.getWidth(null));
 }

 //returns the left image
 public Image getImage()
 {
  return image;
 }


 //create a new Steganography object with the current image
 public Steganography(BufferedImage image)
 {
  this.image = image;
 }
}