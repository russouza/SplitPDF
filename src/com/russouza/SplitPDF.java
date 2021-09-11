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

    public void split(File file, String outDir) throws IOException, TesseractException {

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
            fileName = outDir + "\\SPLIT_"+ i +".pdf";
            //fileName = "D:/TEMP/SPLIT_"+ i +".pdf";
            System.out.println("filename: " + fileName);
            pd.save(fileName);

            // Comentar se não for usar OCR
            // String fileName2 = ocr.ocr(pd);

            File file2 = new File(fileName);
            ReadFile readFile = new ReadFile(file2);

            String fileName2 = readFile.readFile(file2);

            System.out.println("CPF: " + fileName2);

            newFileName = outDir + "\\RP_" + i + "_CPF_" + fileName2 + ".PDF";
            //newFileName = "D:/TEMP/RP_" + i + "_CPF_" + fileName2 + ".PDF";
            System.out.println(newFileName + " file saved");

            readFile.renameFile(fileName,newFileName);

          /*

            //pd.save(readFile.readFile(file2) + i++);

        */
        }

        System.out.println("PDF splitted");
    }
}
