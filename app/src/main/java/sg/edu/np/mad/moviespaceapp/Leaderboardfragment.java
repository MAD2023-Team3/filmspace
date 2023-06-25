package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Model.LeaderboardModelClass;

public class Leaderboardfragment extends Fragment {
    View view;
    List<LeaderboardModelClass> LeaderboardList;
    RecyclerView leaderboardrecyclerView;
    public Leaderboardfragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboardfragment, container, false);

        /*leaderboardrecyclerView = view.findViewById(R.id.fame_recycler_view);
        leaderboardrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LeaderboardRecyclerAdaptor adapter = new LeaderboardRecyclerAdaptor();
        leaderboardrecyclerView.setAdapter(adapter);*/

        return view;
    }

    private List<LeaderboardModelClass> getLeaderboardData() {
        List<LeaderboardModelClass> items = new ArrayList<>();
        /*items.add(new LeaderboardModelClass("Actor 1", 1));
        items.add(new LeaderboardModelClass("Actor 2", 2));
        items.add(new LeaderboardModelClass("Actor 3", 3));*/
        return items;
    }
}