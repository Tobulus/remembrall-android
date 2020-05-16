package com.groceries.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;
import com.groceries.model.pojo.GroceryListEntry;
import com.groceries.servicelocater.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class GroceryListEntryModel extends AndroidViewModel {
    private final MutableLiveData<List<GroceryListEntry>> liveData = new MutableLiveData<>();
    private Backend backend;
    private Long groceryListId;

    public GroceryListEntryModel(@NonNull Application application, Long groceryListId) {
        super(application);
        this.backend = ServiceLocator.getInstance().get(Backend.class);
        this.groceryListId = groceryListId;
        this.liveData.setValue(new ArrayList<>());
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        backend.getGroceryListEntries(groceryListId, liveData::setValue, error -> {
        });
    }

    public LiveData<List<GroceryListEntry>> getLiveData() {
        return liveData;
    }
}
