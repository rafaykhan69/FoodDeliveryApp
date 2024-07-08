package com.example.fooddelivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Chefsendotp extends AppCompatActivity {

    private String verificationId;
    private FirebaseAuth FAuth;
    private Button verify, Resend;
    private TextView txt;
    private EditText entercode;
    private String phoneno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefsendotp);

        phoneno = getIntent().getStringExtra("Phonenum").trim();

        entercode = findViewById(R.id.code);
        txt = findViewById(R.id.text);
        Resend = findViewById(R.id.Resendotp);
        verify = findViewById(R.id.Verify);
        FAuth = FirebaseAuth.getInstance();

        Resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendVerificationCode(phoneno);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = entercode.getText().toString().trim();
                Resend.setVisibility(View.INVISIBLE);

                if (code.isEmpty() || code.length() < 6) {
                    entercode.setError("Enter code");
                    entercode.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend Code Within " + millisUntilFinished / 1000 + " Seconds");
            }

            @Override
            public void onFinish() {
                Resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.INVISIBLE);
            }
        }.start();

        Resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resend.setVisibility(View.INVISIBLE);
                resendOtp(phoneno);

                new CountDownTimer(60000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt.setVisibility(View.VISIBLE);
                        txt.setText("Resend Code Within " + millisUntilFinished / 1000 + " Seconds");
                    }

                    @Override
                    public void onFinish() {
                        Resend.setVisibility(View.VISIBLE);
                        txt.setVisibility(View.INVISIBLE);
                    }
                }.start();
            }
        });
    }

    private void resendOtp(String phonenum) {
        sendVerificationCode(phonenum);
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60L,
                TimeUnit.SECONDS,
                this, // use 'this' instead of TaskExecutors.MAIN_THREAD
                mCallbacks
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                entercode.setText(code);  // Auto Verification
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(Chefsendotp.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        FAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Chefsendotp.this, ChefFoodPanel_BottomNavigation.class));
                            finish();
                        } else {
                            Toast.makeText(Chefsendotp.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
