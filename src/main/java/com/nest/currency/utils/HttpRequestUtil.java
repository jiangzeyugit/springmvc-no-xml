package com.nest.currency.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.nest.currency.bean.IpProxyBean;

public class HttpRequestUtil {
   static String[] agents= {"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60 Opera/8.0 (Windows NT 5.1; U; en)",
		   "Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
		   "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
		   "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
		   "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
		   "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
		   "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
		   "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
		   "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
		   "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
		   "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko"}; 
	
   public static void main(String[] args) throws Exception {
		Map<String,String> param=new HashMap<String,String>();
		param.put("appId","iteach");
		param.put("email","liliang@xdf.cn");
		param.put("sign","nRw0XPJimixTaJV2p/U135l6kV4=");
		String reString= HttpRequestUtil.getHttp("http://tmdmapi.staff.xdf.cn/1/common/teacher/email", param);
		System.out.println(reString);
	}

	/**
	 * @param path
	 * @param key
	 * @param value
	 * @param userAgent 是否开启用户代理
	 * @param proxy 是否开启ip代理
	 * @return
	 * @throws Exception 
	 */
	public static String getHttp(String path, Map<String, String> params,Boolean userAgentStatus,Boolean proxy) throws Exception {
		String info="本机请求";
		HttpURLConnection httpURLConnection = null;
		InputStream bis = null;
		ByteArrayOutputStream bos = null;
		IpProxyBean ipBean = null;
		try {
			if (params != null) {
				int i=0;
				for (String key : params.keySet()) {
					if (i==0) {
						path=path+"?"+key+"="+params.get(key);
					} else {
						path=path+"&"+key+"="+params.get(key);
					}
					i++;
				}
			}
			URL url = new URL(path);
			// 打开连接
			if(proxy) {
				ipBean =IpProxyPool.getIpProxyBean();
			    if(ipBean==null) {
			    	ipBean =IpProxyPool.getIpProxyBeanByOnline();
			    }
			    if (ipBean!=null) {
			    	 info="proxy请求";
			    	 System.out.println("[HTTP请求回复:] 获取到代理IP开始请求目标网址，代理IP:"+ipBean.toString());
			    	 Proxy proxyBean = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipBean.getIp(), ipBean.getPort()));	
					 httpURLConnection = (HttpURLConnection) url.openConnection(proxyBean);	
			    } else {
			    	info="本机请求";
			    	System.out.println("[HTTP请求回复:] 获取代理失败  通过本机IP请求。。。。。");
			    	httpURLConnection = (HttpURLConnection) url.openConnection();
			    }
			} else {
				httpURLConnection = (HttpURLConnection) url.openConnection();
			}
			httpURLConnection.setRequestProperty("content-Type", "text/html; charset=UTF-8");
			httpURLConnection.setRequestProperty("charet", "utf-8");
			if(userAgentStatus) {
				int index=(int)(Math.random()*agents.length);
				String userAgent = agents[index];
				httpURLConnection.setRequestProperty("User-Agent", userAgent);	
			}
		
			if (200 == httpURLConnection.getResponseCode()) {
				System.out.println("["+info+"HTTP请求回复:] 链接:"+path+" Code:"+httpURLConnection.getResponseCode());
				// 得到输入流
				bis = httpURLConnection.getInputStream();
				bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = bis.read(buffer))) {
					bos.write(buffer, 0, len);
					bos.flush();
				}
				if(ipBean!=null) {
					IpProxyPool.setIpProxyBean(ipBean);
				}
				return bos.toString("utf-8");
			} else if(301 == httpURLConnection.getResponseCode()){
				String path_rel=httpURLConnection.getHeaderField("Location");
				Thread.sleep(5000L);
				System.out.println("["+info+"HTTP请求回复:] 301  资源重定向。。。。。。");
				IpProxyPool.setIpProxyBean(ipBean);
				return HttpRequestUtil.getHttp(path_rel, null);
				
			} else if(302 == httpURLConnection.getResponseCode()){
				String path_rel=httpURLConnection.getHeaderField("Location");
				Thread.sleep(5000L);
				System.out.println("["+info+"HTTP请求回复:] 302  资源重定向。。。。。。。");
				IpProxyPool.setIpProxyBean(ipBean);
				return HttpRequestUtil.getHttp(path_rel, null);
				
			}
			
		} catch (Exception e) {
			System.out.println("["+info+"HTTP请求回复:] 请求异常");
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	
	/**
	 * @param path
	 * @param key
	 * @param value
	 * @return
	 * @throws Exception 
	 */
	public static String getHttp(String path, Map<String, String> params) throws Exception {
		HttpURLConnection httpURLConnection = null;
		InputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			if (params != null) {
				int i=0;
				for (String key : params.keySet()) {
					if (i==0) {
						path=path+"?"+key+"="+params.get(key);
					} else {
						path=path+"&"+key+"="+params.get(key);
					}
					i++;
				}
			}
			URL url = new URL(path);
			// 打开连接
			httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestProperty("content-Type", "text/html; charset=UTF-8");
			httpURLConnection.setRequestProperty("charet", "utf-8");
			int index=(int)(Math.random()*agents.length);
			String userAgent = agents[index];
			httpURLConnection.setRequestProperty("User-Agent", userAgent);
			if (200 == httpURLConnection.getResponseCode()) {
				System.out.println("[HTTP请求回复:]链接:"+path+" Code:"+httpURLConnection.getResponseCode());
				// 得到输入流
				bis = httpURLConnection.getInputStream();
				bos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while (-1 != (len = bis.read(buffer))) {
					bos.write(buffer, 0, len);
					bos.flush();
				}
				return bos.toString("utf-8");
			} else if(301 == httpURLConnection.getResponseCode()){
				String path_rel=httpURLConnection.getHeaderField("Location");
				Thread.sleep(5000L);
				System.out.println("[HTTP请求回复:]301  资源重定向");
				return HttpRequestUtil.getHttp(path_rel, null);
				
			} else if(302 == httpURLConnection.getResponseCode()){
				String path_rel=httpURLConnection.getHeaderField("Location");
				Thread.sleep(5000L);
				System.out.println("[HTTP请求回复:]302  资源重定向");
				return HttpRequestUtil.getHttp(path_rel, null);
				
			} else if (403 == httpURLConnection.getResponseCode()) {
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
	}
	
	
	 /**
     * POST请求获取数据
     */
    public static String postHttp(String path, String params){
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try {
            url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setRequestProperty("content-Type", "application/json");
            httpURLConnection.setRequestProperty("charset", "utf-8");
            httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            if(!StringUtils.isEmpty(params)){
                printWriter.write(params);//post的参数 xx=xx&yy=yy
            }
            // flush输出流的缓冲
            printWriter.flush();
            
            //开始获取数据
            bis = new BufferedInputStream(httpURLConnection.getInputStream());
            bos = new ByteArrayOutputStream();
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            return bos.toString("utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos != null){
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
