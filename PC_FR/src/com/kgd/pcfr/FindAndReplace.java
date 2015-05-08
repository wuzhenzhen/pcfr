package com.kgd.pcfr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import com.kgd.pcfr.Mould;

public class FindAndReplace {
	public FindAndReplace() {
	}

	private String pMould[], dMould[]; // pMould Ϊ��������ģ� dMouldΪģ��

	/**
	 * //ģ��modle.txt �� MId=T32,MValue=ROUND 7.2
	 */
	private Vector<Mould> vMould = new Vector<Mould>();

	/**
	 * FLAG_TOOLS = "(*** TOOLS ***)"
	 */
	private static final String FLAG_TOOLS = "(*** TOOLS ***)";

	/**
	 * oldNew<old,new> new ��Ҫ�滻��old
	 */
	private HashMap<String, String> oldNew = new HashMap<String, String>();

	/**
	 * �����滻
	 * 
	 */
	private void ChangePMould() {
		if (pMould == null) {
			System.out.println("pMould is null");
			return;
		}
		int flag = 0;
		// System.out.println("pMould.length==============="+pMould.length);
		for (int i = 0; i < pMould.length; i++) {
			if (pMould[i].indexOf("T") >= 0 && pMould[i].indexOf("T") < 2) // ���ַ����а����Ƿ����T,
																			// ��T��λ����
																			// 0<T<2
			{
				if (flag >= 2) {
					System.out.println("Tools is 2");
					// ֻ��Ҫ�滻 T**����
					String old = pMould[i].trim();
					if (oldNew.get(old) != null) {
						// �滻
						pMould[i] = oldNew.get(old);
					}
				} else {
					String tempString = replace(pMould[i]);
					pMould[i] = tempString;
				}
			} else if (pMould[i].equals(FLAG_TOOLS)) // ͳ�� FLAG_TOOLS
														// ���ֵĴ������������2˵��
														// ����λ�ţ�ģ�����ͣ�ģ�߳ߴ磬ģ�߽Ƕȡ���ʽ�Ѿ�����ȥ��
			{
				flag++;
			}
		}
		return;
	}

	/**
	 * �滻 ����λ�ţ�ģ�����ͣ�ģ�߳ߴ磬ģ�߽Ƕȡ�
	 * 
	 * @param rowData
	 *            ������
	 * @return �����滻�������
	 */
	private String replace(String rowData) {
		Mould mm = getMouldStyle(rowData);
		for (int i = 0; i < vMould.size(); i++) {
			String temp = vMould.get(i).getMValue();
			if (temp.equals(mm.getMValue())) {
				// ��¼Ҫ�滻��λ��
				oldNew.put(mm.getMId(), vMould.get(i).getMId());
				// �滻��λ��
				mm.setMId(vMould.get(i).getMId());
				rowData = "(" + mm.getString() + ")";
			}
		}
		return rowData;
	}

	/**
	 * �����ļ�������
	 */
	private boolean save(String path) {
		if (pMould == null || pMould.length == 0 || path.isEmpty()) {
			return false;
		}
		File file = new File("mould");
		if (!file.exists()) {
			file.mkdir();
		}
		path = "mould" + "/" + path;
		BufferedWriter bfw = null; // �����ļ������
		try {
			FileWriter fw = new FileWriter(path); // �ȶ����ļ�
			bfw = new BufferedWriter(fw);
			for (int k = 0; k < pMould.length; k++) {
				bfw.write(pMould[k]);
				bfw.newLine();
			}
			bfw.flush();
			if (bfw != null) {
				bfw.close();
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			System.out.println("д���ļ�ʧ��"); // TODO should be show dialog
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ��ģ�߶���String [] dMould;
	 */
	private void HandleMould() {
		if (dMould.length < 0)
			return; // TODO should show dialog
		for (int i = 0; i < dMould.length; i++) {
			Mould mm = getMouldStyle(dMould[i]);
			vMould.add(mm);
		}

		// for(int k=0; k < vMould.size(); k++)
		// {
		// Mould mm = vMould.get(k);
		// System.out.println(mm.getString());
		// }
		// System.out.println( vMould.size());
	}

	/**
	 * ��rowData �� �ո񣨡� �����ָ������õ�ǰ�벿������
	 * 
	 * @param rowData
	 *            String ����
	 * @return ����һ�� Mould ���� new Mould(MId, MValue);
	 */
	private Mould getMouldStyle(String rowData) {
		Mould mm = new Mould();
		if (rowData.indexOf("(") == 0) {
			rowData = rowData.substring(1, rowData.length() - 1);
			;
		}
		String[] stringArray = rowData.split(" ");
		// if (stringArray.length == 0)
		// {
		// System.out.println("splitString is empty");
		// mm = null;
		// return mm;
		// }
		mm.setMId(stringArray[0].trim());
		StringBuilder temp = new StringBuilder();
		for (int j = 1; j < stringArray.length; j++) {
			temp.append(stringArray[j]);
			if (j + 1 < stringArray.length) {
				temp.append(" ");
			}
		}
		mm.setMValue(temp.toString());
		return mm;
	}

	private void testFunction() {
		return;
	}

	/**
	 * ��ȡpath ·����txt, ���䱣����String ������
	 * 
	 * @param path
	 *            ģ�ߺ�ѡ����ļ�·��
	 * @return ����һ��String ���� ����Ϊ��λ
	 */
	private String[] getPathString(String path) {
		String data = new String("");
		File file = new File(path);
		BufferedReader reader = null;
		try {
			System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			while ((tempString = reader.readLine()) != null) {
				data = data + tempString + "\n";
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("����TXT ʧ��");
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		String[] resultData = data.split("\n");
		// ��ӡTXT������
		// for(int i = 0; i < resultData.length; i++)
		// {
		// System.out.println("i="+i+ "  "+resultData[i]);
		// }
		return resultData;
	}

	public boolean start(String selectPath) {
		testFunction();
		if (selectPath.isEmpty()) {
			return false;
		}
		boolean flag = false;
		// ����ѡ����ļ�
		pMould = getPathString(selectPath);
		// ����ģ���ļ�
		String modelPath = System.getProperty("user.dir")+"/model.txt";
		dMould = getPathString(modelPath);

		// ����ģ��
		HandleMould();
		System.out.println("load mould is success");

		// ���� TOOLS �� TOOLS ֮�������
		// HandleProgramMould();
		// System.out.println("load program Mould is success");

		// �����滻
		ChangePMould();
		System.out.println("find and replace success");

		// �����ļ�������
		flag = save(new File(selectPath).getName());
		System.out.println("save sucess");
		return flag;
	}

}