package gt.edu.umg.gallery_and_memories.galeria;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import gt.edu.umg.gallery_and_memories.R;

public class PhotoDetailActivity extends AppCompatActivity {
    public static final String EXTRA_PHOTO_URI = "photo_uri";
    public static final String EXTRA_PHOTO_DESC = "photo_description";
    public static final String EXTRA_PHOTO_DATE = "photo_date";
    public static final String EXTRA_PHOTO_LAT = "photo_latitude";
    public static final String EXTRA_PHOTO_LON = "photo_longitude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_detail);

        // Inicializar vistas
        ImageView fullImageView = findViewById(R.id.fullImageView);
        TextView detailDescription = findViewById(R.id.detailDescription);
        TextView detailDateTime = findViewById(R.id.detailDateTime);
        TextView detailLocation = findViewById(R.id.detailLocation);
        Button btnBack = findViewById(R.id.btnBack);

        // Obtener datos del intent
        String photoUri = getIntent().getStringExtra(EXTRA_PHOTO_URI);
        String description = getIntent().getStringExtra(EXTRA_PHOTO_DESC);
        String date = getIntent().getStringExtra(EXTRA_PHOTO_DATE);
        double latitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LAT, 0);
        double longitude = getIntent().getDoubleExtra(EXTRA_PHOTO_LON, 0);

        // Mostrar la imagen
        if (photoUri != null) {
            fullImageView.setImageURI(Uri.parse(photoUri));
        }

        // Mostrar los detalles
        detailDescription.setText(description);
        detailDateTime.setText("Fecha: " + date);
        detailLocation.setText(String.format("Ubicación:\nLatitud: %.6f\nLongitud: %.6f",
                latitude, longitude));

        // Configurar botón de regreso
        btnBack.setOnClickListener(v -> finish());
    }
}