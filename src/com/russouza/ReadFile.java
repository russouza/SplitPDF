package com.russouza;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFile {
    public static File file;

    public ReadFile(File file) {

    }


    public String readFile(File file) {
    
        String fileName = null;
        
        try {
            //File file = new File("D:/TEMP/SPLIT_131623481647883.pdf");
            PDDocument doc = Loader.loadPDF(file);
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            pdfTextStripper.setSortByPosition(true);
            pdfTextStripper.setStartPage(1);
            pdfTextStripper.setEndPage(1);
            String text = pdfTextStripper.getText(doc);

            Pattern p = Pattern.compile("[0-9]{3}.[0-9]{3}.[0-9]{3}.[0-9]{2}");
            Pattern d = Pattern.compile("[A-Za-z]{4,9} de 202[0-9]{1}");

//  Capturar data dentro do Documento
//          Pattern q = Pattern.compile("[0-9]{2}/[0-9]{2}/[0-9]{4}");

            Matcher m = p.matcher(text);
            m.find();

            Matcher e = d.matcher(text);
            e.find();

            //System.out.println(e.group(0));
            String mes_ano = e.group(0);
            String sufixo = mes_ano.replace(" de ","_");
            System.out.println(sufixo);

//            Matcher n = q.matcher(text);
//            n.find();

            //System.out.print("INDEX: ");
            //System.out.println(m.group(0));
            fileName = m.group(0);
            fileName = fileName + "_" + sufixo;
//            System.out.print("DATA: ");
//            System.out.println(n.group(0));

            //            System.out.println(text);
            doc.close();
            //return m.group(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void renameFile(String oldFile, String newFile){
        File oldF = new File(oldFile);
        File newF = new File(newFile);

        if(oldF.renameTo(newF)) {
            System.out.println("Successfully renamed");
        } else {
            System.out.println("Error renaming File");
        }
    }
}


