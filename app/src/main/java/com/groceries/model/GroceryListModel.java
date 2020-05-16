package com.groceries.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;

import java.util.ArrayList;
import java.util.List;

public class GroceryListModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryList>> liveData = new MutableLiveData<>();
    private Backend backend;

    public GroceryListModel(@NonNull Application application) {
        super(application);
        backend = new Backend(application.getApplicationContext(), null);
        liveData.setValue(new ArrayList<>());
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        backend.getGroceryLists(liveData::setValue, error -> {
        });
    }

    public LiveData<List<GroceryList>> getLiveData() {
        return liveData;
    }
}
