package cjkim00.imagesharingapplication.ImageView;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cjkim00.imagesharingapplication.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewerFragment extends Fragment {


    public ImageViewerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_viewer, container, false);
    }

}
