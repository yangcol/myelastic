package com.cto.app.elasticsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main_Test_Resource {
	public static void main(String[] args) {
		
		//ESClient es = new ESClient();
		//System.out.println(response.toString());
		System.out.println("Start Search");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String text = null;
		
		int pageNo = 0;
		while (true) {
			System.out.println("Input resource name，输入资源名称:");
			try {
				text = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			List<ESResourceContent> resources = ESBaseModel.esclient.getResourceByName(text, pageNo);
			for(ESResourceContent resource:resources) {
				System.out.println("id:" + resource.id + ", name:" + resource.name);
			}
			System.out.println("Input resource id:");
			try {
				text = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			resources = ESBaseModel.esclient.getResourceById(text, pageNo);
			for(ESResourceContent resource:resources) {
				System.out.println("id:" + resource.id + ", name:" + resource.name);
			}
		}
	}
}
