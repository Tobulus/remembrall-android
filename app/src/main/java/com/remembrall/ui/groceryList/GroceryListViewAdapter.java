package com.remembrall.ui.groceryList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.remembrall.R;
import com.remembrall.model.database.GroceryList;
import com.remembrall.model.view.GroceryListModel;

import java.util.List;

public class GroceryListViewAdapter
        extends RecyclerView.Adapter<GroceryListViewAdapter.GroceryListHolder> {

    private final GroceryListModel groceryListModel;
    private final GroceryListFragment.GroceryListListener groceriesActivity;
    private final Fragment fragment;

    public GroceryListViewAdapter(LifecycleOwner owner,
                                  GroceryListModel items,
                                  GroceryListFragment.GroceryListListener listener) {
        groceryListModel = items;
        groceriesActivity = listener;
        fragment = (GroceryListFragment) owner;
        items.getLiveData().observe(owner, groceryLists -> this.notifyDataSetChanged());
    }

    @Override
    public GroceryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_grocery_list, parent, false);
        return new GroceryListHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull final GroceryListHolder holder, int position) {
        List<GroceryList> groceryLists = groceryListModel.getLiveData().getValue();
        if (groceryLists != null) {
            holder.groceryList = groceryLists.get(position);
            holder.name.setText(groceryLists.get(position).getName());
            holder.numberInfo.setText(String.format("%s / %s",
                                                    holder.groceryList.getNumberOfCheckedEntries(),
                                                    holder.groceryList.getNumberOfEntries()));
            holder.parent.setOnClickListener(v -> groceriesActivity.onClick(holder.groceryList));
        }
    }

    @Override
    public int getItemCount() {
        return groceryListModel.getLiveData().getValue() != null ?
               groceryListModel.getLiveData().getValue().size() :
               0;
    }

    public void refresh() {
        groceryListModel.refresh();
    }

    public static class GroceryListHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView name;
        public final TextView numberInfo;
        public final LinearLayout parent;
        public final Context ctx;

        public GroceryList groceryList;

        public GroceryListHolder(View view, Context context) {
            super(view);
            this.view = view;
            name = view.findViewById(R.id.name);
            numberInfo = view.findViewById(R.id.numberInfo);
            parent = view.findViewById(R.id.parentLayout);
            ctx = context;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + name.getText() + "'";
        }
    }
}
