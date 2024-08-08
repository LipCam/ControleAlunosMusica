package br.com.controlealunosmusica.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.controlealunosmusica.Forms.Alunos.atvAlunosEdit;
import br.com.controlealunosmusica.Model.Alunos;
import br.com.controlealunosmusica.R;


public class rsvAlunosAdapter extends RecyclerView.Adapter<rsvAlunosAdapter.ViewHolderTestes> {

    private Context context;
    private List<Alunos> lstAlunos;

    public rsvAlunosAdapter(Context context, List<Alunos> lstAlunos) {
        this.context = context;
        this.lstAlunos = lstAlunos;
    }

    //criando o layout das linhaa conforme template criado
    @Override
    public ViewHolderTestes onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rsv_alunos, viewGroup  , false);
        ViewHolderTestes holder = new  ViewHolderTestes(view);
        return holder;
    }

    //montando cada linha bindando os campos
    @Override
    public void onBindViewHolder(ViewHolderTestes holder, final int i) {
        if (lstAlunos != null && lstAlunos.size() > 0) {
            Alunos alunos = lstAlunos.get(i);
            holder.txtrsvNome.setText(alunos.NOME_STR);

            holder.txtrsvDataIni.setText("");
            if (alunos.DATA_INICIO_GEM_DTI != null)
                holder.txtrsvDataIni.setText(new SimpleDateFormat("dd/MM/yyyy").format(alunos.DATA_INICIO_GEM_DTI));

            holder.txtrsvStatus.setText(alunos.STATUS_STR);

            holder.txtrsvDataOfic.setText("");
            if (alunos.DATA_OFICIALIZACAO_DTI != null && alunos.ID_STATUS_INT == 5)
                holder.txtrsvDataOfic.setText(new SimpleDateFormat("dd/MM/yyyy").format(alunos.DATA_OFICIALIZACAO_DTI));

            holder.txtrsvInstrumento.setText(alunos.INSTRUMENTO_STR);

            holder.imgImp.setVisibility(View.INVISIBLE);
            if(alunos.DATA_IMPORTACAO_DTI != null)
                holder.imgImp.setVisibility(View.VISIBLE);

            holder.rsvLinhaAlunos.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent it = new Intent(v.getContext(), atvAlunosEdit.class);
                    it.putExtra("ID_ALUNO_INT", lstAlunos.get(i).ID_ALUNO_INT);
                    ((AppCompatActivity) context).startActivityForResult(it, 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstAlunos.size();
    }

    public class ViewHolderTestes extends RecyclerView.ViewHolder {
        ConstraintLayout rsvLinhaAlunos;
        TextView txtrsvNome;
        TextView txtrsvDataIni;
        TextView txtrsvStatus;
        TextView txtrsvDataOfic;
        TextView txtrsvInstrumento;
        ImageView imgImp;

        public ViewHolderTestes(View itemView) {
            super(itemView);

            rsvLinhaAlunos = itemView.findViewById(R.id.rsvLinhaAlunos);
            txtrsvNome = itemView.findViewById(R.id.txtrsvNome);
            txtrsvDataIni = itemView.findViewById(R.id.txtrsvDataIni);
            txtrsvStatus = itemView.findViewById(R.id.txtrsvStatus);
            txtrsvDataOfic = itemView.findViewById(R.id.txtrsvDataOfic);
            txtrsvInstrumento = itemView.findViewById(R.id.txtrsvInstrumento);
            imgImp = itemView.findViewById(R.id.imgImp);
        }
    }
}
