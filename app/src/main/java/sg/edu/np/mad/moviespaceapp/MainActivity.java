package sg.edu.np.mad.moviespaceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.SendFameDialog.SendFameDialog;

public class MainActivity extends AppCompatActivity{
    SearchView search_view;
    RecyclerView recycler_view_home;

    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    //

    //navbar and nav drawer
    DrawerLayout drawerLayout;
    ImageView btn_menu;
    LinearLayout btn_profile,btn_home,btn_logout,btn_watch_later,btn_leaderboard;
    TextView profile_username,profile_uid,nav_fame;
    //

    // methods involved in the nav_drawer
    public static void openDrawer(@NonNull DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    //

    // methods involved in fragment
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // default fragment page
        replaceFragment(new HomeFragment());
        //
        // firestore database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        FirebaseUser currentUser = auth.getCurrentUser();

        // reading the documentReference_user db
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field
                String username = documentSnapshot.getString("username");
                Double fame = documentSnapshot.getDouble("fame");
                Integer int_fame = fame.intValue();
                // sets the username and fame count in the nav_drawer to user's username
                nav_fame = findViewById(R.id.nav_fame);
                profile_username =findViewById(R.id.profile_username);

                profile_username.setText(username);
                String display_fame = String.format("Fame:" + int_fame);
                nav_fame.setText(display_fame);
                // Use the field value as needed
                Log.d("Firestore", "Field value: " + username);
            }
        });

        // checks if there is a user logged in. another layer of authentication
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        //


        // nav_drawer code block
        drawerLayout = findViewById(R.id.drawerLayout);
        btn_menu = findViewById(R.id.btn_menu);
        btn_profile = findViewById(R.id.nav_profile);
        btn_leaderboard = findViewById(R.id.nav_leaderboard);
        btn_home = findViewById(R.id.nav_Home);
        btn_logout = findViewById(R.id.nav_logout);
        btn_watch_later = findViewById(R.id.nav_watchlater);

        profile_uid = findViewById(R.id.profile_uid);


        // sets the uid in the nav_drawer
        profile_uid.setText(String.format("uid:%s", userUid));

        // navbar and navdrawer buttons
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Profilefragment());
                closeDrawer(drawerLayout);
            }

        });

        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new Leaderboardfragment());
                closeDrawer(drawerLayout);
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomeFragment());
                closeDrawer(drawerLayout);
            }

        });
        btn_watch_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new WatchLaterfragment());
                closeDrawer(drawerLayout);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        //


    }

    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);

    }

}