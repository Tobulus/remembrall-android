package com.groceries.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;
import com.groceries.api.data.GroceryListEntryData;
import com.groceries.locator.ServiceLocator;
import com.groceries.model.database.Database;
import com.groceries.model.database.GroceryListEntry;

import java.util.List;
import java.util.stream.Collectors;

public class GroceryListEntryModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryListEntry>> liveData = new MutableLiveData<>();
    private Backend backend;
    private Long groceryListId;

    public GroceryListEntryModel(@NonNull Application application, Long groceryListId) {
        super(application);
        this.backend = ServiceLocator.getInstance().get(Backend.class);
        this.groceryListId = groceryListId;
        this.liveData.setValue(ServiceLocator.getInstance()
                                             .get(Database.class)
                                             .groceryListEntryRepository()
                                             .getGroceryListEntries(groceryListId));
        loadDataFromBackend();
    }

    public LiveData<List<GroceryListEntry>> getLiveData() {
        return liveData;
    }

    public void refresh() {
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        backend.getGroceryListEntries(groceryListId, groceryListEntries -> {
            List<GroceryListEntry> entries = groceryListEntries.stream()
                                                               .map(GroceryListEntryData::toEntity)
                                                               .collect(Collectors.toList());
            ServiceLocator.getInstance()
                          .get(Database.class)
                          .groceryListEntryRepository().deleteAll();
            ServiceLocator.getInstance()
                          .get(Database.class)
                          .groceryListEntryRepository()
                          .upsert(entries);
            liveData.setValue(entries);
        }, error -> {
        });
    }
}
