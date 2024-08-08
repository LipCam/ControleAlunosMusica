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

import br.com.controlealunosmusica.Forms.Aulas.atvAulasEdit;
import br.com.controlealunosmusica.Model.Aulas;
import br.com.controlealunosmusica.R;


public class rsvAulasAdapter extends RecyclerView.Adapter<rsvAulasAdapter.ViewHolderTestes> {

    private Context context;
    private List<Aulas> lstAulas;
    private int layout;

    public rsvAulasAdapter(Context context, List<Aulas> lstAulas, int layout) {
        this.context = context;
        this.lstAulas = lstAulas;
        this.layout = layout;
    }

    //criando o layout das linhaa conforme template criado
    @Override
    public ViewHolderTestes onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rsv_aulas, viewGroup, false);
        if (layout == 1)
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rsv_aulas_aluno, viewGroup, false);

        ViewHolderTestes holder = new ViewHolderTestes(view);
        return holder;
    }

    //montando cada linha bindando os campos
    @Override
    public void onBindViewHolder(ViewHolderTestes holder, final int i) {
        if (lstAulas != null && lstAulas.size() > 0) {
            Aulas aulas = lstAulas.get(i);
            holder.txtrsvAluno.setText(aulas.ALUNO_STR);
            holder.txtrsvData.setText(new SimpleDateFormat("dd/MM/yyyy").format(aulas.DATA_DTI));
            holder.txtrsvTipo.setText(aulas.TIPO_STR);
            holder.txtrsvConcluido.setText(aulas.STATUS_STR);
            holder.txtrsvAssunto.setText(aulas.ASSUNTO_STR);

            holder.imgImp.setVisibility(View.INVISIBLE);
            if(aulas.DATA_IMPORTACAO_DTI != null)
                holder.imgImp.setVisibility(View.VISIBLE);

            holder.rsvLinhaAulas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Intent it = new Intent(v.getContext(), atvAulasEdit.class);
                    it.putExtra("ID_AULA_INT", lstAulas.get(i).ID_AULA_INT);
                    it.putExtra("ID_ALUNO_INT", lstAulas.get(i).ID_ALUNO_INT);
                    ((AppCompatActivity) context).startActivityForResult(it, 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return lstAulas.size();
    }

    public class ViewHolderTestes extends RecyclerView.ViewHolder {
        ConstraintLayout rsvLinhaAulas;        
        TextView txtrsvAluno;
        TextView txtrsvData;
        TextView txtrsvTipo;
        TextView txtrsvConcluido;
        TextView txtrsvAssunto;
        ImageView imgImp;

        public ViewHolderTestes(View itemView) {
            super(itemView);

            rsvLinhaAulas = itemView.findViewById(R.id.rsvLinhaAulas);
            txtrsvAluno = itemView.findViewById(R.id.txtrsvAluno);
            txtrsvData = itemView.findViewById(R.id.txtrsvData);
            txtrsvTipo = itemView.findViewById(R.id.txtrsvTipo);
            txtrsvConcluido = itemView.findViewById(R.id.txtrsvConcluido);
            txtrsvAssunto = itemView.findViewById(R.id.txtrsvAssunto);
            imgImp = itemView.findViewById(R.id.imgImp);
        }
    }
}
