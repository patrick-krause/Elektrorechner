package com.bkbhuether.elektrorechner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.channels.NonReadableChannelException;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener, View.OnFocusChangeListener {

    private EditText editStrom;
    private EditText editSpannung;
    private EditText editWiderstand;

    private boolean valueWasCalculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonBerechne = findViewById(R.id.button);
        buttonBerechne.setOnClickListener(this);

        editStrom = findViewById(R.id.editTextI);
        editStrom.setOnFocusChangeListener(this);
        editSpannung = findViewById(R.id.editTextU);
        editSpannung.setOnFocusChangeListener(this);
        editWiderstand = findViewById(R.id.editTextR);
        editWiderstand.setOnFocusChangeListener(this);
    }

    private enum EmptyField {
        None,
        Two,
        Three,
        Strom,
        Spannung,
        Widerstand,
    }

    private EmptyField GetToCalculate() {
        boolean lStromEmpty = editStrom.getText().toString().isEmpty(),
                lSpannungEmpty = editSpannung.getText().toString().isEmpty(),
                lWiderstandEmpty = editWiderstand.getText().toString().isEmpty();

        if ((!lStromEmpty && lSpannungEmpty && lWiderstandEmpty) ||
                (lStromEmpty && !lSpannungEmpty && lWiderstandEmpty) ||
                (lStromEmpty && lSpannungEmpty && !lWiderstandEmpty))
            return EmptyField.Two;

        if (lStromEmpty && lSpannungEmpty && lWiderstandEmpty)
            return EmptyField.Three;

        if (lStromEmpty)
            return EmptyField.Strom;
        else if (lSpannungEmpty)
            return EmptyField.Spannung;
        else if (lWiderstandEmpty)
            return EmptyField.Widerstand;
        else
            return EmptyField.None;
    }

    private void clearText(EditText pEditText) {
        pEditText.setText("");
    }

    @Override
    public void onFocusChange(View pView, boolean pHasFocus) {
        if (!pHasFocus)
            return;

        if (valueWasCalculated) {
            valueWasCalculated = false;
            clearText(editStrom);
            clearText(editSpannung);
            clearText(editWiderstand);
        } else {
            clearText((EditText) pView);
        }
    }

    @Override
    public void onClick(View pView) {
        EmptyField lToCalculate = GetToCalculate();
        if (lToCalculate == EmptyField.None) {
            Toast.makeText(this, R.string.onefieldshouldbeempty, Toast.LENGTH_SHORT).show();
            return;
        } else if (lToCalculate == EmptyField.Two) {
            Toast.makeText(this, R.string.noideawhattocalculate, Toast.LENGTH_SHORT).show();
            return;
        } else if (lToCalculate == EmptyField.Three)
            return;

        double lStrom = getValue(editStrom.getText());
        double lSpannung = getValue(editSpannung.getText());
        double lWiderstand = getValue(editWiderstand.getText());
        switch (lToCalculate) {
            case Strom:
                if (isInvalidValue(lSpannung, R.string.invalidvaluespannung) ||
                    isInvalidValue(lWiderstand, R.string.invalidvaluewiderstand)) return;
                break;
            case Spannung:
                if (isInvalidValue(lStrom, R.string.invalidvaluestrom) ||
                    isInvalidValue(lWiderstand, R.string.invalidvaluewiderstand)) return;
                break;
            case Widerstand:
                if (isInvalidValue(lStrom, R.string.invalidvaluestrom) ||
                    isInvalidValue(lSpannung, R.string.invalidvaluespannung)) return;
                break;
            default:
                throw new IndexOutOfBoundsException("Invalid ToCalculate value: " + lToCalculate.toString());
        }

        switch (lToCalculate) {
            case Strom:
                editStrom.setText(Double.toString(calculateStrom(lSpannung, lWiderstand)));
                break;
            case Spannung:
                editSpannung.setText(Double.toString(calculateSpannung(lStrom, lWiderstand)));
                break;
            case Widerstand:
                editWiderstand.setText(Double.toString(calculateWiderstand(lStrom, lSpannung)));
                break;
            default:
        }

        valueWasCalculated = true;
    }

    private boolean isInvalidValue(double pValue, int pResource) {
        if (Double.isNaN(pValue) || Double.isInfinite(pValue)) {
            Toast.makeText(this, pResource, Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private static double calculateStrom(double pSpannung, double pWiderstand) {
        return pSpannung / pWiderstand;
    }

    private static double calculateSpannung(double pStrom, double pWiderstand) {
        return pStrom * pWiderstand;
    }

    private static double calculateWiderstand(double pStrom, double pSpannung) {
        return pSpannung / pStrom;
    }

    private static double getValue(Editable pText) {
        try {
            return Double.parseDouble(pText.toString());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}
