package com.claridy.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class FileUpUtil {

	public static void main(String args[]){
		//得到要保存的路徑
		System.out.println(getFilePath("C:\\Users\\chentao\\workspaceForNTCL\\ntclportal\\src\\main\\webapp\\"));
	}
	public static String getFilePath(String path){
		//得到已存在的資料夾中應該保存的那個子資料夾
		path=path+"document\\";
		File file = new File(path);
		File[] fileList=file.listFiles();
		if(file.exists()){
			String zFileName="";
			int maxNum=0;
			for(int i=0;i<fileList.length;i++){
				//裡面有個文件desktop.ini
				if("desktop.ini".equals(fileList[i].getName())){
					continue;
				}
				zFileName=fileList[i].getName();
				if(maxNum<Integer.parseInt(zFileName.substring(zFileName.lastIndexOf("_")+1,zFileName.length()))){
					maxNum=Integer.parseInt(zFileName.substring(zFileName.lastIndexOf("_")+1,zFileName.length()));
				}
			}
			//判斷該資料夾的大小有沒有起過10G
			//獲得子資料夾的路徑
			String mainPath=path;
			path=path+zFileName;
			long fileSize=getFileSize(path);
			//當檔大於10時創建一個資料夾，返回創建的資料夾路徑
			if(fileSize>1*1024*1024){
				maxNum++;
				File myFilePath=new File(mainPath+"\\"+zFileName.substring(0,zFileName.lastIndexOf("_")+1)+(maxNum));
				if(myFilePath.exists()){//判斷新增的檔是否存在
					try{
					myFilePath.delete();
					}catch(Exception e){
						System.out.println("刪除資料夾失敗");
						e.printStackTrace();
					}
				}
				try{
					myFilePath.mkdir();
				}catch(Exception e){
					System.out.println("創建資料夾失敗");
					e.printStackTrace();
				}
				path=zFileName.substring(0,zFileName.lastIndexOf("_")+1)+maxNum;
			}
		}else{
			System.out.println("文件不存在");
		}
		return path;
	}
	
	public static long getFileSize(String path){
		File file= new File(path);
		//得到子資料夾中所有的檔
		File []fileList=file.listFiles();
		
		File softFile;
		long size=0;
		//遍歷子資料夾中的所有檔獲得檔的大小後加總
		for(int i=0;i<fileList.length;i++){
			if(fileList[i].isFile()){//判斷是否是檔
				softFile=new File(path+"\\"+fileList[i].getName());
				FileInputStream fis = null;
                try{
                    fis = new FileInputStream(softFile);  
                    size=size+fis.available()/1000;
                }catch(IOException e1){   
                    System.out.println("IO出錯！");
                }  
			}else{
				size=size+getFileSize(path+"\\"+fileList[i].getName());
			}
		}
		return size;
	}
	public static String getServerFile(String loginName,String fileName){
		return loginName+"_"+new Date().getTime()+"_"+Math.random()+fileName.substring(fileName.lastIndexOf("."),fileName.length());
	}
}
