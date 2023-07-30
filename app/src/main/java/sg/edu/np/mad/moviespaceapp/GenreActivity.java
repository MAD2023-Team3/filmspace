package sg.edu.np.mad.moviespaceapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.Model.MovieModelClass;

public class GenreActivity extends Activity {
    ListView listViewData;
    ArrayAdapter<String> adapter;

    DocumentReference documentReference_user;

    String userUid;

    String user_email;
    String user_fame;
    String user_pass;
    String user_watchlist;

    String username;
    FirebaseFirestore firestoredb;

    Button nextBtn;
    String[] arrayPeliculas = {"Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama", "Family", "Fantasy", "History", "Horror", "Music", "Mystery", "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"};

    ArrayList<String> genreID = new ArrayList<>(Arrays.asList(
            "28", "12", "16", "35", "80", "99", "18", "10751", "14", "36", "27", "10402",
            "9648", "10749", "878", "10770", "53", "10752", "37"
    ));

    ArrayList<String> Genre_list = new ArrayList<>();
    private static String All_Genres_JSON_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    String all_genres_api_tag = "all_genres_api_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.genre_page);

        listViewData = findViewById(R.id.listView_data);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, arrayPeliculas);
        listViewData.setAdapter(adapter);
        nextBtn = findViewById(R.id.button4);
        nextBtn.setOnClickListener(this::onClick);
    }

    public void onClick(View view) {
        int viewId = view.getId();

        if(viewId == R.id.button4) {
            String itemSelected = "Selected items: \n";
            for (int i = 0; i < listViewData.getCount(); i++) {
                if (listViewData.isItemChecked(i)) {
                    itemSelected += listViewData.getItemAtPosition(i) + "\n";
                    Genre_list.add(genreID.get(i));
                }
            }
            onSuccess();
            Toast.makeText(this, itemSelected, Toast.LENGTH_SHORT).show();

        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void onSuccess() {
        // actors firebase code
        // update fame for actor
        // first check if the actor's id is already present in db
        // if not then add new document
        // firestore database
        firestoredb = FirebaseFirestore.getInstance();
        userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);

        // Data to be added to the new document
        Map<String, Object> newData = new HashMap<>();
        newData.put("Genre_list", Genre_list);

        documentReference_user.set(newData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success","Success");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}