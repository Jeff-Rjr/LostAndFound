package com.example.lostandfound.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lostandfound.R;
import com.example.lostandfound.database.LostFoundItem;
import java.util.List;

public class ItemAdapter extends
        RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    List<LostFoundItem> itemList;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LostFoundItem item);
    }

    public ItemAdapter(List<LostFoundItem> itemList,
                       OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageItem;
        TextView textPostType, textName, textLocation, textTimestamp;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem     = itemView.findViewById(R.id.imageItem);
            textPostType  = itemView.findViewById(R.id.textPostType);
            textName      = itemView.findViewById(R.id.textName);
            textLocation  = itemView.findViewById(R.id.textLocation);
            textTimestamp = itemView.findViewById(R.id.textTimestamp);
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lost_found, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder,
                                 int position) {
        LostFoundItem item = itemList.get(position);

        holder.textName.setText(item.name);
        holder.textLocation.setText("📍 " + item.location);
        holder.textTimestamp.setText("Posted: " + item.timestamp);
        holder.textPostType.setText(item.postType.toUpperCase());

        if (item.postType.equals("Lost")) {
            holder.textPostType.setBackgroundColor(
                    Color.parseColor("#C62828"));
        } else {
            holder.textPostType.setBackgroundColor(
                    Color.parseColor("#2E7D32"));
        }

        if (item.imagePath != null && !item.imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(item.imagePath);
            if (bitmap != null) {
                holder.imageItem.setImageBitmap(bitmap);
            }
        }

        // Tap the whole item row to open the detail screen
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(List<LostFoundItem> newList) {
        itemList = newList;
        notifyDataSetChanged();
    }
}