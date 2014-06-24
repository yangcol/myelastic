package com.cto.app.elasticsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class Main_Test_User {
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//ESClient es = new ESClient();
		//System.out.println(response.toString());
		System.out.println("Start Search");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in, "iso-8859-1"));

		String text = null;
		
		int pageNo = 0;
		while (true) {
			System.out.println("Input user name:");
			try {
				text = br.readLine();
				System.out.println("输入是" + text);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			List<ESUserContent> users = ESBaseModel.esclient.getUserByName(text, pageNo);
			for(ESUserContent user:users) {
				System.out.println("id:" + user.id + ", name:" + user.name);
			}
			System.out.println("Input user id:");
			try {
				text = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			users = ESBaseModel.esclient.getUserById(text, pageNo);
			for(ESUserContent user:users) {
				System.out.println("id:" + user.id + ", name:" + user.name);
			}
		}
	}
}
