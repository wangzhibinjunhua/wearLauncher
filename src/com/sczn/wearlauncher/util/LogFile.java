package com.sczn.wearlauncher.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.sczn.wearlauncher.Config;

import android.os.Environment;


public class LogFile {
	public static String PATH_LOGCAT;
	
	public static void stepLogCat(int step,boolean isError){
        if(Config.IS_DEBUG){
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logCat;
            if(isError){
                logCat = "EEEEEE:"+String.valueOf(step)+":"+sdf.format(time)+"\n";
            }else {
                logCat = String.valueOf(step)+":"+sdf.format(time)+"\n";
            }
            logCat(logCat);
        }
    }
	
	public static void logCatWithTime(String msg){
		if(Config.IS_DEBUG){
            long time = Calendar.getInstance().getTimeInMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String logCat;
            logCat = msg +":"+sdf.format(time)+"\n";
            logCat(logCat);
        }
	}

	public static void logCat(String msg){
		if(Config.IS_DEBUG){

			if(PATH_LOGCAT == null){
				if (Environment.getExternalStorageState().equals(
	                    Environment.MEDIA_MOUNTED)) {// ���ȱ��浽SD����
	                PATH_LOGCAT = Environment.getExternalStorageDirectory()
	                        .getAbsolutePath() + File.separator + "LogCatSave";
	            } else {// ���SD�������ڣ��ͱ��浽��Ӧ�õ�Ŀ¼��
	                PATH_LOGCAT = getExternalSdCardPath()
	                        + File.separator + "LogCatSave";
	            }
			}
			
			byte[] logBytes = msg.getBytes();
	        java.io.File file = new File(PATH_LOGCAT);
	        if (!file.exists()) {
	            file.mkdirs();
	        }
	        FileOutputStream out = null;
	        try {
	        	
	        	final String day = "day" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	            File ssr = new File(PATH_LOGCAT, day+ ".log");
	            if(!ssr.exists()){
	                try {
	                    ssr.createNewFile();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	            if(ssr.exists()){
	                out = new FileOutputStream(ssr,true);
	                out.write(logBytes);
	                out.close();
	            }
	        	
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	private static String getExternalSdCardPath() {

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }
	
	private static ArrayList<String> getDevMountList() {
        ArrayList<String> out = new ArrayList<String>();
        try {
            String fileInfos = readFile("/etc/vold.fstab");
            if (!StringUtils.isEmpty(fileInfos)) {
                String[] toSearch = fileInfos.split(" ");
                for (int i = 0; i < toSearch.length; i++) {
                    if (toSearch[i].contains("dev_mount")) {
                        if (new File(toSearch[i + 2]).exists()) {
                            out.add(toSearch[i + 2]);
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return out;
    }
	
	private static String readFile(String filePath) {
        String fileContent = "";
        File file = new File(filePath);
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file));
            reader = new BufferedReader(is);
            String line = null;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                fileContent += line + " ";
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileContent;
    }
}
