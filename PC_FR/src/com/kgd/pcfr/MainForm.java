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
	 * 主窗体显示
	 */
	public void show() {
		init();
		frame.setLayout(null);
		frame.setSize(450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	/**
	 * 主窗体初始化
	 */
	public void init() {
		frame = new JFrame("查找替换");

		field = new JTextField("路径");
		field.setBounds(10, 30, 300, 40);

		btn = new JButton("浏览");
		btn.setBounds(330, 30, 90, 40);

		btnConfirm = new JButton("确定");
		btnConfirm.setBounds(330, 80, 90, 40);
		btn.addActionListener(this);
		btnConfirm.addActionListener(this);

		tfPath = new JTextField();
		tfPath.setBounds(10, 130, 300, 40);
		tfPath.setEditable(false);
		
		btnExploer = new JButton("目录");
		btnExploer.setBounds(330, 130, 90, 40);
		btnExploer.addActionListener(this);
		
		chooser = new JFileChooser(); // 创建文件选择器
		chooser.setCurrentDirectory(new File(".")); // 设置当前目录
		chooser.setDialogTitle("选择文件"); // 打开对话框的标题
		chooser.setFileFilter(new FileFilter() {

			@Override
			public boolean accept(File f) { // 过滤文件格式
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
				return "TXT(*.txt)）";
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
	 * 窗体事件处理
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btn) {
			System.out.println("打开对话框");
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				field.setText(chooser.getSelectedFile().toString());
				userSelectPath = chooser.getSelectedFile().getAbsolutePath();
				System.out.println("path=" + userSelectPath);
			}
		} else if (e.getSource() == btnConfirm) {
			userSelectPath = field.getText().toString();
			// System.out.println("path="+userSelectPath);
			if (userSelectPath.isEmpty() || userSelectPath.length() < 4) // 检查所选择路径是是否为空
			{
				JOptionPane.showMessageDialog(frame, "请选择TXT文件");
				return;
			}
			String postfix = userSelectPath.substring(
					userSelectPath.length() - 4, userSelectPath.length());
			if (!(postfix.equals(".TXT") || postfix.equals(".txt"))) // 检查格式是否是txt
																		// 文件
			{
				JOptionPane.showMessageDialog(frame, "所选择的文件不是TXT格式");
				return;
			}

			File file = new File(userSelectPath);
			if (!file.exists()) // 检查txt 文件是否真的存在
			{
				JOptionPane.showMessageDialog(frame, "所选择的TXT文件不存在");
				return;
			}

			if(!new File(System.getProperty("user.dir")+"/model.txt").exists())
			{
				JOptionPane.showMessageDialog(frame, "模板文件(model.txt)不存在");
				return;
			}
			
			if(new FindAndReplace().start(userSelectPath))
			{
				String path = System.getProperty("user.dir")+"mould/" + new File(userSelectPath).getName();
				tfPath.setText(path);
			}
		}else if (e.getSource() == btnExploer) {
			// 当前程序所有目录
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
	 * 主函数
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("mould");  // 查看当前目录下是有mould 文件夹，如果没有则创建它
		if (!file.exists()) {
			file.mkdir();
		}
		new MainForm().show();
	}
}
