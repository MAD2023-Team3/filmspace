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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    SearchView search_view;
    RecyclerView recycler_view_home;

    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    //

    //navbar and nav drawer
    DrawerLayout drawerLayout;
    ImageView btn_menu;
    LinearLayout btn_profile,btn_home;
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
        btn_home = findViewById(R.id.nav_Home);

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
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new HomeFragment());
                closeDrawer(drawerLayout);
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