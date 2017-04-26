package jp.alhinc.hara_yuki.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Reading {
	static void ReadingSystem(File file, Map<String,String> nameMap, Map<String,Long> salesMap, String t){
		BufferedReader br = null;//new BufferedReader(bf);

		try{//中身見て処理

			br = new BufferedReader(new FileReader(file));

			String s;
			while((s = br.readLine()) != null){
				String[] branchFileArray = s.split(",");

				//System.out.println(bl);
				//System.out.println(bfarray[0].matches("[0-9]{3}"));
				if(branchFileArray.length == 2  && branchFileArray[0].matches(t)){

					//System.out.println(bfarray[0] + bfarray[1]);
					nameMap.put(branchFileArray[0], branchFileArray[1]);
					//System.out.println(bfm.get(bfarray[0]) + "を取り込んだぞい");
					salesMap.put(branchFileArray[0], 0L);
					//System.out.println("ついでに" + bfarray[0] + "用の売上マップも作ったぞい");

				}else{
					System.out.println("支店定義ファイルのフォーマットが不正です");
					System.exit(0);
				}

			}

		}catch(IOException e){
			System.out.println("予期せぬエラーが発生しました");
			System.exit(0);
		}finally{
			try{
				br.close();
			}catch(IOException e){
				System.out.println("予期せぬエラーが発生しました");
				System.exit(0);
			}
		}
	}
}
