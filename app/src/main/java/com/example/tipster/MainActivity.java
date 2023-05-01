package com.example.tipster;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText txtAmount, txtPeople, txtTipOther;
    private RadioGroup rdoGroupTips;
    private Button btnCalculate, btnReset;

    private TextView txtTipAmount, txtTotalToPay, txtTipPerPerson;

    private int radioCheckedId = -1;
    private void reset() {
        txtTipAmount.setText("");
        txtTotalToPay.setText("");
        txtTipPerPerson.setText("");
        txtAmount.setText("");
        txtPeople.setText("");
        txtTipOther.setText("");
        rdoGroupTips.clearCheck();
        rdoGroupTips.check(R.id.radioFifteen);
        txtAmount.requestFocus();
    }

    private void showErrorAlert(String errorMessage, final EditText edt) {
        new AlertDialog.Builder(this).setTitle("Error").setMessage(errorMessage).setNeutralButton(
                "Close", (dialog, which) -> {
                    edt.requestFocus();
                }).show();
    }

    private void calculate() {
        Double billAmount = Double.parseDouble(txtAmount.getText().toString());
        int totalPeople = Integer.parseInt(txtPeople.getText().toString());
        Double percentage = null;
        boolean isError = false;

        if (billAmount < 0.00) {
            showErrorAlert("Enter a valid Total Amount.", txtAmount);
            isError = true;
        }

        if (totalPeople < 1) {
            showErrorAlert("Enter a valid value for No. of People.", txtPeople);
            isError = true;
        }

        if (radioCheckedId == -1) {
            radioCheckedId = rdoGroupTips.getCheckedRadioButtonId();
        }

        if (radioCheckedId == R.id.radioFifteen) {
            percentage = 15.00;
        } else if (radioCheckedId == R.id.radioTwenty) {
            percentage = 20.00;
        } else if (radioCheckedId == R.id.radioOther) {
            percentage = Double.parseDouble(txtTipOther.getText().toString());

            if (percentage < 1.0) {
                showErrorAlert("Enter a valid Tip percentage", txtTipOther);
                isError = true;
            }
        }

        if (!isError) {
            Double tipAmount = ((billAmount * percentage) / 100);
            Double totalToPay = billAmount + tipAmount;
            Double perPersonPays = totalToPay / totalPeople;

            txtTipAmount.setText(tipAmount.toString());
            txtTotalToPay.setText(totalToPay.toString());
            txtTipPerPerson.setText(perPersonPays.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtAmount = findViewById(R.id.txtAmount);
        txtAmount.requestFocus();

        txtPeople = findViewById(R.id.txtPeople);
        txtTipOther = findViewById(R.id.txtTipOther);
        rdoGroupTips = findViewById(R.id.RadioGroupTips);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnCalculate.setEnabled(false);

        btnReset = findViewById(R.id.btnReset);
        txtTipAmount = findViewById(R.id.txtTipAmount);
        txtTotalToPay = findViewById(R.id.txtTotalToPay);
        txtTipPerPerson = findViewById(R.id.txtTipPerPerson);

        txtTipOther.setEnabled(false);

        rdoGroupTips.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioFifteen || checkedId == R.id.radioTwenty) {
                txtTipOther.setEnabled(false);

                btnCalculate.setEnabled(txtAmount.getText().length() > 0
                        && txtPeople.getText().length() > 0);
            }

            if (checkedId == R.id.radioOther) {
                txtTipOther.setEnabled(true);
                txtTipOther.requestFocus();

                btnCalculate.setEnabled(txtAmount.getText().length() > 0
                        && txtPeople.getText().length() > 0
                        && txtTipOther.getText().length() > 0);
            }

            radioCheckedId = checkedId;
        });

        View.OnKeyListener mKeyListener = (v, keyCode, event) -> {
            switch (v.getId()) {
                case R.id.txtAmount:
                case R.id.txtPeople:
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0
                    && txtPeople.getText().length() > 0);
                    break;
                case R.id.txtTipOther:
                    btnCalculate.setEnabled(txtAmount.getText().length() > 0
                            && txtPeople.getText().length() > 0
                            && txtTipOther.getText().length() > 0);
            }

            return false;
        };

        txtAmount.setOnKeyListener(mKeyListener);
        txtPeople.setOnKeyListener(mKeyListener);
        txtTipOther.setOnKeyListener(mKeyListener);

        View.OnClickListener mClickListener = v -> {
            if (v.getId() == R.id.btnCalculate) calculate();
            else reset();
        };


        btnCalculate.setOnClickListener(mClickListener);
        btnReset.setOnClickListener(mClickListener);
    }
}