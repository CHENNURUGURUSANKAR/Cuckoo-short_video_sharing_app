package com.gurusankar149.cuckoo;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private Button login, create;
    private TextInputEditText email, password, con_password;
    private String semail, spasswrod, scon_password;
    private FirebaseAuth mAuth;
    private Dialog progess_dailog;
    private TextView dailog_text;
    private LinearLayout google_sign_in_area;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 149;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.email_sign_in_button);
        email = findViewById(R.id.email_text_login);
        password = findViewById(R.id.password_text_login);
        con_password = findViewById(R.id.con_password_text_login);
        create = findViewById(R.id.create_acct_button_login);
        mAuth = FirebaseAuth.getInstance();
        google_sign_in_area = findViewById(R.id.google_sign_in_area);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInGoogle();
            }
        });
        progess_dailog = new Dialog(this);
        progess_dailog.setCancelable(false);
        progess_dailog.setContentView(R.layout.progress_dailog);
        progess_dailog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dailog_text = progess_dailog.findViewById(R.id.progess_text);
        dailog_text.setText("Please wait Login ...");

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semail = email.getText().toString().trim();
                spasswrod = password.getText().toString().trim();
                scon_password = con_password.getText().toString().trim();
                if (TextUtils.isEmpty(semail)) {
                    email.setError("Please enter email");
                    return;
                } else if (TextUtils.isEmpty(spasswrod)) {
                    password.setError("Please enter password");
                    return;
                } else if (TextUtils.isEmpty(scon_password)) {
                    con_password.setError("Please enter password");
                    return;
                } else if (!TextUtils.equals(spasswrod, scon_password)) {
                    con_password.setError("password not matching");
                    return;
                } else if (!semail.contains("@gmail.com")) {
                    email.setError("Please enter a valid email");
                    return;
                } else {
                    progess_dailog.show();
                    mAuth.signInWithEmailAndPassword(semail, scon_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getApplicationContext(), UploadExistVideo.class));
                                        Toast.makeText(LoginActivity.this, "Login", Toast.LENGTH_SHORT).show();


                                    } else {
                                        Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                                        progess_dailog.dismiss();

                                    }
                                }
                            });
                    Toast.makeText(LoginActivity.this, "you can login", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void SignInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        dailog_text.setText("SignIn With Google Please Wait...");
        progess_dailog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                              Database.storeUserData(user.getEmail(), user.getDisplayName(), user.getUid(), new MycompleteListener() {
                                  @Override
                                  public void OnSuccess() {
                                      progess_dailog.dismiss();
                                      startActivity(new Intent(getApplicationContext(), UploadExistVideo.class));
                                      LoginActivity.this.finish();
                                  }

                                  @Override
                                  public void OnFailure() {
                                      Toast.makeText(getApplicationContext(),"Something went worng",Toast.LENGTH_SHORT).show();

                                  }
                              });



                            }else {

                                progess_dailog.dismiss();
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(getApplicationContext(), UploadExistVideo.class));
                                LoginActivity.this.finish();
                        }


                } else{
            progess_dailog.dismiss();
            // If sign in fails, display a message to the user.
            Log.d("TAG", "signInWithCredential:failure", task.getException());
            Toast.makeText(LoginActivity.this, "google sign fail", Toast.LENGTH_SHORT).show();
        }
    }
});
        }

@Override
protected void onStart(){
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
        startActivity(new Intent(getApplicationContext(),UploadExistVideo
        .class));
        LoginActivity.this.finish();
        }
        }
        }