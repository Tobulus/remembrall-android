package com.groceries.ui.groceryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.model.database.GroceryList;
import com.groceries.model.view.GroceryListModel;

import java.util.List;

public class GroceryListViewAdapter
        extends RecyclerView.Adapter<GroceryListViewAdapter.GroceryListHolder> {

    private final GroceryListModel groceryListModel;
    private final GroceryListFragment.GroceryListListener groceriesActivity;

    GroceryListViewAdapter(LifecycleOwner owner, GroceryListModel items,
                           GroceryListFragment.GroceryListListener listener) {
        groceryListModel = items;
        groceriesActivity = listener;
        items.getLiveData().observe(owner, groceryLists -> this.notifyDataSetChanged());
    }

    @Override
    public GroceryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list, parent, false);
        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryListHolder holder, int position) {
        List<GroceryList> groceryLists = groceryListModel.getLiveData().getValue();
        if (groceryLists != null) {
            holder.groceryList = groceryLists.get(position);
            holder.name.setText(groceryLists.get(position).getName());
            holder.parent.setOnClickListener(v -> groceriesActivity.onClick(holder.groceryList));
        }
    }

    @Override
    public int getItemCount() {
        return groceryListModel.getLiveData().getValue() != null ?
               groceryListModel.getLiveData().getValue().size() :
               0;
    }

    public static class GroceryListHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final LinearLayout parent;

        public GroceryList groceryList;

        public GroceryListHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            parent = view.findViewById(R.id.parentLayout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
