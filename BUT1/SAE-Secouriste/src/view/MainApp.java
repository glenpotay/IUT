package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.utils.Settings;

import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/auth/LoadingScreen.fxml")));

        stage.getIcons().add(new Image("/images/logo.png"));
        stage.setTitle("SAE Secouriste");
        stage.setMaximized(true);

        stage.setScene(new Scene(root, 1824, 1026));
        stage.show();
    }

    public static void main(String[] args) {
        System.out.println("[*] Settings useGreedy: " + Settings.useGreedy());

        launch(args);
    }
}


