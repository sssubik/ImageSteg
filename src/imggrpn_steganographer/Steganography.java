package imggrpn_steganographer;

import java.nio.charset.StandardCharsets;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import java.awt.Color;
import java.math.BigInteger;

/**
 * Steganography class.
 * This class contains methods for encoding image to text, decoding
 * text from an image, encoding two images to one image, and decoding two images 
 * from one image.
 *
 * @author Miguel Roman-Roman
 * @author Nathan Witt
 * @author Shelby Holden
 * @author Elhadji Fall
 * @author Bryan Raymond
 * @author Wei Huang
 * @author Charles Collins
 *
 * @version Oct 2014
 */
public class Steganography {
    
    /**
     * Private constructor disables instantiation of this class.
     */
    private Steganography(){}

    /** The bits to clear. */
    private static final int BITS_TO_CLEAR = 1;

    /**
     * Converts a given string into a black and white image. Requires the
     * cover image so that the new image will be created with equal resolution.
     *
     * @param text  the string to be encoded
     * @param image the cover image
     * @return      the black and white image encoding from the text as a
     *              BufferedImage
     */
    public static BufferedImage textToImage(final String text,
                                            final BufferedImage image) {
        String  inputTextInBinary = "";

        // Convert string to array of bytes
        byte[] encoded = text.getBytes(StandardCharsets.UTF_8);

        // construct binary string from array of bytes
        for (byte b : encoded) {
            for (int i = Integer.toBinaryString((int) b).length(); i < 8; i++) {
                inputTextInBinary += '0'; // pad with 0's
            }
            System.out.println("Hey this is what you entered:  "
             + Integer.toBinaryString((int) b));
            inputTextInBinary += Integer.toBinaryString((int) b);
            System.out.println(inputTextInBinary);
        }

        return mapStringToImage(inputTextInBinary, image);
    }

