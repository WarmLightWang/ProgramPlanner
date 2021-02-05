package com.example.myapplication.engine;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.element.Image;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private Context context;
    private List<Image> images;
    private OnItemClickListen Listener;

    public ImageAdapter(Context Icontext, List<Image> image) {
        context = Icontext;
        images = image;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
        Image curr = images.get(position);
        Picasso.with(context)
                .load(curr.ImageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public ImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view_upload);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (Listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Listener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Select Action");
            MenuItem save = menu.add(menu.NONE, 1, 1, "Save");
            MenuItem delete = menu.add(menu.NONE, 2, 2, "Delete");

            save.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (Listener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            Listener.OnSaveClick(position);
                            return true;
                        case 2:
                            Listener.OnDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public interface OnItemClickListen {
        void onItemClick(int position);

        void OnDeleteClick(int position);

        void OnSaveClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListen listener) {
        Listener = listener;
    }
}
