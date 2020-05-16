package com.groceries.model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;
import com.groceries.model.pojo.Invitation;

import java.util.ArrayList;
import java.util.List;

public class InvitationModel extends AndroidViewModel {

    private final MutableLiveData<List<Invitation>> liveData = new MutableLiveData<>();
    private Backend backend;

    public InvitationModel(@NonNull Application application) {
        super(application);
        backend = new Backend(application.getApplicationContext(), null);
        liveData.setValue(new ArrayList<>());
        loadDataFromBackend();
    }

    private void loadDataFromBackend() {
        backend.getInvitations(liveData::setValue, error -> {
        });
    }

    public LiveData<List<Invitation>> getLiveData() {
        return liveData;
    }
}
