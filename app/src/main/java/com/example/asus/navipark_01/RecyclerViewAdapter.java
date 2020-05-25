package com.example.asus.navipark_01;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> mImgPark = new ArrayList<>();   //URL
    private ArrayList<String> mTxtTime = new ArrayList<>();
    private ArrayList<String> mTxtDescription = new ArrayList<>();
    private ArrayList<String> mIndex = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<String> mImgPark, ArrayList<String> mTxtTime, ArrayList<String> mTxtDescription, ArrayList<String> mIndex, Context mContext) {
        this.mImgPark = mImgPark;
        this.mTxtTime = mTxtTime;
        this.mTxtDescription = mTxtDescription;
        this.mIndex = mIndex;
        this.mContext = mContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        ImageView imgPark;
        TextView txtTime;
        TextView txtDescription;
        ConstraintLayout parentHistory;

        public ViewHolder(View itemView) {
            super(itemView);

            imgPark = itemView.findViewById(R.id.imgPark);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            parentHistory = itemView.findViewById(R.id.itemLayout);

            parentHistory.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(this.getAdapterPosition(), 0, 0, "Edit");
            menu.add(this.getAdapterPosition(), 1, 1, "Delete");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        Glide.with(mContext).asBitmap().load(mImgPark.get(position)).into(holder.imgPark);

        holder.txtTime.setText(mTxtTime.get(position));
        holder.txtDescription.setText(mTxtDescription.get(position));

        holder.parentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on "+mTxtTime.get(position));

                Toast.makeText(mContext, mTxtTime.get(position), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, DirectionsActivity.class).putExtra("index", mIndex.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImgPark.size();
    }
}
