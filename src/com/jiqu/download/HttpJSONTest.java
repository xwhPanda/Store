package com.jiqu.download;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @date:
 */
public class HttpJSONTest {
    public static String loadJSON (String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                                        yc.getInputStream()));
            String inputLine = null;
            while ( (inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return json.toString();
    }
    
    
    
    private static   String file = "C:\\abc\\abc.txt";
    
  public static void  saveAsFileWriter(String content) {

   FileWriter fwriter = null;
   try {
    fwriter = new FileWriter(file);
    fwriter.write(content);
   } catch (IOException ex) {
    ex.printStackTrace();
   } finally {
    try {
     fwriter.flush();
     fwriter.close();
    } catch (IOException ex) {
     ex.printStackTrace();
    }
   }
  }
}


