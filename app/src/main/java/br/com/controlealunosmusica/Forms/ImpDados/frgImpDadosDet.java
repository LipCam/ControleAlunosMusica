package br.com.controlealunosmusica.Forms.ImpDados;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import br.com.controlealunosmusica.Model.TempAlunos;
import br.com.controlealunosmusica.R;

public class frgImpDadosDet extends Fragment {

    public frgImpDadosDet() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frg_imp_dados_det, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView txvNome = getView().findViewById(R.id.txvNome);
        TextView txvStatus = getView().findViewById(R.id.txvStatus);
        TextView txvInstrumento = getView().findViewById(R.id.txvInstrumento);
        TextView txvMetodo = getView().findViewById(R.id.txvMetodo);
        TextView txvFone = getView().findViewById(R.id.txvFone);
        TextView txvDataNasc = getView().findViewById(R.id.txvDataNasc);
        TextView txvDataBatismo = getView().findViewById(R.id.txvDataBatismo);
        TextView txvDataInicio = getView().findViewById(R.id.txvDataInicio);
        TextView txvDataOficializacao = getView().findViewById(R.id.txvDataOficializacao);
        TextView txvEndereco = getView().findViewById(R.id.txvEndereco);
        TextView txtObsevacao = getView().findViewById(R.id.txtObsevacao);

        TempAlunos objTempAlunos = new TempAlunos(getActivity());
        objTempAlunos.ID_TEMP_INT = getActivity().getIntent().getIntExtra("ID_TEMP_INT", 0);
        objTempAlunos = objTempAlunos.Find();
        txvNome.setText(objTempAlunos.NOME_STR);
        txvStatus.setText(objTempAlunos.STATUS_STR);
        txvInstrumento.setText(objTempAlunos.INSTRUMENTO_STR);
        txvMetodo.setText(objTempAlunos.METODO_STR);
        txvFone.setText(objTempAlunos.FONE_STR);

        txvDataNasc.setText("__/__/____");
        if (objTempAlunos.DATA_NASCIMENTO_DTI != null)
            txvDataNasc.setText(new SimpleDateFormat("dd/MM/yyyy").format(objTempAlunos.DATA_NASCIMENTO_DTI));

        txvDataBatismo.setText("__/__/____");
        if (objTempAlunos.DATA_BATISMO_DTI != null)
            txvDataBatismo.setText(new SimpleDateFormat("dd/MM/yyyy").format(objTempAlunos.DATA_BATISMO_DTI));

        txvDataInicio.setText("__/__/____");
        if (objTempAlunos.DATA_INICIO_GEM_DTI != null)
            txvDataInicio.setText(new SimpleDateFormat("dd/MM/yyyy").format(objTempAlunos.DATA_INICIO_GEM_DTI));

        txvDataOficializacao.setText("__/__/____");
        if (objTempAlunos.DATA_OFICIALIZACAO_DTI!= null)
            txvDataOficializacao.setText(new SimpleDateFormat("dd/MM/yyyy").format(objTempAlunos.DATA_OFICIALIZACAO_DTI));

        txvEndereco.setText(objTempAlunos.ENDERECO_STR);
        txtObsevacao.setText(objTempAlunos.OBSERVACAO_STR);

        super.onViewCreated(view, savedInstanceState);
    }
}
