package com.remembrall.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.remembrall.api.backend.GroceryListEntryBackend;
import com.remembrall.api.data.GroceryListEntryData;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.Database;
import com.remembrall.model.database.GroceryListEntry;
import com.remembrall.model.database.repository.GroceryListEntryRepository;

import java.util.List;
import java.util.stream.Collectors;

public class GroceryListEntryModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryListEntry>> liveData = new MutableLiveData<>();
    private GroceryListEntryBackend backend;
    private Long groceryListId;
    private GroceryListEntryRepository repository;

    public GroceryListEntryModel(@NonNull Application application, Long groceryListId) {
        super(application);
        this.backend = ServiceLocator.getInstance().get(GroceryListEntryBackend.class);
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
