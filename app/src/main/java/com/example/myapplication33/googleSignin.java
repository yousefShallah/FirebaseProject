package com.example.myapplication33;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class googleSignin extends AppCompatActivity {

    Button createAccount, btn_signIn, sign_out;
    EditText email, password;
    TextView failure;
    private FirebaseAuth mAuth;
    String TAG = " CreateAccount";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_signin);
        btn_signIn = findViewById(R.id.btn_signIn);
        createAccount = findViewById(R.id.btn_signUp);
        sign_out = findViewById(R.id.sign_out);
        email = findViewById(R.id. email);
        password = findViewById(R.id.password);
        failure = findViewById(R.id.tv_display);

        mAuth = FirebaseAuth.getInstance();
        final String mail = email.getText().toString();
        final String pass = password.getText().toString();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount(mail, pass);
                hiedKeyBoard(googleSignin.this);
            }
        });
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginAccount(mail, pass);
                hiedKeyBoard(googleSignin.this);
            }
        });
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }
    public  static  void hiedKeyBoard (Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null){
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void CreateAccount ( String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d(TAG ,"create user with email : success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    failure.setText("");
                    UpdateUI(user);
                }else {
                    Log.d(TAG ,"create user with email : error" + task.getException());
                    failure.setText(task.getException().getMessage() + "");
                    UpdateUI(null);
                }
            }
        });

    }
    private void LoginAccount(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(googleSignin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            UpdateUI(null);
                        }

                        // ...
                    }
                });
    }
//    to do
    @SuppressLint("ResourceType")
    private void UpdateUI(FirebaseUser user){
        if ( user != null) {
            createSuccessSnackBar(findViewById(R.layout.support_simple_spinner_dropdown_item), " this email is : " + user.getEmail());
        }else {
            createFeliedSnackBar(findViewById(R.layout.support_simple_spinner_dropdown_item), "you have logged out plaese login agine");
        }
    }
    public Snackbar createSuccessSnackBar (View view, String message) {
        Snackbar snackbar = Snackbar.make(view , message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(R.color.colorAccent));
        return snackbar;
    }
    public Snackbar createFeliedSnackBar (View view, String message) {
        Snackbar snackbar = Snackbar.make(view , message, Snackbar.LENGTH_LONG);
        snackbar.getView().setBackgroundColor(view.getContext().getResources().getColor(R.color.colorPrimary));
        return snackbar;
    }
}

