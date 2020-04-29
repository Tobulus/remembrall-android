package com.groceries.ui.invitation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.groceries.R;
import com.groceries.model.Invitation;

import java.util.List;
import java.util.function.Consumer;

public class InvitationFragment extends Fragment {

    private InvitationFragmentInteractionListener mListener;

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
            mListener.loadInvitations(invitations -> recyclerView.setAdapter(new InvitationViewAdapter(
                    invitations,
                    mListener)), error -> {
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InvitationFragmentInteractionListener) {
            mListener = (InvitationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface InvitationFragmentInteractionListener {
        void ackInvitation(Invitation item);

        void denyInvitation(Invitation item);

        void loadInvitations(Consumer<List<Invitation>> listConsumer,
                             Response.ErrorListener errorListener);
    }
}
