package cjkim00.imagesharingapplication;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import cjkim00.imagesharingapplication.ImageView.ImageViewerActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.sql.PreparedStatement;
import java.util.concurrent.Executor;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {

    private final int PASSWORD_MIN_LENGTH = 8;

    private FirebaseAuth mAuth;

    private EditText mEmail;
    private EditText mPassword;
    private EditText mSecondPassword;

    public RegistrationFragment() {
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
        View v = inflater.inflate(R.layout.fragment_registration, container, false);

        mEmail = v.findViewById(R.id.editText_email_registration);
        mPassword = v.findViewById(R.id.editText_password_registration);
        mSecondPassword = v.findViewById(R.id.editText_second_password_registration);

        Button b = v.findViewById(R.id.button_finish_registration);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkEditTextFields()) {
                    registerUser();
                }

            }
        });
        return v;
    }

    public void onLoginSuccess(String email) {
        Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
        intent.putExtra("email", email);
        getActivity().startActivity(intent);
    }

    public void registerUser() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoginSuccess(user.getEmail());
                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Log.w("Authentication Failed", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed." + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public boolean checkPasswordLength() {
        String password = mPassword.getText().toString();
        if(password.length() >= PASSWORD_MIN_LENGTH) {
            return true;
        }
        return false;
    }

    public boolean checkIfPasswordsMatch() {
        if(mPassword.getText().toString().equals(mSecondPassword.getText().toString())) {
            return true;
        }
        return false;

    }


    public boolean checkEditTextFields() {
        boolean returnBool = true;
        String value = mEmail.getText().toString();
        if(value.length() == 0) {
            mEmail.setError("Cannot be empty.");
            returnBool = false;
        }

        value = mPassword.getText().toString();
        if(!(value.length() == 0)) {
            if(checkPasswordLength()) {
                if(!checkIfPasswordsMatch()) {
                    mSecondPassword.setError("Passwords must match.");
                }
            } else {
                mPassword.setError("Password must be at least 8 characters.");
                returnBool = false;
            }
        } else {
            mPassword.setError("Cannot be empty.");
            returnBool = false;
        }

        value = mSecondPassword.getText().toString();
        if(value.length() == 0) {
            mSecondPassword.setError("Cannot be empty.");
            returnBool = false;
        }
        return returnBool;
    }

}
