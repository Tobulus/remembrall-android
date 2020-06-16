package com.remembrall.model.view.factory;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.remembrall.model.view.GroceryListModel;

public class GroceryListModelFactory implements ViewModelProvider.Factory {

    private boolean archived;
    private Application application;

    public GroceryListModelFactory(boolean archived, @NonNull Application application) {
        this.archived = archived;
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return modelClass.cast(new GroceryListModel(application, archived));
    }
}