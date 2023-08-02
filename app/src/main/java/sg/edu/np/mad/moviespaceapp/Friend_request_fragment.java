package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Friend_requestadaptor.Friend_requestRecyclerViewAdaptor;
import sg.edu.np.mad.moviespaceapp.Model.UserModelClass;

public class Friend_request_fragment extends Fragment {

    View view;
    // Firebase
    FirebaseFirestore firestore;
    String userID;

    ArrayList<UserModelClass> Friend_Request = new ArrayList<>();
    DocumentReference user_documentreferece;

    Friend_requestRecyclerViewAdaptor adaptor;
    // progress Bar
    public Friend_request_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.friend_request_fragment, container, false);


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        firestore = FirebaseFirestore.getInstance();
        user_documentreferece = firestore.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // users friend request list
        user_documentreferece.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if(document.contains("Friend_Request")){
                            List<String> Friend_request_list = (List<String>)document.get("Friend_Request");
                            // generate the recyclerview if the Friend_request_list.size is more than 0. So must have at least 1 item.
                            assert Friend_request_list != null;
                            if(Friend_request_list.size()>0){
                                // to put generate the recyclerview
                                get_other_user_info_and_insert_recyclerview(Friend_request_list);
                            }
                        }
                    } else {

                    }
                } else {

                }
            }
        });

    }
    // gets the list of friend request received and displays them in recyclerview
    void setup_friend_request_recyclerview(ArrayList<UserModelClass> friend_request_list){
        RecyclerView recyclerView = view.findViewById(R.id.friend_request_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adaptor = new Friend_requestRecyclerViewAdaptor(friend_request_list);
        recyclerView.setAdapter(adaptor);
    }

    public void get_other_user_info_and_insert_recyclerview(List<String> Friend_request_list){

        for(int i =0;i<Friend_request_list.size();i++){
            firestore.collection("users").document(Friend_request_list.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            UserModelClass user_friend_request = new UserModelClass();
                            // get what information is needed
                            user_friend_request.setUsername(document.getString("username"));
                            user_friend_request.setUserId(document.getString("userId"));
                            Friend_Request.add(user_friend_request);
                            // when an item is added to Friend_Request, update recyclerview
                            adaptor.notifyItemInserted(Friend_Request.size()-1);
                        } else {

                        }
                    } else {

                    }
                }
            });
        }
        setup_friend_request_recyclerview(Friend_Request);
    }
}