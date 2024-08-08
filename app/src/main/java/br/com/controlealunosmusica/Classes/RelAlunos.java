package br.com.controlealunosmusica.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.controlealunosmusica.DataBase.DadosOpenHelper;
import br.com.controlealunosmusica.R;

public class RelAlunos {
    public void GerarRelatorio(Context context, int IdAluno){
        SQLiteDatabase db = new DadosOpenHelper(context).getWritableDatabase();

        String[] param = new String[1];
        param[0] = String.valueOf(IdAluno);

        String SQL = "SELECT AL.ID_ALUNO_INT, AL.NOME_STR, ST.DESCRICAO_STR AS STATUS_STR, AL.INSTRUMENTO_STR, AL.METODO_STR,\n" +
                "AL.FONE_STR, IFNULL(strftime('%d/%m/%Y', AL.DATA_NASCIMENTO_DTI), '') AS DATA_NASCIMENTO_DTI, \n" +
                "IFNULL(strftime('%d/%m/%Y', AL.DATA_BATISMO_DTI), '') AS DATA_BATISMO_DTI, \n" +
                "IFNULL(strftime('%d/%m/%Y', AL.DATA_INICIO_GEM_DTI), '') AS DATA_INICIO_GEM_DTI, IFNULL(strftime('%d/%m/%Y', AL.DATA_OFICIALIZACAO_DTI), '') AS DATA_OFICIALIZACAO_DTI,\n" +
                "AL.ENDERECO_STR\n" +
                "FROM CAD_ALUNOS_TAB AL\n" +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB ST ON ST.ID_STATUS_INT = AL.ID_STATUS_INT\n" +
                "WHERE AL.ID_ALUNO_INT = ?";

        Cursor curAluno = db.rawQuery(SQL, param);
        curAluno.moveToFirst();

        SQL = "SELECT AL.ID_ALUNO_INT, AL.NOME_STR, \n" +
                "strftime('%d/%m/%Y', A.DATA_DTI) AS DATA_AULA_DTI, TP.DESCRICAO_STR AS TIPO_STR,\n" +
                "CASE WHEN A.CONCLUIDO_BIT = 0 THEN 'Pendente' ELSE 'Concluído' END AS CONCLUIDO_STR,\n" +
                "IFNULL(A.INSTRUTOR_STR, '') AS INSTRUTOR_STR, IFNULL(A.ASSUNTO_STR, '') AS ASSUNTO_STR\n" +
                "FROM CAD_AULAS_TAB A\n" +
                "INNER JOIN CAD_ALUNOS_TAB AL ON AL.ID_ALUNO_INT = A.ID_ALUNO_INT\n" +
                "INNER JOIN SIS_STATUS_ALUNOS_TAB ST ON ST.ID_STATUS_INT = AL.ID_STATUS_INT\n" +
                "INNER JOIN SIS_TIPOS_AULA_TAB TP ON TP.ID_TIPO_INT = A.ID_TIPO_INT\n" +
                "WHERE AL.ID_ALUNO_INT = ?\n" +
                "ORDER BY A.ID_TIPO_INT, A.DATA_DTI";

        Cursor curAulas = db.rawQuery(SQL, param);
        curAulas.moveToFirst();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "//";
        String NomeArq = "Relat " + curAluno.getString(curAluno.getColumnIndexOrThrow("NOME_STR")) + " " + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) + ".pdf";
        Document doc = new Document();

        try {
            PdfWriter.getInstance(doc, new FileOutputStream(path + NomeArq));

            Font fNome = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
            Font fTituloP = new Font(Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD);
            Font fTituloM = new Font(Font.FontFamily.TIMES_ROMAN, 15, Font.BOLD);
            Font fTituloG = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
            Font fLabel = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);

            doc.open();
            doc.add(new Paragraph(curAluno.getString(curAluno.getColumnIndexOrThrow("NOME_STR")), fNome));
            doc.add(new Paragraph("Fone: " + curAluno.getString(curAluno.getColumnIndexOrThrow("FONE_STR"))));
            doc.add(new Paragraph("Instrumento: " + curAluno.getString(curAluno.getColumnIndexOrThrow("INSTRUMENTO_STR"))
                    + "       Método: " + curAluno.getString(curAluno.getColumnIndexOrThrow("METODO_STR"))));
            doc.add(new Paragraph("Status: " + curAluno.getString(curAluno.getColumnIndexOrThrow("STATUS_STR"))));
            doc.add(new Paragraph("Início: " + curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_INICIO_GEM_DTI"))
                    + "       Oficialização: " + curAluno.getString(curAluno.getColumnIndexOrThrow("DATA_OFICIALIZACAO_DTI"))));

            doc.add(new Paragraph("  "));


            Paragraph prAula = new Paragraph("Aulas", fTituloG);
            prAula.setAlignment(Paragraph.ALIGN_CENTER);
            doc.add(prAula);

            doc.add(new Paragraph("  "));

            PdfPTable table = new PdfPTable(new float[]{1.3f, 5, 2, 1.3f});
            if (curAulas.getCount() > 0) {
                String Tipo = "";

                table.setTotalWidth(PageSize.A4.getWidth());
                table.setWidthPercentage(100);
                table.addCell(new Paragraph("Data",fTituloP));
                table.addCell(new Paragraph("Assunto",fTituloP));
                table.addCell(new Paragraph("Instrutor",fTituloP));
                table.addCell(new Paragraph("Status",fTituloP));
                table.setHeaderRows(1);

                do {
                    if (!Tipo.equals(curAulas.getString(curAulas.getColumnIndexOrThrow("TIPO_STR")))) {
                        Tipo = curAulas.getString(curAulas.getColumnIndexOrThrow("TIPO_STR"));
//                        doc.add(new Paragraph(curAulas.getString(curAulas.getColumnIndexOrThrow("TIPO_STR"))));
                        PdfPCell cell = new PdfPCell(new Paragraph(curAulas.getString(curAulas.getColumnIndexOrThrow("TIPO_STR")),fTituloM));
                        cell.setColspan(4);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setBackgroundColor(new BaseColor(220,220,220));
                        table.addCell(cell);
                        table.completeRow();
                    }

//                    doc.add(new Paragraph(curAulas.getString(curAulas.getColumnIndexOrThrow("DATA_AULA_DTI"))
//                            + "       " + curAulas.getString(curAulas.getColumnIndexOrThrow("CONCLUIDO_STR"))
//                            + "       " + curAulas.getString(curAulas.getColumnIndexOrThrow("ASSUNTO_STR"))));

                    table.addCell(curAulas.getString(curAulas.getColumnIndexOrThrow("DATA_AULA_DTI")));
                    table.addCell(curAulas.getString(curAulas.getColumnIndexOrThrow("ASSUNTO_STR")));
                    table.addCell(curAulas.getString(curAulas.getColumnIndexOrThrow("INSTRUTOR_STR")));
                    table.addCell(curAulas.getString(curAulas.getColumnIndexOrThrow("CONCLUIDO_STR")));
                }
                while (curAulas.moveToNext());
            }

            doc.add(table);
            doc.add(new Paragraph("Data: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date())));
        }
        catch (DocumentException dex) {

        }
        catch (IOException ioex){
            String a = ioex.getMessage();
        }

        db.close();
        doc.close();

        Toast.makeText(context, context.getString(R.string.msgArqSalvoDownload).replace("@arq", NomeArq), Toast.LENGTH_LONG).show();
    }

}
