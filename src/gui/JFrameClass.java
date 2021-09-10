package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
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
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

public class JFrameClass extends JFrame implements CaretListener {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane = new JPanel();;
	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuArquivo = new JMenu("Arquivo");
	private JMenuItem novo = new JMenuItem("Novo");
	private JMenuItem abrir = new JMenuItem("Abrir");
	private JMenuItem salvar = new JMenuItem("Salvar");
	private JMenuItem salvarComo = new JMenuItem("Salvar como");
	private JTextArea textAreaGlobal = new JTextArea();
	private JFileChooser fileChooser = new JFileChooser();
	private final JLabel caminhoArquivo = new JLabel("");
	private JScrollPane scrollPane = new JScrollPane();
	private JTabbedPane abas = new JTabbedPane();

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

		novo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					novo(fileChooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

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

		menuArquivo.add(novo);
		menuArquivo.add(abrir);
		menuArquivo.add(salvar);
		menuArquivo.add(salvarComo);
	}

	public void browseFiles(ActionEvent e) throws IOException {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fileChooser.setFileFilter(filter);

		fileChooser.showOpenDialog(this);
		fileChooser.setVisible(true);

		JPanel panel = new JPanel();

		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(0, 0));

		setContentPane(panel);

		panel.add(scrollPane, BorderLayout.CENTER);

		panel.add(caminhoArquivo, BorderLayout.SOUTH);

		add(BorderLayout.CENTER, abas);

		JTextArea textArea = leitor2(fileChooser.getSelectedFile());

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setViewportView(textArea);

		abas.addTab(fileChooser.getSelectedFile().getName(), scrollPane2);

		atualizarLinhasAtuais(textArea);

		caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath());
	}

	private void atualizarLinhasAtuais(JTextArea textArea) {
		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int lineNum = 1;

				try {
					int pos = textAreaGlobal.getCaretPosition();
					lineNum = textAreaGlobal.getLineOfOffset(pos);

					lineNum += 1;
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				updateStatus(lineNum);
			}
		});
	}

	public void leitor(File file) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(file.getAbsolutePath()));
		String linha = "";
		while (true) {
			if (linha != null) {
				textAreaGlobal.append(linha + "\n");
			} else
				break;
			linha = buffRead.readLine();
		}
		buffRead.close();
	}

	public JTextArea leitor2(File file) throws IOException {
		BufferedReader buffRead = new BufferedReader(new FileReader(file.getAbsolutePath()));
		String linha = "";
		JTextArea textArea = new JTextArea();
		while (true) {
			if (linha != null) {
				textArea.append(linha + "\n");
			} else
				break;
			linha = buffRead.readLine();
		}
		buffRead.close();
		textAreaGlobal = textArea;
		return textArea;
	}

	public void escrever(File file) {
		try {
			if (file == null) {
				salvarComo();
			} else {
				FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
				fileWriter.write(textAreaGlobal.getText());
				fileWriter.close();
			}

			caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void escreverNovo() {
		fileChooser.showSaveDialog(this);
		
		File file = fileChooser.getSelectedFile();
		FileWriter fileWriter;
		try {
			fileWriter = new FileWriter(file.getAbsolutePath());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		caminhoArquivo.setText("Caminho: " + fileChooser.getSelectedFile().getAbsolutePath());
	}

	public void salvarComo() {
		int returnVal = fileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
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

	private void novo(File file) throws IOException {
		escreverNovo();
		JPanel panel = new JPanel();

		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.setLayout(new BorderLayout(0, 0));

		setContentPane(panel);

		panel.add(scrollPane, BorderLayout.CENTER);

		panel.add(caminhoArquivo, BorderLayout.SOUTH);

		add(BorderLayout.CENTER, abas);

		JTextArea textArea = leitor2(fileChooser.getSelectedFile());

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setViewportView(textArea);

		abas.addTab(fileChooser.getSelectedFile().getName(), scrollPane2);

		atualizarLinhasAtuais(textArea);
	}

}
