package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.Actoradaptors.ActorRecyclerViewAdapter;

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
    ActorModelClass actorModelClass;
    List<ActorModelClass> actormodellist;
    RecyclerView actorrecyclerview;

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
        String api_tag_movieLink = "movie_api";
        GetData getDatamovieDetails = new GetData(movieLink,api_tag_movieLink);
        getDatamovieDetails.execute();

        // api request for movie credits
        String creditLink = String.format("https://api.themoviedb.org/3/movie/%s/credits?api_key=d51877fbcef44b5e6c0254522b9c1a35", movie_id);
        String api_tag_creditLink = "credits_api";
        GetData getDataActorDetails = new GetData(creditLink,api_tag_creditLink);
        getDataActorDetails.execute();

        // recycler view actor
        actorrecyclerview = view.findViewById(R.id.cast_recyclerview);
        actormodellist = new ArrayList<ActorModelClass>();

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
        private String api_tag;

        public GetData(String jsonUrl,String api_tag){
            this.jsonUrl = jsonUrl;
            this.api_tag = api_tag;
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
            if(api_tag.equals("movie_api")){
                try{
                    JSONObject jsonObject = new JSONObject(s);

                    // scans the jsonArray received from api request turns the request to
                    // MovieModelClass and inserts that in movieList

                    model = new MovieModelClass();
                    model.setId(jsonObject.getString("id"));
                    model.setMovie_name(jsonObject.getString("title"));
                    model.setImg(jsonObject.getString("poster_path"));
                    model.setOverview(jsonObject.getString("overview"));

                    settotext(model);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            } else if (api_tag.equals("credits_api")) {
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("cast");

                    // scans the jsonArray received from api request turns the request to
                    // MovieModelClass and inserts that in movieList
                    for(int i = 0; i<jsonArray.length();i++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        ActorModelClass actorModelClassobj = new ActorModelClass();

                        actorModelClassobj.setId(jsonObject1.getString("id"));
                        actorModelClassobj.setActor_name(jsonObject1.getString("name"));
                        actorModelClassobj.setKnown_for_department(jsonObject1.getString("known_for_department"));
                        actorModelClassobj.setActor_profile_path(jsonObject1.getString("profile_path"));
                        actorModelClassobj.setPlaying_character(jsonObject1.getString("character"));

                        Log.d("actorobj",actorModelClassobj.toString());
                        actormodellist.add(actorModelClassobj);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                // code here
                putDataIntoRecyclerView(actormodellist,actorrecyclerview);
            }
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

    private void putDataIntoRecyclerView(List<ActorModelClass> actormodellist, RecyclerView recyclerView){
        ActorRecyclerViewAdapter adapter = new ActorRecyclerViewAdapter(getContext(),actormodellist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(myLayoutManager);
    }
}