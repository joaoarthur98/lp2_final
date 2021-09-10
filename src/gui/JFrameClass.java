package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class JFrameClass extends JFrame implements CaretListener{

	private static final long serialVersionUID = 1L;
	
	private JPanel contentPane = new JPanel();;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuArquivo = new JMenu("Arquivo");
	private JMenuItem abrir = new JMenuItem("Abrir");
	private JMenuItem salvar = new JMenuItem("Salvar");
	private JMenuItem salvarComo = new JMenuItem("Salvar como");
	private JMenu mnNewMenu = new JMenu("Editar");
	private JTextArea textArea = new JTextArea();
	private JFileChooser fileChooser = new JFileChooser();
	private final JLabel caminhoArquivo = new JLabel("");
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrameClass frame = new JFrameClass();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public JFrameClass() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		this.menuBar = new JMenuBar();
		this.menuArquivo = new JMenu("Arquivo");
		
		setJMenuBar(menuBar);
		
		menuBar.add(menuArquivo);
		
		abrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					browseFiles(e);
				} catch (IOException exception) {
					exception.printStackTrace();
				}
			}
		});
		
		salvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escrever(fileChooser.getSelectedFile());
			}
		});
		
		salvarComo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escrever(fileChooser.getSelectedFile());
			}
		});
		
		menuArquivo.add(abrir);
		menuArquivo.add(salvar);
		menuArquivo.add(salvarComo);
		
		menuBar.add(mnNewMenu);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.setViewportView(textArea);
		
		
		contentPane.add(caminhoArquivo, BorderLayout.SOUTH);
		
		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int lineNum = 1;
				
				try {
					int pos = textArea.getCaretPosition();
					lineNum = textArea.getLineOfOffset(pos);
					
					lineNum += 1;
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				updateStatus(lineNum);
			}
		});
		
	}
	
	public void browseFiles(ActionEvent e) throws IOException {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fileChooser.setFileFilter(filter);
		
		fileChooser.showOpenDialog(this);
		fileChooser.setVisible(true);
		
		caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath());
		leitor(fileChooser.getSelectedFile());
	}
	
	public void leitor(File file) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(file.getAbsolutePath()));
		String linha = "";
		while (true) {
			if (linha != null) {
				textArea.append(linha + "\n");
			} else
				break;
			linha = buffRead.readLine();
		}
		buffRead.close();
	}
	
	public void escrever(File file) {
		try {
			
			if(file == null) {
				salvarComo();
			} else {
				FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
				fileWriter.write(textArea.getText());
				fileWriter.close();
			}
			
			caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void salvarComo() {
		int returnVal = fileChooser.showSaveDialog(this);
		
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			escrever(file);
		}
	}

	private void updateStatus(int linenumber) {
		if (fileChooser.getSelectedFile() == null)
			caminhoArquivo.setText("Line: " + linenumber);
		else
			caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath() + "\t Line: " + linenumber);
    }

	@Override
	public void caretUpdate(CaretEvent e) {
	}

}
