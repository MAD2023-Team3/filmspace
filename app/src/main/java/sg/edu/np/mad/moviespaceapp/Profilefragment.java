package sg.edu.np.mad.moviespaceapp;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Friend_adaptor.Friend_RecyclerView_Adaptor;
import sg.edu.np.mad.moviespaceapp.Friend_adaptor.Friend_RecyclerView_Interface;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;

public class Profilefragment extends Fragment implements Friend_RecyclerView_Interface {

   View view;
    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    //
   TextView profile_username,profile_email,profile_fame;
    Button edit_profile_btn;
    ImageView default_profile_picture;
    View friend_request_button;
    Button test_notifications;
    RecyclerView friends_recyclerView;
    List<UserModelClass> Friends_list = new ArrayList<>();
    Friend_RecyclerView_Adaptor adaptor;

    public Profilefragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profilefragment, container, false);

        profile_fame = view.findViewById(R.id.fame_count);
        profile_email = view.findViewById(R.id.emailfrag_email);
        profile_username = view.findViewById(R.id.profilefrag_username);
        edit_profile_btn = view.findViewById(R.id.edit_profile_btn);
        default_profile_picture = view.findViewById(R.id.default_profile_picture);
        friend_request_button = view.findViewById(R.id.btn_Friend_requests);
        friends_recyclerView = view.findViewById(R.id.friends_recyclerview);

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_profile_fragment fragment = new Update_profile_fragment();
                getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });

        friend_request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragment transaction
                Friend_request_fragment fragment = new Friend_request_fragment();
                getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // firestore database
        user = FirebaseAuth.getInstance().getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();
        documentReference_user = firestoredb.collection("users").document(userUid);
        StorageReference firebasestorage_reference = FirebaseStorage.getInstance().getReference().child("profile_pic").child(userUid);

        // reading the documentReference_user db
        documentReference_user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Retrieve field field
                String username = documentSnapshot.getString("username");
                String email = documentSnapshot.getString("email");
                Double fame = documentSnapshot.getDouble("fame");
                Integer int_fame = fame.intValue();
                // sets the values
                profile_fame.setText(int_fame.toString());
                profile_email.setText(email);
                profile_username.setText(username);

                // Use the field value as needed
                Log.d("Firestore", "Field value: " + username);
                List<String> friends_list = (List<String>) documentSnapshot.get("Friends_list");
                // generate the recyclerview if the Friend_request_list.size is more than 0. So must have at least 1 item.
                assert friends_list != null;
                if(friends_list.size()>0){
                    // to put generate the recyclerview
                    get_friend_info_and_insert_recyclerview(friends_list);
                }
            }
        });

        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Uri uri = task.getResult();
                        Glide.with(getContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(default_profile_picture);
                    }
                });

    }

    void setup_friend_request_recyclerview(List<UserModelClass> friends_list){
        friends_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new Friend_RecyclerView_Adaptor(friends_list,getContext(),this);
        LinearLayoutManager myLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        friends_recyclerView.setAdapter(adaptor);
        friends_recyclerView.setLayoutManager(myLayoutManager);
    }
    public void get_friend_info_and_insert_recyclerview(List<String> friends_list){

        for(int i =0;i<friends_list.size();i++){
            firestoredb.collection("users").document(friends_list.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserModelClass user_friend = new UserModelClass();
                            // get what information is needed
                            user_friend.setUsername(document.getString("username"));
                            user_friend.setUserId(document.getString("userId"));
                            Friends_list.add(user_friend);

                            // when an item is added to Friend_list, update recyclerview
                            adaptor.notifyItemInserted(Friends_list.size()-1);
                        } else {

                        }
                    } else {

                    }
                }
            });
        }
        setup_friend_request_recyclerview(Friends_list);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("friend_userId",Friends_list.get(position).getUserId());
        Friend_details_fragment fragment = new Friend_details_fragment();
        fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }

//    public void testNotifications(View v) {
//        Context context = getActivity();
//        Notification notification = new NotificationCompat.Builder(context.this, "testChannel")
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(getString(R.string.testNotificationTitle))
//                .setContentText(getString(R.string.testNotificationContent))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//    }
}