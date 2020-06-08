package com.remembrall.ui.groceryListEntry;

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
import com.remembrall.api.data.GroceryListEntryData;
import com.remembrall.locator.ServiceLocator;

public class GroceryListEntryDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        Long groceryListId = getArguments().getLong("id");

        View view = inflater.inflate(R.layout.dialog_grocery_list_entry, null);
        final TextView name = view.findViewById(R.id.name);

        builder.setView(view)
               .setPositiveButton(R.string.save, (dialog, id) -> {
                   GroceryListEntryData entry = new GroceryListEntryData();
                   entry.setName(name.getText().toString());
                   ServiceLocator.getInstance()
                                 .get(Backend.class)
                                 .createGroceryListEntry(groceryListId,
                                                         entry,
                                                         json -> getTargetFragment().onActivityResult(
                                                                 getTargetRequestCode(),
                                                                 Activity.RESULT_OK,
                                                                 null),
                                                         error -> Toast.makeText(getContext(),
                                                                                 error.getMessage(),
                                                                                 Toast.LENGTH_LONG)
                                                                       .show());
               })
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> GroceryListEntryDialog.this.getDialog().cancel());

        return builder.create();
    }
}
