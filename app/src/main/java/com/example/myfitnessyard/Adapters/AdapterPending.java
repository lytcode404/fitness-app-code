package com.example.myfitnessyard.Adapters;



import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myfitnessyard.Models.Users;
import com.example.myfitnessyard.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class AdapterPending extends FirebaseRecyclerAdapter<Users, AdapterPending.myViewHolder> {

    int REQUEST_CALL = 1;

    public AdapterPending(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Users model) {

        Glide.with(holder.profileImg.getContext()).load(model.getImageUrl())
                .placeholder(R.drawable.ic_baseline_person_24)
                .into(holder.profileImg);
        holder.uName.setText(model.getuName());
        holder.uNo.setText(model.getuNo());

        holder.txt_option.setOnClickListener(view -> {
            popupmenu(view,holder,model,position);

        });

        holder.wpBtn.setOnClickListener(view -> {
            String ph = "+91"+model.getuPhno().trim();
            String msg = "submit your fee: "+model.getFee();
            Intent i = new Intent(Intent.ACTION_VIEW
                    , Uri.parse("https://api.whatsapp.com/send?phone="+ph+"&text="+msg));

            if (i.resolveActivity(view.getContext().getPackageManager())
                    == null) {
                Toast.makeText(view.getContext()
                        , "Please install whatsapp first.", Toast.LENGTH_SHORT).show();
                return;
            }
            view.getContext().startActivity(i);
        });

        holder.callBtn.setOnClickListener(view -> {
            String dial = "tel:"+model.getuPhno().trim();
            if (ContextCompat.checkSelfPermission(view.getContext()
                    , Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions((Activity) view.getContext(),
                        new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }else {
                view.getContext().startActivity(new Intent(Intent.ACTION_CALL
                        , Uri.parse(dial)));
            }
        });

    }


    private void popupmenu(View view, myViewHolder holder, Users model, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.txt_option);
        popupMenu.inflate(R.menu.options_menu_paid);
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_edit:

                    final DialogPlus dialogPlus = DialogPlus
                            .newDialog(holder.txt_option.getContext())
                            .setContentHolder(new ViewHolder(R.layout.dialog_content))
                            .setExpanded(true,900)
                            .create();
                    View mView = dialogPlus.getHolderView();


                    EditText uName = mView.findViewById(R.id.uName);
                    EditText uNo = mView.findViewById(R.id.uNo);
                    EditText uPhno = mView.findViewById(R.id.uPhnumber);
                    EditText fee = mView.findViewById(R.id.fees);
                    EditText date = mView.findViewById(R.id.joiningDate);

                    Button update = mView.findViewById(R.id.update);



                    uName.setText(model.getuName());
                    uNo.setText(model.getuNo());
                    uPhno.setText(model.getuPhno());
                    fee.setText(model.getFee());
                    date.setText(model.getDate());


                    dialogPlus.show();

                    update.setOnClickListener(view1 -> {



                        Map<String,Object> map = new HashMap<>();
                        map.put("uName",uName.getText().toString());
                        map.put("uNo",uNo.getText().toString());
                        map.put("uPhno",uPhno.getText().toString());
                        map.put("fee",fee.getText().toString());
                        map.put("date",date.getText().toString());


                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(model.getuNo()+model.getuName())
                                .updateChildren(map);

                        FirebaseDatabase.getInstance().getReference().child("paid")
                                .child((getRef(position).getKey()))
                                .updateChildren(map)
                                .addOnSuccessListener(runnable -> {
                                    Toast.makeText(view.getContext(), "Successfully updated", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                }).addOnFailureListener(runnable -> {
                                    Toast.makeText(view.getContext(), "Failed to  update", Toast.LENGTH_SHORT).show();
                                    dialogPlus.dismiss();
                                });


                    });


                    break;
                case R.id.menu_unpay:
                    FirebaseDatabase.getInstance().getReference().child("pending")
                            .child(model.getuNo()+model.getuName()).setValue(model);

                    FirebaseDatabase.getInstance().getReference().child("revenue")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    String fund = snapshot.getValue(String.class);

                                    int money = Integer.parseInt(fund);
                                    int money2 = Integer.parseInt(model.getFee().toString());

                                    int netMoney = money-money2;

                                    FirebaseDatabase.getInstance().getReference().child("revenue")
                                            .setValue(Integer.toString(netMoney));


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                    FirebaseDatabase.getInstance().getReference().child("paid")
                            .child(getRef(position).getKey()).removeValue();
                    Toast.makeText(view.getContext(), "Successfull", Toast.LENGTH_SHORT).show();
                    break;


            }
            return false;
        });
        popupMenu.show();
    }




    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_pending
                , parent , false);

        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView profileImg,callBtn,wpBtn;
        TextView uName;
        TextView uNo;
        TextView txt_option;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            profileImg = itemView.findViewById(R.id.profileImage);
            uName = itemView.findViewById(R.id.uName);
            uNo = itemView.findViewById(R.id.uNo);
            txt_option = itemView.findViewById(R.id.txt_option);
            callBtn = itemView.findViewById(R.id.callBtn);
            wpBtn = itemView.findViewById(R.id.wpBtn);


        }
    }
}
