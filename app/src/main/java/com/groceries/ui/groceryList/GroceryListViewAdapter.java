package com.groceries.ui.groceryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.model.GroceryList;

import java.util.List;

public class GroceryListViewAdapter
        extends RecyclerView.Adapter<GroceryListViewAdapter.GroceryListHolder> {

    private final List<GroceryList> groceryLists;
    private final GroceryListFragment.GroceryListListener groceriesActivity;

    GroceryListViewAdapter(List<GroceryList> items,
                           GroceryListFragment.GroceryListListener listener) {
        groceryLists = items;
        groceriesActivity = listener;
    }

    @Override
    public GroceryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list, parent, false);
        return new GroceryListHolder(view);
    }

    @Override
    public void onBindViewHolder(final GroceryListHolder holder, int position) {
        holder.groceryList = groceryLists.get(position);
        holder.name.setText(groceryLists.get(position).getName());
        holder.parent.setOnClickListener(v -> groceriesActivity.onClick(holder.groceryList));
    }

    @Override
    public int getItemCount() {
        return groceryLists.size();
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
