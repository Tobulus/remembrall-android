package com.groceries.ui.groceryList;

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
import com.groceries.model.GroceryList;

import java.util.List;
import java.util.function.Consumer;

public class GroceryListFragment extends Fragment {

    private GroceryListFragmentInteractionListener mListener;

    public GroceryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_lists, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mListener.loadGroceryLists(lists -> recyclerView.setAdapter(new GroceryListViewAdapter(
                    lists,
                    mListener)), error -> {
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof GroceryListFragmentInteractionListener) {
            mListener = (GroceryListFragmentInteractionListener) context;
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

    public interface GroceryListFragmentInteractionListener {
        void onClickGroceryList(GroceryList item);

        void loadGroceryLists(Consumer<List<GroceryList>> listConsumer,
                              Response.ErrorListener errorListener);
    }
}
