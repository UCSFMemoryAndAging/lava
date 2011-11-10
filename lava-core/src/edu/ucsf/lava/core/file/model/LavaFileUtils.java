package edu.ucsf.lava.core.file.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility methods for files.
 * @author jhesse
 *
 */
public class LavaFileUtils {

	public static final String DEFAULT_HASH_METHOD = "SHA-256";
	
	/**
	 * Return the name part (no extension) of the name
	 * @param name
	 * @return
	 */
	public static String getFileNamePart(String name){
		String fileNamePart;
		int dotIndex = name.lastIndexOf ( '.' );
		if ( dotIndex >= 0 ){
			fileNamePart = name.substring( 0, dotIndex );
		   return fileNamePart;
		}else{
			return name;
		}
		
	}
	
	/**
	 * Return the extension portion of file name. 
	 * @param name
	 * @return
	 */
	public static String getFileExtension(String name){
			String ext;
			int dotIndex = name.lastIndexOf ( '.' );
			if ( dotIndex >= 0 ){
			   ext = name.substring( dotIndex + 1 );
			   }
			else{
			   ext = "";
			}
			return ext;
		}
	
	/**
	 * Generate a checksum for the data using the supplied hash method.
	 * @param data
	 * @param hashMethod
	 * @return
	 */
	public static String getChecksum(byte[] data,String hashMethod){
		 try{
			 MessageDigest md = MessageDigest.getInstance(hashMethod);
			 md.update(data);
			 byte[] mdbytes = md.digest();
			 //convert the byte to hex format method 1
		     StringBuffer sb = new StringBuffer();
		     for (int i = 0; i < mdbytes.length; i++) {
		        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		     }
		     return sb.toString();
		 }catch(NoSuchAlgorithmException e){
			throw new RuntimeException(e);
		 }
		
		
	}
	
	public static String getChecksum(byte[] data){
		return getChecksum(data,LavaFileUtils.DEFAULT_HASH_METHOD);
	}
	
}
