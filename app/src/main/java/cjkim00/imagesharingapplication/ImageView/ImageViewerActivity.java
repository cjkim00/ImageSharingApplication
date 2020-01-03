package cjkim00.imagesharingapplication.ImageView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileSystem;
import java.util.Objects;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cjkim00.imagesharingapplication.Post.Post;
//import cjkim00.imagesharingapplication.Post.PostFragment;
import cjkim00.imagesharingapplication.Post.PostFragment;
import cjkim00.imagesharingapplication.R;

public class ImageViewerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PostFragment.OnListFragmentInteractionListener {

    private StorageReference mStorageRef;
    public String[] test = {"one", "two", "three", "four", "five"};
    public String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        replaceFragment(new ImageViewerFragment());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        mEmail = intent.getStringExtra("email");


        //testRecyclerView();
        loadPostFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            replaceFragment(new ProfileFragment());
        } else if (id == R.id.nav_view_followers) {

        } else if (id == R.id.nav_manage_followers) {

        } else if (id == R.id.nav_liked_posts) {

        } else if (id == R.id.nav_settings) {
            replaceFragment(new SettingsFragment());
        } else if (id == R.id.nav_log_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager)
                .beginTransaction();
        fragmentTransaction.replace(R.id.layout_image_viewer, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Toast.makeText(getApplicationContext(),"Image uploaded",Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            StorageReference riversRef = mStorageRef.child(getRealPathFromURI(imageUri));


            riversRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext()
                                    ,getRealPathFromURI(imageUri),Toast.LENGTH_SHORT).show();
                            //downloadFileTest(getRealPathFromURI(imageUri));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext()
                                    ,"Image not uploaded",Toast.LENGTH_SHORT).show();

                            // Handle unsuccessful uploads
                            // ...
                        }
                    }).addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            //downloadFileTest(getRealPathFromURI(imageUri));
                            uploadToDatabase(getRealPathFromURI(imageUri));

                        }
                    });

        }
    }

    public void uploadToDatabase(String location) {
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {

                try {
                    HttpURLConnection urlConnection = null;
                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath("cjkim00-image-sharing-app.herokuapp.com")
                            .appendPath("InsertPost")
                            .build();

                    URL url = new URL(uri.toString());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(15000);
                    conn.setReadTimeout(15000);


                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("PostLocation", location);
                    jsonParam.put("Email", mEmail);
                    jsonParam.put("Description", "Temporary Value");
                    jsonParam.put("Likes", 1);
                    jsonParam.put("Views", 1);
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());
                    os.flush();
                    os.close();

                    Log.i("MSG", "STATUS: " + String.valueOf(conn.getResponseCode()));
                    Log.i("MSG" , "MESSAGE: " + conn.getResponseMessage());
                    //conn.connect();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void downloadFileTest(String location) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference islandRef = storageRef.child(location);

        final long ONE_MEGABYTE = 1024 * 1024;

        //ImageView imageView = findViewById(R.id.imageView_image_imageViewer);
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                //imageView.setImageBitmap(bitmap);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver()
                .query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void loadPostFragment() {
        Bundle args = new Bundle();
        args.putString(PostFragment.ARG_EMAIL, mEmail);

        Fragment frag = new PostFragment();
        frag.setArguments(args);
        replaceFragment(frag);
    }
    /*
    public void testRecyclerView() {
        Post[] testData = new Post[5];
        for(int i = 0; i < testData.length; i++) {
            Post newPost = new Post("Post number " + i, i, i);
            testData[i] = newPost;
        }
        //Toast.makeText(getApplicationContext(),"" + testData.length,Toast.LENGTH_SHORT).show();
        Bundle args = new Bundle();
        args.putString(PostFragment.ARG_EMAIL, mEmail);

        Fragment frag = new PostFragment();
        frag.setArguments(args);
        replaceFragment(frag);
    }
    */
    @Override
    public void onListFragmentInteraction(Post item) {

    }

    /*
    @Override
    public void onListFragmentInteraction(Post post) {
        //populate full post fragment
    }
    */
}
