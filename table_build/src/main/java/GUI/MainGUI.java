package GUI;

import org.example.FexTemplateGenerator;
import org.example.MultiTableTempGenerator;
import org.example.RuleGenerator;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.example.Main.okPathToDtf;
import static org.example.OkFilePathList.readExcelFileToList;

public class MainGUI {
    private JFrame frame;
    private JTextField fexTemplatePath;
    private JTextField okFilePath;
    private JTextField tableTemplatePath;
    private JTextField ruleTemplatePath;
    private JTextField outputFolderPath;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainGUI window = new MainGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainGUI() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("File Selector App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        frame.getContentPane().add(panel);

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel lblFexTemplate = new JLabel("Fex Template (CSV):");
        fexTemplatePath = new JTextField();
        JButton btnFexTemplate = new JButton("选择文件");
        btnFexTemplate.addActionListener(e -> chooseFile(fexTemplatePath, "csv"));

        JLabel lblOkFile = new JLabel("Ok File (XLSX):");
        okFilePath = new JTextField();
        JButton btnOkFile = new JButton("选择文件");
        btnOkFile.addActionListener(e -> chooseFile(okFilePath, "xlsx"));

        JLabel lblTableTemplate = new JLabel("Table Template (XLSX):");
        tableTemplatePath = new JTextField();
        JButton btnTableTemplate = new JButton("选择文件");
        btnTableTemplate.addActionListener(e -> chooseFile(tableTemplatePath, "xlsx"));

        JLabel lblRuleTemplate = new JLabel("Rule Template (XLS):");
        ruleTemplatePath = new JTextField();
        JButton btnRuleTemplate = new JButton("选择文件");
        btnRuleTemplate.addActionListener(e -> chooseFile(ruleTemplatePath, "xls"));

        JLabel lblOutputFolder = new JLabel("Output Folder:");
        outputFolderPath = new JTextField();
        JButton btnOutputFolder = new JButton("选择文件夹");
        btnOutputFolder.addActionListener(e -> chooseFolder(outputFolderPath));

        JButton btnGenerate = new JButton("一键生成");
        btnGenerate.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenerate.setBackground(new Color(70, 130, 180));
        btnGenerate.setForeground(Color.WHITE);
        btnGenerate.addActionListener(e -> generateFiles());

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(lblFexTemplate)
                                .addComponent(lblOkFile)
                                .addComponent(lblTableTemplate)
                                .addComponent(lblRuleTemplate)
                                .addComponent(lblOutputFolder))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(fexTemplatePath)
                                .addComponent(okFilePath)
                                .addComponent(tableTemplatePath)
                                .addComponent(ruleTemplatePath)
                                .addComponent(outputFolderPath)
                                .addComponent(btnGenerate, GroupLayout.Alignment.TRAILING))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(btnFexTemplate)
                                .addComponent(btnOkFile)
                                .addComponent(btnTableTemplate)
                                .addComponent(btnRuleTemplate)
                                .addComponent(btnOutputFolder))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblFexTemplate)
                                .addComponent(fexTemplatePath)
                                .addComponent(btnFexTemplate))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblOkFile)
                                .addComponent(okFilePath)
                                .addComponent(btnOkFile))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblTableTemplate)
                                .addComponent(tableTemplatePath)
                                .addComponent(btnTableTemplate))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblRuleTemplate)
                                .addComponent(ruleTemplatePath)
                                .addComponent(btnRuleTemplate))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(lblOutputFolder)
                                .addComponent(outputFolderPath)
                                .addComponent(btnOutputFolder))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(btnGenerate))
        );

        frame.pack();
    }

    private void chooseFile(JTextField textField, String fileType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith("." + fileType);
            }

            @Override
            public String getDescription() {
                return fileType.toUpperCase() + " Files";
            }
        });
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            textField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void chooseFolder(JTextField textField) {
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = folderChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFolder = folderChooser.getSelectedFile();
            textField.setText(selectedFolder.getAbsolutePath());
        }
    }

    private void generateFiles() {
        // 检查是否选择了所有必要的文件和文件夹
        if (fexTemplatePath.getText().isEmpty() || okFilePath.getText().isEmpty() ||
                tableTemplatePath.getText().isEmpty() || ruleTemplatePath.getText().isEmpty() ||
                outputFolderPath.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "请确保选择所有必要的文件和输出文件夹。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog progressDialog = new JDialog(frame, "Processing...", true);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Generating files, please wait...");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        progressDialog.getContentPane().add(panel);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(frame);

        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private Exception exception;

            @Override
            protected Void doInBackground() {
                try {
                    System.out.println("Starting file generation...");

                    String fexTemplate = fexTemplatePath.getText();
                    String okFileExcel = okFilePath.getText();
                    String tableTemplate = tableTemplatePath.getText();
                    String ruleTemplate = ruleTemplatePath.getText();
                    String outputFolder = outputFolderPath.getText();

                    System.out.println("Reading OK file...");
                    List<String> inputFileNames = new ArrayList<>();
                    List<String> okFiles = readExcelFileToList(okFileExcel);
                    for (String fileName : okFiles) {
                        inputFileNames.add(okPathToDtf(fileName));
                    }

                    System.out.println("Generating Fex Template...");


                    System.out.println("File generation completed.");

                } catch (Exception e) {
                    this.exception = e;
                }
                return null;
            }

            @Override
            protected void done() {
                progressDialog.dispose();
                if (exception != null) {
                    JOptionPane.showMessageDialog(frame, "Error generating files: " + exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Files generated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        worker.execute();
        progressDialog.setVisible(true);
    }
}
