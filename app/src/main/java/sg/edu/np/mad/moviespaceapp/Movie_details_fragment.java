package sg.edu.np.mad.moviespaceapp;

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


    // moviemodelclass data
    String movie_id,movie_name,poster_path;

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
        movie_name = bundle.getString("movie_name");
        poster_path = bundle.getString("poster_path");

        // set the info to textview in the xml
        ImageView movie_poster = view.findViewById(R.id.movie_poster);
        TextView movie_title = view.findViewById(R.id.title_placeholder);
        TextView overview_placeholder = view.findViewById(R.id.overview_placeholder);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + poster_path).into(movie_poster);
        movie_title.setText(movie_name);

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
}