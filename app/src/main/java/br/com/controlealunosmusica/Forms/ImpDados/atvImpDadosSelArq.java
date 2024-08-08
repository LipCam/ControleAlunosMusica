package br.com.controlealunosmusica.Forms.ImpDados;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import br.com.controlealunosmusica.Classes.JsonAluno;
import br.com.controlealunosmusica.R;

public class atvImpDadosSelArq extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_imp_dados_sel_arq);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CardView btncvSelArq = findViewById(R.id.btncvSelArq);
        btncvSelArq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                fileIntent.setType("*/*");
                fileIntent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(fileIntent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Uri uri = data.getData();
            if (uri.getPath().substring(uri.getPath().lastIndexOf(".")).equals(".cal")) {
                String path = uri.getPath();
                path = path.substring(path.indexOf(":") + 1);
                String Erro = new JsonAluno().LerJson(this, path);

                if (Erro.equals(""))
                    startActivity(new Intent(atvImpDadosSelArq.this, atvImpDadosLista.class));
                else
                    Toast.makeText(this, Erro, Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(this, this.getString(R.string.msgArquivoInvalido), Toast.LENGTH_LONG).show();
        }
    }
}
