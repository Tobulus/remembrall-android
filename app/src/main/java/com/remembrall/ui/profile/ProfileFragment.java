package com.remembrall.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.remembrall.R;
import com.remembrall.api.backend.UserBackend;
import com.remembrall.locator.ServiceLocator;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView changePassword = view.findViewById(R.id.change_password);
        changePassword.setOnClickListener(v -> {
            ChangePasswordDialog dialog = new ChangePasswordDialog();
            dialog.show(getParentFragmentManager(), "change-password");
        });

        TextView logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> ServiceLocator.getInstance()
                                                     .get(UserBackend.class)
                                                     .logout(s -> {
                                                     }, error -> {
                                                     }));

        return view;
    }
}
