package com.groceries.ui.login_registration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.servicelocater.ServiceLocator;

public class RegistrationFragment extends Fragment {

    private RegistrationListener mListener;

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
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final EditText matchingPasswordEditText = view.findViewById(R.id.matchingPassword);

        final Button loginButton = view.findViewById(R.id.register);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);

        loginButton.setEnabled(true);
        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
            ServiceLocator.getInstance().get(Backend.class)
                          .register(usernameEditText.getText().toString(),
                                     passwordEditText.getText().toString(),
                                     matchingPasswordEditText.getText().toString(),
                                     json -> {
                                         loadingProgressBar.setVisibility(View.INVISIBLE);
                                         mListener.onRegistrationComplete();
                                     },
                                     error -> {
                                         loadingProgressBar.setVisibility(View.INVISIBLE);
                                         Toast.makeText(getContext(),
                                                        "Failed:" + error.getMessage(),
                                                        Toast.LENGTH_LONG).show();
                                     });
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegistrationListener) {
            mListener = (RegistrationListener) context;
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

    public interface RegistrationListener {
        void onRegistrationComplete();
    }
}
