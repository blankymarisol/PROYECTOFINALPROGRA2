package gt.edu.umg.gallery_and_memories;


import android.content.Intent;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;
import gt.edu.umg.gallery_and_memories.galeria.GaleriaActivity;
import gt.edu.umg.gallery_and_memories.gps.GpsActivity;
import gt.edu.umg.gallery_and_memories.models.PhotoItem;

public class MainActivity extends AppCompatActivity {
    Button bntSaludo, btndoxeo, btnselfi;
    TextView tvSaludo, lastPhotoDescription, lastPhotoDate;
    ImageView lastPhotoImage;
    DatabaseHelper dbHelper;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupClickListeners();
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar la última foto al iniciar
        updateLastPhotoInfo();
    }

    private void initializeViews() {
        bntSaludo = findViewById(R.id.btnSaludo);
        tvSaludo = findViewById(R.id.tvSaludo);
        btndoxeo = findViewById(R.id.btndoxeo);
        btnselfi = findViewById(R.id.btnselfi);
        lastPhotoImage = findViewById(R.id.lastPhotoImage);
        lastPhotoDescription = findViewById(R.id.lastPhotoDescription);
        lastPhotoDate = findViewById(R.id.lastPhotoDate);

        bntSaludo.setOnClickListener(v -> {
            Toast.makeText(this, "Gallery and Memories", Toast.LENGTH_SHORT).show();
            tvSaludo.setText("Bienvenido a tu galería de viajes");
        });
    }

    private void setupClickListeners() {
        btndoxeo.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("MainActivity", "Error al iniciar GpsActivity: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnselfi.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, GaleriaActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e("MainActivity", "Error al iniciar GaleriaActivity: " + e.getMessage());
                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para actualizar la información de la última foto
    private void updateLastPhotoInfo() {
        try {
            List<PhotoItem> photos = dbHelper.getAllPhotos();
            if (!photos.isEmpty()) {
                PhotoItem lastPhoto = photos.get(0); // La primera foto es la más reciente

                // Actualizar la imagen
                try {
                    Uri photoUri = Uri.parse(lastPhoto.getUri());
                    lastPhotoImage.setImageURI(photoUri);
                } catch (Exception e) {
                    lastPhotoImage.setImageResource(R.drawable.troleohelmado); // Imagen por defecto
                }

                // Actualizar descripción y fecha
                if (lastPhotoDescription != null) {
                    lastPhotoDescription.setText(lastPhoto.getDescription());
                }
                if (lastPhotoDate != null) {
                    lastPhotoDate.setText(lastPhoto.getDate());
                }
            } else {
                // No hay fotos en la base de datos
                lastPhotoImage.setImageResource(R.drawable.troleohelmado); // Imagen por defecto
                if (lastPhotoDescription != null) {
                    lastPhotoDescription.setText("No hay fotos");
                }
                if (lastPhotoDate != null) {
                    lastPhotoDate.setText("");
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error al actualizar la última foto: " + e.getMessage());
            Toast.makeText(this, "Error al cargar la última foto", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la última foto cuando se regrese a MainActivity
        updateLastPhotoInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}