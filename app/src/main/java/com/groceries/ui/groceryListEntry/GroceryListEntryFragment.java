package com.groceries.ui.groceryListEntry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Response;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.groceries.R;
import com.groceries.model.GroceryListEntry;
import com.groceries.ui.activity.CreateGroceryListEntryActivity;

import java.util.List;
import java.util.function.Consumer;

public class GroceryListEntryFragment extends Fragment {
    private static final int LAUNCH_CREATE_ACTIVITY = 1;

    private GroceryListEntryFragmentInteractionListener mListener;
    private Long groceryListId;

    public GroceryListEntryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groceryListId = getArguments().getLong("id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_list_entries, container, false);

        FloatingActionButton fab = getActivity().findViewById(R.id.floating_plus_button);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),
                                       CreateGroceryListEntryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong("id", groceryListId);
            intent.putExtras(bundle);
            startActivityForResult(intent, LAUNCH_CREATE_ACTIVITY);
        });

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            mListener.loadListEntries(groceryListId,
                                      entries -> recyclerView.setAdapter(new GroceryListEntryViewAdapter(
                                              entries,
                                              mListener)),
                                      error -> {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                /*Intent intent = getIntent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                startActivity(intent);*/
            }
        }
    }

    public interface GroceryListEntryFragmentInteractionListener {
        void onClick(GroceryListEntry entry);

        void toggleChecked(GroceryListEntry entry, Runnable onError);

        void loadListEntries(Long groceryListId,
                             Consumer<List<GroceryListEntry>> listConsumer,
                             Response.ErrorListener errorListener);
    }
}
