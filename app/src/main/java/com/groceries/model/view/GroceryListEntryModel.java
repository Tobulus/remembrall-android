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
import com.groceries.model.database.repository.GroceryListEntryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GroceryListEntryModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryListEntry>> liveData = new MutableLiveData<>();
    private Backend backend;
    private Long groceryListId;
    private GroceryListEntryRepository repository;

    public GroceryListEntryModel(@NonNull Application application, Long groceryListId) {
        super(application);
        this.backend = ServiceLocator.getInstance().get(Backend.class);
        this.repository =
                ServiceLocator.getInstance().get(Database.class).groceryListEntryRepository();
        this.groceryListId = groceryListId;
        this.liveData.setValue(repository.getGroceryListEntries(groceryListId));
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
            repository.deleteAll();
            repository.upsert(entries);
            liveData.setValue(entries);
        }, error -> {
        });
    }
}
