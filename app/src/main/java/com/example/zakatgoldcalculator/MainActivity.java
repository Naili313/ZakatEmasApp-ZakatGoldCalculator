package com.example.zakatgoldcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.splashscreen.SplashScreen;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(R.layout.actionbar_title);
        }

        // Radio button setup
        RadioGroup radioGroupType = findViewById(R.id.radioGroupType);

        // Views
        Button btnCalculate = findViewById(R.id.btnCalculate);
        EditText etWeight = findViewById(R.id.etWeight);
        EditText etValue = findViewById(R.id.etValue);
        TextView tvTotalValue = findViewById(R.id.tvTotalValue);
        TextView tvZakatPayable = findViewById(R.id.tvZakatPayable);
        TextView tvTotalZakat = findViewById(R.id.tvTotalZakat);
        TextView tvNoZakatNote = findViewById(R.id.tvNoZakatNote);
        TextInputLayout layoutWeight = findViewById(R.id.layoutWeight);
        TextInputLayout layoutValue = findViewById(R.id.layoutValue);
        LinearLayout resultCard = findViewById(R.id.resultCard);

        btnCalculate.setOnClickListener(v -> {
            // Clear previous errors
            layoutWeight.setError(null);
            layoutValue.setError(null);

            String weightStr = etWeight.getText().toString().trim();
            String valueStr = etValue.getText().toString().trim();

            boolean hasError = false;

            // Check empty fields individually (clearer for the user)
            if (weightStr.isEmpty()) {
                layoutWeight.setError("Please enter gold weight");
                hasError = true;
            }
            if (valueStr.isEmpty()) {
                layoutValue.setError("Please enter gold value per gram");
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight, goldValue;

            // Catch invalid number formats too (e.g. user types "abc")
            try {
                weight = Double.parseDouble(weightStr);
                goldValue = Double.parseDouble(valueStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numbers only.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (weight <= 0) {
                layoutWeight.setError("Weight must be greater than zero");
                hasError = true;
            }
            if (goldValue <= 0) {
                layoutValue.setError("Value must be greater than zero");
                hasError = true;
            }

            if (hasError) {
                Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Nisab threshold
            double nisab = (radioGroupType.getCheckedRadioButtonId() == R.id.radioKeep) ? 85.0 : 200.0;

            // Calculations
            double totalValue = weight * goldValue;
            double zakatableWeight = weight - nisab;
            double zakatPayable = (zakatableWeight > 0) ? zakatableWeight * goldValue : 0.0;
            double totalZakat = zakatPayable * 0.025;

            // Display results
            tvTotalValue.setText("Total Gold Value: RM " + String.format("%.2f", totalValue));

            if (zakatableWeight <= 0) {
                tvZakatPayable.setText("Zakat Payable: RM 0.00 (Below nisab threshold)");
            } else {
                tvZakatPayable.setText("Zakat Payable Amount: RM " + String.format("%.2f", zakatPayable));
            }

            tvTotalZakat.setText("RM " + String.format("%.2f", totalZakat));

// Show note if no zakat is due
            if (zakatableWeight <= 0) {
                tvNoZakatNote.setVisibility(View.VISIBLE);
            } else {
                tvNoZakatNote.setVisibility(View.GONE);
            }

// Reveal the results card
            resultCard.setVisibility(View.VISIBLE);

// ✅ Success message
            Toast.makeText(this, "Calculation successful!", Toast.LENGTH_SHORT).show();
        });

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(v -> {
            etWeight.setText("");
            etValue.setText("");
            radioGroupType.check(R.id.radioKeep);
            layoutWeight.setError(null);
            layoutValue.setError(null);
            tvTotalValue.setText("");
            tvZakatPayable.setText("");
            tvTotalZakat.setText("");
            resultCard.setVisibility(View.GONE);
            tvNoZakatNote.setVisibility(View.GONE);
            Toast.makeText(this, "Form cleared.", Toast.LENGTH_SHORT).show();
        });
    }

    // ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out my Zakat Gold Calculator app: https://github.com/Naili313/ZakatEmasApp-ZakatGoldCalculator");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}