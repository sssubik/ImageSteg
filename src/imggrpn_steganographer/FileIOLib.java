package imggrpn_steganographer;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * Library of basic open and save methods for images and text.
 *
 * @author Miguel Roman-Roman
 * @author Nathan Witt
 * @author Shelby Holden
 * @author Elhadji Fall
 * @author Bryan Raymond
 * @author Wei Huang
 * @author Charles Collins
 *
 * @version Oct 2014 *
 */
public abstract class FileIOLib {

    /** Used by the filechooser to filter the image files to load/save. */
    private static final FileChooser.ExtensionFilter IMG_FILTER
            = new FileChooser.ExtensionFilter("ALL FILES",
                    "*.jpeg", "*.jpg", "*.JPG", "*.JPEG", "*.png",
                    "*.PNG", "*.tif", "*.tiff", "*.TIF", "*.TIFF");

    /** Used by the filechooser to filter the text files to load/save. */
    private static final FileChooser.ExtensionFilter TEXT_FILTER
            = new FileChooser.ExtensionFilter("ALL FILES",
                    "*.txt", "*.TXT"); //add more extensions if needed

    /** Default image save extension. */
    private static final String DEFAULT_IMG_EXT = ".png";

    /** Default text save extension. */
    private static final String DEFAULT_TEXT_EXT = ".txt";

    /**
     * Opens the image from a file specified by the user.
     *
     * @param parent the parent GUI for the file chooser
     * @return the image opened from user
     */
    public static BufferedImage openImg(final Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().setAll(IMG_FILTER);
        File file = fileChooser.showOpenDialog(parent);

        try {
            return ImageIO.read(file);
        } catch (Exception e) {
            System.out.println("Failed to load img: " + e);
            return null;
        }
    }

    /**
     * Saves the image in a file specified by the user.
     *
     * @param img the image to save
     * @param parent the parent GUI for the file chooser
     * @return whether or not a save occurred
     */
    public static boolean saveImg(final BufferedImage img, final Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(IMG_FILTER);
        fileChooser.setTitle("Save Image");
        File file = fileChooser.showSaveDialog(parent);

        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + DEFAULT_IMG_EXT);
        }

        try {
            ImageIO.write(img, file.getName().split("\\.")[1], file);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to save img: " + e);
            return false;
        }
    }

    /**
     * Opens the text from a file specified by the user
     *
     * @param parent the parent GUI for the file chooser
     * @return the text opened from path
     */
    public static String openText(final Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text");
        fileChooser.getExtensionFilters().setAll(TEXT_FILTER);
        File file = fileChooser.showOpenDialog(parent);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String text = "";
            String line;
            while ((line = reader.readLine()) != null) {
                text += line + "\n";
            }
            return text;
        } catch (Exception e) {
            System.out.println("Failed to open: " + e);
            return null;
        }
    }

    /**
     * Saves the text in a file specified by the user.
     *
     * @param text the text to save
     * @param parent the parent GUI for the file chooser
     * @return whether or not a save occurred
     */
    public static boolean saveText(final String text, final Stage parent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(TEXT_FILTER);
        fileChooser.setTitle("Save Text");
        File file = fileChooser.showSaveDialog(parent);

        if (!file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + DEFAULT_TEXT_EXT);
        }

        try {
            PrintWriter out = new PrintWriter(file);
            String[] lines = text.split("\n");
            for (String line : lines) {
                out.println(line);
            }
            out.close();
            return true;
        } catch (Exception e) {
            System.out.println("Failed to save: " + e);
            return false;
        }
    }
}
