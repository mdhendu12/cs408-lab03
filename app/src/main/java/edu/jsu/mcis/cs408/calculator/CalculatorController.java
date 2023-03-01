package edu.jsu.mcis.cs408.calculator;

import java.util.HashMap;

public class CalculatorController extends AbstractController
{

    public static final String KEY_PROPERTY = "Key";
    public static final String SCREEN_PROPERTY = "Screen";

    public static final String FUNCTION_PROPERTY = "Function";

    private final HashMap<String, Character> keyMap;
    private final HashMap<String, CalculatorFunction> functionMap;

    public CalculatorController() {
        super();

        keyMap = createKeyMap();
        functionMap = createFunctionMap();
    }

    public void changeKey(Character newText) {
        setModelProperty(KEY_PROPERTY, newText);
    }

    public void changeFunction(CalculatorFunction newFunction) {
        setModelProperty(FUNCTION_PROPERTY, newFunction);
    }

    public void changeScreen(String newText) {
        setModelProperty(SCREEN_PROPERTY, newText);
    }

    public void processInput(String tag) {

        switch (tag) {

            case "btnAdd": case "btnClr": case "btnDiv":
            case "btnEquals": case "btnMultiply": case "btnPercent":
            case "btnSign": case "btnSqrt": case "btnSub":

                changeFunction(functionMap.get(tag));
                break;

            default:

                changeKey( keyMap.get(tag) );
                break;

        }

    }

    private HashMap createKeyMap() {

        HashMap<String, Character> map = new HashMap<>();

        map.put("btn0", '0');
        map.put("btn1", '1');
        map.put("btn2", '2');
        map.put("btn3", '3');
        map.put("btn4", '4');
        map.put("btn5", '5');
        map.put("btn6", '6');
        map.put("btn7", '7');
        map.put("btn8", '8');
        map.put("btn9", '9');
        map.put("btnDecimal", '.');

        return map;
    }

    private HashMap createFunctionMap() {
        HashMap<String, CalculatorFunction> map = new HashMap<>();

        map.put("btnAdd", CalculatorFunction.ADD);
        map.put("btnClr", CalculatorFunction.CLEAR);
        map.put("btnDiv", CalculatorFunction.DIVIDE);
        map.put("btnEquals", CalculatorFunction.EQUALS);
        map.put("btnMultiply", CalculatorFunction.MULTIPLY);
        map.put("btnPercent", CalculatorFunction.PERCENT);
        map.put("btnSign", CalculatorFunction.SIGN);
        map.put("btnClear", CalculatorFunction.CLEAR);
        map.put("btnSqrt", CalculatorFunction.SQRT);
        map.put("btnSub", CalculatorFunction.SUBTRACT);

        return map;
    }


}
