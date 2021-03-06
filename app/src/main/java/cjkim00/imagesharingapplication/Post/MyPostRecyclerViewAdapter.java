package cjkim00.imagesharingapplication.Post;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import cjkim00.imagesharingapplication.Post.PostFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import cjkim00.imagesharingapplication.R;

public class MyPostRecyclerViewAdapter extends RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder> {

    private final List<Post> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyPostRecyclerViewAdapter(List<Post> items, OnListFragmentInteractionListener listener) {
        mValues = (ArrayList<Post>) items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDescView.setText(mValues.get(position).getDescription());
        holder.mLikesView.setText(String.valueOf(mValues.get(position).getLikes()));
        holder.mViewsView.setText(String.valueOf(mValues.get(position).getViews()));
        getImageFromStorage(holder.mImage, mValues.get(position).getImageLocation());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("MSG", "Size: " + mValues.size());
        return mValues.size();

    }

    public void getImageFromStorage(ImageView imageView, String location) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(location);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImage;
        public final TextView mDescView;
        public final TextView mLikesView;
        public final TextView mViewsView;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImage = view.findViewById(R.id.imageView_postImage_postfragment);
            mDescView = view.findViewById(R.id.textview_postdesc_fragmentpost);
            mLikesView = view.findViewById(R.id.textview_likes_fragmentpost);
            mViewsView = view.findViewById(R.id.textview_views_fragmentpost);
        }

        //@Override
        //public String toString() {
        //    return super.toString() + " '" + mContentView.getText() + "'";
        //}
    }
}
