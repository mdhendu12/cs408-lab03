package edu.jsu.mcis.cs408.calculator;

public class CalculatorController extends AbstractController
{

    public static final String KEY_PROPERTY = "Key";
    public static final String SCREEN_PROPERTY = "Screen";

    public void changeKey(Character newText) {
        setModelProperty(KEY_PROPERTY, newText);
    }

    public void changeScreen(String newText) {
        setModelProperty(SCREEN_PROPERTY, newText);
    }

}
