package cjkim00.imagesharingapplication;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cjkim00.imagesharingapplication.ImageView.ImageViewerActivity;
import cjkim00.imagesharingapplication.R;

public class LoginFragment extends Fragment {


    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        mEmail = v.findViewById(R.id.editText_email_login);
        mPassword = v.findViewById(R.id.editText_password_login);
        Button registerButton = (Button) v.findViewById(R.id.button_register_login);
        registerButton.setOnClickListener(v12 -> {
            replaceFragment(new RegistrationFragment());
        });

        Button loginButton = v.findViewById(R.id.button_login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTextFields()) {
                    loginUser();
                }
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //works but commented out to do other testing
            onLoginSuccess();
        }

        //updateUI(currentUser);//login with information from currentUser
    }

    public void loginUser() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "signInWithEmail:success");
                            onLoginSuccess();
                        } else {
                            Log.w("", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Login Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public boolean checkEditTextFields() {
        boolean returnBool = true;
        String value = mEmail.getText().toString();

        if(value.length() == 0) {
            mEmail.setError("Cannot be empty.");
            returnBool = false;
        }

        value = mPassword.getText().toString();
        if(value.length() == 0) {
            mPassword.setError("Cannot be empty.");
            returnBool = false;
        }
        return returnBool;
    }

    public void onLoginSuccess() {
        FirebaseUser user = mAuth.getCurrentUser();
        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
        intent.putExtra("User", user);
        getActivity().startActivity(intent);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(fragmentManager)
                .beginTransaction();
        fragmentTransaction.replace(((ViewGroup)(Objects.requireNonNull(getView()).getParent()))
                .getId(), fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }

}
