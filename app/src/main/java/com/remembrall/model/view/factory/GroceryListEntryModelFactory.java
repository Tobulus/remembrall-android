package com.remembrall.model.view.factory;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remembrall.model.view.GroceryListEntryModel;

public class GroceryListEntryModelFactory implements ViewModelProvider.Factory {

    private Long groceryListId;
    private Application application;

    public GroceryListEntryModelFactory(Long groceryListId, @NonNull Application application) {
        this.groceryListId = groceryListId;
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new GroceryListEntryModel(application, groceryListId));
    }
}
