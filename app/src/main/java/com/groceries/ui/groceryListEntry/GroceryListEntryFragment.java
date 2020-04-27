package com.groceries.ui.groceryListEntry;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.groceries.R;
import com.groceries.model.GroceryListEntry;

import java.util.List;
import java.util.function.Consumer;

public class GroceryListEntryFragment extends Fragment {

    private GroceryListEntryFragmentInteractionListener mListener;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list_entries, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            mListener.loadListEntries(entries -> recyclerView.setAdapter(new GroceryListEntryViewAdapter(
                    entries,
                    mListener)), error -> {
            });
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof GroceryListEntryFragmentInteractionListener) {
            mListener = (GroceryListEntryFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                                       + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface GroceryListEntryFragmentInteractionListener {
        void onClick(GroceryListEntry entry);

        void toggleChecked(GroceryListEntry entry, Runnable onError);

        void loadListEntries(Consumer<List<GroceryListEntry>> listConsumer,
                             Response.ErrorListener errorListener);
    }
}
