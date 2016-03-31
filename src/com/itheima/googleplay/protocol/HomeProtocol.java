package com.itheima.googleplay.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.itheima.googleplay.domain.AppInfo;
import com.itheima.googleplay.http.HttpHelper;
import com.itheima.googleplay.http.HttpHelper.HttpResult;
import com.itheima.googleplay.tools.FileUtils;
import com.lidroid.xutils.util.IOUtils;

public class HomeProtocol extends BaseProtocol<List<AppInfo>>{

	//  1 ������json�ļ�д��һ�������ļ���  **
	// 2  ��ÿ�����ݶ�ժ�����浽���ݿ��� 
	// ���������� ����JsonObject ,���������ž���JsonArray
	public List<AppInfo> paserJson(String json) {
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		try {
			JSONObject jsonObject=new JSONObject(json);
			JSONArray jsonArray = jsonObject.getJSONArray("list");
			for(int i=0;i<jsonArray.length();i++){
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				long id=jsonObj.getLong("id");
				String name = jsonObj.getString("name");
				String packageName=jsonObj.getString("packageName");
				String iconUrl = jsonObj.getString("iconUrl");
				float stars=Float.parseFloat(jsonObj.getString("stars"));
				long size=jsonObj.getLong("size");
				String downloadUrl = jsonObj.getString("downloadUrl");
				String des = jsonObj.getString("des");
				AppInfo info=new AppInfo(id, name, packageName, iconUrl, stars, size, downloadUrl, des);
				appInfos.add(info);
			}
			return appInfos;
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getKey() {
		return "home";
	}

	
}