package presenter;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import model.Calculator;
import model.ExpressionException;
import view.CalculatorView;

import java.util.Objects;
import java.util.Optional;

public class CalculatorPresenter {

    private final CalculatorView view;
    private final Calculator model;
    private Tooltip toolTip;

    public CalculatorPresenter(CalculatorView view, Calculator model) {
        this.view = view;
        this.model = model;
        this.view.bindEvents(this);
    }

    public void doEvaluate() {
        view.clearError();
        String expr = view.getExpression();

        if(expr.isBlank()) {
            view.showError("Empty expression.");
            return;
        }

        try {
            view.updateLastEvaluation();
            double result = model.evaluate(expr);
            view.setExpressionResult(result);
        } catch (ExpressionException e) {
            view.showError(e.getMessage());
        }
    }

    public void doMemoryStore(){
        // clear any error
        view.clearError();
        // get the expression to view
        String expr = view.getExpression();

        try{
            double validateExpression = Double.valueOf(expr);
            // gets here if valid number
            view.hightLightMemoryRecqall(expr);
            model.setMemory(expr);

        }
        catch (NumberFormatException e){
            // code arrives here if expression is not a valid number
            view.showError("Expression not allowed");
        }

    }

    public void doMemoryRecall(){

        view.clearError();
        String storedValue = model.getMemory();
        if (storedValue.isEmpty()){
            view.showError("No value stored.");
        }
        else {
            double checkNumber = Double.parseDouble(storedValue);
            if(checkNumber<0){
                view.appendToExpression("(" + storedValue + ")");
            }
            else{
                view.appendToExpression(storedValue);
            }

        }
    }


    public void doClearMemory(){
        /*
        String storedValue = model.getMemory();
        if(storedValue.isEmpty()){
            view.showError("No value stored.");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Clearing Memory ");
        alert.setContentText("Please confirm you want to clear memory");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){

        }

         */
        model.setMemory("");
        view.removeHightLightMemoryRecqall();

    }

    public void showToolTip() {
        String cv = model.getMemory();
        toolTip = new Tooltip(cv);
    }
}
