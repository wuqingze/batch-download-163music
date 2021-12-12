import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;  
import java.io.*;
import java.util.Scanner;
import util.NetEaseUtil;
import java.util.Map;
import java.util.List;

public class APP{
    public static void main(String[] args) {
        if(args.length==0) {
            System.out.println("没有指定音乐文件...");
            return;
        }
        try{
            NetEaseUtil.batchMusic10(args[0]);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

