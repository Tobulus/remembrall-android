package com.groceries.ui.groceryListEntry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.api.BackendProvider;
import com.groceries.model.GroceryListEntry;

import java.util.List;

public class GroceryListEntryViewAdapter
        extends RecyclerView.Adapter<GroceryListEntryViewAdapter.GroceryListEntryHolder> {

    private final List<GroceryListEntry> groceryListEntries;
    private final GroceryListEntryFragment.GroceryListEntryListener
            groceriesActivity;
    private final BackendProvider backendProvider;

    GroceryListEntryViewAdapter(List<GroceryListEntry> items,
                                GroceryListEntryFragment.GroceryListEntryListener listener,
                                BackendProvider provider) {
        groceryListEntries = items;
        groceriesActivity = listener;
        backendProvider = provider;
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
        holder.groceryListEntry = groceryListEntries.get(position);
        holder.name.setText(holder.groceryListEntry.getName());
        holder.name.setOnClickListener(v -> groceriesActivity.onClick(holder.groceryListEntry));
        holder.checked.setChecked(holder.groceryListEntry.isChecked());
        holder.checked.setOnClickListener(v -> {
            holder.groceryListEntry.setChecked(!holder.groceryListEntry.isChecked());
            backendProvider.getBackend()
                           .updateGroceryListEntry(holder.groceryListEntry.getGroceryList().getId(),
                                                   holder.groceryListEntry,
                                                   response -> {
                                                   },
                                                   e -> holder.checked.toggle());
        });
    }

    @Override
    public int getItemCount() {
        return groceryListEntries.size();
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
