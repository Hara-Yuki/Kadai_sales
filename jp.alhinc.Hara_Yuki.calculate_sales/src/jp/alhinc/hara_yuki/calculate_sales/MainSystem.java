package jp.alhinc.hara_yuki.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainSystem {
	public static void main(String[] args){
		if(args.length != 1){
			System.out.println("予期せぬエラーが発生しました");
			return;
		}
		File branchFile = new File(args[0] , "branch.lst");
		Map<String, String> branchFileMap = new HashMap<String, String>();
		Map<String, Long> salesBranchMap = new HashMap<String, Long>();
		Map<String, Long> salesCommodityMap = new HashMap<String, Long>();
		if(!branchFile.exists()) {//ファイル探し
			System.out.println("支店定義ファイルが存在しません");
			return;
		}
		Reading.readingSystem(branchFile, branchFileMap, salesBranchMap, "[0-9]{3}","支店");
		File commodityFile = new File(args[0] , "commodity.lst");
		if(!commodityFile.exists()) {
			System.out.println("商品定義ファイルが存在しません");
			return;
		}
		Map<String, String> commodityFileMap = new HashMap<String, String>();
		Reading.readingSystem(commodityFile, commodityFileMap, salesCommodityMap, "^[a-zA-Z0-9]{8}$", "商品");
		File folder = new File(args[0]);
		File[] failList = folder.listFiles();
		List<Integer> rcdList = new ArrayList<Integer>();
		List<String> rcdList2 = new ArrayList<String>();
		for(int i = 0; i < failList.length ;i++){
			if(failList[i].isFile() && failList[i].getName().matches("^[0-9]{8}.rcd$")){
				String[] rcdDate = failList[i].getName().split("\\.");
				rcdList.add(Integer.valueOf(rcdDate[0]));
				rcdList2.add(rcdDate[0]);
			}
		}
		Collections.sort(rcdList);
		int min = rcdList.get(0);
		int max = rcdList.get(rcdList.size() - 1);
		if(rcdList.size() != max - min + 1){
			System.out.println("売上ファイル名が連番になっていません");
			return;
		}
		BufferedReader br = null;
		for(int n = 0; n < rcdList.size(); n++){
			File salesList = new File(args[0] , rcdList2.get(n) + ".rcd");
			try{
				br = new BufferedReader(new FileReader(salesList));
				String r;
				List<String> srl = new ArrayList<String>();
				while((r = br.readLine()) != null){
					srl.add(r);
				}
				if(srl.size() != 3){
					System.out.println(rcdList2.get(n) + ".rcdのフォーマットが不正です");
					return;
				}
				if(!branchFileMap.containsKey(srl.get(0))){
					System.out.println(rcdList2.get(n) + ".rcdの支店コードが不正です");
					return;
				}
				if(!commodityFileMap.containsKey(srl.get(1))){
					System.out.println(rcdList2.get(n) + ".rcdの商品コードが不正です");
					return;
				}
				if(!srl.get(2).matches("[0-9]*")){
					System.out.println("予期せぬエラーが発生しました");
					return;
				}
				long salesSum;
				salesSum = Long.parseLong(srl.get(2));
				salesSum = salesSum + salesBranchMap.get(srl.get(0));
				if(salesSum > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				salesBranchMap.put(srl.get(0), salesSum);
				salesSum = Long.parseLong(srl.get(2));
				salesSum = salesSum + salesCommodityMap.get(srl.get(1));
				if(salesSum > 9999999999L){
					System.out.println("合計金額が10桁を超えました");
					return;
				}
				salesCommodityMap.put(srl.get(1),salesSum);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println(rcdList2.get(n) + ".rcdのフォーマットが不正です");
				return;
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
		File branchFileWriter = new File(args[0] , "branch.out");
		File commodityFileWriter = new File(args[0] , "commodity.out");
		Writing.writingSystem(salesBranchMap,  branchFileMap, branchFileWriter);
		Writing.writingSystem(salesCommodityMap,  commodityFileMap, commodityFileWriter);
	}

}