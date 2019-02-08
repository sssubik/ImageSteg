package imggrpn_steganographer;

import java.net.URL;
import java.util.ResourceBundle;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * Simple Steganographer FXML Document Controller.
 * Handles GUI operations.
 *
 * @author Miguel Roman-Roman
 * @author Nathan Witt
 * @author Shelby Holden
 * @author Elhadji Fall
 * @author Bryan Raymond
 * @author Wei Huang
 * @author Charles Collins
 *
 * @version Oct 11, 2014
 */
public class FXMLDocumentController implements Initializable {

    /**
     * Indicators for what buttons/menu items to enable and disable.
     */
    private final static int COVER_SAVECLEAR = 0,
            SECRET_SAVECLEAR = 1,
            COMPOSITE_SAVECLEAR = 2,
            TEXT_SAVECLEAR = 3,
            SECRET_LOAD = 4,
            COMPOSE = 5,
            DECOMPOSE = 6,
            TO_TEXT = 7,
            FROM_TEXT = 8;

    @FXML
    private MenuItem loadCoverMenu, loadSecretMenu, loadCompMenu, loadTextMenu;
    @FXML
    private MenuItem saveCoverMenu, saveSecretMenu, saveCompMenu, saveTextMenu;
    @FXML
    private MenuItem clearCoverMenu, clearSecretMenu, clearCompMenu, clearTextMenu, clearAllMenu;
    @FXML
    private MenuItem toCmpMenu, fromCmpMenu;
    @FXML
    private MenuItem textToImageMenu, imageToTextMenu;
    @FXML
    private MenuItem closeMenu;

    @FXML
    private Button loadCoverButton, loadSecretButton, loadCompButton, loadTextButton;
    @FXML
    private Button saveCoverButton, saveSecretButton, saveCompButton, saveTextButton;
    @FXML
    private Button clearCoverButton, clearSecretButton, clearCompButton, clearTextButton;
    @FXML
    private Button toCmpButton, fromCmpButton;
    @FXML
    private Button textToImageButton, imageToTextButton;

    @FXML
    private Stage stage;
    @FXML
    private ImageView coverImage, secretImage, compositeImage;
    @FXML
    private TextArea textArea;

    /**
     * The images to be shown on the GUI.
     */
    private static BufferedImage coverPic, secretPic, compositePic;

    /**
     * Handles all actions related to loading.
     *
     * @param event The load event that was chosen by user.
     */
    @FXML
    private void handleLoadEvent(final ActionEvent event) {
        if (event.getSource() == loadCoverButton
                || event.getSource() == loadCoverMenu) {
            loadCoverImage();
        } else if (event.getSource() == loadSecretButton
                || event.getSource() == loadSecretMenu) {
            loadSecretImage();
        } else if (event.getSource() == loadCompButton
                || event.getSource() == loadCompMenu) {
            loadCompImage();
        } else if (event.getSource() == loadTextButton
                || event.getSource() == loadTextMenu) {
            loadText();
        }
    } //end handleButtonActionLoads

    /**
     * Handles all actions related to saving.
     *
     * @param event the save event that was chosen by user.
     */
    @FXML
    private void handleSaveEvent(final ActionEvent event) {
        if (event.getSource() == saveCoverButton
                || event.getSource() == saveCoverMenu) {
            FileIOLib.saveImg(coverPic, stage);
        } else if (event.getSource() == saveSecretButton
                || event.getSource() == saveSecretMenu) {
            FileIOLib.saveImg(secretPic, stage);
        } else if (event.getSource() == saveCompButton
                || event.getSource() == saveCompMenu) {
            FileIOLib.saveImg(compositePic, stage);
        } else if (event.getSource() == saveTextButton
                || event.getSource() == saveTextMenu) {
            FileIOLib.saveText(textArea.getText(), stage);
        }
    } //end handleButtonActionSaves

    /**
     * Handles all actions related to clearing.
     *
     * @param event The clear event chosen by the user.
     */
    @FXML
    private void handleClearEvent(final ActionEvent event) {
        if (event.getSource() == clearCoverButton
                || event.getSource() == clearCoverMenu) {
            clearCoverImage();
            clearSecretImage();
        } else if (event.getSource() == clearSecretButton
                || event.getSource() == clearSecretMenu) {
            clearSecretImage();
        } else if (event.getSource() == clearCompButton
                || event.getSource() == clearCompMenu) {
            clearCompImage();
        } else if (event.getSource() == clearTextButton
                || event.getSource() == clearTextMenu) {
            clearText();
        } else if (event.getSource() == clearAllMenu) {
            clearCoverImage();
            clearSecretImage();
            clearCompImage();
            clearText();
        }
    } //end handleButtonActionClears

