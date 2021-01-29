// Saca el md5 de un archivo
package connectorsnet;

import java.io.*;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MD5Checksum {

   public static byte[] createChecksum(String filename) throws Exception {
       InputStream fis =  new FileInputStream(filename);

       byte[] buffer = new byte[1024];
       MessageDigest complete = MessageDigest.getInstance("MD5");
       int numRead;
  
       do {
           numRead = fis.read(buffer);
           if (numRead > 0) {
               complete.update(buffer, 0, numRead);
           }
       } while (numRead != -1);

       fis.close();
       return complete.digest();
   }

   // see this How-to for a faster way to convert
   // a byte array to a HEX string
   public static String getMD5Checksum(String filename) {
       String result="";
       try {
           byte[] b = createChecksum(filename);
           for (int i=0; i < b.length; i++) {
               result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
           }
       } catch (Exception ex) {
           Logger.getLogger(MD5Checksum.class.getName()).log(Level.SEVERE, null, ex);
       }
       return result;
   }
   /*
    public static void main(String args[]) {
       try {
           System.out.println(getMD5Checksum("nuevo.txt"));
           
       }
       catch (Exception e) {
           e.printStackTrace();
       }
   }
*/
   
   
}
