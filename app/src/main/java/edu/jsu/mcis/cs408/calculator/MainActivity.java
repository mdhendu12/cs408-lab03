package edu.jsu.mcis.cs408.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.beans.PropertyChangeEvent;

import edu.jsu.mcis.cs408.calculator.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AbstractView {

    public static final String TAG = "MainActivity";

    public static final int DIGIT_TAG_LENGTH = 4;

    private ActivityMainBinding binding;

    private CalculatorController controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /* Create Controller and Model */

        controller = new CalculatorController();
        CalculatorModel model = new CalculatorModel();

        /* Register Activity View and Model with Controller */

        controller.addView(this);
        controller.addModel(model);

        /* Initialize Model to Default Values */

        model.init();

        CalculatorClickHandler click = new CalculatorClickHandler();
        ConstraintLayout layout = binding.layout;

        for (int i = 0; i < layout.getChildCount(); ++i) {
            View child = layout.getChildAt(i);
            if(child instanceof Button) {
                child.setOnClickListener(click);
            }
        }
    }

    class CalculatorClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String tag = ((Button) v).getTag().toString();
            Toast toast = Toast.makeText(binding.getRoot().getContext(), tag, Toast.LENGTH_SHORT);
            toast.show();

            if ( tag.length() == DIGIT_TAG_LENGTH ) {

                Character digit = tag.charAt(tag.length() - 1);
                controller.changeKey(digit);

            }
            else {
                switch (tag) {
                    case "btnSqrt":
                        controller.changeKey('q');
                        break;
                    case "btnClear":
                        controller.changeKey('c');
                        break;
                    case "btnDiv":
                        controller.changeKey('/');
                        break;
                    case "btnMultiply":
                        controller.changeKey('*');
                        break;
                    case "btnAdd":
                        controller.changeKey('+');
                        break;
                    case "btnSub":
                        controller.changeKey('-');
                        break;
                    case "btnSign":
                        controller.changeKey('s');
                        break;
                    case "btnDecimal":
                        controller.changeKey('.');
                        break;
                    case "btnEqual":
                        controller.changeKey('=');
                        break;
                    case "btnPercent":
                        controller.changeKey('%');
                        break;
                }
            }

        }

    }

    public void modelPropertyChange(final PropertyChangeEvent evt) {


        String propertyName = evt.getPropertyName();
        String propertyValue = evt.getNewValue().toString();

        Log.i(TAG, "New " + propertyName + " Value from Model: " + propertyValue);

        if ( propertyName.equals(CalculatorController.SCREEN_PROPERTY) ) {

            String oldPropertyValue = binding.calcScreen.getText().toString();

            if ( !oldPropertyValue.equals(propertyValue) ) {
                binding.calcScreen.setText(propertyValue);
            }

        }

    }
}
