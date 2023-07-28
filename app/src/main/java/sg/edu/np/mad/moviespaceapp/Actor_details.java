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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.Model.ActorModelClass;
import sg.edu.np.mad.moviespaceapp.SendFameDialog.SendFameDialog;


public class Actor_details extends Fragment implements SendFameDialog.OnInputSelected {
    // related to firestore db
    FirebaseUser user;
    DocumentReference documentReference_user,docref_actor;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    String actor_id,actor_profile_path,actor_name;
    View view;
    TextView nav_fame,profile_fame;

    public Actor_details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_actor_details, container, false);

        // set Textview
        nav_fame = getActivity().findViewById(R.id.nav_fame);

        // unpack the bundle
        Bundle bundle = getArguments();
        actor_id= bundle.getString("Actor_Id");

        //api request
        GetData getData = new GetData(String.format("https://api.themoviedb.org/3/person/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35",actor_id));
        getData.execute();

        // send fame button
        Button btn_send_fame = view.findViewById(R.id.btn_send_fame);
        btn_send_fame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFameDialog sendFameDialog = new SendFameDialog();
                sendFameDialog.setTargetFragment(Actor_details.this,1);
                sendFameDialog.show(getFragmentManager(),"dialog");
            }
        });

        return view;
    }

    // input is fame sent
    @Override
    public void sendInput(int input) {
        updatefame(input);
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
                ActorModelClass actorModelClassobj = new ActorModelClass();
                actor_profile_path = jsonObject.getString("profile_path");
                actor_name = jsonObject.getString("name");

                actorModelClassobj.setId(jsonObject.getString("id"));
                actorModelClassobj.setActor_profile_path(actor_profile_path);
                actorModelClassobj.setActor_name(actor_name);
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

    public void updatefame(int sent_fame){
        // firestore database
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        // document reference for actors collection
        docref_actor =firestoredb.collection("actors").document(actor_id);
        //

        Log.d("fafafafafafaf",String.valueOf(sent_fame));
        // get user's fame count
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field

                // get user's fame
                Double users_fame = documentSnapshot.getDouble("fame");
                int int_users_fame = users_fame.intValue();
                Log.v("user_fame",String.valueOf(users_fame));

                // check if user has more fame than the fame they are trying to send
                if(users_fame>sent_fame){

                    // user's new fame count
                    int new_users_fame = int_users_fame-sent_fame;

                    // fame count in navbar
                    nav_fame.setText("Fame:" + String.valueOf(new_users_fame));
                    Log.v("new_users_fame",String.valueOf(new_users_fame));

                    Map<String,Object> updatedData = new HashMap<>();
                    updatedData.put("fame",new_users_fame);

                    // update user's fame count in db
                    documentReference_user.set(updatedData, SetOptions.merge())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // actors firebase code
                                    // update fame for actor
                                    // first check if the actor's id is already present in db
                                    // if not then add new document
                                    Map<String,Object> actor = new HashMap<>();
                                    actor.put("actor_id",actor_id);
                                    actor.put("name",actor_name);
                                    actor.put("fame",sent_fame);
                                    actor.put("actor_profile_path",actor_profile_path);

                                    docref_actor.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    // Document(actor data) exists
                                                    // update the actor's fame count
                                                    Double actor_fame = document.getDouble("fame");
                                                    int int_actor_fame = actor_fame.intValue();
                                                    int updated_fame = int_actor_fame + sent_fame;
                                                    Log.d("actorname",String.valueOf(document.getString("name")));
                                                    Log.d("actor_fame",String.valueOf(actor_fame));
                                                    Log.d("updatedfame",String.valueOf(updated_fame));
                                                    actor.put("fame",updated_fame);
                                                    docref_actor.set(actor,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    });
                                                    // Perform actions with the document data
                                                } else {
                                                    // Document(actor data) doesn't exist/ is not on db

                                                    docref_actor.set(actor) // pushes actor hashmap to db
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    Log.d("UPDATES FIRESTORE ACTOR","success firestore");
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d("FAILES FIRESTORE ACTOR","fails firestore");
                                                                }
                                                            });
                                                }
                                            } else {
                                                // An error occurred while fetching the document
                                                Log.d("TAG", "Error getting document: " + task.getException());
                                            }
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                    // update fame textview
                }else {
                    Toast.makeText(getContext(),"Insufficient Fame",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}