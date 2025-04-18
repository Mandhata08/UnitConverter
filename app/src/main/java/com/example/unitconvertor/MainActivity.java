package com.example.unitconvertor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    String[] units = {"Feet", "Inches", "Centimeters", "Meters", "Yards"};
    EditText inputValue;
    Spinner fromSpinner, toSpinner;
    TextView resultText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(isDark ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        inputValue = findViewById(R.id.inputValue);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        resultText = findViewById(R.id.resultText);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        // Listeners to handle changes
        inputValue.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { convert(); }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { convert(); }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { convert(); }
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void convert() {
        String inputStr = inputValue.getText().toString();
        if (inputStr.isEmpty()) {
            resultText.setText("Result");
            return;
        }

        double input = Double.parseDouble(inputStr);
        String fromUnit = fromSpinner.getSelectedItem().toString();
        String toUnit = toSpinner.getSelectedItem().toString();

        double meters = toMeters(input, fromUnit);
        double result = fromMeters(meters, toUnit);

        resultText.setText(String.format("%.4f %s", result, toUnit));
    }

    private double toMeters(double value, String unit) {
        switch (unit) {
            case "Feet": return value * 0.3048;
            case "Inches": return value * 0.0254;
            case "Centimeters": return value / 100;
            case "Meters": return value;
            case "Yards": return value * 0.9144;
            default: return 0;
        }
    }

    private double fromMeters(double meters, String unit) {
        switch (unit) {
            case "Feet": return meters / 0.3048;
            case "Inches": return meters / 0.0254;
            case "Centimeters": return meters * 100;
            case "Meters": return meters;
            case "Yards": return meters / 0.9144;
            default: return 0;
        }
    }
}
