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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Actor_details extends Fragment {
    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    String actor_id;
    View view;
    public Actor_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_actor_details, container, false);

        // firestore database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        //
        // unpack the bundle
        Bundle bundle = getArguments();
        actor_id= bundle.getString("Actor_Id");

        //api request
        GetData getData = new GetData(String.format("https://api.themoviedb.org/3/person/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35",actor_id));
        getData.execute();

        // send fame
        // retrieving watchlater array from user info
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field
                Integer fame = (Integer) documentSnapshot.get("fame");

                // start: watch later code block
                Button btn_send_fame = view.findViewById(R.id.btn_send_fame);

                btn_send_fame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // if watchlater_list does not contain movie_id of selected movie
                        // add it to watchlater_list
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



    // get data

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
                ActorModelClass actorModelClassobj = new ActorModelClass();

                actorModelClassobj.setId(jsonObject.getString("id"));
                actorModelClassobj.setActor_profile_path(jsonObject.getString("profile_path"));
                actorModelClassobj.setActor_name(jsonObject.getString("name"));
                actorModelClassobj.setOverview(jsonObject.getString("biography"));

                settext(actorModelClassobj);

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    public void settext(ActorModelClass obj){
        ImageView actor_img = view.findViewById(R.id.Actor_img);
        TextView actor_name = view.findViewById(R.id.actor_name);
        TextView biography = view.findViewById(R.id.overview);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + obj.getActor_profile_path()).into(actor_img);
        actor_name.setText(obj.getActor_name());
        biography.setText(obj.getOverview());
    }
}