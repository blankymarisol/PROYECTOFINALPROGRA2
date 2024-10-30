package gt.edu.umg.gallery_and_memories.galeria;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Locale;

import gt.edu.umg.gallery_and_memories.R;
import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PHOTO_URI = "photo_uri"; //constantes para pasar datos
    public static final String EXTRA_PHOTO_DESC = "photo_description";
    public static final String EXTRA_PHOTO_DATE = "photo_date";
    public static final String EXTRA_PHOTO_LAT = "photo_latitude";
    public static final String EXTRA_PHOTO_LON = "photo_longitude";
    public static final String EXTRA_PHOTO_ID = "photo_id";

    //variables de la clase info. foto
    private DatabaseHelper dbHelper;
    private long photoId;
    private String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        dbHelper = new DatabaseHelper(this);

        // Obtener informacion de la foto
        photoId = getIntent().getLongExtra(EXTRA_PHOTO_ID, -1);
        photoUri = getIntent().getStringExtra(EXTRA_PHOTO_URI);

        initializeViews();
        loadPhotoData();
    }

    //configura vistas y listenrs
    private void initializeViews() {
        ImageView fullImageView = findViewById(R.id.fullImageView);
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDateTime = findViewById(R.id.detailDateTime);
        TextView detailLocation = findViewById(R.id.detailLocation);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    //carga y muestra detalles de la foto
    private void loadPhotoData() {
        //obtener fotos del intent xd
        String description = getIntent().getStringExtra(EXTRA_PHOTO_DESC);
        String date = getIntent().getStringExtra(EXTRA_PHOTO_DATE);
        double latitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LAT, 0);
        double longitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LON, 0);

        // Mostrar la imagen
        ImageView fullImageView = findViewById(R.id.fullImageView);
        if (photoUri != null) {
            try {
                fullImageView.setImageURI(Uri.parse(photoUri));
            } catch (Exception e) {
                fullImageView.setImageResource(R.drawable.troleohelmado);
                Toast.makeText(this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
            }
        }

        // Mostrar los detalles
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDateTime = findViewById(R.id.detailDateTime);
        TextView detailLocation = findViewById(R.id.detailLocation);

        detailDescription.setText(description);
        detailDateTime.setText("Fecha: " + date);
        detailLocation.setText(String.format(Locale.getDefault(),
                "Ubicación:\nLatitud: %.6f\nLongitud: %.6f",
                latitude, longitude));
    }

    //pide confirmacion para eliminar foto
    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar foto")
                .setMessage("¿Estás seguro que deseas eliminar esta foto?")
                .setPositiveButton("Eliminar", (dialog, which) -> deletePhoto())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    //proceso de eliminacion
    private void deletePhoto() {
        if (photoId != -1) {
            try {
                //elimina foto de la base de datos
                boolean deleted = dbHelper.deletePhoto(photoId);
                if (deleted) {
                    // Intentar eliminar el archivo físico
                    try {
                        if (photoUri != null) {
                            Uri uri = Uri.parse(photoUri);
                            getContentResolver().delete(uri, null, null);
                        }
                    } catch (Exception e) {
                        Log.e("PhotoDetailActivity", "Error al eliminar archivo físico: " + e.getMessage());
                    }

                    Toast.makeText(this, "Foto eliminada exitosamente", Toast.LENGTH_SHORT).show();

                    // Enviar resultado a la actividad anterior
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Error al eliminar la foto", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //cierra conexion con base de datos
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}