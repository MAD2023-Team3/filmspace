package sg.edu.np.mad.moviespaceapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;
import sg.edu.np.mad.moviespaceapp.SendFameDialog.SendFameDialog;

public class Friend_details_fragment extends Fragment implements SendFameDialog.OnInputSelected {
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
    TextView friend_username;
    Button btn_send_fame_tofriend;
    TextView nav_fame;
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
}