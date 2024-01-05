package view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
        TextArea textArea = new TextArea();
        textArea.setText("With this mini Calculator project, the primary objective " +
                "was to explore System Architecture. We utilized the Java language for coding, " +
                "JavaFX for the interface, and CSS for styling. While there are improvements to consider, " +
                "we can confidently say that we achieved the main goal. Let's connect and create!✨");

        textArea.setWrapText(true);

        vb.setSpacing(20);
        vb.getChildren().addAll(title,textArea);

        setCenter(vb);
    }

}