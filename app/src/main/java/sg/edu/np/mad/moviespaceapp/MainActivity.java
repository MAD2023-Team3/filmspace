package sg.edu.np.mad.moviespaceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    SearchView search_view;
    RecyclerView recycler_view_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // firestore database



        search_view = findViewById(R.id.search_view);
        recycler_view_home = findViewById(R.id.recycler_view_home);
        // nav bar and nav drawer

        //

    }
}