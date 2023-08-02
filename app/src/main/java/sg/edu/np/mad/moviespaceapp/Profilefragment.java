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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profilefragment extends Fragment {

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
    public void onStart(){
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
            }
        });

        firebasestorage_reference.getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        Glide.with(getContext()).load(uri).apply(RequestOptions.circleCropTransform()).into(default_profile_picture);
                    }
                });

    }
}