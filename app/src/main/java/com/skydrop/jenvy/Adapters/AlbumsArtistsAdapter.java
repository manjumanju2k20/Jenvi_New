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

import com.skydrop.jenvy.Interfaces.AlbumArtistClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.models.SongModel;
import com.skydrop.jenvy.singleton.SongsList_singleton;


public class AlbumsArtistsAdapter extends RecyclerView.Adapter<AlbumsArtistsAdapter.MyViewHolder> {
    private final SongsList_singleton songsList = SongsList_singleton.getInstance();
    private Context context;
    private String item;
    private AlbumArtistClickListener listener;

    public AlbumsArtistsAdapter(Context context, String item, AlbumArtistClickListener listener) {
        this.context = context;
        this.item = item;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumsArtistsAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.album_recview, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String itemValue = songsList.getItemName(item, position);
        SongModel model = songsList.getItemModel(item, itemValue, 0);
        holder.name.setText(itemValue);
        holder.art.setImageBitmap(model.getAlbumArt());
    }

    @Override
    public int getItemCount() {
        return songsList.getSize(item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        ImageButton playBnt;
        ImageView art;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.album_artist_name_rec);
            playBnt = itemView.findViewById(R.id.album_artist_play_bnt_rec);
            art = itemView.findViewById(R.id.album_artist_art_rec);

            name.setSelected(true);

            itemView.setOnClickListener(this);
            playBnt.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int type;
            if (view == playBnt) {
                type = 1;
            } else {
                type = 0;
            }
            listener.onClick(type, item, getAdapterPosition());
        }
    }
}
