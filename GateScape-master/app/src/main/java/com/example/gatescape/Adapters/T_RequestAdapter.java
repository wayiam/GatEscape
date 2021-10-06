package com.example.gatescape.Adapters;

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

public class T_RequestAdapter extends FirestoreRecyclerAdapter<RequestInfo , T_RequestAdapter.T_ReqViewHolder> {

    private OnItemClickListener listener;

    public T_RequestAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull T_ReqViewHolder holder, int position, @NonNull RequestInfo model) {
        holder.getUserName().setText(model.getUser().getName());
        holder.getUserBranch().setText(model.getUser().getBranch());
        holder.getUserSem().setText(model.getUser().getSem());
        holder.getCreatedAt().setText("Applied : "+ TimeAgo.getTimeAgo(model.getCreatedAt()));
    }

    @NonNull
    @Override
    public T_ReqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item , parent , false);
        return new T_ReqViewHolder(view);
    }

    class T_ReqViewHolder extends RecyclerView.ViewHolder{

        private final TextView UserName , UserBranch , UserSem , createdAt;
        public T_ReqViewHolder(@NonNull View itemView) {
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
        public void OnItemClick(DocumentSnapshot documentSnapshot , int position);
    }

    public void setOnItemCLickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
