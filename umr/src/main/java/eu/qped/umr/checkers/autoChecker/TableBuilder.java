package eu.qped.umr.checkers.autoChecker;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TableBuilder {

    private final Map<String , Integer> data;
    private Document document;

    private TableBuilder (Map<String , Integer> data){
        this.data = data;
    }

    public static TableBuilder createTableBuilder(Map<String , Integer> data){
        return new TableBuilder(data);
    }

    public void build(){
        String path = "documentProfi.pdf";

        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(path));
            document = new Document(pdfDocument);
            Table table = new Table(2);
            for (Map.Entry<String , Integer> entry :new ArrayList<>(data.entrySet()).stream()
                    .sorted((x , y) -> y.getValue() - x.getValue())
                    .collect(Collectors.toList())
            ) {
                table.addCell(entry.getKey());
                table.addCell(entry.getValue().toString());
            }
            document.add(table);
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public void closeDocument (){
        document.close();
    }

}
