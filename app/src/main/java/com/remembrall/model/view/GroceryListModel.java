package com.remembrall.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.remembrall.api.backend.GroceryListBackend;
import com.remembrall.api.data.GroceryListData;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.database.Database;
import com.remembrall.model.database.GroceryList;
import com.remembrall.model.database.repository.GroceryListRepository;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GroceryListModel extends AndroidViewModel {

    private final MutableLiveData<List<GroceryList>> liveData = new MutableLiveData<>();
    private final boolean archived;
    private GroceryListBackend backend;
    private GroceryListRepository repository;

    public GroceryListModel(@NonNull Application application, boolean archived) {
        super(application);
        this.archived = archived;
        backend = ServiceLocator.getInstance().get(GroceryListBackend.class);
        repository = ServiceLocator.getInstance().get(Database.class).groceryListRepository();
        liveData.setValue(ServiceLocator.getInstance()
                                        .get(Database.class)
                                        .groceryListRepository()
                                        .getGroceryLists(archived));
    }

    public void refresh(Consumer<Boolean> onFinish) {
        loadDataFromBackend(onFinish);
    }

    public LiveData<List<GroceryList>> getLiveData() {
        return liveData;
    }

    private void loadDataFromBackend(Consumer<Boolean> onFinish) {
        backend.getGroceryLists(archived, groceryLists -> {
            List<GroceryList> lists = groceryLists.stream()
                                                  .map(GroceryListData::toEntity)
                                                  .collect(Collectors.toList());
            repository.deleteAll(archived);
            repository.upsert(lists);
            liveData.setValue(lists);

            onFinish.accept(true);
        }, error -> onFinish.accept(false));
    }
}
