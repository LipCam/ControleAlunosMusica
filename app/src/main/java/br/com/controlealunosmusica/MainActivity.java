package br.com.controlealunosmusica;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import br.com.controlealunosmusica.Forms.Alunos.atvAlunosLista;
import br.com.controlealunosmusica.Forms.Aulas.atvAulasLista;
import br.com.controlealunosmusica.Forms.ExpDados.atvExpDados;
import br.com.controlealunosmusica.Forms.ImpDados.atvImpDadosSelArq;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.atv_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, 1000);
            }
        }

        CardView btncvAulas = findViewById(R.id.btncvAulas);
        btncvAulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, atvAulasLista.class));
            }
        });

        CardView btncvAlunos = findViewById(R.id.btncvAlunos);
        btncvAlunos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, atvAlunosLista.class));
            }
        });

        CardView btncvExportDados = findViewById(R.id.btncvExportDados);
        btncvExportDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, atvExpDados.class));
            }
        });

        CardView btncvImportDados = findViewById(R.id.btncvImportDados);
        btncvImportDados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, atvImpDadosSelArq.class));
            }
        });
    }
}