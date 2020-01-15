package cjkim00.imagesharingapplication;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cjkim00.imagesharingapplication.Search.Member;

import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    String mUsername;
    String mDescription;
    String mLocation;
    int mFollowers;
    int mFollowing;

    public ProfileFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        mUsername = bundle.getString("Username");
        mDescription = bundle.getString("Description");
        mLocation = bundle.getString("Location");
        mFollowers = bundle.getInt("Followers");
        mFollowing = bundle.getInt("Following");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        view.setMinimumWidth(width);
        view.setMinimumHeight(height);

        TextView username = view.findViewById(R.id.textView_username_fragment_profile);
        TextView description = view.findViewById(R.id.textView_profile_description_fragment_profile);
        TextView followers = view.findViewById(R.id.textView_followers_fragment_profile);
        TextView following = view.findViewById(R.id.textView_following_fragment_profile);

        ImageView profileImage = view.findViewById(R.id.imageView_profile_image_fragment_profile);

        setProfileImage(profileImage, mLocation);
        username.setText(mUsername);
        description.setText(mDescription);
        followers.setText("Followers: " + mFollowers);
        following.setText("Following: " + mFollowing);



        return view;
    }

    private void setProfileImage(ImageView imageView, String imageLocation) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imageLocation);

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

}
