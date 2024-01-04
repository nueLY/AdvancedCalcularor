package model;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class Calculator {
    public static final String OPERATOR_EXP = "^";
    public static final String OPERATOR_MINUS = "–";
    public static final String OPERATOR_PLUS = "+";
    public static final String OPERATOR_MULT = "*";
    public static final String OPERATOR_DIV = "/";
    public static final String NEGATIVE_SIGN = "-"; // this is different from MINUS (shorter dash)
    public static final String DECIMAL = ".";
    private String storedValue;

    public Calculator() {
        storedValue = "";
    }

    public void setMemory(String str){
        this.storedValue = str;
    }

    public String getMemory(){
        return this.storedValue;
    }


    public double evaluate(String infixExpr) throws ExpressionException {

        try {

            // Convert to postfix notation
            String postfix = infix2postfix(infixExpr);

            // Split into tokens, e.g., "4.4 5 + 6 2 1 - * 3 / -"
            String[] tokens = postfix.split(" ");

            // Parse expression catching any thrown exception
            Stack<Double> s = new Stack<>();
            for (String token : tokens) {
                if(isNumber(token)) { // operand
                    double number = Double.parseDouble(token);
                    s.push(number);
                } else { // token is an operator (+,-,*,/)
                    double a = s.pop();
                    double b = s.pop();
                    double result = operation(b, a, token);
                    s.push(result);
                }
            }

            if(s.size() != 1) throw new ExpressionException("Invalid expression.");
            return s.pop(); // the final result

        } catch(EmptyStackException | NumberFormatException e) {

            throw new ExpressionException("Invalid expression.");
        } catch(ArithmeticException e2) {
            throw new ExpressionException("The expression results in division by zero.");
        }
    }

    private String infix2postfix(String infixExpr) {
        // Sanitize expression before parsing
        infixExpr = sanitize(infixExpr);

        // Break expression into tokens
        String[] tokens = infixExpr.split(" ");

        // Instantiate auxiliary collections
        List<String> postfix = new ArrayList<>();
        Stack<String> stack = new Stack<>();

        // Shunting Yard Algorithm - Edsger Dijkstra
        for (String e : tokens) {
            if(isNumber(e)) {
                postfix.add(e);
            } else if(isLeftParenthesis(e)) {
                stack.push(e);
            } else if(isRightParenthesis(e)) {
                while (!stack.isEmpty() && !isLeftParenthesis(stack.peek())) {
                    postfix.add(stack.pop());
                }
                stack.pop();  // Discard the "("
            } else { // isOperator
                while ( !stack.isEmpty() && precedence(e) <= precedence(stack.peek()) ) {
                    postfix.add(stack.pop());
                }
                stack.push(e);
            }
        }

        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        StringBuilder postfixExpr = new StringBuilder();
        for (String token : postfix) {
            postfixExpr.append( token + " " );
        }

        return postfixExpr.toString().trim();
    }

    private double operation(double firstOperand, double secondOperand, String operator) {
        switch (operator) {
            case OPERATOR_EXP:return Math.pow(firstOperand,secondOperand);
            case OPERATOR_DIV: if(secondOperand == 0) throw new ArithmeticException();
                return firstOperand / secondOperand;
            case OPERATOR_MULT: return firstOperand * secondOperand;
            case OPERATOR_PLUS: return firstOperand + secondOperand;
            case OPERATOR_MINUS: return firstOperand - secondOperand;
            default: return 0;
        }
    }
    private String sanitize(String expr) {
        // Sanitizes an expression. Makes it so every token has a space around it.
        String sanitized = "";
        for(int i=0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if(Character.isDigit(c) || c == DECIMAL.charAt(0)
                    || c == NEGATIVE_SIGN.charAt(0) || c == 'E') {
                sanitized += c;
            } else {
                sanitized += " " + c + " "; // possibly excess space will be removed below
            }
        }

        // returns the sanitized string, removing space in excess, if present.
        // If we receive space in excess, this will keep only one space between tokens
        return sanitized.trim().replaceAll(" +", " ");
    }

    private int precedence(String operator) {
        switch (operator) {
            case OPERATOR_EXP: return 3;
            case OPERATOR_DIV:
            case OPERATOR_MULT: return 2;
            case OPERATOR_PLUS:
            case OPERATOR_MINUS: return 1;
            default: return 0;
        }
    }

    private boolean isOperator(String token) {
        switch (token) {
            case OPERATOR_EXP:
            case OPERATOR_DIV:
            case OPERATOR_MULT:
            case OPERATOR_PLUS:
            case OPERATOR_MINUS: return true;
            default: return false;
        }
    }

    private boolean isLeftParenthesis(String token) {
        return token.equalsIgnoreCase("(");
    }

    private boolean isRightParenthesis(String token) {
        return token.equalsIgnoreCase(")");
    }

    private boolean isNumber(String token) {
        return !isOperator(token) && !isLeftParenthesis(token) && !isRightParenthesis(token);
    }
}
