package com.remembrall.ui.login_registration;

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

public class ResetPasswordDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_forgot_password, null);

        final TextView email = view.findViewById(R.id.email);

        builder.setView(view)
               .setPositiveButton(R.string.save,
                                  (dialog, id) -> resetPassword(email.getText().toString()))
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> ResetPasswordDialog.this.getDialog().cancel());

        return builder.create();
    }

    private void resetPassword(String email) {
        ServiceLocator.getInstance().get(Backend.class).resetPassword(email, s -> { /* TODO Toast.makeText(requireActivity().getApplicationContext(),
                                                         "Your password has been reset and you will receive an email in a few moments.",
                                                         Toast.LENGTH_LONG).show()*/
        }, error -> { /* TODO Toast.makeText(requireActivity().getApplicationContext(),
                                                             "Failed:" + error.getMessage(),
                                                             Toast.LENGTH_LONG).show())*/
        });
    }
}
