package com.remembrall.ui.groceryListEntry;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroceryListEntryViewAdapter
        extends RecyclerView.Adapter<GroceryListEntryViewAdapter.GroceryListEntryHolder> {

    private final GroceryListEntryModel groceryListEntryModel;
    private final GroceryListEntryFragment fragment;
    private final RecyclerView recyclerView;

    private List<Integer> selected = new ArrayList<>();

    GroceryListEntryViewAdapter(LifecycleOwner owner,
                                GroceryListEntryModel model,
                                RecyclerView recycler) {
        groceryListEntryModel = model;
        fragment = (GroceryListEntryFragment) owner;
        recyclerView = recycler;
        model.getLiveData().observe(owner, entries -> this.notifyDataSetChanged());
    }

    @NonNull
    @Override
    public GroceryListEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list_entry, parent, false);
        return new GroceryListEntryHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(final GroceryListEntryHolder holder, int position) {
        List<GroceryListEntry> groceryListEntries = groceryListEntryModel.getLiveData().getValue();
        if (groceryListEntries != null) {
            holder.groceryListEntry = groceryListEntries.get(position);

            holder.name.setText(holder.groceryListEntry.getName());
            holder.name.setOnLongClickListener(v -> {
                selected.add(position);
                holder.view.setSelected(true);
                fragment.requireActivity().invalidateOptionsMenu();
                return true;
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

    public int getNofSelectedItems() {
        return selected.size();
    }

    public GroceryListEntry getSelectedGroceryListEntry() {
        return groceryListEntryModel.getLiveData().getValue().get(selected.get(0));
    }

    public List<GroceryListEntry> getSelectedGroceryListEntries() {
        return selected.stream()
                       .map(position -> groceryListEntryModel.getLiveData()
                                                             .getValue()
                                                             .get(position))
                       .collect(Collectors.toList());
    }

    @Override
    public int getItemCount() {
        return groceryListEntryModel.getLiveData().getValue() != null ?
               groceryListEntryModel.getLiveData().getValue().size() :
               0;
    }

    public boolean unselect() {
        boolean isNotEmpty = selected.size() > 0;
        selected.forEach(id -> ((GroceryListEntryViewAdapter.GroceryListEntryHolder) recyclerView.findViewHolderForAdapterPosition(
                id)).view.setSelected(false));
        selected.clear();
        fragment.requireActivity().invalidateOptionsMenu();
        return isNotEmpty;
    }

    public void refresh() {
        // clear selection as the dataset might not match the dataset the selection is based on
        unselect();
        groceryListEntryModel.refresh();
    }

    public static class GroceryListEntryHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final CheckBox checked;
        public final Context ctx;

        public GroceryListEntry groceryListEntry;

        GroceryListEntryHolder(View view, Context context) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            checked = view.findViewById(R.id.checked);
            ctx = context;
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
