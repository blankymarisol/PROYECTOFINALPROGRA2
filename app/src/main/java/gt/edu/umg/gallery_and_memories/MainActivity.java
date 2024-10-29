package gt.edu.umg.gallery_and_memories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;
import gt.edu.umg.gallery_and_memories.galeria.GaleriaActivity;
import gt.edu.umg.gallery_and_memories.gps.GpsActivity;
import gt.edu.umg.gallery_and_memories.models.PhotoItem;


public class MainActivity extends AppCompatActivity {

    Button bntSaludo,btndoxeo, btnselfi;
    TextView tvSaludo, lastPhotoDescription, lastPhotoDate;
    ImageView lastPhotoImage;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        bntSaludo = findViewById(R.id.btnSaludo);
        tvSaludo = findViewById(R.id.tvSaludo);
        btndoxeo = findViewById(R.id.btndoxeo);
        btnselfi = findViewById(R.id.btnselfi);
        lastPhotoImage = findViewById(R.id.lastPhotoImage);
        lastPhotoDescription = findViewById(R.id.lastPhotoDescription);
        lastPhotoDate = findViewById(R.id.lastPhotoDate);
    }

    private void setupClickListeners() {
        bntSaludo.setOnClickListener(v -> {
            Toast.makeText(this, "¡Saludos!", Toast.LENGTH_SHORT).show();
            tvSaludo.setText("Bienvenido usuario nuevo");
        });

        btndoxeo.setOnClickListener(view -> {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "¡TE DOXEO MI LOCO!", Toast.LENGTH_SHORT).show();
        });

        btnselfi.setOnClickListener(v -> {
            Intent intent = new Intent(this, GaleriaActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLastPhoto();
    }

    private void loadLastPhoto() {
        List<PhotoItem> photos = dbHelper.getAllPhotos();
        if (!photos.isEmpty()) {
            PhotoItem lastPhoto = photos.get(0); // La primera foto es la más reciente

            // Cargar la imagen
            try {
                lastPhotoImage.setImageURI(Uri.parse(lastPhoto.getUri()));
            } catch (Exception e) {
                lastPhotoImage.setImageResource(R.drawable.troleohelmado);
            }

            // Mostrar la descripción
            lastPhotoDescription.setText(lastPhoto.getDescription());

            // Mostrar la fecha
            lastPhotoDate.setText("Tomada el " + lastPhoto.getDate());

            // Hacer visible el CardView
            findViewById(R.id.lastPhotoCard).setVisibility(View.VISIBLE);
        } else {
            // Si no hay fotos, ocultar el CardView
            findViewById(R.id.lastPhotoCard).setVisibility(View.GONE);
        }
    }
}
