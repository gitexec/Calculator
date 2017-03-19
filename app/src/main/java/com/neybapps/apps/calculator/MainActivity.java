package com.neybapps.apps.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {
    //The numeric buttons
    private int[] numericButtons = {R.id.btnZero, R.id.btnOne,R.id.btnTwo,R.id.btnThree,R.id.btnFour,
            R.id.btnFive,R.id.btnSix,R.id.btnSeven,R.id.btnEight,R.id.btnNine};
    //operators
    private int[] operatorButtons = {R.id.btnAdd, R.id.btnSubtract,R.id.btnMultiply,R.id.btnDivide};
    //Output/Results
    private TextView mTextScreen;
    //Lastly pressed Key. Is numeric?
    private boolean lastNumeric;
    //Current state is Error ?
    private boolean stateError;
    //No more dots can be added
    private boolean lastDot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextScreen = (TextView) findViewById(R.id.txtScreen);
        setNumericOnClickListener();
        setOperatorOnClickListener();

    }

    private void setNumericOnClickListener(){
        //Set common click listener
        View.OnClickListener listener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //Append/set the text of clicked button
                Button button = (Button) v;
                if(stateError){
                    mTextScreen.setText(button.getText());
                    stateError = false;
                }
                else{
                    mTextScreen.append(button.getText());
                }
                lastNumeric = true;
            }
        };
        //Assign common listener to all button
        for(Integer id:numericButtons){
            findViewById(id).setOnClickListener(listener);
        }
    }

    //Listener for operators,equal, and dot
    private void setOperatorOnClickListener(){
        //Common on click listener
        View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               //Append/reset text only if numeric exist and no error
                if(lastNumeric && !stateError){
                    Button button = (Button)v;
                    mTextScreen.append(button.getText());
                    lastNumeric = false;
                    lastDot = false;
                }
            }
        };
        //Set listenr for all operators/equal
        for(Integer id : operatorButtons){
            findViewById(id).setOnClickListener(listener);
        }
        findViewById(R.id.btnDot).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Button button = (Button)v;
                if(lastNumeric && !lastDot && !stateError){
                    mTextScreen.setText(button.getText());
                    lastDot = true;
                    lastNumeric = false;
                    stateError = false;
                }
            }
        });

        findViewById(R.id.btnEqual).setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View v){
            Button button = (Button)v;
            onEqual();
          }
        });
    }

    private void onEqual(){
        // Get String Calculator
        String userExpression = mTextScreen.getText().toString();
        Expression expression = new ExpressionBuilder(userExpression).build();

        try{
            double result = expression.evaluate();
            mTextScreen.setText(Double.toString(result));
            lastDot = true;
        }catch (ArithmeticException ex){
            mTextScreen.setText("Error");
            stateError = true;
            lastNumeric = false;
        }



    }
}
