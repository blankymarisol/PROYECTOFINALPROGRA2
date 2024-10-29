package gt.edu.umg.gallery_and_memories;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import gt.edu.umg.gallery_and_memories.database.DatabaseHelper;
import gt.edu.umg.gallery_and_memories.galeria.GaleriaActivity;
import gt.edu.umg.gallery_and_memories.gps.GpsActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button bntSaludo, btndoxeo, btnselfi;
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
        btndoxeo.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, GpsActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error al iniciar GpsActivity: " + e.getMessage(), e);
                Toast.makeText(this, "Error al abrir el mapa: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        btnselfi.setOnClickListener(view -> {
            try {
                Intent intent = new Intent(MainActivity.this, GaleriaActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error al iniciar GaleriaActivity: " + e.getMessage(), e);
                Toast.makeText(this, "Error al abrir la galería: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}