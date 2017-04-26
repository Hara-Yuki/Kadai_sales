package jp.alhinc.hara_yuki.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainSystem {
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		long salesSum;
		File branchFile = new File(args[0] , "branch.lst");
		File commodityFile = new File(args[0] , "commodity.lst");
		File folder = new File(args[0]);
		File branchFileWriter = new File(args[0] , "branch.out");
		File commodityFileWriter = new File(args[0] , "commodity.out");

		Map<String, String> branchFileMap = new HashMap<String, String>();
		Map<String, String> commodityFileMap = new HashMap<String, String>();
		Map<String, Long> salesBranchMap = new HashMap<String, Long>();
		Map<String, Long> salesCommodityMap = new HashMap<String, Long>();

		if(!branchFile.exists()) {//ファイル探し
			System.out.println("支店定義ファイルが存在しません");
			return;
		}

		Reading.ReadingSystem(branchFile, branchFileMap, salesBranchMap, "[0-9]{3}");

		if(!commodityFile.exists()) {
			System.out.println("商品定義ファイルが存在しません");
			return;
		}

		Reading.ReadingSystem(commodityFile, commodityFileMap, salesCommodityMap, "^[a-zA-Z0-9]{8}$");



		//System.out.println(bfm.entrySet());
		//System.out.println(cfm.entrySet());

		//String[] storege = folder.list();
		File[] failList = folder.listFiles();
		List<Integer> rcdList = new ArrayList<Integer>();
		List<String> rcdList2 = new ArrayList<String>();

		for(int i = 0; i < failList.length ;i++){
			//System.out.println(failList[i]);
			//System.out.println(failList[i].getName());
			//File fake = new File(args[0] + storege[i]);

			if(failList[i].isFile()){
				if(failList[i].isHidden()){
					continue;
				}


				if(failList[i].getName().matches("[0-9]{8}\\.rcd")){



					String[] rcdDate = failList[i].getName().split("\\.");
					//System.out.println(Arrays.toString(rcdDate));

					rcdList.add(Integer.valueOf(rcdDate[0]));
					rcdList2.add(rcdDate[0]);
				}else if(failList[i].getName().matches(".*\\.rcd")){
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}else if(failList[i].getName().matches(".*\\.rcd.*")){
					System.out.println("売上ファイル名が連番になっていません");
					return;
				}
			}


		}
		int max = rcdList.get(0), min = rcdList.get(0);
		for(int i = 0; i < rcdList.size(); i++ ){
			if(max < rcdList.get(i)){
				max = rcdList.get(i);
			}
			if(min > rcdList.get(i)){
				min = rcdList.get(i);
			}

		}
		if(rcdList.size() != max - min + 1){
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}

		BufferedReader br = null;


		for(int n = 0; n < rcdList.size(); n++){

			File salesList = new File(args[0] , rcdList2.get(n) + ".rcd");

			try{

				//FileReader sr = new FileReader(SR);
				br = new BufferedReader(new FileReader(salesList));
				String r;
				String[] srl = new String[3];
				int s = 0;
				//System.out.println(storege[i]  + "を呼び出したぞい");

				while((r = br.readLine()) != null){
					//System.out.println(r);
					srl[s] = r;
					//System.out.println(srl[s] + "をpから入力したぞい");
					s++;
				}

				if(srl[2] == null){
					System.out.println(rcdList2.get(n) + ".rcdのフォーマットが不正です");
					return;
				}else if(!branchFileMap.containsKey(srl[0])){
					System.out.println(rcdList2.get(n) + ".rcdの支店コードが不正です");
					return;
				}else if(!commodityFileMap.containsKey(srl[1])){
					System.out.println(rcdList2.get(n) + ".rcdの商品コードが不正です");
					return;
				}else if(!srl[2].matches("[0-9]*")){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}

				salesSum = Long.parseLong(srl[2]);
				//System.out.println(money);
				salesSum = salesSum + salesBranchMap.get(srl[0]);
				if(salesSum > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				//System.out.println(money);
				salesBranchMap.put(srl[0],salesSum);

				salesSum = Long.parseLong(srl[2]);
				//System.out.println(money);
				salesSum = salesSum + salesCommodityMap.get(srl[1]);
				if(salesSum > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				//System.out.println(money);
				salesCommodityMap.put(srl[1],salesSum);


			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println(rcdList2.get(n) + ".rcdのフォーマットが不正です");
				return;
				//System.out.println(e);
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
				return;
			}finally{
				if(br != null){
					try{
						br.close();
					}catch(IOException e){
						System.out.println("予期せぬエラーが発生しました");
						return;
					}
				}
			}
		}

		Writing.WritingSystem(salesBranchMap,  branchFileMap, branchFileWriter);
		Writing.WritingSystem(salesCommodityMap,  commodityFileMap, commodityFileWriter);

	}

}
