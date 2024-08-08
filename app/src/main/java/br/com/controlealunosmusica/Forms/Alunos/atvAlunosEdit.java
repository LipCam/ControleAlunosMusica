package br.com.controlealunosmusica.Forms.Alunos;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import br.com.controlealunosmusica.Adapters.spnStatusAlunosAdapter;
import br.com.controlealunosmusica.Classes.RelAlunos;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.Model.StatusAlunos;
import br.com.controlealunosmusica.R;

public class atvAlunosEdit extends AppCompatActivity {

    private int DiaNasc, MesNasc, AnoNasc;
    private int DiaBat, MesBat, AnoBat;
    private int DiaIni, MesIni, AnoIni;
    private int DiaOfic, MesOfic, AnoOfic;
    
    EditText txtNome;
    Spinner spnStatus;
    EditText txtInstrumento;
    EditText txtMetodo;
    EditText txtFone;
    ImageView imgFone;
    ImageView imgWhatsApp;
    TextView txvDataNasc;
    TextView txvLimpaDtNasc;
    TextView txvDataBatismo;
    TextView txvLimpaDtBat;
    TextView txvDataInicio;
    TextView txvLimpaDtIni;
    TextView txvDataOficializacao;
    TextView txvLimpaDtOfic;
    EditText txtEndereco;
    EditText txtObsevacao;
    TextView txvImportEm;
    TextView txvDataImport;

    MenuItem mnAulas;
    MenuItem mnRelatorio;
    MenuItem mnExcluir;

    Alunos objAlunos;

    List<StatusAlunos> lstStatusAlunos;

