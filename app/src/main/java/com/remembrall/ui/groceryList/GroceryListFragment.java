package com.remembrall.ui.groceryList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.listener.BackPressedListener;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.GroceryList;
import com.remembrall.model.view.GroceryListModel;

public class GroceryListFragment extends Fragment implements BackPressedListener {

    static final int LAUNCH_CREATE_GROCERY_LIST = 1;

    private GroceryListListener mListener;
    private GroceryListViewAdapter adapter;

    public GroceryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grocery_lists, container, false);
        GroceryListModel model = ViewModelProviders.of(this).get(GroceryListModel.class);

        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroceryListViewAdapter(this, model, mListener, recyclerView);
        recyclerView.setAdapter(adapter);

        ((SwipeRefreshLayout) view).setOnRefreshListener(() -> {
            adapter.refresh();
            ((SwipeRefreshLayout) view).setRefreshing(false);
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_grocery_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_edit).setEnabled(adapter.getNofSelectedItems() == 1);
        menu.findItem(R.id.action_edit).setVisible(adapter.getNofSelectedItems() == 1);

        menu.findItem(R.id.action_delete).setEnabled(adapter.getNofSelectedItems() > 0);
        menu.findItem(R.id.action_delete).setVisible(adapter.getNofSelectedItems() > 0);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            showGroceryListDialog();
            return true;
        }

        if (item.getItemId() == R.id.action_delete) {
            deleteSelectedItems();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.floating_plus_button);
        fab.setOnClickListener(v -> {
            GroceryListDialog dialog = new GroceryListDialog();
            dialog.setTargetFragment(this, LAUNCH_CREATE_GROCERY_LIST);
            dialog.show(requireFragmentManager(), "dialog-grocery-list");
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_CREATE_GROCERY_LIST && resultCode == Activity.RESULT_OK) {
            adapter.refresh();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof GroceryListListener) {
            mListener = (GroceryListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement GroceryListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onBackPressed() {
        return adapter.unselect();
    }

    private void showGroceryListDialog() {
        GroceryListDialog dialog = new GroceryListDialog(adapter.getSelectedGroceryList());
        dialog.setTargetFragment(this, LAUNCH_CREATE_GROCERY_LIST);
        dialog.show(requireFragmentManager(), "edit-grocery-list");
    }

    private void deleteSelectedItems() {
        adapter.getSelectedGroceryLists()
               .forEach(groceryList -> ServiceLocator.getInstance()
                                                     .get(Backend.class)
                                                     .deleteGroceryList(groceryList,
                                                                        json -> adapter.refresh(),
                                                                        error -> Toast.makeText(
                                                                                getContext(),
                                                                                error.getMessage(),
                                                                                Toast.LENGTH_LONG)
                                                                                      .show()));
    }

    public interface GroceryListListener {
        void onClick(GroceryList item);
    }
}
