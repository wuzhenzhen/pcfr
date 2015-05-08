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

	private String pMould[], dMould[]; // pMould 为程序出来的， dMould为模具

	/**
	 * //模具modle.txt 例 MId=T32,MValue=ROUND 7.2
	 */
	private Vector<Mould> vMould = new Vector<Mould>();

	/**
	 * FLAG_TOOLS = "(*** TOOLS ***)"
	 */
	private static final String FLAG_TOOLS = "(*** TOOLS ***)";

	/**
	 * oldNew<old,new> new 需要替换掉old
	 */
	private HashMap<String, String> oldNew = new HashMap<String, String>();

	/**
	 * 查找替换
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
			if (pMould[i].indexOf("T") >= 0 && pMould[i].indexOf("T") < 2) // 此字符串中包含是否包含T,
																			// 且T的位置是
																			// 0<T<2
			{
				if (flag >= 2) {
					System.out.println("Tools is 2");
					// 只需要替换 T**即可
					String old = pMould[i].trim();
					if (oldNew.get(old) != null) {
						// 替换
						pMould[i] = oldNew.get(old);
					}
				} else {
					String tempString = replace(pMould[i]);
					pMould[i] = tempString;
				}
			} else if (pMould[i].equals(FLAG_TOOLS)) // 统计 FLAG_TOOLS
														// 出现的次数，如果大于2说明
														// “工位号，模具类型，模具尺寸，模具角度”格式已经读过去了
			{
				flag++;
			}
		}
		return;
	}

	/**
	 * 替换 “工位号，模具类型，模具尺寸，模具角度”
	 * 
	 * @param rowData
	 *            行数据
	 * @return 返回替换后的数据
	 */
	private String replace(String rowData) {
		Mould mm = getMouldStyle(rowData);
		for (int i = 0; i < vMould.size(); i++) {
			String temp = vMould.get(i).getMValue();
			if (temp.equals(mm.getMValue())) {
				// 记录要替换工位号
				oldNew.put(mm.getMId(), vMould.get(i).getMId());
				// 替换工位号
				mm.setMId(vMould.get(i).getMId());
				rowData = "(" + mm.getString() + ")";
			}
		}
		return rowData;
	}

	/**
	 * 生成文件并保存
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
		BufferedWriter bfw = null; // 定义文件输出流
		try {
			FileWriter fw = new FileWriter(path); // 先定义文件
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
			System.out.println("写入文件失败"); // TODO should be show dialog
			e1.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 将模具读入String [] dMould;
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
	 * 将rowData 以 空格（“ ”）分隔开，得到前半部分内容
	 * 
	 * @param rowData
	 *            String 类型
	 * @return 返回一个 Mould 对象 new Mould(MId, MValue);
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
	 * 读取path 路径的txt, 将其保存在String 数组内
	 * 
	 * @param path
	 *            模具和选择的文件路径
	 * @return 返回一个String 数组 以行为单位
	 */
	private String[] getPathString(String path) {
		String data = new String("");
		File file = new File(path);
		BufferedReader reader = null;
		try {
			System.out.println("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				data = data + tempString + "\n";
			}
			reader.close();
		} catch (IOException e) {
			System.out.println("加载TXT 失败");
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
		// 打印TXT的内容
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
		// 加载选择的文件
		pMould = getPathString(selectPath);
		// 加载模具文件
		String modelPath = System.getProperty("user.dir")+"/model.txt";
		dMould = getPathString(modelPath);

		// 加载模具
		HandleMould();
		System.out.println("load mould is success");

		// 加载 TOOLS 到 TOOLS 之间的数据
		// HandleProgramMould();
		// System.out.println("load program Mould is success");

		// 查找替换
		ChangePMould();
		System.out.println("find and replace success");

		// 生成文件并保存
		flag = save(new File(selectPath).getName());
		System.out.println("save sucess");
		return flag;
	}

}