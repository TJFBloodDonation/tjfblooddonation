package ro.ubb.tjfblooddonation.utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ResourceBundle;
public class SpringFxmlLoader {

    private static final ApplicationContext applicationContext =
            new AnnotationConfigApplicationContext("ro.ubb.tjfblooddonation.config");

    private static final Integer width = 450, height = 450;

    public Parent load(String url) throws IOException {//, String resources) {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(applicationContext::getBean);
        loader.setLocation(getClass().getResource(url));
        //loader.setResources(ResourceBundle.getBundle(resources));
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    public void createNewWindow(String resource, String title, Event event) throws IOException {

        Parent root;
        root = this.load(resource);
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));
        stage.show();
        if(event != null)
            ((Node)(event.getSource())).getScene().getWindow().hide();

    }

}