    /**
     * Private method used by textToImage.
     * Used for modularity.
     *
     * @param inputTextInBinary the string to be encoded
     * @param image             the cover image
     * @return                  the black and white encoded image
     */
    private static BufferedImage mapStringToImage(final String inputTextInBinary,
                                                  final BufferedImage image) {
        int i = 0;
        int counter = 0; // counts number of characters
        char c;
        BufferedImage newImage = image;

        // Create clone of cover image to get same dimensions

        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                if (i < inputTextInBinary.length()) {
                    c = inputTextInBinary.charAt(i);

                    // pad the first pixels with 0's until you get to the point
                    if (counter + inputTextInBinary.length()
                        < newImage.getHeight() * newImage.getWidth()) {
                        newImage.setRGB(x, y, new Color(0, 0, 0).getRGB());
                    }
                    else if (c == '1') {
                        newImage.setRGB(x, y, new Color(255, 255, 255).getRGB());
                        i++;
                    }
                    else {
                        newImage.setRGB(x, y, new Color(0, 0, 0).getRGB());
                        i++;
                    }
                        counter++;
                }
            }
        }
        return newImage;
    }

    /**
     * Decodes a given black and white image to a string.
     * Image must have been previously encoded in a similar fashion as in
     * textToImage.
     *
     * @param newImage the image to be decoded
     * @return      the string decoded from the given image
     */
    public static String imageToText(final BufferedImage newImage) {
        String inputTextInBinary = ""; // reset string to empty string
        boolean begginingFound = false; // used to skip passed the padding of 0s

        for (int x = 0; x < newImage.getWidth(); x++) {
            for (int y = 0; y < newImage.getHeight(); y++) {
                if (newImage.getRGB(x, y) <= new Color(127, 127, 127).getRGB()){
                    // Only append 0's after the initial 1 has been found
                    if (begginingFound) {
                        inputTextInBinary += '0';
                    }
                }
                else {
                    begginingFound = true;
                    inputTextInBinary += '1';
                }
            }
        }
        BigInteger charCode = new BigInteger(inputTextInBinary, 2);
        return new String(charCode.toByteArray(), StandardCharsets.UTF_8);
    }

    /**
     * Encodes the composite image. This is done by adding the secret image and
     * cover image together.
     * @param coverPic The cover image to be used for hiding the secret image.
     * @param secretPic The secret image to be hidden under the cover image.
     * @return The encoded image.
     */
     public static BufferedImage encodeComposite(final BufferedImage coverPic,
                                                final BufferedImage secretPic) {
        if ((coverPic.getHeight() != secretPic.getHeight())
         || (coverPic.getWidth() != secretPic.getWidth())) {
            System.out.println("The two pictures are not the same size.");
            //coverPic = new BufferedImage(need to resize to the secret pic size
        }

        BufferedImage cover = removeLowerOrderBits(coverPic);
        BufferedImage secret = compressSecretImage(secretPic);
        int r, b, g;

        BufferedImage compPic = new BufferedImage(cover.getWidth(),
                                                  cover.getHeight(),
                                                  BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < compPic.getWidth(); i++) {
            for (int j = 0; j < compPic.getHeight(); j++) {
                Color coverCol = new Color(cover.getRGB(i, j));
                Color secretCol = new Color(secret.getRGB(i, j));

                r = coverCol.getRed() + secretCol.getRed();
                g = coverCol.getGreen() + secretCol.getGreen();
                b = coverCol.getBlue() + secretCol.getBlue();

                //Check to make sure values are not less than 0 and less than 255.
                r = Math.min(Math.max(0, r), 255);
                g = Math.min(Math.max(0, g), 255);
                b = Math.min(Math.max(0, b), 255);

                Color newColor = new Color(r, g, b);
                compPic.setRGB(i, j, newColor.getRGB());
            } //end for
        } //end for

        return compPic;
    } //end encodeComposite

    /**
     * Returns an image with the lower order bits removed.
     * @param image
     * @return image with low order bits removed
     */
     private static BufferedImage removeLowerOrderBits(final BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage reducedImg = new BufferedImage(width,
                                                     height,
                                                     BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color currentColor = new Color(img.getRGB(i, j));
                Color reducedColor = removeLowerOrderBitsHelper(currentColor);
                reducedImg.setRGB(i, j, reducedColor.getRGB());
            }
        }
        return reducedImg;
    } //end removeLowerOrderBits

        /**
     * Returns the Color that is produced when removing the lower order
     * bits.
     * @param c The current color for this pixel.
     * @return New color with low order bits removed.
     */
    private static Color removeLowerOrderBitsHelper(final Color c) {
        int r = c.getRed();
    int g = c.getGreen();
    int b = c.getBlue();
        int factor = (int) Math.pow(2, BITS_TO_CLEAR);

        r = (r / factor) * factor;
        g = (g / factor) * factor;
        b = (b / factor) * factor;

    return new Color(r, g, b);
    } //end removeLowerOrderBitsHelper

    /**
     * Compresses the secret image into a form where it can be combined
     * with the cover image.
     * @param img The secret image to be compressed.
     * @return image with pixels that are just 0s or 1s.
     */
    private static BufferedImage compressSecretImage(final BufferedImage img) {
        int width = img.getWidth();
    int height = img.getHeight();
    int r, g, b;
    BufferedImage compressedImg = new BufferedImage(width,
                                                    height,
                                                    BufferedImage.TYPE_INT_RGB);

    for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
        Color currentCol = new Color(img.getRGB(i, j));

        r = currentCol.getRed();
        g = currentCol.getGreen();
        b = currentCol.getBlue();

                r = r / 128;
                g = g / 128;
                b = b / 128;
                System.out.println("R: "+r);
        Color newColor = new Color(r, g, b);
        compressedImg.setRGB(i, j, newColor.getRGB());
            } //end for
    } //end for

    return compressedImg;
    } //end compressSecretImage

    public static BufferedImage decodedCoverImage(final BufferedImage compositePic) {
        return removeLowerOrderBits(compositePic);
    }

    /**
     * Decomposes the composite image into the cover and secret image.
     */
    public static BufferedImage decodedSecretImage(BufferedImage compositePic) {
        BufferedImage coverPic = removeLowerOrderBits(compositePic);
        BufferedImage secretPic = new BufferedImage(compositePic.getWidth(),
                                                    compositePic.getHeight(),
                                                    BufferedImage.TYPE_INT_RGB);
        int r, g, b;
        for (int i = 0; i < compositePic.getWidth(); i++) {
            for (int j = 0; j < compositePic.getHeight(); j++) {
                Color compColor = new Color(compositePic.getRGB(i, j));
                Color coverColor = new Color(coverPic.getRGB(i, j));

                 r = (compColor.getRed() - coverColor.getRed()) * 255;
                 g = (compColor.getGreen() - coverColor.getGreen()) * 255;
                 b = (compColor.getBlue() - coverColor.getBlue()) * 255;

                 Color newColor = new Color(r, g, b);
                 secretPic.setRGB(i, j, newColor.getRGB());
            } //end for
        } //end for

        return secretPic;
    } //end decodedSecretImage
} //end Steganography
