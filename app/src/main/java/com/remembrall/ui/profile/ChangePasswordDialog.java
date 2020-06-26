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
import com.remembrall.api.Backend;
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

        builder.setView(view)
               .setPositiveButton(R.string.save,
                                  (dialog, id) -> changePassword(oldPassword.getText().toString(),
                                                                 newPassword.getText().toString()))
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> ChangePasswordDialog.this.getDialog().cancel());

        return builder.create();
    }

    private void changePassword(String oldPassword, String newPassword) {
        ServiceLocator.getInstance()
                      .get(Backend.class)
                      .changePassword(oldPassword, newPassword, s -> {
                      }, e -> {
                      });
    }
}
