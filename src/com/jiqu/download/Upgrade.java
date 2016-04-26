package com.jiqu.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

public class Upgrade {
	private String downloadUrl;
	private String path;
	private String fileName;
	
	public Upgrade(String downloadUrl,String path,String fileName){
		this.downloadUrl = downloadUrl;
		this.path = path;
		this.fileName = fileName;
	}
	
	public void startDownload(){
		new UpgradeTask().execute(downloadUrl,path,fileName);
	}

	class UpgradeTask extends AsyncTask<String, Integer, Float> {

		@Override
		protected Float doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream in = null;
			FileOutputStream out = null;
			HttpURLConnection connection = null;
			try {
				String savePath = params[1] + File.separator + params[2];
				File file = new File(savePath);
				Log.i("TAG", savePath);
				if (file.exists()) {
					file.delete();
				}
				
				URL url = new URL(params[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(10 * 1000);
				long size = connection.getContentLength();
				in = connection.getInputStream();
				out = new FileOutputStream(file);
				byte[] buffer = new byte[8 * 1024];
				long complete = 0;
				int length;
				while((length = in.read(buffer)) != -1){
					out.write(buffer,0,length);
					complete += length;
					publishProgress(new Integer((int) (complete  * 100 / size)));
				}
				if (size == complete) {
					Intent installIntent = new Intent(Intent.ACTION_VIEW);
					installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					String path = savePath;
					installIntent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
					AppUtil.getContext().startActivity(installIntent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.flush();
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (connection != null) {
					connection.disconnect();
				}
			}
			
			return null;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		@Override
		protected void onPostExecute(Float result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			Log.i("TAG", "Progress : " + values[0]);
		}

	}

}
