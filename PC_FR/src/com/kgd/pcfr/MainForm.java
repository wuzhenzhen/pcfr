package com.kgd.pcfr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class MainForm implements ActionListener {
	public JFrame frame = null;
	JButton btn = null;
	JButton btnConfirm = null;
	JTextField field = null;
	JFileChooser chooser = null;
	JTextField tfPath = null;
	JButton btnExploer = null;

	private String userSelectPath = "";

	/**
	 * ��������ʾ
	 */
	public void show() {
		init();
		frame.setLayout(null);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * �������ʼ��
	 */
	public void init() {
		frame = new JFrame("�����滻");

		field = new JTextField("·��");
		field.setBounds(10, 30, 300, 40);

		btn = new JButton("���");
		btn.setBounds(330, 30, 90, 40);

		btnConfirm = new JButton("ȷ��");
		btnConfirm.setBounds(330, 80, 90, 40);
		btn.addActionListener(this);
		btnConfirm.addActionListener(this);

		tfPath = new JTextField();
		tfPath.setBounds(10, 130, 300, 40);
		tfPath.setEditable(false);
		
		btnExploer = new JButton("Ŀ¼");
		btnExploer.setBounds(330, 130, 90, 40);
		btnExploer.addActionListener(this);
		
		chooser = new JFileChooser(); // �����ļ�ѡ����
		chooser.setCurrentDirectory(new File(".")); // ���õ�ǰĿ¼
		chooser.setDialogTitle("ѡ���ļ�"); // �򿪶Ի���ı���
		chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) { // �����ļ���ʽ
				// TODO Auto-generated method stub
				if (f.getName().endsWith(".txt")
						|| f.getName().endsWith(".TXT") || f.isDirectory()) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				// TODO Auto-generated method stub
				return "TXT(*.txt)��";
			}

		});

		frame.add(btn);
		frame.add(btnConfirm);
		frame.add(field);
		frame.add(chooser);
		frame.add(tfPath);
		frame.add(btnExploer);
	}

	/**
	 * �����¼�����
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btn) {
			System.out.println("�򿪶Ի���");
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				field.setText(chooser.getSelectedFile().toString());
				userSelectPath = chooser.getSelectedFile().getAbsolutePath();
				System.out.println("path=" + userSelectPath);
			}
		} else if (e.getSource() == btnConfirm) {
			userSelectPath = field.getText().toString();
			// System.out.println("path="+userSelectPath);
			if (userSelectPath.isEmpty() || userSelectPath.length() < 4) // �����ѡ��·�����Ƿ�Ϊ��
			{
				JOptionPane.showMessageDialog(frame, "��ѡ��TXT�ļ�");
				return;
			}
			String postfix = userSelectPath.substring(
					userSelectPath.length() - 4, userSelectPath.length());
			if (!(postfix.equals(".TXT") || postfix.equals(".txt"))) // ����ʽ�Ƿ���txt
																		// �ļ�
			{
				JOptionPane.showMessageDialog(frame, "��ѡ����ļ�����TXT��ʽ");
				return;
			}

			File file = new File(userSelectPath);
			if (!file.exists()) // ���txt �ļ��Ƿ���Ĵ���
			{
				JOptionPane.showMessageDialog(frame, "��ѡ���TXT�ļ�������");
				return;
			}

			if(!new File(System.getProperty("user.dir")+"/model.txt").exists())
			{
				JOptionPane.showMessageDialog(frame, "ģ���ļ�(model.txt)������");
				return;
			}
			
			if(new FindAndReplace().start(userSelectPath))
			{
				String path = System.getProperty("user.dir")+"mould/" + new File(userSelectPath).getName();
				tfPath.setText(path);
			}
		}else if (e.getSource() == btnExploer) {
			// ��ǰ��������Ŀ¼
			// System.out.println(System.getProperty("user.dir"));
			try {
				String filePath = System.getProperty("user.dir")+ "/mould";
				System.out.println("filePath====exploer===="+filePath);
				java.awt.Desktop.getDesktop().open(
						new File(filePath));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
	}

	/**
	 * ������
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("mould");  // �鿴��ǰĿ¼������mould �ļ��У����û���򴴽���
		if (!file.exists()) {
			file.mkdir();
		}
		new MainForm().show();
	}
}
