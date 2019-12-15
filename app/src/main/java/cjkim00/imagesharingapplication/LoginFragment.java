package cjkim00.imagesharingapplication;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Objects;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import cjkim00.imagesharingapplication.R;

public class LoginFragment extends Fragment {



    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button registerButton = (Button) v.findViewById(R.id.button_register_login);
        registerButton.setOnClickListener(v12 -> {
            replaceFragment(new RegistrationFragment());
        });

        Button loginButton = v.findViewById(R.id.button_login_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check for empty email

                //check for empty password

                //check for valid email

                //check for valid password
            }
        });

        return v;
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
