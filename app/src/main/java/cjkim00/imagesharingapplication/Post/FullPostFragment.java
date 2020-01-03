package cjkim00.imagesharingapplication.Post;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cjkim00.imagesharingapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullPostFragment extends Fragment {

    private Bitmap mImageBitmap;
    private String mDesc;
    private int mLikes;
    private int mViews;

    public FullPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        if(getArguments() != null) {
            byte[] image = getArguments().getByteArray("image");
            mImageBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
            mDesc = getArguments().getString("description");
            mLikes = getArguments().getInt("likes");
            mViews = getArguments().getInt("views");

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_post, container, false);
    }

}
