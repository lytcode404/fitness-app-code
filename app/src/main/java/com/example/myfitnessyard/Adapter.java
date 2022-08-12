package com.example.myfitnessyard;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Adapter  extends FirebaseRecyclerAdapter<Users,Adapter.myViewHolder> {

    public Adapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Users model) {
        Glide.with(holder.profileImg.getContext()).load(model.getImageUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.profileImg);
        holder.uName.setText(model.getuName());
        holder.uNo.setText(model.getuNo());
        holder.feeStatus.setText(model.getFeeStatus());
        holder.fee.setText(model.getFee());
        holder.plan.setText(model.getPlan());
        holder.date.setText(model.getDate());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample, parent , false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        TextView uName;
        TextView uNo;
        TextView feeStatus;
        TextView fee;
        TextView plan;
        TextView date;
//        String imageUrl, uName, uNo, feeStatus, fee, plan, date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImage);
            uName = itemView.findViewById(R.id.uName);
            uNo = itemView.findViewById(R.id.uNo);
            feeStatus = itemView.findViewById(R.id.feeStatus);
            fee = itemView.findViewById(R.id.fees);
            plan = itemView.findViewById(R.id.plan);
            date = itemView.findViewById(R.id.date);

        }
    }
}
