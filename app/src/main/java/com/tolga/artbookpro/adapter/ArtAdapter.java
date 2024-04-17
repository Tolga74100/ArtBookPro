package com.tolga.artbookpro.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tolga.artbookpro.art.ArtActivity;
import com.tolga.artbookpro.databinding.RecyclerRowBinding;
import com.tolga.artbookpro.maps.MapsActivity;
import com.tolga.artbookpro.model.Art;
import com.tolga.artbookpro.model.ArtAndPlace;

import java.util.ArrayList;
import java.util.List;

public class ArtAdapter extends RecyclerView.Adapter<ArtAdapter.ArtHolder> {
    List<ArtAndPlace> artAndPlaceList;
    public ArtAdapter(List<ArtAndPlace> artAndPlaceList) {
        this.artAndPlaceList = artAndPlaceList;
    }


    @NonNull
    @Override
    public ArtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new ArtHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtHolder holder, int position) {
        holder.binding.recyclerViewTextView.setText(artAndPlaceList.get(position).art.artName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ArtActivity.class);
                intent.putExtra("info", "old");
                intent.putExtra("art", artAndPlaceList.get(position));
                holder.itemView.getContext().startActivity(intent);

            }
        });
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), MapsActivity.class);
                intent.putExtra("place", artAndPlaceList.get(position));

            }
        });*/

    }

    @Override
    public int getItemCount() {
        return artAndPlaceList.size();
    }

    public class ArtHolder extends RecyclerView.ViewHolder{

        private RecyclerRowBinding binding;
        public ArtHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
