package com.remembrall.ui.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.remembrall.R;
import com.remembrall.api.backend.UserBackend;
import com.remembrall.locator.ServiceLocator;

public class ChangePasswordDialog extends DialogFragment {

    public ChangePasswordDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);

        final TextView oldPassword = view.findViewById(R.id.old_password);
        final TextView newPassword = view.findViewById(R.id.new_password);

        builder.setView(view).setTitle(R.string.dialog_title_change_password)
               .setPositiveButton(R.string.save,
                                  (dialog, id) -> changePassword(oldPassword.getText().toString(),
                                                                 newPassword.getText().toString()))
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> ChangePasswordDialog.this.getDialog().cancel());

        return builder.create();
    }

    private void changePassword(String oldPassword, String newPassword) {
        ServiceLocator.getInstance()
                      .get(UserBackend.class)
                      .changePassword(oldPassword, newPassword);
    }
}
