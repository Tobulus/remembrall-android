package com.remembrall.ui.groceryList;

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
import com.remembrall.api.data.GroceryListData;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.GroceryList;

public class GroceryListDialog extends DialogFragment {

    private GroceryList groceryList;

    public GroceryListDialog(GroceryList groceryList) {
        this.groceryList = groceryList;
    }

    public GroceryListDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_grocery_list, null);

        final TextView name = view.findViewById(R.id.name);

        if (groceryList != null) {
            name.setText(groceryList.getName());
        }

        builder.setView(view)
               .setPositiveButton(R.string.save, (dialog, id) -> {
                   if (groceryList == null) {
                       createList(name.getText().toString());
                   } else {
                       groceryList.setName(name.getText().toString());
                       updateList(groceryList);
                   }
               })
               .setNegativeButton(R.string.cancel,
                                  (dialog, id) -> GroceryListDialog.this.getDialog().cancel());
        return builder.create();
    }

    private void createList(String name) {
        GroceryListData groceryList = new GroceryListData();
        groceryList.setName(name);
        ServiceLocator.getInstance()
                      .get(Backend.class)
                      .createGroceryList(groceryList,
                                         json -> getTargetFragment().onActivityResult(
                                                 getTargetRequestCode(),
                                                 Activity.RESULT_OK,
                                                 null),
                                         error -> Toast.makeText(getContext(),
                                                                 error.getMessage(),
                                                                 Toast.LENGTH_LONG).show());
    }

    private void updateList(GroceryList list) {
        ServiceLocator.getInstance()
                      .get(Backend.class)
                      .updateGroceryList(list,
                                         json -> getTargetFragment().onActivityResult(
                                                 getTargetRequestCode(),
                                                 Activity.RESULT_OK,
                                                 null),
                                         error -> Toast.makeText(getContext(),
                                                                 error.getMessage(),
                                                                 Toast.LENGTH_LONG).show());
    }
}
