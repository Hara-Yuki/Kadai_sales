package jp.alhinc.hara_yuki.calculate_sales;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class Writing {
	static void writingSystem(Map<String,Long> salesMap, Map<String,String> nameMap, File file){
		List<Map.Entry<String,Long>> entries =
				new ArrayList<Map.Entry<String,Long>>(salesMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {

			public int compare(
					Entry<String,Long> entry1, Entry<String,Long> entry2) {
				return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
			}
		});

		BufferedWriter bw = null;
		try{
			//FileWriter bFW = new FileWriter(branchFileWriter);
			bw = new BufferedWriter(new FileWriter(file));

			for (Map.Entry<String, Long> entry : entries) {
				//System.out.println(entry.getKey()+ "," + branchFileMap.get(entry.getKey()) + ", " + entry.getValue());

				bw.write(entry.getKey()+ "," + nameMap.get(entry.getKey()) + "," + entry.getValue() + System.getProperty("line.separator"));
			}

		}catch (IOException e){
			System.out.println("予期せぬエラーが発生しました");
			System.exit(0);
		}finally{
			if(bw != null){
				try{
					bw.close();
				}catch(IOException e){
					System.out.println("予期せぬエラーが発生しました");
					System.exit(0);
				}
			}
		}
	}
}
