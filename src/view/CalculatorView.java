package view;

import presenter.CalculatorPresenter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Calculator;


import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CalculatorView extends BorderPane {

    // Atri
    private TextField textExpression;
    private Label labelStatus;
    private Button buttonEqual;
    private Label labelLastExpression;
    private Button buttonMemoryStore;
    private Button buttonMemoryRecall;
    private  MenuItem clearMemory;
    private  RadioMenuItem lightMode ;
    private  RadioMenuItem darkMode;
    private Properties configuration;


    private void loadConfiguration(){
        this.configuration = new Properties();
        try {
            this.configuration.load(new FileReader("calculator.config"));
        } catch (IOException e) {
            showError("Configuration file not found");
        }
    }

    private void saveConfiguration(){
        try {
            this.configuration.store(new FileWriter("calculator.config"),"");
        } catch (IOException e) {
            showError(e.getMessage());
        }
    }

    public CalculatorView() {
        doLayout();
        loadConfiguration();
        String themeName = this.configuration.getProperty("theme","Light");
        changeTheme(themeName);
    }

    public void bindEvents(CalculatorPresenter presenter) {
        buttonEqual.setOnAction(event -> presenter.doEvaluate());
        buttonMemoryStore.setOnAction(event -> presenter.doMemoryStore());
        buttonMemoryRecall.setOnAction(event -> presenter.doMemoryRecall());
        clearMemory.setOnAction(event -> presenter.doClearMemory());

    }

    public String getExpression() {
        return textExpression.getText();
    }

    public void setExpressionResult(double result) {
        // TODO: treat "rounding-off" errors when presenting a result, e.g., "1.000000003"
        //  or implement a configuration for the result precision.
        if(result == 0){
            textExpression.setText("0");
        }
        else{
            textExpression.setText(String.valueOf(result));
        }

    }

    public void showError(String message) {
        labelStatus.setText(message);
        // As soon as an error is shown, the border is set to red.
        setErrorStyle();
    }

    public void clearError() {
        labelStatus.setText("Ready");
        // As soon as an error is cleared, the border is removed.
        clearErrorStyle();
    }

    private void doLayout() {
        // Set internal margin for the panel
        setPadding(new Insets(0,30,30,30));

        // Initialization of controls
        this.textExpression = new TextField("0");
        this.labelStatus = new Label("Ready");
        this.buttonEqual = new Button("=");
        this.labelLastExpression = new Label("");
        this.buttonMemoryStore = new Button("MS");
        this.buttonMemoryRecall = new Button("MR");

        Button buttonDivide = new Button(Calculator.OPERATOR_DIV);
        Button buttonMultiply = new Button(Calculator.OPERATOR_MULT);
        Button buttonAdd = new Button(Calculator.OPERATOR_PLUS);
        Button buttonSubtract = new Button(Calculator.OPERATOR_MINUS);
        Button buttonParenthesisOpen = new Button("(");
        Button buttonParenthesisClose = new Button(")");
        Button buttonBackspace = new Button("<<");
        Button button9 = new Button("9");
        Button button8 = new Button("8");
        Button button7 = new Button("7");
        Button button6 = new Button("6");
        Button button5 = new Button("5");
        Button button4 = new Button("4");
        Button button3 = new Button("3");
        Button button2 = new Button("2");
        Button button1 = new Button("1");
        Button button0 = new Button("0");
        Button buttonDecimal = new Button(Calculator.DECIMAL);
        Button buttonPlusMinus = new Button("+/-");

        // New buttons
        Button buttonExponent = new Button("^");
        Button buttonClearEntry = new Button("CE");


        // Layout Top
        HBox hBox = new HBox(labelLastExpression);
        hBox.setAlignment(Pos.BASELINE_RIGHT);

        textExpression.setPrefHeight(50);
        textExpression.setDisable(true);

        MenuBar menu = createMenuLayout();
        VBox vBox = new VBox(menu,hBox,textExpression);
        vBox.setSpacing(10);
        setMargin(vBox, new Insets(0, 0, 20, 0));
        setTop(vBox);

        // Layout Bottom
        setMargin(labelStatus, new Insets(20, 0, 0, 0));
        setBottom(labelStatus);

        // Layout Center
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(buttonMemoryStore, 0, 0);
        grid.add(buttonMemoryRecall, 1, 0);
        grid.add(buttonClearEntry, 2, 0);
        grid.add(buttonBackspace, 3, 0);

        grid.add(buttonParenthesisOpen,0,1);
        grid.add(buttonParenthesisClose,1,1);
        grid.add(buttonExponent,2,1);
        grid.add(buttonDivide,3,1);

        grid.add(button7, 0, 2);
        grid.add(button8, 1, 2);
        grid.add(button9, 2, 2);
        grid.add(buttonMultiply, 3, 2);

        grid.add(button4, 0, 3);
        grid.add(button5, 1, 3);
        grid.add(button6, 2, 3);
        grid.add(buttonSubtract, 3, 3);

        grid.add(button1, 0, 4);
        grid.add(button2, 1, 4);
        grid.add(button3, 2, 4);
        grid.add(buttonAdd, 3, 4);

        grid.add(buttonPlusMinus, 0, 5);
        grid.add(button0, 1, 5);
        grid.add(buttonDecimal, 2, 5);
        grid.add(buttonEqual, 3, 5);
        buttonEqual.getStyleClass().add("myEqual");
        buttonEqual.setDisable(true);

        // Make all columns have the same proportion
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(25);
        cc.setHgrow(Priority.ALWAYS) ; // allow column to grow
        cc.setFillWidth(true); // ask nodes to fill space for column
        grid.getColumnConstraints().addAll(cc, cc, cc, cc);

        RowConstraints rc = new RowConstraints();
        rc.setVgrow(Priority.ALWAYS) ; // allow a row to grow
        rc.setFillHeight(true); // ask nodes to fill height for row
        grid.getRowConstraints().addAll(rc, rc, rc, rc, rc);

        // Make all buttons fill the available space
        buttonDivide.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonMultiply.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonAdd.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonSubtract.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonParenthesisOpen.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonParenthesisClose.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonBackspace.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button9.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button8.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button7.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button6.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button5.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button4.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button3.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button2.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button1.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        button0.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonDecimal.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonPlusMinus.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonMemoryRecall.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonMemoryStore.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonClearEntry.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonExponent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        buttonEqual.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        setCenter(grid);

        // Bind events
        button0.setOnAction(event -> appendToExpression("0") );
        button1.setOnAction(event -> appendToExpression("1") );
        button2.setOnAction(event -> appendToExpression("2") );
        button3.setOnAction(event -> appendToExpression("3") );
        button4.setOnAction(event -> appendToExpression("4") );
        button5.setOnAction(event -> appendToExpression("5") );
        button6.setOnAction(event -> appendToExpression("6") );
        button7.setOnAction(event -> appendToExpression("7") );
        button8.setOnAction(event -> appendToExpression("8") );
        button9.setOnAction(event -> appendToExpression("9") );
        buttonDecimal.setOnAction(event -> appendToExpression(Calculator.DECIMAL) );
        buttonParenthesisOpen.setOnAction(event -> appendToExpression("(") );
        buttonParenthesisClose.setOnAction(event -> appendToExpression(")") );
        buttonAdd.setOnAction(event -> appendToExpression(Calculator.OPERATOR_PLUS) );
        buttonSubtract.setOnAction(event -> appendToExpression(Calculator.OPERATOR_MINUS) );
        buttonMultiply.setOnAction(event -> appendToExpression(Calculator.OPERATOR_MULT) );
        buttonDivide.setOnAction(event -> appendToExpression(Calculator.OPERATOR_DIV) );
        buttonBackspace.setOnAction(event -> removeLastCharacterFromExpression());
        buttonPlusMinus.setOnAction(event -> negateLastNumber() );
        buttonExponent.setOnAction(event -> appendToExpression(Calculator.OPERATOR_EXP));
        buttonClearEntry.setOnAction(event->clearEntry());
    }


    private MenuBar createMenuLayout(){

        // define the menu bar
        MenuBar menuBar = new MenuBar();

        // define the menu
        Menu calcMenu = new Menu("Calculator");

        // create exit item
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(event->exit());

        // create clearMemory item
        this.clearMemory = new MenuItem("Clear memory");

        // create Theme menu
        Menu themeMenu = new Menu("Theme");

        // Create the toggle group
        ToggleGroup themetoggleGroup = new ToggleGroup();


        // create lightMode item
        lightMode = new RadioMenuItem("Light mode");
        lightMode.setToggleGroup(themetoggleGroup);

        // create darkMode item
        darkMode = new RadioMenuItem("Dark mode");
        darkMode.setToggleGroup(themetoggleGroup);

        darkMode.setOnAction(event-> changeTheme("Dark"));
        lightMode.setOnAction(event-> changeTheme("Light"));


        // create about item
        Menu helpMenu = new Menu("Help");

        // create item/s for helpMenu
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().add(about);
        about.setOnAction(event->showAboutApp());

        // add the items to themeMenu
        themeMenu.getItems().addAll(lightMode,darkMode);

        calcMenu.getItems().addAll(clearMemory,
                new SeparatorMenuItem(),exitItem);

        menuBar.getMenus().addAll(calcMenu,themeMenu,helpMenu);

        // return the main menu:
        return menuBar;
    }

    private void changeTheme(String themeName){

        getStylesheets().clear();
        String styleFile = "appStyle"+themeName+".css";
        getStylesheets().add(styleFile);

        // save configuration
        this.configuration.setProperty("theme",themeName);
        saveConfiguration();

        if (themeName.equals("Dark")) {
            darkMode.setSelected(true);
        } else {
            lightMode.setSelected(true);
        }
    }

    private void showAboutApp(){
        AboutView test = new AboutView();

        Scene sc = new Scene(test,400,200);
        Stage modalStage = new Stage();
        modalStage.setTitle("This is about");
        modalStage.setScene(sc);

        modalStage.setResizable(false);
        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.show();
    }

    private void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Closing the application");
        alert.setContentText("Please confirm you want to leave");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            System.exit(0);
        }
    }

    private void clearEntry(){
        textExpression.setText("0");
        updateLastEvaluation();
        buttonEqual.setDisable(true);
        clearError();
    }

    private void negateLastNumber() {
        String expr = getExpression();
        // if the last character in the expression is not a number, then the
        // user missed the opportunity to negate the last number, e.g., "4+" or "6-("
        if(!Character.isDigit( expr.charAt( expr.length() - 1))) return;

        // This regex pattern matches any number (integer, real and/or negative)
        // TODO: change decimal point to match Calculator.DECIMAL
        Pattern pattern = Pattern.compile("-?\\d*\\.?\\d+(?:[eE]-?\\d+)?");
        Matcher matcher = pattern.matcher(expr);

        int lastNumberIndex = -1;
        while (matcher.find()) {
            lastNumberIndex = matcher.start();
        }

        if (lastNumberIndex != -1) {
            // there is a number in the expression, and lastNumberIndex is the start index
            // of the last number found; It can then be a digit or a minus sign
            char c = expr.charAt(lastNumberIndex);
            String modifiedExpr;

            if(Character.isDigit(c) || c == Calculator.DECIMAL.charAt(0)) {
                // Put a minus sign before the number
                modifiedExpr = expr.substring(0, lastNumberIndex) +
                        Calculator.NEGATIVE_SIGN +
                        expr.substring(lastNumberIndex);

            } else {
                // Remove the minus sign
                modifiedExpr = expr.substring(0, lastNumberIndex) +
                        expr.substring(lastNumberIndex + 1);
            }

            textExpression.setText(modifiedExpr);

        }  // No numbers found in the string. Nothing to do yet!

    }

    public void appendToExpression(String s) {
        buttonEqual.setDisable(false);
        String existing = this.textExpression.getText();
        if(existing.equals("0")){
            textExpression.setText(s);
        }
        else{
            this.textExpression.setText(existing + s);
        }
        clearError();
    }

    private void removeLastCharacterFromExpression() {
        String existing = this.textExpression.getText();
        if (existing.isBlank()){ return;}

        // remove the last character
        String undone = existing.substring(0, existing.length() - 1);
        this.textExpression.setText(undone);

        // Disable button when expression is empty
        if(undone.isBlank() || undone.compareTo("-")==0){
            textExpression.setText("0");
            buttonEqual.setDisable(true);
        }
        clearError();
    }

    // Method used to set textfield border
    public void setErrorStyle(){
        textExpression.getStyleClass().add("error-textfield");
    }

    // Method used to remove textfield border
    public void clearErrorStyle(){
        textExpression.getStyleClass().remove("error-textfield");
    }

    public void updateLastEvaluation() {
        String expression = textExpression.getText();

        if (!expression.equals("0")) {
            labelLastExpression.setText(expression);
        } else {
            labelLastExpression.setText("");
        }
    }

    public void hightLightMemoryRecqall(String str){
        buttonMemoryRecall.getStyleClass().add("myMemomoryStore");
        buttonMemoryRecall.setTooltip(new Tooltip(str));
    }

    public void removeHightLightMemoryRecqall(){
        buttonMemoryRecall.getStyleClass().remove("myMemomoryStore");
        buttonMemoryRecall.setTooltip(null);
    }
}
