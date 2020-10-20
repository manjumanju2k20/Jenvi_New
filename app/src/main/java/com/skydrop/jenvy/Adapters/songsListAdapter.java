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

import com.skydrop.jenvy.Interfaces.SongitemClickListener;
import com.skydrop.jenvy.R;
import com.skydrop.jenvy.models.SongModel;
import com.skydrop.jenvy.singleton.SongsList_singleton;


public class songsListAdapter extends RecyclerView.Adapter<songsListAdapter.MyViewHolder> {
    private final SongsList_singleton singleton = SongsList_singleton.getInstance();
    private final Context context;
    private final SongitemClickListener Listener;
    private String item;
    private String itemname;

    public songsListAdapter(Context context, SongitemClickListener listener, String item, String itemname) {
        this.context = context;
        Listener = listener;
        this.item = item;
        this.itemname = itemname;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rec_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SongModel model = singleton.getitemmodel(item,itemname,position);
        holder.songName.setText(model.getTitle());
        holder.albumName.setText(model.getAlbum());
        holder.songTime.setText(model.getFormattedDuration());
        holder.imageView.setImageBitmap(model.getAlbumart());
    }

    @Override
    public int getItemCount() {
        return singleton.getsize(item,itemname);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView songName;
        public ImageButton options;
        public TextView songTime;
        public ImageView imageView;
        public TextView albumName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.title_songs_rec);
            songTime = itemView.findViewById(R.id.songtime_songs_rec);
            imageView = itemView.findViewById(R.id.albumart_songs_rec);
            albumName = itemView.findViewById(R.id.album_songs_rec);
            options = itemView.findViewById(R.id.options_songs_rec);
//            songName.setSelected(true);
            options.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Listener.onClick(getAdapterPosition(),item,itemname);
        }
    }
}
