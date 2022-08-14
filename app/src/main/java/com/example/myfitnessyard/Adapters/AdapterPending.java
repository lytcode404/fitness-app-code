package com.example.myfitnessyard.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfitnessyard.Models.Users;
import com.example.myfitnessyard.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdapterPending extends FirebaseRecyclerAdapter<Users, AdapterPending.myViewHolder> {

    public AdapterPending(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Users model) {
        Glide.with(holder.profileImg.getContext()).load(model.getImageUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.profileImg);
        holder.uName.setText(model.getuName());
        holder.uNo.setText(model.getuNo());

        holder.txt_option.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.txt_option);
            popupMenu.inflate(R.menu.options_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()){
                    case R.id.menu_edit:

                        Toast.makeText(view.getContext()
                                , "menu item edit clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_remove:
                        Toast.makeText(view.getContext()
                                , "menu item remove clicked", Toast.LENGTH_SHORT).show();
                        break;


                }
                return false;
            });
            popupMenu.show();

        });




    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_pending
                , parent , false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg,callBtn,wpBtn;
        TextView uName;
        TextView uNo;
        TextView txt_option;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profileImage);
            uName = itemView.findViewById(R.id.uName);
            uNo = itemView.findViewById(R.id.uNo);
            txt_option = itemView.findViewById(R.id.txt_option);
            callBtn = itemView.findViewById(R.id.callBtn);
            wpBtn = itemView.findViewById(R.id.wpBtn);


        }
    }
}
