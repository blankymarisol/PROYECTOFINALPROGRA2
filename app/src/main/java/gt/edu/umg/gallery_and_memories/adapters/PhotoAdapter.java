package gt.edu.umg.gallery_and_memories.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import gt.edu.umg.gallery_and_memories.R;
import gt.edu.umg.gallery_and_memories.models.PhotoItem;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<PhotoItem> photos;
    private Context context;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(PhotoItem photo, int position);
    }

    public PhotoAdapter(List<PhotoItem> photos, Context context) {
        this.photos = photos;
        this.context = context;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoItem photo = photos.get(position);

        try {
            holder.imageView.setImageURI(Uri.parse(photo.getUri()));
        } catch (Exception e) {
            // Si hay error al cargar la imagen, mostrar una imagen por defecto
            holder.imageView.setImageResource(R.drawable.troleohelmado);
        }

        // Configurar los textos
        holder.descriptionView.setText(photo.getDescription());
        holder.dateView.setText(photo.getDate());

        // Configurar el click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(photo, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public void updatePhotos(List<PhotoItem> newPhotos) {
        this.photos = newPhotos;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView descriptionView;
        TextView dateView;

        PhotoViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_photo_image);
            descriptionView = itemView.findViewById(R.id.item_photo_description);
            dateView = itemView.findViewById(R.id.item_photo_date);
        }
    }
}