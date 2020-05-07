package com.groceries.ui.invitation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.groceries.R;
import com.groceries.model.Invitation;

import java.util.List;

public class InvitationViewAdapter
        extends RecyclerView.Adapter<InvitationViewAdapter.InvitationHolder> {

    private final List<Invitation> invitations;

    public InvitationViewAdapter(List<Invitation> items) {
        invitations = items;
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
        holder.title.setText(invitations.get(position).getTitle());
        //TODO: ack and deny fields
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }

    public class InvitationHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView title;

        public Invitation invitation;

        public InvitationHolder(View view) {
            super(view);
            this.view = view;
            title = view.findViewById(R.id.title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}
