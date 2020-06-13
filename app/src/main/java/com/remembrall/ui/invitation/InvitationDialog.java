package com.remembrall.ui.invitation;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.locator.ServiceLocator;

public class InvitationDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_invitation, null);

        Long groceryListId = getArguments().getLong("id");

        final TextView email = view.findViewById(R.id.email);

        builder.setView(view)
               .setPositiveButton(R.string.save, (dialog, id) -> {
                   createInvitation(groceryListId, email.getText().toString());
               })
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> InvitationDialog.this.getDialog().cancel());
        return builder.create();
    }

    private void createInvitation(Long groceryListId, String email) {
        ServiceLocator.getInstance()
                      .get(Backend.class)
                      .createInvitation(groceryListId,
                                        email,
                                        json -> getTargetFragment().onActivityResult(
                                                getTargetRequestCode(),
                                                Activity.RESULT_OK,
                                                null),
                                        error -> Toast.makeText(getContext(),
                                                                error.getMessage(),
                                                                Toast.LENGTH_LONG).show());
    }
}
