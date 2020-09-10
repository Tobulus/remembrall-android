package com.remembrall.model.view;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.remembrall.api.backend.InvitationBackend;
import com.remembrall.api.data.InvitationData;
import com.remembrall.locator.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class InvitationModel extends AndroidViewModel {

    private final MutableLiveData<List<InvitationData>> liveData = new MutableLiveData<>();
    private InvitationBackend backend;

    public InvitationModel(@NonNull Application application) {
        super(application);
        backend = ServiceLocator.getInstance().get(InvitationBackend.class);
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

    public void refresh() {
        loadDataFromBackend();
    }
}
