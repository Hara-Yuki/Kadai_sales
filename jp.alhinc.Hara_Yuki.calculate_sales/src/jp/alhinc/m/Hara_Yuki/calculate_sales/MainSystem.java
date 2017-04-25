package jp.alhinc.m.Hara_Yuki.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MainSystem {
	public static void main(String[] args){
		long salesSum;
		String sep = System.getProperty("line.separator");
		File branchFile = new File(args[0] + "branch.lst");
		File commodityFile = new File(args[0] + "commodity.lst");
		File folder = new File(args[0]);
		File branchFileWriter = new File(args[0] + "branch.out");
		File commodityFileWriter = new File(args[0] + "commodity.out");

		HashMap<String, String> branchFileMap = new HashMap<String, String>();
		HashMap<String, String> commodityFileMap = new HashMap<String, String>();
		HashMap<String, Long> salesBranchMap = new HashMap<String, Long>();
		HashMap<String, Long> salesCommodityMap = new HashMap<String, Long>();

		if(!branchFile.exists()) {//ファイル探し
			System.out.println("支店定義ファイルが存在しません");
			return;
		}

		//FileReader bf = null;//new FileReader(branchFile);
		BufferedReader branchFileBuffer = null;//new BufferedReader(bf);

		try{//中身見て処理

			branchFileBuffer = new BufferedReader(new FileReader(branchFile));

			String s;
			while((s = branchFileBuffer.readLine()) != null){
				String[] branchFileArray = s.split(",");
				int bl = branchFileArray.length;

				//System.out.println(bl);
				//System.out.println(bfarray[0].matches("[0-9]{3}"));
				if(bl == 2  && branchFileArray[0].matches("[0-9]{3}")){

					//System.out.println(bfarray[0] + bfarray[1]);
					branchFileMap.put(branchFileArray[0], branchFileArray[1]);
					//System.out.println(bfm.get(bfarray[0]) + "を取り込んだぞい");
					salesBranchMap.put(branchFileArray[0], 0L);
					//System.out.println("ついでに" + bfarray[0] + "用の売上マップも作ったぞい");

				}else{
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}

			}

		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}finally{
			try{
				branchFileBuffer.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		if(!commodityFile.exists()) {
			System.out.println("商品定義ファイルが存在しません");
			return;
		}

		BufferedReader commodityFileBuffer = null; //new BufferedReader(cf);

		try{

			//FileReader cf = new FileReader(commodityFile);
			commodityFileBuffer = new BufferedReader(new FileReader(commodityFile));
			String t;

			while((t = commodityFileBuffer.readLine()) != null){

				String[] cfArray = t.split(",");
				int cl = cfArray.length;

				if(cl == 2  && cfArray[0].matches("^[a-zA-Z0-9]{8}$")){

					//System.out.println(cfarray[0] + cfarray[1]);

					commodityFileMap.put(cfArray[0], cfArray[1]);
					//System.out.println(cfm.get(cfarray[0]) + "を取り込んだぞい");
					salesCommodityMap.put(cfArray[0], 0L);
					//System.out.println("ついでに" + cfarray[0] + "用の売上マップも作ったぞい");
				}else{
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}

			}

		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}finally{
			try{
				commodityFileBuffer.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
		}

		//System.out.println(bfm.entrySet());
		//System.out.println(cfm.entrySet());

		String[] storege = folder.list();
		List<Integer> rcdList = new ArrayList<Integer>();
		List<String> rcdList2 = new ArrayList<String>();
		for(int i = 0; i < storege.length ;i++){
			//System.out.println(storege[i]);

			if(storege[i].matches("[0-9]{8}.rcd")){
				//List <String> rcdlist = new ArrayList<String>();
				//rcdlist.add(storege[i]);

				String[] rcdDate = storege[i].split("\\.");
				//System.out.println(Arrays.toString(rcddate));


				rcdList.add(Integer.valueOf(rcdDate[0]));
				rcdList2.add(rcdDate[0]);
			}
		}

		if(rcdList.size() == (rcdList.get(rcdList.size() - 1) - rcdList.get(0) + 1)){

			for(int n = 0; n < rcdList.size(); n++){

				File salesList = new File(args[0] + rcdList2.get(n) + ".rcd");
				BufferedReader salesListBuffer = null;

				try{

					//FileReader sr = new FileReader(SR);
					salesListBuffer = new BufferedReader(new FileReader(salesList));
					String p;
					String[] srl = new String[3];
					int s = 0;
					//System.out.println(storege[i]  + "を呼び出したぞい");
					while((p = salesListBuffer.readLine()) != null){
						srl[s] = p;
						//System.out.println(srl[s] + "をpから入力したぞい");
						s++;
					}
					if(branchFileMap.get(srl[0]) == null){
						System.out.println(args[0] + rcdList2.get(n) + ".rcdの支店コードが不正です");
						return;
					}else if(commodityFileMap.get(srl[1]) == null){
						System.out.println(args[0] + rcdList2.get(n) + ".rcdの商品コードが不正です");
						return;
					}

					salesSum = Long.parseLong(srl[2]);
					//System.out.println(money);
					salesSum = salesSum + salesBranchMap.get(srl[0]);
					if(salesSum > 999999999){
						System.out.println("合計金額が10桁を超えました");
						return;
					}
					//System.out.println(money);
					salesBranchMap.put(srl[0],salesSum);

					salesSum = Long.parseLong(srl[2]);
					//System.out.println(money);
					salesSum = salesSum + salesCommodityMap.get(srl[1]);
					if(salesSum > 999999999){
						System.out.println("合計金額が10桁を超えました");
						return;
					}
					//System.out.println(money);
					salesCommodityMap.put(srl[1],salesSum);


				}catch(ArrayIndexOutOfBoundsException e){
					System.out.println(args[0] + rcdList2.get(n) + ".rcdのフォーマットが不正です");
					//System.out.println(e);
				}catch(IOException e){
					System.out.println("予期せぬエラーが発生しました");
				}finally{
					try{
						salesListBuffer.close();
					}catch(IOException e){
						System.out.println("予期せぬエラーが発生しました");
					}
				}
			}

		}else{
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}





		List<Map.Entry<String,Long>> entries =
				new ArrayList<Map.Entry<String,Long>>(salesBranchMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {

			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		BufferedWriter branchBufferedWriter = null;
		try{
			//FileWriter bFW = new FileWriter(branchFileWriter);
			branchBufferedWriter = new BufferedWriter(new FileWriter(branchFileWriter));

			for (Map.Entry<String, Long> entry : entries) {
				//System.out.println(entry.getKey()+ "," + branchFileMap.get(entry.getKey()) + ", " + entry.getValue());

				branchBufferedWriter.write(entry.getKey()+ "," + branchFileMap.get(entry.getKey()) + "," + entry.getValue() + sep);
			}

		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}finally{
			try{
				branchBufferedWriter.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
		}


		List<Map.Entry<String,Long>> entries2 =
				new ArrayList<Map.Entry<String,Long>>(salesCommodityMap.entrySet());
		Collections.sort(entries2, new Comparator<Map.Entry<String,Long>>() {

			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		BufferedWriter commodityBufferedWriter = null;
		try{

			commodityBufferedWriter = new BufferedWriter(new FileWriter(commodityFileWriter));

			for (Map.Entry<String, Long> entry : entries2) {
				//System.out.println(entry.getKey()+ "," + commodityFileMap.get(entry.getKey()) + ", " + entry.getValue());

				commodityBufferedWriter.write(entry.getKey()+ "," + commodityFileMap.get(entry.getKey()) + "," + entry.getValue() + sep);
			}

		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
		}finally{
			try{
				commodityBufferedWriter.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
			}
		}

	}

}
