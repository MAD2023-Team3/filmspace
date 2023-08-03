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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class Login extends AppCompatActivity {

    String TAG = "Login";
    TextInputEditText editTextEmail, editTextpassword;
    Button buttonLogin;
    TextView btn_click_to_register;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    DocumentReference documentReference_user;

    String userUid;

    FirebaseFirestore firestoredb;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail =findViewById(R.id.login_email);
        editTextpassword = findViewById(R.id.login_password);
        buttonLogin = findViewById(R.id.btn_login);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar_login);
        btn_click_to_register =findViewById(R.id.click_to_register);

        btn_click_to_register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
                return false;
            }
        });

        // login click button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email= String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextpassword.getText());


                if(TextUtils.isEmpty(email)){
                    // if the email textbox is empty
                    Toast.makeText(Login.this,"Enter email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else if (TextUtils.isEmpty(password)) {
                    // if the password textbox is empty
                    Toast.makeText(Login.this,"Enter password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }else{
                    // when authenticated
                    // store in firestore
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        user = FirebaseAuth.getInstance().getCurrentUser();
                                        firestoredb = FirebaseFirestore.getInstance();
                                        userUid = user.getUid();
                                        documentReference_user = firestoredb.collection("users").document(userUid);
                                        documentReference_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> t) {
                                                if (t.isSuccessful()) {
                                                    DocumentSnapshot document = t.getResult();
                                                    if (document.exists()) {
                                                        if (document.contains("Genre_list")) {
                                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                            startActivity(intent);
                                                        }
                                                        else {
                                                            Intent intent = new Intent(getApplicationContext(), GenreActivity.class);
                                                            startActivity(intent);
                                                        }
                                                        // Perform actions with the document data
                                                    } else {

                                                    }
                                                } else {
                                                    // An error occurred while fetching the document
                                                    Log.d("TAG", "Error getting document: " + t.getException());
                                                }
                                            }
                                        });
                                        // Sign in success, update UI with the signed-in user's information

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure");

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