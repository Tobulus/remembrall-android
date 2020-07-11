package com.remembrall.ui.groceryListEntry;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
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
import com.remembrall.model.database.GroceryListEntry;

import java.security.InvalidParameterException;

public class GroceryListEntryDialog extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    private GroceryListEntry groceryListEntry;
    private Backend backend;

    public GroceryListEntryDialog() {
        backend = ServiceLocator.getInstance().get(Backend.class);
    }

    public GroceryListEntryDialog(GroceryListEntry groceryListEntry) {
        this();
        this.groceryListEntry = groceryListEntry;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getArguments() == null) {
            throw new InvalidParameterException("GroceryListId was empty but is required.");
        }
        Long groceryListId = getArguments().getLong("id");

        View view = inflater.inflate(R.layout.dialog_grocery_list_entry, null);
        final TextView name = view.findViewById(R.id.name);
        final TextView quantity = view.findViewById(R.id.quantity);
        final Spinner quantityUnit = view.findViewById(R.id.quantityUnit);

        QuantityUnitAdapter adapter =
                new QuantityUnitAdapter(requireContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityUnit.setAdapter(adapter);
        quantityUnit.setOnItemSelectedListener(this);

        if (groceryListEntry != null) {
            name.setText(groceryListEntry.getName());
            quantity.setText(String.format("%s", groceryListEntry.getQuantity()));
            quantityUnit.setSelection(adapter.getPosition(groceryListEntry.getQuantityUnit()));
        } else {
            quantityUnit.setSelection(0);
        }

        builder.setView(view)
               .setTitle(groceryListEntry == null ?
                         R.string.dialog_title_grocery_list_entry_create :
                         R.string.dialog_title_grocery_list_entry_update)
               .setPositiveButton(R.string.save, (dialog, id) -> {
                   Double quantityDouble = quantity.getText().toString().isEmpty() ?
                                           1d :
                                           Double.parseDouble(quantity.getText().toString());

                   String quantityUnitCode =
                           adapter.getItem(quantityUnit.getSelectedItemPosition()) == null ?
                           QuantityUnit.PIECE.getCode() :
                           adapter.getItem(quantityUnit.getSelectedItemPosition()).getCode();

                   if (groceryListEntry == null) {
                       createEntry(groceryListId,
                                   name.getText().toString(),
                                   quantityDouble,
                                   quantityUnitCode);
                   } else {
                       groceryListEntry.setName(name.getText().toString());
                       groceryListEntry.setQuantity(quantityDouble);
                       groceryListEntry.setQuantityUnit(quantityUnitCode);
                       updateEntry(groceryListId, groceryListEntry);
                   }
               }).setNegativeButton(R.string.cancel, (dialog, id) -> {
            if (GroceryListEntryDialog.this.getDialog() != null) {
                GroceryListEntryDialog.this.getDialog().cancel();
            }
        });

        return builder.create();
    }

    private void createEntry(Long groceryListId,
                             String name,
                             Double quantity,
                             String quantityUnitCode) {
        GroceryListEntryData entry = new GroceryListEntryData();
        entry.setName(name);
        entry.setQuantity(quantity);
        entry.setQuantityUnit(quantityUnitCode);

        backend.createGroceryListEntry(groceryListId,
                                       entry,
                                       json -> signalSuccess(),
                                       error -> Toast.makeText(getContext(),
                                                               error.getMessage(),
                                                               Toast.LENGTH_LONG).show());
    }

    private void updateEntry(Long groceryListId, GroceryListEntry entry) {
        backend.updateGroceryListEntry(groceryListId,
                                       entry,
                                       json -> signalSuccess(),
                                       error -> Toast.makeText(getContext(),
                                                               error.getMessage(),
                                                               Toast.LENGTH_LONG).show());
    }

    private void signalSuccess() {
        if (getTargetFragment() != null) {
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
