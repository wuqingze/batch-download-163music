package util;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import java.net.*;
import java.util.concurrent.TimeUnit;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;  
import java.util.zip.GZIPInputStream;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSON;  
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.io.*;

public class NetEaseUtil{

    private static JSONObject getJsonObj(String filename) throws Exception{
//        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
//        InputStream fis = classloader.getResourceAsStream("music-list.json");
        FileInputStream fis = new FileInputStream(new File(filename));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int n;
        while((n=fis.read(bytes)) != -1){
            buffer.write(bytes, 0, n);
        }
        bytes = buffer.toByteArray();
        String body = new String(bytes);
        JSONObject jsonObject = JSON.parseObject(body);   
        return jsonObject;
   }

   private static byte[] getMusic(int songid) throws Exception{
       String url = String.format("https://music.163.com/song/media/outer/url?id=%d",songid);
       OkHttpClient client = new OkHttpClient.Builder().build();
       Request request = new Request.Builder()
       .addHeader("Accept","*/*")
       .addHeader("Accept-Encoding","gzip, deflate, br")
       .addHeader("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
       .addHeader("Connection", "keep-alive")
       .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36Accept: */*")
       .addHeader("Accept-Encoding","gzip, deflate, br")
       .addHeader("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8,zh-TW;q=0.7")
       .addHeader("Connection","keep-alive")
       .url(url)
       .build();
       Response response = client.newCall(request).execute();
       return response.body().bytes();
    }

   public static void batchMusic10(String filename) throws Exception{
       JSONObject jsonobj = getJsonObj(filename);
       List<JSONObject> musiclist = (List<JSONObject>) ((JSONObject) jsonobj.get("playlist")).get("tracks");
       for(JSONObject entry : musiclist){
           String name = String.format("%s . %s.mp3", entry.get("name"), singerName((List<JSONObject>) entry.get("ar")));
           name = name.replaceAll("/", "&");
           Integer songid = (Integer) entry.get("id");
           System.out.println(String.format("%s  downloading... ", name));
           try{
           byte[] bytes = getMusic(songid);
           FileOutputStream ofs = new FileOutputStream(new File(name));
           ofs.write(bytes);
           ofs.close();
           }catch(Exception e) { System.out.println(String.format("歌曲%s下载失败, %s", name, e.getMessage()));}
       }
       System.out.println("所有歌曲下载完成");
   }

   private static String singerName(List<JSONObject> arr){
       String name = "";
       for(JSONObject obj: arr){
           name += obj.get("name")+" & ";
       }
       name = name.replaceAll("/", "&");
       if(name.length()>0) name = name.substring(0, name.length()-3);
       return name;
   }
}
