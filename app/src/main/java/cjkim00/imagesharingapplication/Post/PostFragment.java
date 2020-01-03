package cjkim00.imagesharingapplication.Post;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cjkim00.imagesharingapplication.R;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class PostFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Post> mPosts;
    public static final String ARG_POST_LIST = "posts";
    public static final String ARG_EMAIL = "email";
    public String mEmail;
    RecyclerView recyclerView;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PostFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static PostFragment newInstance(int columnCount) {
        PostFragment fragment = new PostFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosts = new ArrayList<Post>();


        mEmail = getArguments().getString(ARG_EMAIL);
        //Toast.makeText(getContext(),"Email: " + mEmail ,Toast.LENGTH_LONG).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            try {
                getPosts();
                //getImageFromStorage();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Log.i("MSG", "RESULT: " + mPosts.get(0).getDescription());
            recyclerView.setAdapter(new MyPostRecyclerViewAdapter(mPosts, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getPosts() throws InterruptedException {
        List<Post> tempArray = new ArrayList<>();
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {

                try {
                    Log.i("MSG", "START");
                    HttpURLConnection urlConnection = null;
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath("cjkim00-image-sharing-app.herokuapp.com")
                            .appendPath("GetAllPosts")
                            .build();

                    URL url = new URL(uri.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Content-length", "0");
                    conn.setUseCaches(false);
                    conn.setAllowUserInteraction(false);
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);
                    conn.connect();
                    int status = conn.getResponseCode();
                    Log.i("MSG", "" + status);
                    switch (status) {
                        case 200:
                        case 201:
                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream()));
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null) {
                                sb.append(line);
                            }
                            br.close();
                            Log.i("MSG", sb.toString());

                            getResults(sb.toString() , tempArray);

                        }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
        mPosts = tempArray;
    }

    public void getResults(String result, List<Post> arr) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success") ) {
                //JSONObject response = root.getJSONObject("success");
                JSONArray data = root.getJSONArray("data");
                List<Post> posts = new ArrayList<>();
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonPost = data.getJSONObject(i);
                    Post tempPost = new Post(jsonPost.getString("postlocation")
                            , jsonPost.getString("postdesc")
                            , jsonPost.getInt("likes")
                            , jsonPost.getInt("views"));
                    arr.add(tempPost);

                }
                //arr = posts;


            } else {
                Log.i("MSG", "No response");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("MSG", e.getMessage());
        }
    }


    private void onLoading() {
        //show a loading circle
    }

    private void onFinishedLoading() {
        //remove the loading circle
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Post item);
    }
}
