package gt.edu.umg.gallery_and_memories.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import gt.edu.umg.gallery_and_memories.galeria.PhotoDetailActivity;
import gt.edu.umg.gallery_and_memories.models.PhotoItem;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<PhotoItem> photos; //lista de fotos a mostrar
    private Context context; //Contexto de la aplicacion
    private static final int REQUEST_DELETE_PHOTO = 1;

    //costructor, inicializa en lista de fotos y contexto
    public PhotoAdapter(List<PhotoItem> photos, Context context) {
        this.photos = photos;
        this.context = context;
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

        // Configurar la imagen
        try {
            holder.imageView.setImageURI(Uri.parse(photo.getUri()));
        } catch (Exception e) {
            holder.imageView.setImageResource(R.drawable.troleohelmado);
        }

        // Configurar los textos
        holder.descriptionView.setText(photo.getDescription());
        holder.dateView.setText(photo.getDate());

        // Configurar el click listener
        holder.itemView.setOnClickListener(v -> openPhotoDetail(photo));
    }
    //abre una nueva actividad con la foto seleccionada
    private void openPhotoDetail(PhotoItem photo) {
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_ID, photo.getId());
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_URI, photo.getUri());
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_DESC, photo.getDescription());
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_DATE, photo.getDate());
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_LAT, photo.getLatitude());
        intent.putExtra(PhotoDetailActivity.EXTRA_PHOTO_LON, photo.getLongitude());

        // Iniciar la actividad esperando resultado
        ((Activity) context).startActivityForResult(intent, REQUEST_DELETE_PHOTO);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    //metodo para actualizar la foto en el inicio
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
            //inicializa las vistas
            imageView = itemView.findViewById(R.id.item_photo_image);
            descriptionView = itemView.findViewById(R.id.item_photo_description);
            dateView = itemView.findViewById(R.id.item_photo_date);
        }
    }
}