package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AboutView extends BorderPane{

    public AboutView(){
        doLayout();
    }

    private void doLayout(){
        VBox vb = new VBox();
        vb.setPadding(new Insets(20));
        Label title = new Label("Title");
        Label text = new Label("Something");
        vb.setSpacing(20);
        vb.getChildren().addAll(title,text);

        setCenter(vb);
    }

}