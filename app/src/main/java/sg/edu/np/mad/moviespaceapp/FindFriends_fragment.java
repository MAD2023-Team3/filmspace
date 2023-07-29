package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FindFriends_fragment extends Fragment {

    View view;
    RecyclerView findFriends_recyclerview;

    // related to firestore db
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    String username;
    FirebaseFirestore firestoredb;
    //

    public FindFriends_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.findfriends_fragment, container, false);

        findFriends_recyclerview = view.findViewById(R.id.findFriends_recyclerview);

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

        /*// reading the documentReference_user db
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
        });*/

    }
}