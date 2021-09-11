package com.russouza;

import net.sourceforge.tess4j.TesseractException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.List;
import javax.swing.ImageIcon;

public class SplitForm extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel northPanel;
    private JPanel northPanel2;
    private JPanel northPanel3;
    private JButton btnSplit;
    private JButton btnPDFFile;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private JProgressBar progressBar;
    private JButton btnPDFOut;
    JTextField textField;
    private JLabel label;
    private JLabel logo;
    private JLabel copyright;
    File pdfFile;
    String outDir;
    String sufixo;

    ImageIcon imageLf;

    URL iconURL = getClass().getResource("lfi_br.png");
    // iconURL is null when not found
    ImageIcon icon = new ImageIcon(iconURL);

    {
        imageLf = new ImageIcon("/res/lfi_br.png");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(SplitForm::new);
    }

    public SplitForm() {
        setTitle("PDF Splitter Beta");
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        setIconImage(new ImageIcon(getClass().getResource("lficon.png")).getImage());
    }

    private void initComponents() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 300));
        this.contentPane = new JPanel();
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(this.contentPane);

        this.northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        this.contentPane.add(this.northPanel, BorderLayout.NORTH);

        this.northPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,5));
        this.contentPane.add(this.northPanel2, BorderLayout.AFTER_LINE_ENDS);

        this.northPanel3 = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5,5));
        this.contentPane.add(this.northPanel3, BorderLayout.PAGE_END);

        this.logo = new JLabel();
        //this.logo.setIcon(imageLf);
        this.logo.setIcon(icon);
        this.northPanel.add(this.logo, BorderLayout.BEFORE_FIRST_LINE);

        this.btnPDFFile = new JButton("Select PDF");
        this.btnPDFFile.addActionListener(new ActionWorker());
        this.northPanel.add(this.btnPDFFile);

        this.label = new JLabel("Filename Suffix:");
        this.northPanel.add(this.label);

        this.textField = new JTextField();
        //this.textField.setSize(250,100);
        Dimension d = textField.getPreferredSize();
        d.height = 26;
        this.textField.setPreferredSize(d);
        this.textField.setColumns(10);
        this.northPanel.add(this.textField);
        this.textField.setToolTipText("Don't use special characters");

        this.btnPDFOut = new JButton("Select/Create Output Dir");
        this.btnPDFOut.addActionListener(new ActionWorker2());
        this.northPanel.add(this.btnPDFOut);

        this.btnSplit = new JButton("Split PDF >>");
        this.btnSplit.addActionListener(new ActionWorker3());
        this.northPanel.add(this.btnSplit);

        this.textArea = new JTextArea();
        this.textArea.setBackground(Color.BLACK);
        this.textArea.setForeground(Color.WHITE);

        this.scrollPane = new JScrollPane(this.textArea);
        this.scrollPane.setBorder(new EmptyBorder(2, 0, 2, 0));
        this.contentPane.add(this.scrollPane, BorderLayout.CENTER);

        this.progressBar = new JProgressBar(0, 10);
        this.contentPane.add(this.progressBar, BorderLayout.SOUTH);

        this.copyright = new JLabel("© Copyright 2021, LaserForm Informática Ltda. All rights reserved.");
        copyright.setFont(new Font("", Font.ITALIC, 10));
        this.northPanel3.add(this.copyright);
    }

    private void textAreaAppend(String str) {
        this.textArea.append(str + System.lineSeparator());
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    class ActionWorker implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("Escolha PDF");
            int result = jFileChooser.showSaveDialog(null);
            textAreaAppend(String.valueOf(jFileChooser.getSelectedFile()));

            pdfFile = jFileChooser.getSelectedFile();
        }
    }

    class ActionWorker2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            jFileChooser.setDialogTitle("Escolha Diretório de Saída");
            int result = jFileChooser.showSaveDialog(null);
            textAreaAppend(String.valueOf(jFileChooser.getSelectedFile()));

            //System.out.println(jFileChooser.getSelectedFile());
            outDir = String.valueOf(jFileChooser.getSelectedFile());
            sufixo = textField.getText();
            //System.out.println("Suffix: " + sufixo);
        }
    }

    class ActionWorker3 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            sufixo = textField.getText();
            textAreaAppend("Splitting...");
            textAreaAppend("Wait...");
            textAreaAppend("Suffix: " + sufixo);
            redirectSystemStreams();
            SplitPDF splitPDF = new SplitPDF(pdfFile);
            try {
                splitPDF.split(pdfFile, outDir, sufixo);
            } catch (IOException | TesseractException ioException) {
                ioException.printStackTrace();
            }
            textAreaAppend("Done!");
        }
    }

    class ActionWorker1 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {

                @Override
                protected Void doInBackground() throws Exception {

                    //aqui tudo ocorrerá em paralelo a gui
                    //desativo o botão, adiciono uma mensagem inicial
                    btnSplit.setEnabled(false);
                    textAreaAppend("Splitting...");
                    //laço que alimenta a barra de progresso apenas para exemplo
                    for (int i = 0; i < progressBar.getMaximum(); i++) {

                        if (i > 0 && i % 2 == 0)
                            textAreaAppend("Wait...");
                        Thread.sleep(500);
                        //esse metodo "publica" atualizações da tarefa
                        //para a que a barra de progresso seja alimentada
                        //conforme o incremento do laço
                        publish(i);
                    }
                    SplitPDF splitPDF = new SplitPDF(pdfFile);
                    try {
                        splitPDF.split(pdfFile, outDir, sufixo);
                    } catch (IOException | TesseractException ioException) {
                        ioException.printStackTrace();
                    }
                    textAreaAppend("Done!");
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    //este métodor recebe o que foi "publicado"
                    // no doInBackground para que possamos atualizar
                    //o progresso na tela
                    progressBar.setValue(chunks.stream().reduce(Integer::sum).orElse(0));
                }

                @Override
                protected void done() {
                    //entrará aqui quando terminar a tarefa, ou
                    //ocorrer alguma exceção
                    progressBar.setValue(0);
                    btnSplit.setEnabled(true);
                }
            };
            worker.execute();
        }
    }

    void updateText(String text) {
        textAreaAppend(text);
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateText(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateText(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }


}