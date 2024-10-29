package gt.edu.umg.gallery_and_memories.galeria;


import android.app.AlertDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import gt.edu.umg.gallery_and_memories.R;
import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PHOTO_URI = "photo_uri";
    public static final String EXTRA_PHOTO_DESC = "photo_description";
    public static final String EXTRA_PHOTO_DATE = "photo_date";
    public static final String EXTRA_PHOTO_LAT = "photo_latitude";
    public static final String EXTRA_PHOTO_LON = "photo_longitude";
    public static final String EXTRA_PHOTO_ID = "photo_id";

    private DatabaseHelper dbHelper;
    private long photoId;
    private String photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        dbHelper = new DatabaseHelper(this);

        initializeViews();
        loadPhotoData();
        setupButtons();
    }

    private void initializeViews() {
        ImageView fullImageView = findViewById(R.id.fullImageView);
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDateTime = findViewById(R.id.detailDateTime);
        TextView detailLocation = findViewById(R.id.detailLocation);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Configurar botones
        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void loadPhotoData() {
        // Obtener datos del intent
        photoId = getIntent().getLongExtra(EXTRA_PHOTO_ID, -1);
        photoUri = getIntent().getStringExtra(EXTRA_PHOTO_URI);
        String description = getIntent().getStringExtra(EXTRA_PHOTO_DESC);
        String date = getIntent().getStringExtra(EXTRA_PHOTO_DATE);
        double latitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LAT, 0);
        double longitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LON, 0);

        // Mostrar la imagen
        if (photoUri != null) {
            ImageView fullImageView = findViewById(R.id.fullImageView);
            fullImageView.setImageURI(Uri.parse(photoUri));
        }

        // Mostrar los detalles
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDateTime = findViewById(R.id.detailDateTime);
        TextView detailLocation = findViewById(R.id.detailLocation);

        detailDescription.setText(description);
        detailDateTime.setText("Fecha: " + date);
        detailLocation.setText(String.format("Ubicación:\nLatitud: %.6f\nLongitud: %.6f",
                latitude, longitude));
    }

    private void setupButtons() {
        Button btnBack = findViewById(R.id.btnBack);
        Button btnDelete = findViewById(R.id.btnDelete);

        btnBack.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar foto")
                .setMessage("¿Estás seguro que deseas eliminar esta foto?")
                .setPositiveButton("Eliminar", (dialog, which) -> deletePhoto())
                .setNegativeButton("Cancelar", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private void deletePhoto() {
        if (photoId != -1) {
            boolean deleted = dbHelper.deletePhoto(photoId);
            if (deleted) {
                Toast.makeText(this, "Foto eliminada exitosamente", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al eliminar la foto", Toast.LENGTH_SHORT).show();
            }
        }
    }

}