package imggrpn_steganographer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Image Group-N's Simple Steganographer Main.
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
public class ImgGrpN_Steganographer extends Application {

    /**
     * Default start method used by JavaFX when launching this program
     * 
     * @param stage a JavaFX stage
     * @throws Exception 
     */
    @Override
    public void start(final Stage stage) throws Exception {
        Parent root = FXMLLoader.load(
                      getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Steganographer 1.0");
        stage.show();
    }

    /**
     * Default main method used to launch program.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
