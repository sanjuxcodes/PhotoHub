package com.example.photoHub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private Context context;
    private ArrayList<Photographer> photographerList;

    public Adapter(Context context, ArrayList<Photographer> photographerList) {
        this.context = context;
        this.photographerList = photographerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, exp;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nam);
            exp = itemView.findViewById(R.id.exp);
            img = itemView.findViewById(R.id.img);
        }
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Photographer model = photographerList.get(position);

        holder.name.setText(model.getName());
        holder.exp.setText(model.getExp());

        if (model.getImg() != null && model.getImg().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(model.getImg(), 0, model.getImg().length);
            holder.img.setImageBitmap(bitmap);
        } else {
            holder.img.setImageResource(R.drawable.placeholder);
        }

        animateItem(holder.itemView);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, PhotographerDetailsActivity.class);
            intent.putExtra("uid", model.getUid());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return photographerList.size();
    }

    private void animateItem(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        view.setAnimation(animation);
    }
}
