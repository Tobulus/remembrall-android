package com.groceries.ui.groceryList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.model.GroceryList;

import java.util.List;

public class GroceryListViewAdapter
        extends RecyclerView.Adapter<GroceryListViewAdapter.GroceryListHolder> {

    private final List<GroceryList> groceryLists;
    private final GroceryListFragment.GroceryListFragmentInteractionListener groceryListActivity;

    public GroceryListViewAdapter(List<GroceryList> items,
                                  GroceryListFragment.GroceryListFragmentInteractionListener listener) {
        groceryLists = items;
        groceryListActivity = listener;
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
        holder.view.setOnClickListener(v -> groceryListActivity.onClickGroceryList(holder.groceryList));
    }

    @Override
    public int getItemCount() {
        return groceryLists.size();
    }

    public class GroceryListHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;

        public GroceryList groceryList;

        public GroceryListHolder(View view) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