    private DecimalFormat formater00 = new DecimalFormat("00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atv_alunos_edit);

        //declarando a toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.tbrToolBar);
        setSupportActionBar(toolbar);

        //colocando o botão de voltar na toolbar()
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNome = findViewById(R.id.txtNome);
        spnStatus = findViewById(R.id.spnStatus);
        txtInstrumento = findViewById(R.id.txtInstrumento);
        txtMetodo = findViewById(R.id.txtMetodo);
        txtFone = findViewById(R.id.txtFone);
        imgFone  = findViewById(R.id.imgFone);
        imgWhatsApp = findViewById(R.id.imgWhatsApp);
        txvDataNasc = findViewById(R.id.txvDataNasc);
        txvLimpaDtNasc = findViewById(R.id.txvLimpaDtNasc);
        txvDataBatismo = findViewById(R.id.txvDataBatismo);
        txvLimpaDtBat = findViewById(R.id.txvLimpaDtBat);
        txvDataInicio = findViewById(R.id.txvDataInicio);
        txvLimpaDtIni = findViewById(R.id.txvLimpaDtIni);
        txvDataOficializacao = findViewById(R.id.txvDataOficializacao);
        txvLimpaDtOfic = findViewById(R.id.txvLimpaDtOfic);
        txtEndereco = findViewById(R.id.txtEndereco);
        txtObsevacao = findViewById(R.id.txtObsevacao);
        txvImportEm = findViewById(R.id.txvImportEm);
        txvDataImport = findViewById(R.id.txvDataImport);

        txvImportEm.setVisibility(View.GONE);
        txvDataImport.setVisibility(View.GONE);

        CarregaSpinners();

        imgFone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtFone.getText().toString().equals("")) {
                    Uri u = Uri.parse("tel:" + txtFone.getText().toString());
                    Intent i = new Intent(Intent.ACTION_DIAL, u);

                    try {
                        startActivity(i);
                    } catch (SecurityException s) {
                        Toast.makeText(getApplicationContext(), s.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        imgWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txtFone.getText().toString().equals("")) {
                    //String formattedNumber = Util.formatPhone(phone);
                    try {
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                        sendIntent.putExtra("jid", "55" + txtFone.getText().toString() + "@s.whatsapp.net");
                        sendIntent.setPackage("com.whatsapp");
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(sendIntent);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Error/n" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Calendar calHoje = Calendar.getInstance();
        DiaNasc = calHoje.get(Calendar.DAY_OF_MONTH);
        MesNasc = calHoje.get(Calendar.MONTH);
        AnoNasc = calHoje.get(Calendar.YEAR);

        txvDataNasc.setText("__/__/____");
        txvDataNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txvDataNasc.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        AnoNasc = year;
                        MesNasc = month;
                        DiaNasc = dayOfMonth;
                    }
                }, AnoNasc, MesNasc, DiaNasc);

                dtp.show();
            }
        });

        txvLimpaDtNasc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvDataNasc.setText("__/__/____");
            }
        });

        DiaBat = calHoje.get(Calendar.DAY_OF_MONTH);
        MesBat = calHoje.get(Calendar.MONTH);
        AnoBat = calHoje.get(Calendar.YEAR);
        txvDataBatismo.setText("__/__/____");
        txvDataBatismo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txvDataBatismo.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        AnoBat = year;
                        MesBat = month;
                        DiaBat = dayOfMonth;
                    }
                }, AnoBat, MesBat, DiaBat);

                dtp.show();
            }
        });

        txvLimpaDtBat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvDataBatismo.setText("__/__/____");
            }
        });

        DiaIni = calHoje.get(Calendar.DAY_OF_MONTH);
        MesIni = calHoje.get(Calendar.MONTH);
        AnoIni = calHoje.get(Calendar.YEAR);
        txvDataInicio.setText("__/__/____");
        txvDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txvDataInicio.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        AnoIni = year;
                        MesIni = month;
                        DiaIni = dayOfMonth;
                    }
                }, AnoIni, MesIni, DiaIni);

                dtp.show();
            }
        });

        txvLimpaDtIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvDataInicio.setText("__/__/____");
            }
        });

        DiaOfic = calHoje.get(Calendar.DAY_OF_MONTH);
        MesOfic = calHoje.get(Calendar.MONTH);
        AnoOfic = calHoje.get(Calendar.YEAR);
        txvDataOficializacao.setText("__/__/____");
        txvDataOficializacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dtp = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txvDataOficializacao.setText(formater00.format(dayOfMonth) + "/" + formater00.format(month + 1) + "/" + year);
                        AnoOfic = year;
                        MesOfic = month;
                        DiaOfic = dayOfMonth;
                    }
                }, AnoOfic, MesOfic, DiaOfic);

                dtp.show();
            }
        });

        txvLimpaDtOfic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txvDataOficializacao.setText("__/__/____");
            }
        });

        objAlunos = new Alunos(this);
        Bundle bundle = getIntent().getExtras();
        if ((bundle != null) && (bundle.containsKey("ID_ALUNO_INT"))) {
            objAlunos.ID_ALUNO_INT = bundle.getInt("ID_ALUNO_INT");
            objAlunos = objAlunos.Find();
            Carrega();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_alunos, menu);

        mnAulas = menu.findItem(R.id.mnAulas);
        mnRelatorio = menu.findItem(R.id.mnRelatorio);
        mnExcluir = menu.findItem(R.id.mnExcluir);

        if (objAlunos.ID_ALUNO_INT == 0){
            mnAulas.setVisible(false);
            mnRelatorio.setVisible(false);
            mnExcluir.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mnSalvar) {
            OnSalvar();
            return true;
        } else if (id == R.id.mnAulas) {
            Intent it = new Intent(atvAlunosEdit.this, atvAlunosAulasLista.class);
            it.putExtra("ID_ALUNO_INT", objAlunos.ID_ALUNO_INT);
            it.putExtra("ALUNO_STR", objAlunos.NOME_STR);
            startActivity(it);
            return true;
        } else if (id == R.id.mnRelatorio) {;
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, 1000);
                }
                else {
                    GerarRelatorio();
                }
            }
            else {
                GerarRelatorio();
            }
            return true;
        } else if (id == R.id.mnExcluir) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(this.getString(R.string.msgConfirmDelete));
            builder.setPositiveButton(this.getString(R.string.lblSim), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    OnExcluir();
                    finish();
                }
            });

            builder.setNegativeButton(this.getString(R.string.lblNao), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GerarRelatorio() {
        new RelAlunos().GerarRelatorio(this, objAlunos.ID_ALUNO_INT);
    }

    private void CarregaSpinners() {
        lstStatusAlunos = new StatusAlunos(this).Get();
        spnStatus.setAdapter(new spnStatusAlunosAdapter(this, lstStatusAlunos));
    }

    private void Carrega() {
        txtNome.setText(String.valueOf(objAlunos.NOME_STR));

        for (int i = 0; i < lstStatusAlunos.size(); i++) {
            if (lstStatusAlunos.get(i).ID_STATUS_INT == objAlunos.ID_STATUS_INT) {
                spnStatus.setSelection(i);
                break;
            }
        }

        txtInstrumento.setText(String.valueOf(objAlunos.INSTRUMENTO_STR));
        txtMetodo.setText(String.valueOf(objAlunos.METODO_STR));
        txtFone.setText(String.valueOf(objAlunos.FONE_STR));

        if (objAlunos.DATA_NASCIMENTO_DTI != null)
            txvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAlunos.DATA_NASCIMENTO_DTI));

        if (objAlunos.DATA_BATISMO_DTI != null)
            txvDataBatismo.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAlunos.DATA_BATISMO_DTI));

        if (objAlunos.DATA_INICIO_GEM_DTI != null)
            txvDataInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAlunos.DATA_INICIO_GEM_DTI));

        if (objAlunos.DATA_OFICIALIZACAO_DTI != null)
            txvDataOficializacao.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAlunos.DATA_OFICIALIZACAO_DTI));

        txtEndereco.setText(String.valueOf(objAlunos.ENDERECO_STR));
        txtObsevacao.setText(String.valueOf(objAlunos.OBSERVACAO_STR));

        if (objAlunos.DATA_IMPORTACAO_DTI != null) {
            txvImportEm.setVisibility(View.VISIBLE);
            txvDataImport.setVisibility(View.VISIBLE);
            txvDataImport.setText(new SimpleDateFormat("dd/MM/yyyy").format(objAlunos.DATA_IMPORTACAO_DTI));
        }
    }

    private void OnSalvar() {
        if (txtNome.getText().toString().equals("")) {
            ShowMessage("Campo Nome deve ser preenchido.");
            txtNome.requestFocus();
            return;
        }

        if (txtInstrumento.getText().toString().equals("")) {
            ShowMessage("Informe um instrumento.");
            txtInstrumento.requestFocus();
            return;
        }

        objAlunos.NOME_STR = txtNome.getText().toString();
        objAlunos.ID_STATUS_INT = ((StatusAlunos) spnStatus.getSelectedItem()).ID_STATUS_INT;
        objAlunos.INSTRUMENTO_STR = txtInstrumento.getText().toString();
        objAlunos.METODO_STR = txtMetodo.getText().toString();
        objAlunos.FONE_STR = txtFone.getText().toString();

        try {
            objAlunos.DATA_NASCIMENTO_DTI = new SimpleDateFormat("dd/MM/yyyy").parse(txvDataNasc.getText().toString());
        } catch (Exception ex)
        {
            objAlunos.DATA_NASCIMENTO_DTI = null;
            String a = ex.getMessage();
        }

        try {
            objAlunos.DATA_BATISMO_DTI = new SimpleDateFormat("dd/MM/yyyy").parse(txvDataBatismo.getText().toString());
        } catch (Exception ex)
        {
            objAlunos.DATA_BATISMO_DTI = null;
            String a = ex.getMessage();
        }

        try {
            objAlunos.DATA_INICIO_GEM_DTI = new SimpleDateFormat("dd/MM/yyyy").parse(txvDataInicio.getText().toString());
        } catch (Exception ex)
        {
            objAlunos.DATA_INICIO_GEM_DTI = null;
            String a = ex.getMessage();
        }

        try {
            objAlunos.DATA_OFICIALIZACAO_DTI = new SimpleDateFormat("dd/MM/yyyy").parse(txvDataOficializacao.getText().toString());
        } catch (Exception ex)
        {
            objAlunos.DATA_OFICIALIZACAO_DTI = null;
            String a = ex.getMessage();
        }

        objAlunos.ENDERECO_STR = txtEndereco.getText().toString();
        objAlunos.OBSERVACAO_STR = txtObsevacao.getText().toString();

        String Message = "";
        if (objAlunos.ID_ALUNO_INT == 0) {
            Message = objAlunos.Add();
            mnAulas.setVisible(true);
            mnRelatorio.setVisible(true);
            mnExcluir.setVisible(true);
        }
        else
            Message = objAlunos.Update();

        Toast.makeText(this, Message, Toast.LENGTH_SHORT).show();
        //finish();
    }

    private void OnExcluir() {
        Toast.makeText(this, objAlunos.Delete(), Toast.LENGTH_SHORT).show();
    }

    private void ShowMessage(String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Titulo");
        builder.setMessage(Message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }
}
