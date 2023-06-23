package sg.edu.np.mad.moviespaceapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Movie_details_fragment extends Fragment {

    View view;

    // start: moviemodelclass data
    String movie_id,movie_name,poster_path;
    // end: moviemodelclass data
    public Movie_details_fragment() {
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
        view = inflater.inflate(R.layout.fragment_movie_details_fragment, container, false);

        // unpack the bundle
        Bundle bundle = this.getArguments();
        movie_id = bundle.getString("Movie_Id");
        movie_name = bundle.getString("movie_name");
        poster_path = bundle.getString("poster_path");

        // set the info to textview in the xml
        ImageView movie_poster = view.findViewById(R.id.movie_poster);
        TextView movie_title = view.findViewById(R.id.title_placeholder);
        TextView overview_placeholder = view.findViewById(R.id.overview_placeholder);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500" + poster_path).into(movie_poster);
        movie_title.setText(movie_name);

        return view;
    }
}