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
import com.android.volley.VolleyError;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.GroceryListEntry;

public class GroceryListEntryFragment extends Fragment {

    private static final String ARG_ID = "id";

    private Long mGroceryListId = -1L;
    private OnGroceryListEntryFragmentInteractionListener mListener;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroceryListId = getActivity().getIntent().getExtras().getLong(ARG_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Backend.get().getGroceryListEntries(mGroceryListId, entries -> {
                recyclerView.setAdapter(new GroceryListEntryViewAdapter(entries, mListener));
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroceryListEntryFragmentInteractionListener) {
            mListener = (OnGroceryListEntryFragmentInteractionListener) context;
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

    public interface OnGroceryListEntryFragmentInteractionListener {
        void onClickGroceryListEntry(GroceryListEntry entry);
    }
}
