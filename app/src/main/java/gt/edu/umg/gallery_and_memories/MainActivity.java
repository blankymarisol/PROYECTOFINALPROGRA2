package gt.edu.umg.gallery_and_memories;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import gt.edu.umg.gallery_and_memories.galeria.GaleriaActivity;
import gt.edu.umg.gallery_and_memories.gps.GpsActivity;


public class MainActivity extends AppCompatActivity {

    Button bntSaludo, btnCrearDb, btndoxeo, btnselfi;
    TextView tvSaludo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bntSaludo = findViewById(R.id.btnSaludo);
        tvSaludo = findViewById(R.id.tvSaludo);
        btndoxeo = findViewById(R.id.btndoxeo);
        btnselfi = findViewById(R.id.btnselfi); // Asegúrate de inicializar btnselfi

        bntSaludo.setOnClickListener(v -> {
            Toast.makeText(this, "¡Saludos!", Toast.LENGTH_SHORT).show();
            tvSaludo.setText("Bienvenido usuario nuevo");
        });


        btndoxeo.setOnClickListener(view -> {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "¡TE DOXEO MI LOCO!", Toast.LENGTH_SHORT).show();
        });

        // Configurar btnselfi para abrir CameraActivity
        btnselfi.setOnClickListener(v -> {
            Intent intent = new Intent(this, GaleriaActivity.class);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
