package com.groceries.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.groceries.api.Backend;
import com.groceries.api.data.InvitationData;

import java.util.ArrayList;
import java.util.List;

public class InvitationModel extends AndroidViewModel {

    private final MutableLiveData<List<InvitationData>> liveData = new MutableLiveData<>();
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

    public LiveData<List<InvitationData>> getLiveData() {
        return liveData;
    }
}
