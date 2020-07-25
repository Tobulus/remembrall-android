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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GroceryListEntryViewAdapter
        extends RecyclerView.Adapter<GroceryListEntryViewAdapter.GroceryListEntryHolder> {

    private final GroceryListEntryModel groceryListEntryModel;
    private final GroceryListEntryFragment fragment;
    private final RecyclerView recyclerView;

    private List<Integer> selected = new ArrayList<>();
    private QuantityUnitAdapter quantityUnitAdapter;

    GroceryListEntryViewAdapter(LifecycleOwner owner,
                                GroceryListEntryModel model,
                                RecyclerView recycler) {
        groceryListEntryModel = model;
        fragment = (GroceryListEntryFragment) owner;
        recyclerView = recycler;
        model.getLiveData().observe(owner, entries -> this.notifyDataSetChanged());
        quantityUnitAdapter = new QuantityUnitAdapter(fragment.requireContext(),
                                                      android.R.layout.simple_spinner_item);
    }

    @NonNull
    @Override
    public GroceryListEntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list_entry, parent, false);
        return new GroceryListEntryHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryListEntryHolder holder, int position) {
        List<GroceryListEntry> groceryListEntries = groceryListEntryModel.getLiveData().getValue();
        if (groceryListEntries != null) {
            holder.groceryListEntry = groceryListEntries.get(position);

            holder.name.setText(holder.groceryListEntry.getName());
            holder.name.setOnLongClickListener(v -> {
                if (selected.contains(position)) {
                    selected.remove(Integer.valueOf(position));
                    holder.view.setSelected(false);
                } else {
                    selected.add(position);
                    holder.view.setSelected(true);
                }
                fragment.requireActivity().invalidateOptionsMenu();
                return true;
            });

            holder.quantityWithUnit.setText(formatQuantityWithUnit(holder.groceryListEntry.getQuantity(),
                                                                   holder.groceryListEntry.getQuantityUnit()));

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

    private String formatQuantityWithUnit(Double quantity, String quantityUnit) {
        if (quantity != null && quantityUnit != null) {
            return String.format("%s %s",
                                 new DecimalFormat("#.##").format(quantity),
                                 quantityUnitAdapter.fromCode(quantityUnit)
                                                    .getShortLabel(fragment.getContext()));
        } else if (quantity != null) {
            return String.format("%s", new DecimalFormat("#.##").format(quantity));
        } else if (quantityUnit != null) {
            return String.format("%s",
                                 quantityUnitAdapter.fromCode(quantityUnit)
                                                    .getShortLabel(fragment.getContext()));
        }
        return "";
    }

    public int getNofSelectedItems() {
        return selected.size();
    }

    public Optional<GroceryListEntry> getSelectedGroceryListEntry() {
        List<GroceryListEntry> data = groceryListEntryModel.getLiveData().getValue();

        if (data == null) {
            return Optional.empty();
        }

        return Optional.of(groceryListEntryModel.getLiveData().getValue().get(selected.get(0)));
    }

    public List<GroceryListEntry> getSelectedGroceryListEntries() {
        return selected.stream()
                       .map(position -> groceryListEntryModel.getLiveData().getValue() != null ?
                                        groceryListEntryModel.getLiveData()
                                                             .getValue()
                                                             .get(position) :
                                        null)
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
        selected.forEach(id -> {
            GroceryListEntryViewAdapter.GroceryListEntryHolder holder =
                    ((GroceryListEntryViewAdapter.GroceryListEntryHolder) recyclerView.findViewHolderForAdapterPosition(
                            id));
            if (holder != null) {
                holder.view.setSelected(false);
            }
        });
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
        public final TextView quantityWithUnit;
        public final CheckBox checked;
        public final Context ctx;

        public GroceryListEntry groceryListEntry;

        GroceryListEntryHolder(View view, Context context) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            quantityWithUnit = view.findViewById(R.id.quantity_with_unit);
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
