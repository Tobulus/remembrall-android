package com.remembrall.ui.groceryListEntry;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.stream.IntStream;

public class GroceryListEntryDialog extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    private GroceryListEntry groceryListEntry;

    public GroceryListEntryDialog() {
    }

    public GroceryListEntryDialog(GroceryListEntry groceryListEntry) {
        this.groceryListEntry = groceryListEntry;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // TODO: https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        Long groceryListId = getArguments().getLong("id");

        View view = inflater.inflate(R.layout.dialog_grocery_list_entry, null);

        final TextView name = view.findViewById(R.id.name);
        final TextView quantity = view.findViewById(R.id.quantity);

        Spinner quantityUnit = view.findViewById(R.id.quantityUnit);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                                                                             R.array.quantity_units,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantityUnit.setAdapter(adapter);
        quantityUnit.setOnItemSelectedListener(this);

        if (groceryListEntry != null) {
            name.setText(groceryListEntry.getName());
            quantity.setText(groceryListEntry.getQuantity().toString());

            String[] codes = getResources().getStringArray(R.array.quantity_units_codes);
            int idx = IntStream.range(0, codes.length)
                               .filter(i -> codes[i].equals(groceryListEntry.getQuantityUnit()))
                               .findFirst()
                               .orElse(0);
            quantityUnit.setSelection(idx);
        } else {
            quantityUnit.setSelection(1);
        }

        builder.setView(view)
               .setPositiveButton(R.string.save, (dialog, id) -> {
                   Double quantityDouble = quantity.getText().toString().isEmpty() ?
                                           1d :
                                           Double.parseDouble(quantity.getText().toString());
                   String quantityUnitCode =
                           getResources().getStringArray(R.array.quantity_units_codes)[quantityUnit.getSelectedItemPosition()];
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
               })
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> GroceryListEntryDialog.this.getDialog().cancel());

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
                                                                      Toast.LENGTH_LONG).show());
    }

    private void updateEntry(Long groceryListId, GroceryListEntry entry) {
        ServiceLocator.getInstance()
                      .get(Backend.class)
                      .updateGroceryListEntry(groceryListId,
                                              entry,
                                              json -> getTargetFragment().onActivityResult(
                                                      getTargetRequestCode(),
                                                      Activity.RESULT_OK,
                                                      null),
                                              error -> Toast.makeText(getContext(),
                                                                      error.getMessage(),
                                                                      Toast.LENGTH_LONG).show());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        parent.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
