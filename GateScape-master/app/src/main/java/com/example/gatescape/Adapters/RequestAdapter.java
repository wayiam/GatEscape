package com.example.gatescape.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gatescape.R;
import com.example.gatescape.models.RequestInfo;
import com.example.gatescape.util.TimeAgo;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class RequestAdapter extends FirestoreRecyclerAdapter<RequestInfo , RequestAdapter.ReqViewHolder> {

    private OnItemClickListener listener;

    public RequestAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReqViewHolder holder, int position, @NonNull RequestInfo model) {
        holder.getUserName().setText(model.getUser().getName());
        holder.getUserBranch().setText(model.getUser().getBranch());
        holder.getUserSem().setText(model.getUser().getSem());
        holder.getCreatedAt().setText("Created : "+ TimeAgo.getTimeAgo(model.getCreatedAt()));
    }

    @NonNull
    @Override
    public ReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item , parent , false);
        return new ReqViewHolder(view);
    }

    class ReqViewHolder extends RecyclerView.ViewHolder{

        private final TextView UserName , UserBranch , UserSem , createdAt;
        public ReqViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.OnItemClick(getSnapshots().getSnapshot(position) , position);
                    }
                }
            });

            UserName = itemView.findViewById(R.id.i_username);
            UserBranch = itemView.findViewById(R.id.i_userbranch);
            UserSem = itemView.findViewById(R.id.i_usersem);
            createdAt = itemView.findViewById(R.id.i_createdAt);
        }

        public TextView getUserName() {
            return UserName;
        }

        public TextView getUserBranch() {
            return UserBranch;
        }

        public TextView getUserSem() {
            return UserSem;
        }

        public TextView getCreatedAt() {
            return createdAt;
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
