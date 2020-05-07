package com.groceries.ui.registration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Response;
import com.groceries.R;
import com.groceries.ui.login.LoginFragment;

import java.util.function.Consumer;

public class RegistrationFragment extends Fragment {

    private RegistrationFragmentInteractionListener mListener;

    public RegistrationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final EditText matchingPasswordEditText = view.findViewById(R.id.matchingPassword);

        final Button loginButton = view.findViewById(R.id.register);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        loginButton.setEnabled(true);
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            mListener.register(usernameEditText.getText().toString(),
                               passwordEditText.getText().toString(),
                               matchingPasswordEditText.getText().toString(),
                               json -> {
                                   loadingProgressBar.setVisibility(View.INVISIBLE);
                                   switchToLogin();
                               },
                               error -> Toast.makeText(getContext(),
                                                       "Failed:" + error.getMessage(),
                                                       Toast.LENGTH_LONG).show());
        });
        return view;
    }

    private void switchToLogin() {
        LoginFragment fragment = new LoginFragment();
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationFragmentInteractionListener) {
            mListener = (RegistrationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement LoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface RegistrationFragmentInteractionListener {
        void register(String user,
                      String password,
                      String matchingPassword,
                      Consumer<String> onSuccess,
                      Response.ErrorListener errorListener);
    }
}
