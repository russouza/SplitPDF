package com.russouza;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.Loader;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws IOException, TesseractException {

	// write your code here
        //System.out.println("Hello, this is the Splitter");
        //Loading an existing PDF document
        //File file = new File("D:/TEMP/MERGED2.PDF");
        File file = new File("D:/TEMP/HOLERITES.PDF");
        SplitPDF splitPDF = new SplitPDF(file);
        splitPDF.split(file);

        /*
        OcrPDF ocr = new OcrPDF();
        File tifFile = new File("D:/TEMP/holerite.tif");
        ocr.ocr(tifFile);
        */

        /*

        File file = new File("D:/TEMP/MERGED.pdf");
        //PDDocument doc = PDDocument.load(file);
        PDDocument doc = Loader.loadPDF(file);

        //Instantiating Splitter class
        Splitter splitter = new Splitter();

        //splitting the pages of a PDF document
        List<PDDocument> Pages = splitter.split(doc);

        //Creating an iterator
        Iterator<PDDocument> iterator = Pages.listIterator();

        //Saving each page as an individual document
        int i = 1;

        while(iterator.hasNext()){
            PDDocument pd = iterator.next();
            pd.save("D:/TEMP/SPLIT_"+ i++ +".pdf");
        }
        System.out.println("PDF splitted");
*/
    }
}
