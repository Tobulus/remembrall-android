package com.remembrall.ui.login_registration;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.locator.ServiceLocator;

public class LoginFragment extends Fragment {

    private LoginListener mListener;

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
        final TextView forgotPassword = view.findViewById(R.id.forgot_password);

        loginButton.setEnabled(true);

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);

            ServiceLocator.getInstance()
                          .get(Backend.class)
                          .login(usernameEditText.getText().toString(),
                                 passwordEditText.getText().toString(),
                            json -> {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                mListener.onLoginComplete();
                                ServiceLocator.getInstance()
                                              .get(Backend.class)
                                              .registerFirebaseToken(s -> {
                                              }, e -> {
                                              });
                            },
                                 error -> Toast.makeText(getContext(),
                                                         "Failed:" + error.getMessage(),
                                                         Toast.LENGTH_LONG).show());
        });

        forgotPassword.setOnClickListener(v -> {
            ResetPasswordDialog dialog = new ResetPasswordDialog();
            dialog.show(getParentFragmentManager(), "forgot-password");
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof LoginListener) {
            mListener = (LoginListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement LoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface LoginListener {
        void onLoginComplete();
    }
}
