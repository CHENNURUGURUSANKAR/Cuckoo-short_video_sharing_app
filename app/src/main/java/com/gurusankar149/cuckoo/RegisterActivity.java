package com.gurusankar149.cuckoo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText name, email, password, con_password;
    private String sname, semail, spassword, scon_password;
    private Button Create;
    LinearLayout login_area;
    private FirebaseAuth mAuth;
    private Dialog progess_dailog;
    private TextView dailog_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.reg_name_text);
        email = findViewById(R.id.reg_email_text);
        password = findViewById(R.id.reg_password_text);
        con_password = findViewById(R.id.reg_con_password_text);
        Create = findViewById(R.id.create_acct_button);
        login_area = findViewById(R.id.login_layout);
        mAuth = FirebaseAuth.getInstance();
        progess_dailog = new Dialog(this);
        progess_dailog.setCancelable(false);
        progess_dailog.setContentView(R.layout.progress_dailog);
        progess_dailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dailog_text = progess_dailog.findViewById(R.id.progess_text);
        dailog_text.setText("Creating Account for you...");
        login_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                // RegisterActivity.this.finish();
            }
        });
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sname = name.getText().toString().trim();
                semail = email.getText().toString().trim();
                spassword = password.getText().toString().trim();
                scon_password = con_password.getText().toString().trim();
                if (TextUtils.isEmpty(sname)) {
                    name.setError("Please enter name");
                    return;
                } else if (TextUtils.isEmpty(semail)) {
                    email.setError("Please enter email");
                    return;
                } else if (TextUtils.isEmpty(spassword)) {
                    password.setError("Please enter passsword");
                    return;
                } else if (TextUtils.isEmpty(scon_password)) {
                    con_password.setError("Please enter confirm password");
                    return;
                } else if (!TextUtils.equals(spassword, scon_password)) {
                    con_password.setError("password not matching");
                    return;
                } else if (!semail.contains("@gmail.com")) {
                    email.setError("add @gmail.com \n" + "enter a valid email");
                    return;
                } else if (spassword.length() < 8) {
                    password.setError("more than 8");
                    return;
                } else {

                    createAccount(semail, scon_password, sname);
                    Toast.makeText(RegisterActivity.this, "You can create account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createAccount(String semail, String scon_password, String sname) {
        progess_dailog.show();
        mAuth.createUserWithEmailAndPassword(semail, scon_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getAdditionalUserInfo().isNewUser())
                    {
                        FirebaseUser user=mAuth.getCurrentUser();

                        Database.storeUserData(semail, sname, user.getUid(), new MycompleteListener() {
                            @Override
                            public void OnSuccess() {
                                progess_dailog.dismiss();
                                startActivity(new Intent(getApplicationContext(), UploadExistVideo.class));
                                RegisterActivity.this.finish();
                            }

                            @Override
                            public void OnFailure() {
                                Toast.makeText(getApplicationContext(),"Something went worng",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }else{
                        progess_dailog.dismiss();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        Toast.makeText(RegisterActivity.this, " You have already Account "+"\n"+"Please Login", Toast.LENGTH_SHORT).show();

                    }


                } else {
                    progess_dailog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Account not created", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(getApplicationContext(), UploadExistVideo.class));
            RegisterActivity.this.finish();
        }
    }
}