package com.example.craftlink;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Register2 extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutPassword, layoutConfirmPassword;
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    // بيانات الخطوة 1
    private String role, firstName, lastName, city;
    private ArrayList<String> specialties;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        Intent in = getIntent();
        role = in.getStringExtra("role");
        firstName = in.getStringExtra("firstName");
        lastName = in.getStringExtra("lastName");
        city = in.getStringExtra("city");
        specialties = in.getStringArrayListExtra("specialties");
        if (specialties == null) specialties = new ArrayList<>();

        // لو فتحت الصفحة بدون بيانات الخطوة 1 نرجع
        if (role == null || firstName == null) {
            finish();
            return;
        }

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutConfirmPassword =findViewById(R.id.layoutConfirmPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> register());
        findViewById(R.id.tvGoLogin).setOnClickListener(v ->
                startActivity(new Intent(this, Login.class)));
    }

    private void register() {
        layoutEmail.setError(null);
        layoutPassword.setError(null);
        layoutConfirmPassword.setError(null);

        String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        String pass = etPassword.getText() == null ? "" : etPassword.getText().toString();
        String conf_pass = etPassword.getText() == null ? "" : etConfirmPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("اكتب بريداً إلكترونياً صحيحاً");
            etEmail.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            layoutPassword.setError("كلمة المرور 6 أحرف على الأقل");
            etPassword.requestFocus();
            return;
        }
        if(!pass.equals(conf_pass)){
            layoutConfirmPassword.setError("كلمتا المرور غير متطابقة ");
            etConfirmPassword.requestFocus();
            return;
        }

        setLoading(true);

        // 1) إنشاء الحساب في Firebase Authentication
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                setLoading(false);
                Toast.makeText(this, AuthErrors.message(task.getException()), Toast.LENGTH_LONG).show();
                return;
            }

            FirebaseUser user = task.getResult().getUser();
            if (user == null) {
                setLoading(false);
                return;
            }

            // 2) حفظ بيانات المستخدم في Firestore
            Map<String, Object> data = new HashMap<>();
            data.put("role", role);
            data.put("firstName", firstName);
            data.put("lastName", lastName);
            data.put("city", city);
            data.put("specialties", specialties);
            data.put("email", email);
            data.put("createdAt", FieldValue.serverTimestamp());

            db.collection("users").document(user.getUid()).set(data)
                    .addOnCompleteListener(t2 -> {
                        if (t2.isSuccessful()) {
                            goHome();
                        } else {
                            // فشل الحفظ: نحذف الحساب ليقدر يعيد المحاولة
                            user.delete();
                            setLoading(false);
                            Toast.makeText(this, "تعذر حفظ البيانات، حاول مرة أخرى", Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void setLoading(boolean loading) {
        btnRegister.setEnabled(!loading);
        btnRegister.setText(loading ? "جاري إنشاء الحساب..." : getString(R.string.create_account));
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

