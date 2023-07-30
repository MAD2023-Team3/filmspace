package sg.edu.np.mad.moviespaceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {
    String TAG = "Register Activity";
    // TextInput
    TextInputEditText editTextEmail, editTextpassword,editTextusername,editTextconfirmpassword;
    Button btn_register;
    TextView btn_click_to_login;
    // Firebase
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    String userID;
    // progress Bar
    ProgressBar progressBar;

    // used for register button
    String email,password,username,confirm_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail =findViewById(R.id.register_email);
        editTextpassword = findViewById(R.id.register_password);
        editTextconfirmpassword = findViewById(R.id.register_confirm_password);
        editTextusername = findViewById(R.id.register_username);
        btn_register = findViewById(R.id.btn_register);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_register);
        btn_click_to_login =findViewById(R.id.click_to_login);
        firestore = FirebaseFirestore.getInstance();


        // directs you to the login activty
        btn_click_to_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return false;
            }
        });
        // register button
        btn_register.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                progressBar.setVisibility(View.VISIBLE);
                email= String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextpassword.getText());
                confirm_password = String.valueOf(editTextconfirmpassword.getText());
                username = String.valueOf(editTextusername.getText());
                List<String> watchlater_list=new ArrayList<String>();
                Integer fame = 100;

                if(TextUtils.isEmpty(username)){
                    // if the name textbox is empty
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Enter name",Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(email)) {
                    // if the email textbox is empty
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Enter email",Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(password)) {
                    // if the password textbox is empty
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Enter password",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(confirm_password)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Enter confirm password",Toast.LENGTH_SHORT).show();
                }else if(!confirm_password.equals(password)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"Wrong Confirm Password",Toast.LENGTH_SHORT).show();
                } else if(confirm_password.length()<=5){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Register.this,"password must contain at least 6 characters",Toast.LENGTH_SHORT).show();
                }else{
                    // creates the user profile in the firestore database
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");

                                        // update mAuth
                                        mAuth = FirebaseAuth.getInstance();

                                        // add user data into realtime firebase database
                                        userID = mAuth.getCurrentUser().getUid();
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("email",email);
                                        user.put("password",password);
                                        user.put("username",username);
                                        user.put("watchlist_array",watchlater_list);
                                        user.put("fame",fame);
                                        firestore.collection("users").document(userID).set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d(TAG,"success firestore");
                                                        return null;
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
                                        try {
                                            throw task.getException();
                                        }  catch (FirebaseAuthInvalidCredentialsException e) {
                                            // Handle invalid email exception
                                            Toast.makeText(Register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                        } catch (FirebaseAuthUserCollisionException e) {
                                            // Handle collision with existing user (if there is already the same email in the firebase)
                                            Toast.makeText(Register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // start: Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
        // end: Check if user is signed in (non-null) and update UI accordingly.
    }
}