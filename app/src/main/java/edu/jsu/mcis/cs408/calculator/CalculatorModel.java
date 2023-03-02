package edu.jsu.mcis.cs408.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private final String START_VALUE = "0";
    private final Character DECIMAL_POINT = '.';

    private static final String ERROR = "ERROR";
    private int MAX_SCREEN_WIDTH = 12;
    private boolean hasDecimalPoint = false;

    private StringBuilder screen;

    private BigDecimal lhs, rhs;

    private CalculatorState state;

    private CalculatorFunction operator;

    public CalculatorModel() {

        screen = new StringBuilder();

    }


    public void init() {

        state = CalculatorState.CLEAR;
        lhs = rhs = null;
        operator = null;
        hasDecimalPoint = false;
        setScreen("0");

        state = CalculatorState.LHS;

    }

    public void setKey(Character key) {

        if (state.equals(CalculatorState.CLEAR)) {
            changeState(CalculatorState.LHS);
        }

        if (state.equals(CalculatorState.OP_SELECTED)) {
            changeState(CalculatorState.RHS);
            setScreen("0");
        }

        if (state.equals(CalculatorState.LHS) || state.equals(CalculatorState.RHS)) {
            if (key == DECIMAL_POINT) {

                if(!hasDecimalPoint) {
                    appendDigit(key);
                    hasDecimalPoint = true;
                }
            }

            else {

                if (screen.toString().equals(START_VALUE)) {
                    screen.setLength(0);
                }

                appendDigit(key.charValue());
            }
        }

    }

    public void setFunction(CalculatorFunction function) {

        Log.i(TAG, "Function Change: " + function.toString());

        try {

            switch (function) {
                case ADD: case SUBTRACT: case MULTIPLY: case DIVIDE:
                    changeState(CalculatorState.OP_SELECTED);
                    operator = function;
                    break;
                case CLEAR:
                    changeState(CalculatorState.CLEAR);
                    break;
                case SIGN:
                    if (!(state.equals(CalculatorState.ERROR))) {
                        changeSign();
                    }
                    break;
                case SQRT:
                    computeSqrt();
                    break;
                case EQUALS:
                    changeState(CalculatorState.RESULT);
                    break;
                case PERCENT:
                    if (state.equals(CalculatorState.RHS)) {
                        computePercent();
                    }
                    break;
            }

        }
        catch (Exception e) {
            e.toString();
        }
    }

    private void setScreen(String newText) {

        String oldText = screen.toString();
        screen.setLength(0);

        if (newText.length() > MAX_SCREEN_WIDTH) {
            newText = newText.substring(0, MAX_SCREEN_WIDTH);
        }

        screen.append(newText);

        Log.i(TAG, "Screen Change: " + newText);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
    }
    private void changeState(CalculatorState newState) {

        if (newState.equals(CalculatorState.CLEAR)) {
            init();
        }
        else if (newState.equals(CalculatorState.ERROR)) {
            lhs = null;
            rhs= null;
            hasDecimalPoint = false;
            operator = null;
            setScreen(ERROR);
        }
        else {
            // specifies what should happen when we exit state

            switch (state) {

                case LHS:
                    trimDecimal();
                    lhs = new BigDecimal(screen.toString());
                    rhs = null;
                    break;

                case RHS:
                    trimDecimal();
                    rhs = new BigDecimal(screen.toString());
                    evaluate();
                    break;

                case OP_SELECTED:
                    hasDecimalPoint = false;
                    break;
            }

        }

        state = newState;

    }

    private void evaluate() {

        BigDecimal result = new BigDecimal("0");
        if (lhs == null) {
            lhs = result;
        }
        if (rhs == null) {
            rhs = lhs;
        }

        if (operator != null) {
            try {
                switch (operator) {
                    case ADD:
                        result = lhs.add(rhs);
                        break;
                    case SUBTRACT:
                        result = lhs.subtract(rhs);
                        break;
                    case MULTIPLY:
                        result = lhs.multiply(rhs);
                        break;
                    case DIVIDE:
                        if (rhs.compareTo(BigDecimal.ZERO) == 0) {
                            throw new ArithmeticException();
                        }
                        else {
                            result = lhs.divide(rhs, MathContext.DECIMAL128);
                        }

                        break;
                }

                lhs = result;
                rhs = null;
                setScreen(result.toString());
            }
            catch (ArithmeticException e) {
                e.printStackTrace();
                changeState(CalculatorState.ERROR);
            }

        }

    }


    public void appendDigit(char digit) {

        String oldText = screen.toString();
        screen.append(digit);
        String newText = screen.toString();

        Log.i(TAG, "Screen Change: " + digit);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }

    private void changeSign() {
        String oldText = screen.toString();

        BigDecimal temp = new BigDecimal(oldText);
        String newText = temp.negate().toString();

        setScreen(newText);

        Log.i(TAG, "Sign Changed");

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
    }

    private void computePercent() {
        String oldText = screen.toString();

        BigDecimal temp = new BigDecimal(oldText);

        String newText = temp.divide(BigDecimal.valueOf(100)).multiply(lhs).toString();

        setScreen(newText);

        Log.i(TAG, "Percentage Calculated");

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
    }

    private void computeSqrt() {
        String oldText = screen.toString();
        String newText = oldText;

        if (lhs != null && !(state.equals(CalculatorState.RHS))) {
            this.lhs = new BigDecimal(Math.sqrt(lhs.doubleValue()));
            newText = lhs.toString();
        }
        else {
            double sqrt = Math.sqrt(Double.parseDouble(oldText));
            newText = String.valueOf(sqrt);
        }

        setScreen(newText);

        Log.i(TAG, "Sqrt Taken");

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
    }


    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

    private void trimDecimal() {
        if (screen.charAt(screen.length() - 1) == '.') {
            screen.deleteCharAt(screen.length() - 1);
        }
    }

}
