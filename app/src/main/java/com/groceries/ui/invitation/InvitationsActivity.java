package com.groceries.ui.invitation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.groceries.R;
import com.groceries.api.Backend;
import com.groceries.model.Invitation;

import java.util.List;
import java.util.function.Consumer;

public class InvitationsActivity extends AppCompatActivity
        implements InvitationFragment.InvitationFragmentInteractionListener {

    private Backend backend;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        backend = new Backend(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitations);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void ackInvitation(Invitation item) {

    }

    @Override
    public void denyInvitation(Invitation item) {

    }

    @Override
    public void loadInvitations(Consumer<List<Invitation>> listConsumer, Response.ErrorListener errorListener) {
        backend.getInvitations(listConsumer, errorListener);
    }
}
