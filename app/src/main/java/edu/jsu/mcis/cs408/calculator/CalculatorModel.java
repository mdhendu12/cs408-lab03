package edu.jsu.mcis.cs408.calculator;

import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    private final String START_VALUE = "0";
    private final Character DECIMAL_POINT = '.';

    // period flag: makes sure multiple periods haven't been entered
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
            }

        }
        catch (Exception e) {
            e.toString();
        }
    }

    private void setScreen(String newText) {

        String oldText = screen.toString();
        screen.setLength(0);
        screen.append(newText);

        Log.i(TAG, "Screen Change: " + newText);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);
    }
    private void changeState(CalculatorState newState) {

        if (newState.equals(CalculatorState.CLEAR)) {
            init();
        }
        else {
            // specifies what should happen when we exit state

            switch (state) {

                case LHS:
                    lhs = new BigDecimal(screen.toString());
                    rhs = null;
                    break;

                case RHS:
                    rhs = new BigDecimal(screen.toString());
                    evaluate();
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
                    result = lhs.divide(rhs);
                    break;
            }

            lhs = result;
            rhs = null;
            setScreen(result.toString());
        }

    }

    public void appendDigit(char digit) {

        String oldText = screen.toString();
        screen.append(digit);
        String newText = screen.toString();

        Log.i(TAG, "Screen Change: " + digit);

        firePropertyChange(CalculatorController.SCREEN_PROPERTY, oldText, newText);

    }


    private enum CalculatorState {
        CLEAR, LHS, OP_SELECTED, RHS, RESULT, ERROR
    }

}
