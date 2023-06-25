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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Leaderboardadaptors.LeaderboardRecyclerAdaptor;
import sg.edu.np.mad.moviespaceapp.Leaderboardadaptors.LeaderboardRecyclerViewInterface;
import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;
import sg.edu.np.mad.moviespaceapp.Model.PopularActorModelClass;

public class Leaderboardfragment extends Fragment implements LeaderboardRecyclerViewInterface {
    View view;
    List<LeaderboardModelClass> LeaderboardList;
    RecyclerView leaderboardrecyclerView;

    // related to firestore db
    FirebaseAuth auth;
    FirebaseUser user;
    DocumentReference docref_actors;
    String userUid;
    FirebaseFirestore firestoredb;
    //

    List<LeaderboardModelClass> leaderboard_list;
    public Leaderboardfragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboardfragment, container, false);


        // firestore database
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestoredb = FirebaseFirestore.getInstance();
        userUid = user.getUid();


        leaderboard_list = new ArrayList<>();
        // get firestore leaderboard data
        Query query = firestoredb.collection("actors").orderBy("fame", Query.Direction.DESCENDING).limit(10);


        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // The query successfully completed, and you can access the result
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    List<DocumentSnapshot> documents = querySnapshot.getDocuments();
                    // Iterate through the documents and extract the data as needed
                    int placement = 1;
                    for (DocumentSnapshot document : documents) {
                        // Extract the data from the document
                        String actor_id = document.getString("actor_id");
                        int fame = document.getLong("fame").intValue();
                        // ... continue extracting other fields as needed

                        // Create an instance of your model class using the extracted data
                        LeaderboardModelClass actor = new LeaderboardModelClass();
                        actor.setActor_id(actor_id);
                        actor.setFame(fame);
                        actor.setProfile_path(document.getString("actor_profile_path"));
                        actor.setActor_name(document.getString("name"));
                        actor.setActor_placement(placement);

                        placement += 1;
                        Log.d("actor",actor.toString());
                        // add to list
                        leaderboard_list.add(actor);
                    }
                    // Update your RecyclerView
                    leaderboardrecyclerView = view.findViewById(R.id.fame_recycler_view);
                    leaderboardrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    LeaderboardRecyclerAdaptor adapter = new LeaderboardRecyclerAdaptor(getContext(),leaderboard_list,this);
                    leaderboardrecyclerView.setAdapter(adapter);
                }
            } else {

            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("Actor_Id",leaderboard_list.get(position).getActor_id());
        Actor_details actor_details_fragment = new Actor_details();
        actor_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,actor_details_fragment).commit();
    }
}