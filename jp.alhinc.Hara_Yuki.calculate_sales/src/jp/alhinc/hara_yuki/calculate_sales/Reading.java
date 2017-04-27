package jp.alhinc.hara_yuki.calculate_sales;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class Reading {
	static void readingSystem(File file, Map<String,String> nameMap, Map<String,Long> salesMap, String t, String r){
		BufferedReader br = null;
		try{//中身見て処理
			br = new BufferedReader(new FileReader(file));
			String s;
			while((s = br.readLine()) != null){
				String[] FileArray = s.split(",");
				if(FileArray.length == 2  && FileArray[0].matches(t)){
					nameMap.put(FileArray[0], FileArray[1]);
					salesMap.put(FileArray[0], 0L);
				}else{
					System.out.println(r + "定義ファイルのフォーマットが不正です");
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