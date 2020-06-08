package com.remembrall.ui.groceryListEntry;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.GroceryListEntry;
import com.remembrall.model.view.GroceryListEntryModel;

import java.util.List;

import static com.remembrall.ui.groceryListEntry.GroceryListEntryFragment.LAUNCH_CREATE_GROCERY_LIST_ENTRY;

public class GroceryListEntryViewAdapter
        extends RecyclerView.Adapter<GroceryListEntryViewAdapter.GroceryListEntryHolder> {

    private final GroceryListEntryModel groceryListEntryModel;
    private final GroceryListEntryFragment fragment;

    GroceryListEntryViewAdapter(LifecycleOwner owner, GroceryListEntryModel model) {
        groceryListEntryModel = model;
        fragment = (GroceryListEntryFragment) owner;
        model.getLiveData().observe(owner, entries -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroceryListEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list_entry, parent, false);
        return new GroceryListEntryHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroceryListEntryHolder holder, int position) {
        List<GroceryListEntry> groceryListEntries = groceryListEntryModel.getLiveData().getValue();
        if (groceryListEntries != null) {
            holder.groceryListEntry = groceryListEntries.get(position);

            holder.name.setText(holder.groceryListEntry.getName());
            holder.name.setOnClickListener(v -> {
                GroceryListEntryDialog dialog = new GroceryListEntryDialog(holder.groceryListEntry);
                Bundle bundle = new Bundle();
                bundle.putLong("id", holder.groceryListEntry.getGroceryList());
                dialog.setArguments(bundle);
                dialog.setTargetFragment(fragment, LAUNCH_CREATE_GROCERY_LIST_ENTRY);
                dialog.show(fragment.getFragmentManager(), "create-grocery-list-entry");
            });

            holder.checked.setChecked(holder.groceryListEntry.isChecked());
            holder.checked.setOnClickListener(v -> {
                holder.groceryListEntry.setChecked(!holder.groceryListEntry.isChecked());
                ServiceLocator.getInstance()
                              .get(Backend.class)
                              .updateGroceryListEntry(holder.groceryListEntry.getGroceryList(),
                                                      holder.groceryListEntry,
                                                      response -> {
                                                      },
                                                      e -> holder.checked.toggle());
            });
        }
    }

    @Override
    public int getItemCount() {
        return groceryListEntryModel.getLiveData().getValue() != null ?
               groceryListEntryModel.getLiveData().getValue().size() :
               0;
    }

    public void refresh() {
        groceryListEntryModel.refresh();
    }

    public static class GroceryListEntryHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final CheckBox checked;

        public GroceryListEntry groceryListEntry;

        GroceryListEntryHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            checked = view.findViewById(R.id.checked);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
