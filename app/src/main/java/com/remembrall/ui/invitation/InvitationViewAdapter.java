package com.remembrall.ui.invitation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import com.remembrall.R;
import com.remembrall.api.Backend;
import com.remembrall.api.data.InvitationData;
import com.remembrall.locator.ServiceLocator;
import com.remembrall.model.view.InvitationModel;

import java.util.List;

public class InvitationViewAdapter
        extends RecyclerView.Adapter<InvitationViewAdapter.InvitationHolder> {

    private final InvitationModel invitationModel;

    public InvitationViewAdapter(LifecycleOwner owner, InvitationModel invitationModel) {
        this.invitationModel = invitationModel;
        invitationModel.getLiveData().observe(owner, invitations -> this.notifyDataSetChanged());
    }

    @Override
    public InvitationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_invitation, parent, false);
        return new InvitationHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvitationHolder holder, int position) {
        List<InvitationData> invitations = invitationModel.getLiveData().getValue();
        if (invitations != null) {
            holder.invitation = invitations.get(position);
            holder.title.setText(invitations.get(position).getSender().getUsername()
                                 + " invites you to list "
                                 + invitations.get(position).getGroceryList().getName());

            holder.ack.setOnClickListener(v -> ServiceLocator.getInstance()
                                                             .get(Backend.class)
                                                             .acknowledge(holder.invitation.getId(),
                                                                          s -> refresh(),
                                                                          e -> {

                                                                          }));

            holder.deny.setOnClickListener(v -> ServiceLocator.getInstance()
                                                              .get(Backend.class)
                                                              .deny(holder.invitation.getId(),
                                                                    s -> refresh(),
                                                                    e -> {

                                                                    }));
        }
    }

    private void refresh() {
        invitationModel.refresh();
    }

    @Override
    public int getItemCount() {
        return invitationModel.getLiveData().getValue() != null ?
               invitationModel.getLiveData().getValue().size() :
               0;
    }

    public class InvitationHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final ImageView ack;
        public final ImageView deny;

        public InvitationData invitation;

        public InvitationHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.title);
            ack = view.findViewById(R.id.ack);
            deny = view.findViewById(R.id.deny);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}
