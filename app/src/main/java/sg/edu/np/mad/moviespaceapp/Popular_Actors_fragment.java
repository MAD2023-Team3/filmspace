package sg.edu.np.mad.moviespaceapp;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sg.edu.np.mad.moviespaceapp.Model.PopularActorModelClass;
import sg.edu.np.mad.moviespaceapp.PopularActorAdaptors.PopularActorRecyclerViewAdaptor;
import sg.edu.np.mad.moviespaceapp.PopularActorAdaptors.PopularActorRecyclerViewInterface;

public class Popular_Actors_fragment extends Fragment implements PopularActorRecyclerViewInterface {

    List<PopularActorModelClass> popularActorModelClassList;


    private String JSON_URL = "https://api.themoviedb.org/3/person/popular?api_key=d51877fbcef44b5e6c0254522b9c1a35";
    RecyclerView popular_actor_recyclerview;
    View view;
    public Popular_Actors_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.popular_actor_fragment, container, false);

        popular_actor_recyclerview = view.findViewById(R.id.popular_actor_recyclerview);
        popularActorModelClassList = new ArrayList<>();

        GetData getData = new GetData(JSON_URL,popular_actor_recyclerview);
        getData.execute();


        return view;
    }


    // start: code block for GetData
    public class GetData extends AsyncTask<String,String,String> {
        private String jsonUrl;

        private RecyclerView recyclerView;

        public GetData(String jsonUrl,RecyclerView recyclerView){
            this.recyclerView = recyclerView;
            this.jsonUrl = jsonUrl;
        }
        @Override
        protected String doInBackground(String... strings){
            String current = "";

            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try {
                    url = new URL(jsonUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while(data != -1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }finally {
                    if(urlConnection!= null){
                        urlConnection.disconnect();;
                    }
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            return current;
        }

        // end: code block for GetData

        // start: converting json into object
        @Override
        protected void onPostExecute(String s) {
            try{

                Log.d("ok","ok?");
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("results");

                Log.d("you good","you good?");
                // scans the jsonArray received from api request turns the request to
                // MovieModelClass and inserts that in movieList
                for(int i = 0; i<jsonArray.length();i++){
                    Log.d("you good","you goosdsdsd?");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    PopularActorModelClass obj = new PopularActorModelClass();


                    Log.d("you good","you gosdafsafdod?");
                    if(jsonObject1.getString("known_for_department").equals("Acting")){
                        obj.setActor_id(jsonObject1.getString("id"));
                        obj.setActor_name(jsonObject1.getString("name"));
                        obj.setProfile_path(jsonObject1.getString("profile_path"));
                        obj.setKnown_for_department("Acting");

                        popularActorModelClassList.add(obj);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            // code here
            putDataIntoRecyclerView(popularActorModelClassList);
        }
    }

    // recyclerview
    private void putDataIntoRecyclerView(List<PopularActorModelClass> popularActorModelClassList){
        PopularActorRecyclerViewAdaptor adapter = new PopularActorRecyclerViewAdaptor(getContext(),popularActorModelClassList,this);
        popular_actor_recyclerview.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        popular_actor_recyclerview.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("Actor_Id",popularActorModelClassList.get(position).getActor_id());
        Actor_details actor_details_fragment = new Actor_details();
        actor_details_fragment.setArguments(bundle);
        // fragment transaction
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,actor_details_fragment).commit();
    }
}