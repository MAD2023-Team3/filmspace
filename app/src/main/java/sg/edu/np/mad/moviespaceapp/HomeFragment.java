package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {

    View view;

    // recycler view
    SearchView search_view;
    RecyclerView recycler_view_home;
    //
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_homefragment, container, false);

        // movie recycler view
        search_view = view.findViewById(R.id.search_view);
        recycler_view_home = view.findViewById(R.id.recycler_view_home);
        //
        return view;
    }
}