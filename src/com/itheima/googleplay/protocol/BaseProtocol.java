package com.itheima.googleplay.protocol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.List;

import com.itheima.googleplay.domain.AppInfo;
import com.itheima.googleplay.http.HttpHelper;
import com.itheima.googleplay.http.HttpHelper.HttpResult;
import com.itheima.googleplay.tools.FileUtils;
import com.lidroid.xutils.util.IOUtils;

public abstract class BaseProtocol<T> {
	public T load(int index) {
		// ���ر�������
		String json = loadLocal(index);
		if (json == null) {
			// ���������
			json = loadServer(index);
			if (json != null) {
				saveLocal(json, index);
			}
		}
		if (json != null) {
			return paserJson(json);
		} else {
			return null;
		}
	}


	private String loadServer(int index) {
		HttpResult httpResult = HttpHelper.get(HttpHelper.URL +getKey()
				+ "?index=" + index); 
		String json = httpResult.getString();
		return json;
	}
	private void saveLocal(String json, int index) {
		
		BufferedWriter bw = null;
		try {
			File dir=FileUtils.getCacheDir();
			//�ڵ�һ��дһ������ʱ�� 
			File file = new File(dir, getKey()+"_" + index); // /mnt/sdcard/googlePlay/cache/home_0
			FileWriter fw = new FileWriter(file);
			 bw = new BufferedWriter(fw);
			bw.write(System.currentTimeMillis() + 1000 * 100 + "");
			bw.newLine();// ����
			bw.write(json);// ������json�ļ���������
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(bw);
		}
	}

	private String loadLocal(int index) {
		//  ��������ļ��Ѿ������� �Ͳ�Ҫ��ȥ���û�����
		File dir=FileUtils.getCacheDir();// ��ȡ�������ڵ��ļ���
		File file = new File(dir, getKey()+"_" + index); 
		try {
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			long outOfDate = Long.parseLong(br.readLine());
			if(System.currentTimeMillis()>outOfDate){
				return null;
			}else{
				String str=null;
				StringWriter sw=new StringWriter();
				while((str=br.readLine())!=null){
				
					sw.write(str);
				}
				return sw.toString();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����json
	 * @param json
	 * @return
	 */
	public abstract T paserJson(String json);
	/**
	 * ˵���˹ؼ���
	 * @return
	 */
	public abstract String getKey();
}