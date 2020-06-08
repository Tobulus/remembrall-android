package com.remembrall.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.remembrall.api.Backend;
import com.remembrall.api.data.GroceryListData;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.Database;
import com.remembrall.model.database.GroceryList;
import com.remembrall.model.database.repository.GroceryListRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GroceryListModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryList>> liveData = new MutableLiveData<>();
    private Backend backend;
    private GroceryListRepository repository;

    public GroceryListModel(@NonNull Application application) {
        super(application);
        backend = ServiceLocator.getInstance().get(Backend.class);
        repository = ServiceLocator.getInstance().get(Database.class).groceryListRepository();
        liveData.setValue(ServiceLocator.getInstance()
                                        .get(Database.class)
                                        .groceryListRepository()
                                        .getGroceryLists());
        loadDataFromBackend();
    }

    public void refresh() {
        loadDataFromBackend();
    }

    public LiveData<List<GroceryList>> getLiveData() {
        return liveData;
    }

    private void loadDataFromBackend() {
        backend.getGroceryLists(groceryLists -> {
            List<GroceryList> lists = groceryLists.stream()
                                                  .map(GroceryListData::toEntity)
                                                  .collect(Collectors.toList());
            repository.deleteAll();
            repository.upsert(lists);
            liveData.setValue(lists);
        }, error -> {
        });
    }
}