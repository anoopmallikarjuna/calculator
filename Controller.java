package calculator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;


public class Controller {
    @FXML
    private Label outputLabel;

    @FXML
    private Label CalculationSequenceID;

    //flags to handle the calculationmlogic
    //Num1Flag, Num2Flag tell if the numbers are stored in the variables or not
    private boolean pressBinaryOp, pressEqual, pressUnary;
    private boolean Num1Flag, Num2Flag;

    private double num1, num2;
    private String binaryOperator;

    public void handleNumberButtonClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        String numInput = button.getText();
        String outputLabelText = outputLabel.getText();

        if(shouldReplacebyZero(outputLabelText)){
            outputLabel.setText(numInput);

            if (shouldStoreNum2()){
                Num2Flag = true;
            }
        }else{
            outputLabel.setText(outputLabelText + numInput);
        }

    }

    private boolean shouldReplacebyZero(String outputLabelText){
        //replace 0 when about to enter the text
        //replace 0 after pressing 0
        //replace 0 after pressing unary button
        //replace 0 if the current value is zero
        return (Num1Flag && pressBinaryOp && !Num2Flag)
                || pressEqual
                || pressUnary
                || Double.parseDouble(outputLabelText) == 0;
    }

    private boolean shouldStoreNum2(){
        //press num2 after pressing num1 and pressing a binary operator
        return !Num2Flag && Num1Flag && pressBinaryOp;
    }

    public void handleUnaryButtonClick(ActionEvent event){
        Button button = (Button) event.getSource();
        String unaryOperator = button.getText();

        //store num1
        num1 = Double.parseDouble(outputLabel.getText());


        //perform unary operation
        switch (unaryOperator){
            case CommonConstants.OPERATOR_NEGATE -> {
               num1 *= -1;
               break;
            }
            case CommonConstants.OPERATOR_PERCENT -> {
                num1/=100;
                CalculationSequenceID.setText(Double.toString(num1));
                break;
            }
            case CommonConstants.OPERATOR_RECIPROCAL -> {
                num1 = 1/num1;
                CalculationSequenceID.setText("1/"+ num1);
                break;
            }
            case CommonConstants.OPERATOR_SQRT -> {
                CalculationSequenceID.setText("sqrt(" + num1 + ")");
                num1 = Math.sqrt(num1);
                break;
            }
            case CommonConstants.OPERATOR_SQUARE -> {
                CalculationSequenceID.setText("sqr(" + num1 + ")");
                num1 = num1 * num1;
                break;
            }
        }

        //output
        outputLabel.setText(Double.toString(num1));

        //updtae flags
        pressUnary = true;
        Num1Flag = true;
        pressEqual = false;
        pressBinaryOp = false;
    }

    public void handleBinaryButtonClick(ActionEvent event){
        Button button = (Button) event.getSource();
        String BinaryOperator = button.getText();

        //store num1
        if(!Num1Flag){
            num1 = Double.parseDouble(outputLabel.getText());

            //update flag
            Num1Flag = true;
        }

        //update binary operator
        if(Num1Flag){
            updateBinaryOperator(BinaryOperator);
        }

        //update flags
        pressBinaryOp = true;
        pressUnary = false;
        pressEqual = false;

    }
    private void updateBinaryOperator(String BinaryOperator){
        this.binaryOperator = BinaryOperator;

        //update calculation flow
        CalculationSequenceID.setText(num1 + " " + this.binaryOperator);
    }

    public void handleDotButtonClick(){
        //adding a dot
        if(!outputLabel.getText().contains(".")){
            outputLabel.setText(outputLabel.getText()+".");
        }
    }

    public void handleOtherButtonClick(ActionEvent event){
        Button button = (Button) event.getSource();
        String otherButton = button.getText();

        switch (otherButton){
            case CommonConstants.CLEAR_ENTRY_BUTTON -> {
                outputLabel.setText("0");
                break;
            }
            case CommonConstants.CLEAR_BUTTON -> {
                reset();
                break;
            }
            case CommonConstants.DEL_BUTTON -> {
                if(Double.parseDouble(outputLabel.getText())!=0){
                    outputLabel.setText(outputLabel.getText().substring(0,outputLabel.getText().length()-1));
                }

                //resets to zero if attempting to delete everything in the output label
                if(outputLabel.getText().length() <=0){
                    outputLabel.setText("0");
                }
                break;
            }
        }
    }

    private void reset(){
        outputLabel.setText("0");
        CalculationSequenceID.setText("");
        Num1Flag = false;
        Num2Flag = false;
        pressEqual = false;
        pressUnary = false;
        pressBinaryOp = false;
    }

    public void handleEqualButtonClick(){
        //store num2
        if(shouldStoreNum2()){
            num2 = num1;
            Num2Flag = true;
        }
        //update flags
        pressEqual = true;
        pressUnary = false;
        pressBinaryOp = false;

        if(shouldCalculate()){
            calculate();
        }
    }

    private boolean shouldCalculate(){
        return Num2Flag && Num2Flag;
    }

    private void calculate(){
        //store num2
        num2 = Double.parseDouble(outputLabel.getText());
        Num2Flag = true;

        //store result into num1
        num1 = performBinaryCalculation();
        outputLabel.setText(Double.toString(num1));
    }

    public double performBinaryCalculation(){
        double result = 0;

        switch (binaryOperator){
            case CommonConstants.OPERATOR_ADD -> {
                result = num1 + num2;
                break;
            }
            case CommonConstants.OPERATOR_SUB -> {
                result = num1-num2;
                break;
            }
            case CommonConstants.OPERATOR_MUL -> {
                result = num1*num2;
                break;
            }
            case CommonConstants.OPERATOR_DIV -> {
                if(num2==0){
                    outputLabel.setText("Error cannot divide by zero");
                    reset();
                }else{
                    result = num1/num2;
                }
                break;
            }
        }
        CalculationSequenceID.setText(num1 + binaryOperator + num2 + "=");

        //reset num2
        num2 = 0;
        Num2Flag = false;

        return result;
    }
}




