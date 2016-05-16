package com.lp.oop.model;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class SearchAndReplaceFrame{
	private JFrame frame;
	private JPanel northPanel = new JPanel(new GridLayout());
	private JPanel centerPanel = new JPanel(new GridLayout(1,2,10,10));
	private JPanel southPanel = new JPanel(new GridLayout(2,4,10,10));
	
	private JLabel lbTitle = new JLabel("Заміна слів у текстовому файлі", JLabel.CENTER);
	private JLabel lbFindWord = new JLabel("Слово пошуку:", JLabel.RIGHT);
	private JTextField txbFindWord = new JTextField();
	private JLabel lbReplaceWord = new JLabel("Слово заміни:", JLabel.RIGHT);
	private JTextField txbReplaceWord = new JTextField();
	private JCheckBox chbCheckCase = new JCheckBox("Облік регістру");
	private JButton btnReplace = new JButton("Замінити");
	private JLabel lbCountOfReplace = new JLabel("Кількість замін:", JLabel.RIGHT);
	private JTextField txbCountOfReplace = new JTextField();
	private JButton btnOpenFileChooser = new JButton("Відкрити файл");
	private JTextField txbOpenFileChooser = new JTextField(); 
	private JFileChooser fileChooser = new JFileChooser();
	private int returnedValue = 0;
	private String textFileBefore = "";
	private StringBuffer textFileAfter;
	private int countOfReplace = 0;
	
	private StreamTokenizer st;
	private OutputStreamWriter osw;
	public SearchAndReplaceFrame(String s){
		frame = new JFrame(s);
		frame.setSize(500, 200);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		Container container = frame.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(northPanel, BorderLayout.NORTH);
		container.add(centerPanel, BorderLayout.CENTER);
		container.add(southPanel, BorderLayout.SOUTH);
		northPanel.add(lbTitle);
		centerPanel.add(btnOpenFileChooser);
		centerPanel.add(txbOpenFileChooser);
		southPanel.add(lbFindWord);
		southPanel.add(txbFindWord);
		southPanel.add(lbReplaceWord);
		southPanel.add(txbReplaceWord);
		southPanel.add(chbCheckCase);
		southPanel.add(btnReplace);
		southPanel.add(lbCountOfReplace);
		southPanel.add(txbCountOfReplace);
		txbCountOfReplace.setText("0");
		txbCountOfReplace.setEditable(false);
		txbOpenFileChooser.setEditable(false);
		
		fileChooser.setCurrentDirectory(new File("D:\\JavaProjects\\GraphicSearchAndReplace"));
		btnOpenFileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				returnedValue = fileChooser.showOpenDialog(frame);
				if(returnedValue == JFileChooser.APPROVE_OPTION)
				{
					try {
						txbCountOfReplace.setText("0");
						countOfReplace = 0;
						st = new StreamTokenizer(new InputStreamReader(new FileInputStream(fileChooser.getSelectedFile()), "CP1251"));
						txbOpenFileChooser.setText(fileChooser.getSelectedFile().getName());
						ReadFromFile(st);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		btnReplace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(txbFindWord.getText() != "" && txbReplaceWord.getText() != "" && !textFileBefore.equals(""))
				{
					countOfReplace = 0;
					textFileAfter = new StringBuffer(textFileBefore);
					int position = 0;
					while (true)
					{
						if (chbCheckCase.isSelected())
							if ((position = textFileAfter.indexOf(txbFindWord.getText(), position)) != -1) {
								textFileAfter.replace(position,
										textFileAfter.indexOf(txbFindWord.getText()) + txbFindWord.getText().length(),
										txbReplaceWord.getText());
								countOfReplace++;
							} else
								break;
						else
							if ((position = textFileAfter.toString().toLowerCase().indexOf(txbFindWord.getText().toLowerCase(), position)) != -1) {
								textFileAfter.replace(position,
										textFileAfter.toString().toLowerCase().indexOf(txbFindWord.getText().toLowerCase()) + txbFindWord.getText().length(),
										txbReplaceWord.getText());
								countOfReplace++;
							} else
								break;
					}
					try {
						txbCountOfReplace.setText(Integer.toString(countOfReplace));
						File f = new File("temp.txt");
						if(!f.exists())
							f.createNewFile();
						osw = new OutputStreamWriter(new FileOutputStream(f));
						osw.write(textFileAfter.toString());
						osw.close();
					} catch (IOException e) { e.printStackTrace(); }
					System.out.println(textFileBefore);
					System.out.println(textFileAfter);
				}
			}
		});
	}
	
	private void ReadFromFile(StreamTokenizer st)
	{
		try {
			while(st.nextToken() != StreamTokenizer.TT_EOF)
			{
				if(st.sval != null)
					if(textFileBefore.equals(""))
						textFileBefore += st.sval;
					else
						textFileBefore += " " + st.sval;
				else if(st.ttype > 0)
					textFileBefore += (char)st.ttype;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