    /**
     * Performs functionalities that involve changing the text or
     * image.
     *
     * @param event The functionality event that was chosen by user.
     */
    @FXML
    private void handleTransformEvent(final ActionEvent event) {
        if (event.getSource() == textToImageButton
                || event.getSource() == textToImageMenu) {
            textToImage();
        } else if (event.getSource() == imageToTextButton
                || event.getSource() == imageToTextMenu) {
            imageToText();
        } else if (event.getSource() == toCmpButton
                || event.getSource() == toCmpMenu) {
            encodeCompImg();
        } else if (event.getSource() == fromCmpButton
                || event.getSource() == fromCmpMenu) {
            decodeCompImg();
        }
    } //end handleButtonActionTransform

    /**
     * Sets the "textToImage" options if the user has typed something and a
     * cover image is present.
     *
     * @param event not used.
     */
    @FXML
    private void handleTextKeyEvent(final KeyEvent event) {
        if (textArea.getLength() != 0) {
            setDisable(TEXT_SAVECLEAR, false);
            if (coverPic != null) {
                setDisable(FROM_TEXT, false);
            }
        } else {
            setDisable(TEXT_SAVECLEAR, true);
            setDisable(FROM_TEXT, true);
        }
    }

    /**
     * Handles the close operation
     * Exits this program.
     *
     * @param event the calling event
     */
    @FXML
    private void handleCloseEvent(final ActionEvent event) {
        System.exit(0);
    }

    /**
     * Sets the default buttons enabled when the program opens up.
     *
     * @param url unused
     * @param rb unused
     */
    @Override
    public void initialize(final URL url, final ResourceBundle rb) {
        setDisable(COVER_SAVECLEAR, true);
        setDisable(SECRET_SAVECLEAR, true);
        setDisable(SECRET_LOAD, true);
        setDisable(COMPOSITE_SAVECLEAR, true);
        setDisable(TEXT_SAVECLEAR, true);

        setDisable(COMPOSE, true);
        setDisable(DECOMPOSE, true);
        setDisable(TO_TEXT, true);
        setDisable(FROM_TEXT, true);
    } //end initialize

    /**
     * Sets the disable value of the indicated group of buttons/menu items.
     *
     * @param element the group of buttons/menu items to disable.
     * @param bool the value to set disable to.
     */
    private void setDisable(int elements, boolean bool) {
        switch (elements) {
            case COVER_SAVECLEAR:
                saveCoverMenu.setDisable(bool);
                clearCoverMenu.setDisable(bool);
                saveCoverButton.setDisable(bool);
                clearCoverButton.setDisable(bool);
                break;
            case SECRET_SAVECLEAR:
                saveSecretMenu.setDisable(bool);
                clearSecretMenu.setDisable(bool);
                saveSecretButton.setDisable(bool);
                clearSecretButton.setDisable(bool);
                break;
            case COMPOSITE_SAVECLEAR:
                saveCompMenu.setDisable(bool);
                clearCompMenu.setDisable(bool);
                saveCompButton.setDisable(bool);
                clearCompButton.setDisable(bool);
                break;
            case TEXT_SAVECLEAR:
                saveTextMenu.setDisable(bool);
                clearTextMenu.setDisable(bool);
                saveTextButton.setDisable(bool);
                clearTextButton.setDisable(bool);
                break;
            case SECRET_LOAD:
                loadSecretMenu.setDisable(bool);
                loadSecretButton.setDisable(bool);
                break;
            case COMPOSE:
                toCmpMenu.setDisable(bool);
                toCmpButton.setDisable(bool);
                break;
            case DECOMPOSE:
                fromCmpMenu.setDisable(bool);
                fromCmpButton.setDisable(bool);
                break;
            case TO_TEXT:
                imageToTextMenu.setDisable(bool);
                imageToTextButton.setDisable(bool);
                break;
            case FROM_TEXT:
                textToImageMenu.setDisable(bool);
                textToImageButton.setDisable(bool);
                break;
        }
    }

