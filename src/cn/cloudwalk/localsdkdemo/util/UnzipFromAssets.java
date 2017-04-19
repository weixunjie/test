package cn.cloudwalk.localsdkdemo.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import android.content.Context;

/**
 * 解压assets的zip压缩文件到本地的sdcard根目录下
 * @author kongjing
 * @since 2016.2.18
 */
public class UnzipFromAssets{
	
	 /**   
     * DeCompress the ZIP to the path   
     * @param zipFileString  name of ZIP   
     * @param outPathString   path to be unZIP  
     * @throws Exception   
     */    
    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {    
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));    
        ZipEntry zipEntry;    
        String szName = "";    
        while ((zipEntry = inZip.getNextEntry()) != null) {    
            szName = zipEntry.getName();    
            if (zipEntry.isDirectory()) {    
                // get the folder name of the widget    
                szName = szName.substring(0, szName.length() - 1);    
                File folder = new File(outPathString + File.separator + szName);    
                folder.mkdirs();    
            } else {    
            
                File file = new File(outPathString + File.separator + szName);    
                file.createNewFile();    
                // get the output stream of the file    
                FileOutputStream out = new FileOutputStream(file);    
                int len;    
                byte[] buffer = new byte[1024];    
                // read (len) bytes into buffer    
                while ((len = inZip.read(buffer)) != -1) {    
                    // write (len) byte from buffer at the position 0    
                    out.write(buffer, 0, len);    
                    out.flush();    
                }    
                out.close();    
            }    
        }   
        inZip.close();    
    }  
	//流转换方法
	 public static byte[] InputStreamToByte(InputStream is) throws Exception {
	        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	        int ch;
	        while ((ch = is.read()) != -1) {
	            bytestream.write(ch);
	        }
	        byte imgdata[] = bytestream.toByteArray();
	        bytestream.close();
	        return imgdata;
	    }
	 
	 //判断目录内容是否相同
	  
}