package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie_details_fragment extends Fragment {

    View view;
    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    List<String> watchlater_list=new ArrayList<String>();
    //

    // JSON api url
    String JSON_URL = "https://api.themoviedb.org/3/movie/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    //
    // api details
    String movie_id,movie_name,poster_path,overview;
    MovieModelClass model;

    public Movie_details_fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_movie_details_fragment, container, false);

        // firestore database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        //

        // unpack the bundle
        Bundle bundle = getArguments();
        movie_id = bundle.getString("Movie_Id");

        // api request for movie details
        String movieLink = String.format("https://api.themoviedb.org/3/movie/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35", movie_id);
        GetData getData = new GetData(movieLink);
        getData.execute();

        // api request for movie credits
        String creditLink = String.format("https://api.themoviedb.org/3/movie/%scredits?api_key=d51877fbcef44b5e6c0254522b9c1a35", movie_id);

        // retrieving watchlater array from user info
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field
                List<String> array = (List<String>) documentSnapshot.get("watchlist_array");

                // add the retrieved array to watchlater_list
                if (array != null) {
                    for (Object item : array) {
                        watchlater_list.add((String) item);
                    }
                }
                // Use the field value as needed
                Log.d("Movie details fragment", "Field value: " + watchlater_list);

                // start: watch later code block
                Button btn_watch_later = view.findViewById(R.id.btn_watch_later);

                btn_watch_later.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // if watchlater_list does not contain movie_id of selected movie
                        // add it to watchlater_list
                        if(!watchlater_list.contains(movie_id)){
                            watchlater_list.add(movie_id);

                            // updating watchlater_list in firestore
                            Map<String,Object> updatedData = new HashMap<>();
                            updatedData.put("watchlist_array",watchlater_list);
                            documentReference_user.set(updatedData, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // when its already in your watch later
                                            Toast.makeText(getContext(),"Added to Watch Later",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                        }else{
                            // when its already in your watch later
                            Toast.makeText(getContext(),"Already added",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                // end: watch later code block
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return view;
    }

    public void onStart() {
        super.onStart();

    }

    // start: code block for GetData
    public class GetData extends AsyncTask<String,String,String> {
        private String jsonUrl;

        public GetData(String jsonUrl){
            this.jsonUrl = jsonUrl;
        }
        @Override
        protected String doInBackground(String... strings){
            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    Log.d("JSON",jsonUrl);
                    url = new URL(jsonUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if(urlConnection!= null){
                        urlConnection.disconnect();;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }

        // end: code block for GetData

        // start: converting json into object
        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                // scans the jsonArray received from api request turns the request to
                // MovieModelClass and inserts that in movieList

                model = new MovieModelClass();
                model.setId(jsonObject.getString("id"));
                model.setMovie_name(jsonObject.getString("title"));
                model.setImg(jsonObject.getString("poster_path"));
                model.setOverview(jsonObject.getString("overview"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            settotext(model);
        }
    }

    public void settotext(MovieModelClass obj){
        // set the info to textview in the xml
        ImageView movie_poster = view.findViewById(R.id.movie_poster);
        TextView movie_title = view.findViewById(R.id.title_placeholder);
        TextView overview = view.findViewById(R.id.overview);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + obj.getImg()).into(movie_poster);
        movie_title.setText(obj.getMovie_name());
        overview.setText(obj.getOverview());
    }
}