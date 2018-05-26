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

    private static final Integer width = 600, height = 450;

    public FXMLLoader getLoader(String url) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(applicationContext::getBean);
        loader.setLocation(getClass().getResource(url));
        return loader;
    }

    public Parent load(String url) throws IOException {//, String resources) {
        FXMLLoader loader = getLoader(url);
        return loader.load();
    }

    public Stage createNewWindow(String resource, String title, Event event) throws IOException {
        return createNewWindow(this.load(resource), title, event);
    }

    public Stage createNewWindow(Parent root, String title, Event event){
        Stage stage = new Stage();
        stage.setTitle(title);
//        stage.setScene(new Scene(root, width, height));
        stage.setScene(new Scene(root));
        stage.show();
        if(event != null)
            ((Node)(event.getSource())).getScene().getWindow().hide();
        return stage;
    }

}
