package com.groceries.ui.invitation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.api.BackendProvider;
import com.groceries.model.Invitation;

import java.util.List;

public class InvitationViewAdapter
        extends RecyclerView.Adapter<InvitationViewAdapter.InvitationHolder> {

    private final List<Invitation> invitations;
    private final BackendProvider backendProvider;

    public InvitationViewAdapter(List<Invitation> items, BackendProvider provider) {
        invitations = items;
        backendProvider = provider;
    }

    @Override
    public InvitationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.fragment_invitation, parent, false);
        return new InvitationHolder(view);
    }

    @Override
    public void onBindViewHolder(final InvitationHolder holder, int position) {
        holder.invitation = invitations.get(position);
        holder.title.setText(invitations.get(position).getSender().getUsername()
                             + " invites you to list "
                             + invitations.get(position).getGroceryList().getName());

        holder.ack.setOnClickListener(v -> backendProvider.getBackend()
                                                          .acknowledge(holder.invitation.getId(),
                                                                       s -> invitations.remove(
                                                                               position),
                                                                       e -> {

                                                                       }));

        holder.deny.setOnClickListener(v -> backendProvider.getBackend()
                                                           .deny(holder.invitation.getId(),
                                                                 s -> invitations.remove(position),
                                                                 e -> {

                                                                 }));
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public class InvitationHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;
        public final ImageView ack;
        public final ImageView deny;

        public Invitation invitation;

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
