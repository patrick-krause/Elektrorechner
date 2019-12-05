package com.bkbhuether.elektrorechner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener {

    Button buttonBerechne;
    EditText editStrom;
    EditText editSpannung;
    EditText editWiderstand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBerechne = findViewById(R.id.button);
        buttonBerechne.setOnClickListener(this);

        editStrom = findViewById(R.id.editTextI);
        editStrom.setText("0");
        editSpannung = findViewById(R.id.editTextU);
        editSpannung.setText("0");
        editWiderstand = findViewById(R.id.editTextR);
        editWiderstand.setText("0");
    }

    @Override
    public void onClick(View view) {

        String strI = editStrom.getText().toString();
        String strU = editSpannung.getText().toString();
        String strR = editWiderstand.getText().toString();
        double I;
        double U;
        double R;

        if(strI.isEmpty()){
            U = Double.parseDouble(strU);
            R = Double.parseDouble(strR);
            I = U/R;
            editStrom.setText(Double.toString(I));
        }

        if(strU.isEmpty()){
            I = Double.parseDouble(strI);
            R = Double.parseDouble(strR);
            U = I*R;
            editSpannung.setText(Double.toString(U));
        }

        if(strR.isEmpty()){
            I = Double.parseDouble(strI);
            U = Double.parseDouble(strU);
            R = U/I;
            editWiderstand.setText(Double.toString(R));
        }
    }
}
