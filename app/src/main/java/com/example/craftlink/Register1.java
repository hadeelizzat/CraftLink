package com.example.craftlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


public class Register1 extends AppCompatActivity {
    // المستخدم الافتراضي هو العميل
    private boolean isWorker = false;

    private View cardClient, cardWorker, layoutSpecialties;
    private ImageView badgeClient, badgeWorker;
    private TextView tvClientTitle, tvWorkerTitle;
    private TextInputLayout layoutFirstName,layoutLastName ,layoutCity;
    private TextInputEditText etFirstName, etLastName, etCity;
    private ChipGroup chipGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register1);

        // تعريف العناصر
        cardClient = findViewById(R.id.cardClient);
        cardWorker = findViewById(R.id.cardWorker);
        badgeClient = findViewById(R.id.badgeClient);
        badgeWorker = findViewById(R.id.badgeWorker);
        tvClientTitle = findViewById(R.id.tvClientTitle);
        tvWorkerTitle = findViewById(R.id.tvWorkerTitle);
        layoutSpecialties = findViewById(R.id.layoutSpecialties);
        chipGroup = findViewById(R.id.chipGroupSpecialties);

        layoutFirstName = findViewById(R.id.layoutFirstName);
        layoutLastName = findViewById(R.id.layoutLastName);
        layoutCity = findViewById(R.id.layoutCity);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etCity = findViewById(R.id.etCity);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        cardClient.setOnClickListener(v -> selectRole(false));
        cardWorker.setOnClickListener(v -> selectRole(true));
        findViewById(R.id.btnNext).setOnClickListener(v -> next());
        selectRole(false);
    }

        private void selectRole ( boolean worker){
            isWorker = worker;

            cardClient.setBackgroundResource(worker ? R.drawable.bg_role_unselected : R.drawable.bg_role_selected);
            cardWorker.setBackgroundResource(worker ? R.drawable.bg_role_selected : R.drawable.bg_role_unselected);

            badgeClient.setVisibility(worker ? View.GONE : View.VISIBLE);
            badgeWorker.setVisibility(worker ? View.VISIBLE : View.GONE);

            tvClientTitle.setTextColor(ContextCompat.getColor(this,
                    worker ? R.color.text_dark : R.color.blue_selected));
            tvWorkerTitle.setTextColor(ContextCompat.getColor(this,
                    worker ? R.color.blue_selected : R.color.text_dark));

            // التخصصات للعامل فقط
            layoutSpecialties.setVisibility(worker ? View.VISIBLE : View.GONE);
        }

        private String text (TextInputEditText et){
            return et.getText() == null ? "" : et.getText().toString().trim();
        }


        private void next () {
            String first =  text(etFirstName);
            String last = text(etLastName);
            String city = text(etCity);

            layoutFirstName.setError(null);
            layoutLastName.setError(null);
            layoutCity.setError(null);

            if (first.isEmpty()) {
                layoutFirstName.setError("اكتب الاسم الأول");
                etFirstName.requestFocus();
                return;
            }
            if (last.isEmpty()) {
                layoutLastName.setError("اكتب الاسم الأخير");
                etLastName.requestFocus();
                return;
            }
            if (city.isEmpty()) {
                layoutCity.setError("اكتب المدينة");
                etCity.requestFocus();
                return;
            }

            ArrayList<String> specialties = new ArrayList<>();
            if (isWorker) {
                for (int i = 0; i < chipGroup.getChildCount(); i++) {
                    View child = chipGroup.getChildAt(i);
                    if (child instanceof Chip && ((Chip) child).isChecked()) {
                        specialties.add(String.valueOf(child.getTag()));
                    }
                }
                if (specialties.isEmpty()) {
                    Toast.makeText(this, "اختر تخصصاً واحداً على الأقل", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Intent intent = new Intent(this, Register2.class);
            intent.putExtra("role", isWorker ? "worker" : "client");
            intent.putExtra("firstName", first);
            intent.putExtra("lastName", last);
            intent.putExtra("city", city);
            intent.putStringArrayListExtra("specialties", specialties);
            startActivity(intent);
        }
    }

