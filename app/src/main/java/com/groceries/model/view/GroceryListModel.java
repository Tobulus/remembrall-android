package com.groceries.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;
import com.groceries.api.data.GroceryListData;
import com.groceries.locator.ServiceLocator;
import com.groceries.model.database.Database;
import com.groceries.model.database.GroceryList;

import java.util.List;
import java.util.stream.Collectors;

public class GroceryListModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryList>> liveData = new MutableLiveData<>();
    private Backend backend;

    public GroceryListModel(@NonNull Application application) {
        super(application);
        backend = new Backend(application.getApplicationContext(), null);
        liveData.setValue(ServiceLocator.getInstance()
                                        .get(Database.class)
                                        .groceryListRepository()
                                        .getGroceryLists());
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        backend.getGroceryLists(groceryLists -> {
            List<GroceryList> lists = groceryLists.stream()
                                                  .map(GroceryListData::toEntity)
                                                  .collect(Collectors.toList());
            ServiceLocator.getInstance().get(Database.class).groceryListRepository().upsert(lists);
            liveData.setValue(lists);
        }, error -> {
        });
    }

    public LiveData<List<GroceryList>> getLiveData() {
        return liveData;
    }
}
