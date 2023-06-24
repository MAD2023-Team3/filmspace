package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class Leaderboardfragment extends Fragment {
    View view;
    List<LeaderboardModelClass> LeaderboardList;
    RecyclerView recyclerView;
    LeaderboardAdapter leaderboardAdapter;
    public Leaderboardfragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboardfragment, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        leaderboardAdapter = new LeaderboardAdapter(getLeaderboardData());
        recyclerView.setAdapter(leaderboardAdapter);

        return view;
    }

    private List<LeaderboardModelClass> getLeaderboardData() {
        List<LeaderboardModelClass> items = new ArrayList<>();
        items.add(new LeaderboardModelClass("Actor 1", 1));
        items.add(new LeaderboardModelClass("Actor 2", 2));
        items.add(new LeaderboardModelClass("Actor 3", 3));
        return items;
    }
}