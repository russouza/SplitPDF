package com.russouza;

import net.sourceforge.tess4j.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OcrPDF {

    public String ocr(PDDocument pdfFile) throws IOException, TesseractException {

        String fileName = null;

        PDFRenderer pdfRenderer = new PDFRenderer(pdfFile);
        BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
        File imageFile = File.createTempFile("tempfile_" + 0 , ".png");
        ImageIO.write(bufferedImage, "png", imageFile);


        ITesseract instance = new Tesseract();  // JNA Interface Mapping
        // ITesseract instance = new Tesseract1(); // JNA Direct Mapping
        instance.setDatapath("D:/Projeto_JAVA/Library/Tess4J/tessdata"); // path to tessdata directory
        instance.setLanguage("eng");
        try {
            String result = instance.doOCR(imageFile);
            //System.out.println(result);

            //Pattern p = Pattern.compile("CPF [0-9]{3}.[0-9]{3}.[0-9]{3}.[0-9]{2}");
            //Pattern p = Pattern.compile("CPF [0-9]{3}.[0-9]{3}.[0-9]{3}");
            Pattern p = Pattern.compile("CTPS [0-9]{8}");


            Matcher m = p.matcher(result);
            m.find();

            //System.out.print("INDEX: ");
            //System.out.println(m.group(0));
            fileName = m.group(0);

        } catch (TesseractException e) {
            System.err.println(e.getMessage());
        }
        return fileName;
    }
}


