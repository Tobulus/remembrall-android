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
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.Response;
import com.groceries.R;
import com.groceries.ui.groceryList.GroceryListFragment;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private LoginFragmentInteractionListener mListener;

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

            mListener.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),
                            json -> {
                                loadingProgressBar.setVisibility(View.INVISIBLE);
                                switchToGroceryLists();
                            },
                            error -> Toast.makeText(getContext(),
                                                    "Failed:" + error.getMessage(),
                                                    Toast.LENGTH_LONG).show());
        });

        return view;
    }

    private void switchToGroceryLists() {
        GroceryListFragment fragment = new GroceryListFragment();
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment, fragment);
        transaction.commit();

        getActivity().findViewById(R.id.navigation).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.floating_plus_button).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.main_fragment).setVisibility(View.VISIBLE);
        getActivity().findViewById(R.id.view_pager).setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentInteractionListener) {
            mListener = (LoginFragmentInteractionListener) context;
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

    public interface LoginFragmentInteractionListener {
        void login(String username,
                   String password,
                   Response.Listener<JSONObject> onSuccess,
                   Response.ErrorListener onError);
    }
}
