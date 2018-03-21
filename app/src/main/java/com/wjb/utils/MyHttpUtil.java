package com.wjb.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 网络连接的工具类
 *
 * @author xray
 */
public class MyHttpUtil {
    public final static String TAG = "MyHttpUtil";
    private final static int Conncet_Time_Out=5000;
    /**
     * 通过GET请求获得服务器的数据
     *
     * @param urlStr
     * @return
     */
    public static String get(String urlStr) {
        HttpURLConnection conn = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //打开连接
            conn = (HttpURLConnection) url.openConnection();
            //设置请求方法
            conn.setRequestMethod("GET");
            //设置网络连接超时
            conn.setConnectTimeout(Conncet_Time_Out);
            //获得输入流
            InputStream inputStream = conn.getInputStream();
            //定义字节数组作为缓冲
            byte[] buffer = new byte[1024];
            //每次读取的长度
            int len = 0;
            //StringBuilder用于拼接字符串
            StringBuilder builder = new StringBuilder();
            //循环从输入流中读取数据放入字节数组中
            while ((len = inputStream.read(buffer)) != -1) {
                //将字节数组转换为字符串
                String str = new String(buffer, 0, len);
                //将字符串拼接起来
                builder.append(str);
            }
            inputStream.close();
            //返回完整的字符串
            return builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
    /**
     * 通过POST请求获得服务器的数据
     *
     * @param urlStr
     * @return
     */
    public static String post(String urlStr, String args) {

        HttpURLConnection conn = null;
        try {
            //创建URL对象
            URL url = new URL(urlStr);
            //打开连接
            conn = (HttpURLConnection) url.openConnection();
            //设置请求方法POST
            conn.setRequestMethod("POST");
            //设置网络连接超时情况
            conn.setConnectTimeout(Conncet_Time_Out);
            //设置可以向服务器发送数据
            conn.setDoOutput(true);
            //获得输出流
            OutputStream outputStream = conn.getOutputStream();
            //将参数从后台发送给服务器
            outputStream.write(args.getBytes());
            outputStream.flush();
            outputStream.close();
            //获得输入流
            InputStream inputStream = conn.getInputStream();
            //定义字节数组作为缓冲
            byte[] buffer = new byte[1024];
            //每次读取的长度
            int len = 0;
            //StringBuilder用于拼接字符串
            StringBuilder builder = new StringBuilder();
            //循环从输入流中读取数据放入字节数组中
            while ((len = inputStream.read(buffer)) != -1) {
                //将字节数组转换为字符串
                String str = new String(buffer, 0, len);
                //将字符串拼接起来
                builder.append(str);
            }
            inputStream.close();
            //返回完整的字符串
            return builder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

//    public static String post(String urlStr,String args){
//
//        String result = "";
//        HttpClient mHttpClient = new DefaultHttpClient();
//        HttpPost mHttpPost = new HttpPost(urlStr);
//
//        List<NameValuePair> params = new ArrayList<>();
//        char[] mChar = args.toCharArray();
//        int length = args.length();
//        int i = 0, j = 0;
//        while (i < length && j < length) {
//
//            while (mChar[j] != '=')
//                j++;
//            String key = args.substring(i, j);
//
//            i = j + 1;
//            j = i;
//
//            while (j < length && mChar[j] != '&')
//                j++;
//
//            String value = args.substring(i, j);
//
//            params.add(new BasicNameValuePair(key, value));
//            i = j + 1;
//            j = i;
//        }
//
//        Log.e(TAG, params.toString());
//        try {
//            mHttpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
//            HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
//            if(mHttpResponse.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
//                HttpEntity entity = mHttpResponse.getEntity();
//
//                Log.e(TAG, entity.toString());
//                if(entity!=null)
//                {
//                    result+= EntityUtils.toString(entity);
//                }
//                else{
//                    mHttpPost.abort();
//                }
//            }
//            else{
//                Log.e(TAG, "null");
//                return result;
//            }
//        } catch (UnsupportedEncodingException pE) {
//            pE.printStackTrace();
//        } catch (ClientProtocolException pE) {
//            pE.printStackTrace();
//        } catch (IOException pE) {
//            pE.printStackTrace();
//        }
//        finally {
//            Log.e(TAG, "finally");
//            mHttpClient.getConnectionManager().shutdown();
//            return result;
//        }
//    }
}

