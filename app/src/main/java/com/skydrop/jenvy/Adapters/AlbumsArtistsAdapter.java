package com.skydrop.jenvy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skydrop.jenvy.Interfaces.AlbumArtistClickListner;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.models.SongModel;
import com.skydrop.jenvy.singleton.SongsList_singleton;


public class AlbumsArtistsAdapter extends RecyclerView.Adapter<AlbumsArtistsAdapter.MyViewHolder> {
    private final SongsList_singleton songslist = SongsList_singleton.getInstance();
    private Context context;
    private String item;
    private AlbumArtistClickListner listner;

    public AlbumsArtistsAdapter(Context context, String item,AlbumArtistClickListner listner) {
        this.context = context;
        this.item = item;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumsArtistsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.album_recview, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String itemvalue = songslist.getitemname(item,position);
        SongModel model = songslist.getitemmodel(item,itemvalue,0);
        holder.name.setText(itemvalue);
        holder.art.setImageBitmap(model.getAlbumart());
    }

    @Override
    public int getItemCount() {
        return songslist.getsize(item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageButton playbnt;
        ImageView art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.album_artist_name_rec);
            playbnt = itemView.findViewById(R.id.album_artist_play_bnt_rec);
            art = itemView.findViewById(R.id.album_artist_art_rec);
            itemView.setOnClickListener(this);
            playbnt.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int type;
            if(view==playbnt){
                type=1;
            }
            else {
                type = 0;
            }
            listner.onClick(type,item,getAdapterPosition());
        }
    }
}
