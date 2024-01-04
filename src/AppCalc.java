import presenter.CalculatorPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Calculator;
import view.CalculatorView;

public class AppCalc extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        CalculatorView view = new CalculatorView();
        Calculator model = new Calculator();
        CalculatorPresenter presenter = new CalculatorPresenter(view, model);

        Scene scene = new Scene(view, 500, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.show();
    }
}