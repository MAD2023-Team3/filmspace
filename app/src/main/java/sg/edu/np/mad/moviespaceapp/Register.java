package sg.edu.np.mad.moviespaceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    String TAG = "Register Activity";
    TextInputEditText editTextEmail, editTextpassword;
    Button btn_register;
    TextView btn_click_to_login;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail =findViewById(R.id.register_email);
        editTextpassword = findViewById(R.id.register_password);
        btn_register = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_register);
        btn_click_to_login =findViewById(R.id.click_to_login);
        firestore = FirebaseFirestore.getInstance();


        // directs you to the login activty
        btn_click_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });

        // register button
        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email= String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextpassword.getText());

                // if the email textbox is empty
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Enter email",Toast.LENGTH_SHORT).show();
                }

                // if the password textbox is empty
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Enter password",Toast.LENGTH_SHORT).show();
                }

                // creates the user profile in the firestore database
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");

                                    // add user data into realtime firebase database
                                    userID = mAuth.getCurrentUser().getUid();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("email",email);
                                    user.put("password",password);
                                    firestore.collection("users").document(userID).set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d(TAG,"success firestore");
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG,"unsucess firestore");
                                                }
                                            });

                                    //

                                    Toast.makeText(Register.this, "Authentication success.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),Login.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure");
                                    Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}