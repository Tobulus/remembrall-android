package com.groceries.ui.login;

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
import com.groceries.R;
import com.groceries.api.BackendProvider;

public class LoginFragment extends Fragment {

    private LoginListener mListener;
    private BackendProvider backendProvider;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.login);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        loginButton.setEnabled(true);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            backendProvider.getBackend().login(usernameEditText.getText().toString(),
                                               passwordEditText.getText().toString(),
                            json -> {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                mListener.onLoginComplete();
                            },
                            error -> Toast.makeText(getContext(),
                                                    "Failed:" + error.getMessage(),
                                                    Toast.LENGTH_LONG).show());
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof BackendProvider) {
            backendProvider = (BackendProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement LoginFragmentInteractionListener");
        }

        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backendProvider = null;
        mListener = null;
    }

    public interface LoginListener {
        void onLoginComplete();
    }
}
