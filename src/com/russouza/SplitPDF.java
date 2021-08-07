package com.russouza;

import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import java.io.File;
import java.io.IOException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Iterator;

public class SplitPDF {
    public static File file;

    public SplitPDF(File file) {

    }

    public void split(File file) throws IOException, TesseractException {

        System.out.println("Hello, this is the Splitter");
        //Loading an existing PDF document
        //File file = new File("D:/TEMP/MERGED.pdf");
        //PDDocument doc = PDDocument.load(file);
        PDDocument doc = Loader.loadPDF(file);

        //Instantiating Splitter class
        Splitter splitter = new Splitter();

        //splitting the pages of a PDF document
        List<PDDocument> Pages = splitter.split(doc);

        //Creating an iterator
        Iterator<PDDocument> iterator = Pages.listIterator();

        //Saving each page as an individual document
        int i = 0;
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String fileName = null;
        String newFileName = null;
        OcrPDF ocr = new OcrPDF();

        while(iterator.hasNext()){
            PDDocument pd = iterator.next();
            i++;
            fileName = "D:/TEMP/SPLIT_"+ i +".pdf";
            System.out.println("filename: " + fileName);
            pd.save(fileName);

            String fileName2 = ocr.ocr(pd);
            System.out.println("CPF: " + fileName2);

            newFileName = "D:/TEMP/holerite_" + fileName2 + "_" + i + ".PDF";
            System.out.println("Salvo Arquivo " + newFileName);
            File file2 = new File(fileName);
            ReadFile readFile = new ReadFile(file2);
            readFile.renameFile(fileName,newFileName);

          /*

            //pd.save(readFile.readFile(file2) + i++);

        */
        }

        System.out.println("PDF splitted");
    }
}
