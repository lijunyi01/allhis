package com.allhis.toolkit;

import org.springframework.context.support.ResourceBundleMessageSource;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ljy on 15/6/18.
 * ok
 */
public class GlobalTools {

    private static ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();


//    生成随机字符串
    public static String getRandomString(int length,boolean numberflag) { //length表示生成字符串的长度;numberflag表示是否纯数字
        String base = "";
        if(numberflag){
            base = "0123456789";
        }else {
            base = "abcdefghijklmnopqrstuvwxyz0123456789";
        }
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 格式化时间
     * Locale是设置语言敏感操作
     * @param formatTime
     * @return
     */
    public static String getTimeStampNumberFormat(Timestamp formatTime) {
//        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss", new Locale("zh", "cn"));
        SimpleDateFormat m_format = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        return m_format.format(formatTime);
    }

    /**
     * 计算两个日期的时间差
     * @param formatTime1
     * @param formatTime2
     * @return
     * 大的时间在前，得到的时间差的单位是秒
     */
    public static long getTimeDifference(Timestamp formatTime1, Timestamp formatTime2) {
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
        long t1 = 0L;
        long t2 = 0L;
        try {
            t1 = timeformat.parse(getTimeStampNumberFormat(formatTime1)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            t2 = timeformat.parse(getTimeStampNumberFormat(formatTime2)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//因为t1-t2得到的是毫秒级,所以要初3600000得出小时.算天数或秒同理
//        int hours=(int) ((t1 - t2)/3600000);
//        int minutes=(int) (((t1 - t2)/1000-hours*3600)/60);
//        int second=(int) ((t1 - t2)/1000-hours*3600-minutes*60);
//        return ""+hours+"小时"+minutes+"分"+second+"秒";
        return (t1-t2)/1000;
    }

    // 获得当前时间之前多少秒的时间;由long到timeStamp
    public static Timestamp getTimeBefore(long seconds){
        Timestamp ret= null;
        long ct = 0L;
        long bt = 0L;
        ct = System.currentTimeMillis();
        bt = ct - seconds*1000;   //毫秒
        ret = new Timestamp(bt);
        return ret;
    }

//    获取ip前三段;对于ipv6，就是去除最后一段，分隔符是：
    public static String getIpSegment(String ip){
        String ret="";
        if(ip.indexOf(".")>0) {
            ret = ip.substring(0, ip.lastIndexOf("."));
        }else if(ip.indexOf(":")>0){
            ret = ip.substring(0,ip.lastIndexOf(":"));
        }else{
            ret = ip;
        }
        return  ret;
    }

//    获取国际化信息内容
    public static String getMessageByLocale(String area,String key){
        resourceBundleMessageSource.setBasename("resource");

        String ret = "";
        Object[] params = {""};
        Locale locale = null;
        if(area.equals("en")){
            locale = Locale.US;
        }else{
            locale = Locale.CHINA;
        }
        ret = resourceBundleMessageSource.getMessage(key,params,locale);
        return  ret;
    }

//    字符串转数字,-10000表示转换失败
    public static int convertStringToInt(String sValue) {
        int iValue = -1000000000;
        if(isNumeric(sValue)) {
            try {
                iValue = Integer.parseInt(sValue);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return iValue;
    }

    public static long convertStringToLong(String sValue) {
        long lValue = -1000000000;
        if(isNumeric(sValue)) {
            try {
                lValue = Long.parseLong(sValue);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return lValue;
    }

    public static Map<String,String> parseInput(String input){
        Map<String,String> ret = new HashMap<String,String>();
        String[] a = input.split("\\<\\[CDATA\\]\\>");
        for(String str:a){
            //第二个参数表示做多分成几部分；默认不限制；如果是1则不分割，返回整个输入的串
            String[] str_ = str.split("=",2);
            if(str_.length==1){
                //形如：username=
                ret.put(str_[0],"");
            }else {
                ret.put(str_[0], str_[1]);
            }
        }
        return ret;
    }

    //更通用的方法，把分割符作参数
    public static Map<String,String> parseInput(String input,String splitString){
        Map<String,String> ret = new HashMap<String,String>();
        String[] a = input.split(splitString);
        for(String str:a){
            String[] str_ = str.split("=",2);
            if(str_.length==1){
                //形如：username=
                ret.put(str_[0],"");
            }else {
                ret.put(str_[0], str_[1]);
            }
        }
        return ret;
    }

    //判断字符串是否是数字形式（可转为数值型）
    public static boolean isNumeric(String number_s){
        boolean ret = false;
        if(number_s!=null) {
            //通过正则表达式判断;修改正则表达式可以做到判断正数，负数等
            Pattern pattern = Pattern.compile("^-?[0-9]+");
            Matcher isNum = pattern.matcher(number_s);
            if (isNum.matches()) {
                ret = true;
            }
        }
        return ret;
    }

    //判断String型参数中是否有null,如有则返回true;使用不定长参数
    public static boolean stringParamHasNull(String ...args){
        boolean ret = false;
        for(String arg:args){
            if(arg == null){
                ret = true;
                break;
            }
        }
        return ret;
    }

    //判断String型参数中是否有null或者"",如有则返回true;使用不定长参数
    public static boolean stringParamHasNullOrEmpty(String ...args){
        boolean ret = false;
        for(String arg:args){
            if(arg == null){
                ret = true;
                break;
            }else{
                if(arg.equals("")){
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    //base64解码；返回“”表示解码失败
    public static String base64Decode(String input){
        String ret = "";
        if(input != null){
            try {
                byte[] asBytes = Base64.getDecoder().decode(input);
                ret = new String(asBytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return ret;
    }

    //base64编码；返回""表示编码失败
    public static String base64Eecode(String input){
        String ret = "";
        if(input != null){
            try {
                ret = Base64.getEncoder().encodeToString(input.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return ret;
    }

    public static String getEmailAddr(String line){
        String regex = "([\\w-.]+@[\\w-\\.]+\\.[\\w]{2,4})";
        Pattern pt = Pattern.compile(regex);
        Matcher m = pt.matcher(line);
        if (m.find()) {
            return m.group(1).toLowerCase();
        }
        return null;
    }
    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        String emaillink = "^([a-z0-9A-Z]+[_|\\-|\\.]?)*[a-z0-9A-Z]+@([\\w]+(-[a-z0-9A-Z]+)*\\.)+[a-zA-Z]{2,}$";
        return email.matches(emaillink);
    }

    // 10.1.30.195
    public static boolean isIPV4(String ip) {
        if (ip == null) {
            return false;
        }
        String ipPattern = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
        return ip.matches(ipPattern);
    }


    public static void getFileList(List<String> list,String dirPath,boolean allFileFlag){
        File dir = new File(dirPath);
        if(dir !=null && dir.exists()) {
            File[] files = dir.listFiles();
            if(files !=null){
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        //递归
                        getFileList(list,files[i].getAbsolutePath(),allFileFlag);
                    } else {
                        String absoluteFilePath = files[i].getAbsolutePath();
                        String fileName = files[i].getName();
                        if(fileName!=null && fileName.length()>0) {
                            if(allFileFlag) {
                                list.add(absoluteFilePath);
                            }else{
                                //排除隐含文件即.打头的文件
                                if(!fileName.startsWith(".")){
                                    list.add(absoluteFilePath);
                                }
                            }
                        }
                    }
                }
            }else{
                //logger.error("files is null!");
            }
        }else{
            //logger.error("dir "+dirPath+" not exists!");
        }
    }

    public static String readFileToString(String filePath,boolean lineSeparatorFlag){
        String ret = "";
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
            String str = "";
            while ((str = reader.readLine())!=null){
                if(lineSeparatorFlag) {
                    if(ret.equals("")){
                        ret = ret + str;
                    }else{
                        ret = ret + System.getProperty("line.separator") + str;
                    }

                }else{
                    ret = ret + str;
                }
            }
            reader.close();
        }catch (Exception e){
            //log.info("file:"+ filePath +"not found!");
            e.printStackTrace();
        }
        return ret;
    }

    //md5算法
    public static String md5(String str) throws NoSuchAlgorithmException,IllegalArgumentException{
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException(
                    "String to encript cannot be null or zero length");
        }

        StringBuffer hexString = new StringBuffer();

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        byte[] hash = md.digest();

        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        return hexString.toString();
    }

//    public static void main(String[] args){
//        1.测试随机串
//        String rs = GlobalTools.getRandomString(6,true);
//        System.out.print("测试随机串:"+rs);

//        2.测试时间差
//        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//        System.out.print("timestamp now:" + currentTime);
//
//        try {
//            sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Timestamp currentTime2 = new Timestamp(System.currentTimeMillis());
//        System.out.print("timestamp now:" + currentTime2);
//
//        System.out.print("timestamp diff:");
//
//        System.out.println(getTimeDifference(currentTime2, currentTime));

//        测试获取ip前三段
//        String ip = "192.168.0.112";
//        System.out.print(getIpSegment(ip));

//        测试通过错误码获取错误信息
//        String s = getMessageByLocale("cn","-2");
//        System.out.print(s);

//        测试解析通用输入参数
//        String input="username=tt=asdf<[CDATA]>pass=111";
//        Map<String,String> map = parseInput(input);
//        String pass = map.get("pass1");
//        System.out.print(pass);
//        System.out.print(map);

//        测试数字判断
//        String s=null;
//        if(isNumeric(s)){
//            System.out.print("is number!");
//        }

//        String input = "sub|sdfa|dsafasd||cont|cot";
//        String[] a = input.split("\\|\\|");
//        String b = a[0];

//        测试函数paramHasNull
//        String a="";
//        String b=null;
//        if(!paramHasNull(a)){
//            System.out.print("param has null");
//        }
//        String str = "李俊轶 <lijunyi@allcomchina.com>";
//        String email = getEmailAddr(str);
//        System.out.print(email);

//        测试getFileList
//        List<String> fileList = new ArrayList<String>();
//        getFileList(fileList,"/Users/ljy/applog",false);
//        for(String absoluteFilePath:fileList){
//            int pos = absoluteFilePath.lastIndexOf("/");
//            String fileName = absoluteFilePath.substring(pos+1,absoluteFilePath.length());
//            String fileDir = absoluteFilePath.substring(0, pos);
//            System.out.print("sds");
//        }

//    }
}
