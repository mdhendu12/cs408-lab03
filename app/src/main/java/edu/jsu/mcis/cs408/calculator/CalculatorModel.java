package edu.jsu.mcis.cs408.calculator;

import android.util.Log;

import java.math.BigDecimal;

public class CalculatorModel extends AbstractModel {

    public static final String TAG = "CalculatorModel";

    // period flag: makes sure multiple periods haven't been entered
    private boolean periodEntered;

    private StringBuilder screen;

    private BigDecimal lhs, rhs;

    private CalculatorState state;

    public CalculatorModel() {

        screen = new StringBuilder();

    }


    public void init() {

        appendDigit('0');

    }

    public void setKey(Character digit) {

        appendDigit(digit.charValue());

        // check for function keys, if not, it's a digit

        switch (digit) {
            case '+': case '-': case '/': case '*':
                //
                break;
            default:

        }

    }

    private void changeState(CalculatorState newState) {

        switch (state) {


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
