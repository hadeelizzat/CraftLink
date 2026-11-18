package com.example.craftlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Patterns;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseNetworkException;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText etEmail, etPassword;
    private MaterialButton btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // تعريف العناصر
        auth = FirebaseAuth.getInstance();
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v ->  login());
        findViewById(R.id.tvGoRegister).setOnClickListener(v ->
                startActivity(new Intent(this, Register1.class)));

        findViewById(R.id.tv_ForgetPassword).setOnClickListener
                (v -> showForgotPasswordDialog());

    }

    private void login() {
        layoutEmail.setError(null);
        layoutPassword.setError(null);

        String email = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        String pass = etPassword.getText() == null ? "" : etPassword.getText().toString();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("اكتب بريداً إلكترونياً صحيحاً");
            return;
        }
        if (pass.isEmpty()) {
            layoutPassword.setError("اكتب كلمة المرور");
            return;
        }

        setLoading(true);
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            setLoading(false);
            if (task.isSuccessful()) {
                goHome();
            } else {
                String errorMessage = task.getException() != null ? task.getException().getMessage() : "حدث خطأ ما";
                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showForgotPasswordDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        TextInputLayout layoutResetEmail = view.findViewById(R.id.layoutRestEmail);
        TextInputEditText etResetEmail = view.findViewById(R.id.etRestEmail);

        // لو كاتب البريد بصفحة الدخول نعبّيه تلقائياً
        String typed = etEmail.getText() == null ? "" : etEmail.getText().toString().trim();
        etResetEmail.setText(typed);

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setPositiveButton("إرسال الرابط", null) // null عشان ما يسكر تلقائياً
                .setNegativeButton("إلغاء", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnSend = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnSend.setOnClickListener(v -> {
                layoutResetEmail.setError(null);
                String email = etResetEmail.getText() == null
                        ? "" : etResetEmail.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    layoutResetEmail.setError("اكتب بريداً إلكترونياً صحيحاً");
                    return;
                }

                btnSend.setEnabled(false);
                auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                        Toast.makeText(this,
                                "إذا كان البريد مسجلاً، سيصلك رابط إعادة التعيين",
                                Toast.LENGTH_LONG).show();
                    } else {
                        btnSend.setEnabled(true);
                        String msg = task.getException() instanceof FirebaseNetworkException
                                ? "تحقق من اتصال الإنترنت"
                                : "تعذر الإرسال، حاول مرة أخرى";
                        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    }
                });
            });
        });

        dialog.show();
    }
    private void setLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? "جاري الدخول..." : getString(R.string.login_btn));
    }

    private void goHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}


