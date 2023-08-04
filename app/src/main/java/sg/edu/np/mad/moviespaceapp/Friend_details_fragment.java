package sg.edu.np.mad.moviespaceapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewAdapter;
import sg.edu.np.mad.moviespaceapp.Homeadaptor.HomeRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Model.MovieModelClass;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;
import sg.edu.np.mad.moviespaceapp.SendFameDialog.SendFameDialog;

public class Friend_details_fragment extends Fragment implements SendFameDialog.OnInputSelected, HomeRecyclerViewInterface {
    View view;
    String friend_userId;
    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    //

    List<String> watch_list;
    ImageView profile_picture;
    List<MovieModelClass> movieList = new ArrayList<>();
    TextView friend_username;
    Button btn_send_fame_tofriend,unfriend_btn;
    TextView nav_fame;

    RecyclerView friend_watch_later_list;
    private String JSON_API = "https://api.themoviedb.org/3/movie/%s?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    public Friend_details_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.friend_details_fragment, container, false);

        profile_picture = view.findViewById(R.id.friend_profile_picture);
        friend_username = view.findViewById(R.id.friend_username);
        btn_send_fame_tofriend = view.findViewById(R.id.btn_send_fame_tofriend);
        friend_watch_later_list = view.findViewById(R.id.friend_watch_later_list);
        unfriend_btn = view.findViewById(R.id.unfriend_btn);

        // set Textview
        nav_fame = getActivity().findViewById(R.id.nav_fame);

        // unpack the bundle
        Bundle bundle = getArguments();
        friend_userId = bundle.getString("friend_userId");

        btn_send_fame_tofriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendFameDialog sendFameDialog = new SendFameDialog();
                sendFameDialog.setTargetFragment(Friend_details_fragment.this,1);
                sendFameDialog.show(getFragmentManager(),"dialog");
            }
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        // firestore database
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        StorageReference firebasestorage_reference_friend_profilepic = FirebaseStorage.getInstance().getReference().child("profile_pic").child(friend_userId);

        unfriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Unfriend")
                        .setMessage("Are you sure you want to unfriend?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                remove_friend();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked Cancel button, perform any action if needed
                                dialog.dismiss(); // Close the dialog
                            }
                        });
                builder.show();
            }
        });

        //get friend's info to display
        firestoredb.collection("users").document(friend_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserModelClass friend_user = new UserModelClass();
                        friend_user.setWatchlist_array((List<String>) document.get("watchlist_array"));
                        friend_user.setUserId(document.getString("userId"));
                        friend_user.setUsername(document.getString("username"));
                        friend_user.setFame(document.getLong("fame").intValue());

                        // sends api to search for these movies
                        for(int i =0;i< friend_user.getWatchlist_array().size();i++){
                            String JSON_URL = String.format(JSON_API,friend_user.getWatchlist_array().get(i));
                            GetData getData = new GetData(JSON_URL);
                            getData.execute();
                        }

                        // displaying the information
                        friend_username.setText(friend_user.getUsername());
                        // retrieve their profile_picture
                        firebasestorage_reference_friend_profilepic.getDownloadUrl()
                                .addOnCompleteListener(t -> {
                                    if (t.isSuccessful()) {
                                        Uri uri = t.getResult();
                                        Glide.with(getContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(profile_picture);
                                    }
                                });

                    }else {
                    }
                }
            }
        });
    }

    @Override
    public void sendInput(int input) {
        // firestore database
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);

        documentReference_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // check if the fame sent is more than fame currently have
                        if(input <= document.getLong("fame").intValue()){
                            // user's new fame count
                            int new_currentuser_fame = document.getLong("fame").intValue()-input;

                            // fame count in navbar
                            nav_fame.setText("Fame:" + String.valueOf(new_currentuser_fame));
                            Map<String,Object> updatedData = new HashMap<>();
                            updatedData.put("fame",new_currentuser_fame);

                            firestoredb.collection("users").document(userUid)
                                    .set(updatedData, SetOptions.merge());

                            // after updating our value then update friend value
                            firestoredb.collection("users").document(friend_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot friend_document = task.getResult();
                                        if (friend_document.exists()) {
                                            Map<String,Object> friend_updatedData = new HashMap<>();
                                            friend_updatedData.put("fame",friend_document.getLong("fame").intValue() + input);

                                            firestoredb.collection("users").document(friend_userId)
                                                    .set(friend_updatedData, SetOptions.merge());
                                        }else {
                                        }
                                    }
                                }
                            });

                        }
                    }else {
                    }
                }
            }
        });

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

                MovieModelClass model = new MovieModelClass();
                model.setId(jsonObject.getString("id"));
                model.setMovie_name(jsonObject.getString("title"));
                model.setImg(jsonObject.getString("poster_path"));
                movieList.add(model);
            }catch (JSONException e){
                e.printStackTrace();
            }

            putDataIntoRecyclerView(movieList,friend_watch_later_list,"");

        }
    }

    // start: recyclerview code block sd
    private void putDataIntoRecyclerView(List<MovieModelClass> movieList, RecyclerView recyclerView, String recyclerviewIdentifier){
        HomeRecyclerViewAdapter adapter = new HomeRecyclerViewAdapter(getContext(),movieList,this,recyclerviewIdentifier);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(myLayoutManager);
    }
    @Override
    public void onItemClick(int position,String recyclerViewIdentifier) {
        Bundle bundle = new Bundle();
        bundle.putString("Movie_Id",movieList.get(position).getId());

        Movie_details_fragment movie_details_fragment = new Movie_details_fragment();
        movie_details_fragment.setArguments(bundle);
        // fragment transaction
        getParentFragmentManager().beginTransaction().replace(R.id.frameLayout,movie_details_fragment).commit();
    }
    // end: when clicking on a recyclerview item


    public void remove_friend(){
        // adds the friend uid to a Friends_list in firestore for the current user
        documentReference_user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.contains("Friends_list")) {
                        List<String> Friends_list = (List<String>)document.get("Friends_list");
                        Friends_list.remove(friend_userId);

                        // update values
                        Map<String, Object> data = new HashMap<>();
                        data.put("Friends_list", Friends_list);

                        firestoredb.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(data, SetOptions.merge());
                    }else {

                    }
                }
            }
        });

        // friend
        firestoredb.collection("users").document(friend_userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.contains("Friends_list")) {
                        List<String> Friends_list = (List<String>)document.get("Friends_list");
                        Friends_list.remove(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        // update values
                        Map<String, Object> data = new HashMap<>();
                        data.put("Friends_list", Friends_list);

                        firestoredb.collection("users").document(friend_userId)
                                .set(data, SetOptions.merge());
                    }else {

                    }
                }
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Profilefragment()).commit();
    }
}