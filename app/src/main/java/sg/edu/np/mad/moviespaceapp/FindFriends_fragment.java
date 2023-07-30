package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.FindFriendadaptor.FindFriendAdaptor;
import sg.edu.np.mad.moviespaceapp.Model.FindFriendViewModel;

public class FindFriends_fragment extends Fragment {

    View view;
    RecyclerView findFriends_recyclerview;

    // related to firestore db
    FirebaseUser user;
    DocumentReference documentReference_user;
    String userUid;
    FirebaseFirestore firestoredb;
    CollectionReference allUserCollectionReference;
    //
    FindFriendAdaptor adaptor;
    SearchView searchbarView;
    public FindFriends_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.findfriends_fragment, container, false);

        findFriends_recyclerview = view.findViewById(R.id.findFriends_recyclerview);
        searchbarView = view.findViewById(R.id.searchView_friend);

        searchbarView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search or filtering operations here
                setupRecyclerView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform real-time filtering or search operations here
                return false;
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

        if(adaptor != null){
            adaptor.startListening();
        }

    }

    @Override
    public void onStop(){
        super.onStop();
        if(adaptor != null){
            adaptor.stopListening();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if(adaptor != null){
            adaptor.startListening();
        }
    }
    void setupRecyclerView(String searchstring){

        allUserCollectionReference= FirebaseFirestore.getInstance().collection("users");
        Query query = allUserCollectionReference
                .whereGreaterThanOrEqualTo("username",searchstring);

        FirestoreRecyclerOptions<FindFriendViewModel> options = new FirestoreRecyclerOptions.Builder<FindFriendViewModel>()
                .setQuery(query,FindFriendViewModel.class).build();


        adaptor = new FindFriendAdaptor(options,getContext());
        findFriends_recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        findFriends_recyclerview.setAdapter(adaptor);
        adaptor.startListening();
    }


}