    /**
     * The load cover image action.
     */
    private void loadCoverImage() {
        coverPic = FileIOLib.openImg(stage);
        if (coverPic != null) {
            coverImage.setImage(SwingFXUtils.toFXImage(coverPic, null));
            setDisable(COVER_SAVECLEAR, false);
            setDisable(SECRET_LOAD, false);
        }
    }

    /**
     * The load secret image action.
     */
    private void loadSecretImage() {
        secretPic = FileIOLib.openImg(stage);
        if (secretPic != null) {
            secretImage.setImage(SwingFXUtils.toFXImage(secretPic, null));
            setDisable(SECRET_SAVECLEAR, false);
            setDisable(TO_TEXT, false);
            setDisable(COMPOSE, false);
        }
    }

    /**
     * The load composite image action.
     */
    private void loadCompImage() {
        compositePic = FileIOLib.openImg(stage);
        if (compositePic != null) {
            compositeImage.setImage(SwingFXUtils.toFXImage(compositePic, null));
            setDisable(COMPOSITE_SAVECLEAR, false);
            setDisable(DECOMPOSE, false);
        }
    }

    /**
     * The load text action.
     */
    private void loadText() {
        String text = FileIOLib.openText(stage);
        if (text != null) {
            textArea.appendText(text);
            setDisable(TEXT_SAVECLEAR, false);
        }
    }

    /**
     * Clears the cover image and sets the buttons accordingly
     */
    private void clearCoverImage() {
        coverImage.setImage(null);
        coverPic = null;
        setDisable(COVER_SAVECLEAR, true);
        setDisable(COMPOSE, true);
        setDisable(FROM_TEXT, true);
        setDisable(SECRET_LOAD, true);
    }

    /**
     * Clears the secret image  and sets the buttons accordingly
     */
    private void clearSecretImage() {
        secretImage.setImage(null);
        secretPic = null;
        setDisable(SECRET_SAVECLEAR, true);
        setDisable(COMPOSE, true);
        setDisable(TO_TEXT, true);
    }

    /**
     * Clears the composite image  and sets the buttons accordingly
     */
    private void clearCompImage() {
        compositeImage.setImage(null);
        compositePic = null;
        setDisable(DECOMPOSE, true);
        setDisable(COMPOSITE_SAVECLEAR, true);
    }

    /**
     * Clears the text and sets the buttons accordingly
     */
    private void clearText() {
        textArea.clear();
        setDisable(TEXT_SAVECLEAR, true);
        setDisable(FROM_TEXT, true);
    }

    /**
     * Encodes the composite image. This is done by adding the secret image and
     * cover image together.
     */
    private void encodeCompImg() {
        compositePic = Steganography.encodeComposite(coverPic, secretPic);
        //check null here
        compositeImage.setImage(SwingFXUtils.toFXImage(compositePic, null));
        setDisable(COMPOSITE_SAVECLEAR, false);
        setDisable(DECOMPOSE, false);
    }

    /**
     * Decomposes the composite image into the cover and secret image.
     */
    private void decodeCompImg() {
        coverPic = Steganography.decodedCoverImage(compositePic);
        //check null here
        coverImage.setImage(SwingFXUtils.toFXImage(coverPic, null));

        secretPic = Steganography.decodedSecretImage(compositePic);
        //check null here
        secretImage.setImage(SwingFXUtils.toFXImage(secretPic, null));

        setDisable(COMPOSE, false);
        setDisable(TO_TEXT, false);
        setDisable(COVER_SAVECLEAR, false);
        setDisable(SECRET_SAVECLEAR, false);
    } //end decodeCompImg

    /**
     * The text to image action.
     */
    private void textToImage() {
        BufferedImage tempImg = SwingFXUtils.fromFXImage(coverImage.getImage(),
                null);
        //check null here
        secretPic = Steganography.textToImage(textArea.getText(), tempImg);
        Image s = SwingFXUtils.toFXImage(secretPic, null);

        secretImage.setImage(s);

        setDisable(COMPOSE, false);
        setDisable(TO_TEXT, false);
        setDisable(SECRET_SAVECLEAR, false);
    }

    /**
     * The image to text action.
     */
    private void imageToText() {
        BufferedImage tempImg = SwingFXUtils.fromFXImage(
                secretImage.getImage(), null);
        //check null here
        textArea.clear();
        textArea.appendText(Steganography.imageToText(tempImg));

        setDisable(FROM_TEXT, false);
        setDisable(TEXT_SAVECLEAR, false);
    }

} //end FXMLDocumentController
