package cjkim00.imagesharingapplication.Post;

import android.graphics.Bitmap;

public class Post {

    private Bitmap mBitmap;
    private String mImageLocation;
    private String mDesc;
    private int mLikes;
    private int mViews;

    public Post(String imageLocation, String desc, int likes, int views) {
        //mBitmap = image;
        mImageLocation = imageLocation;
        mDesc = desc;
        mLikes = likes;
        mViews = views;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() { return mBitmap; }

    public String getImageLocation() { return mImageLocation; }

    public String getDescription() {
        return mDesc;
    }

    public int getLikes() {
        return mLikes;
    }

    public int getViews() {
        return mViews;
    }
}
