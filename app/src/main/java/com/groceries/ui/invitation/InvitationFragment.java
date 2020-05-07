package com.groceries.ui.invitation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.api.BackendProvider;
import com.groceries.model.Invitation;

import java.util.ArrayList;
import java.util.List;

public class InvitationFragment extends Fragment {

    private BackendProvider backendProvider;

    public InvitationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invitations, container, false);

        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            List<Invitation> invitations = new ArrayList<>();
            InvitationViewAdapter adapter = new InvitationViewAdapter(invitations);
            recyclerView.setAdapter(adapter);
            backendProvider.getBackend().getInvitations(i -> {
                invitations.addAll(i);
                adapter.notifyDataSetChanged();
            }, error -> {
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackendProvider) {
            backendProvider = (BackendProvider) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BackendProvider");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        backendProvider = null;
    }
}
