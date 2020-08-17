package com.nest.currency.utils;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nest.currency.bean.IpProxyBean;


public class IpProxyPool {
	private static final String PROXYURL_HTTP = "https://www.xicidaili.com/wt/";
	private static final String PROXYURL_HTTPS = "https://www.xicidaili.com/wn/";
	private static final String PROXYURL_ALL = "http://www.89ip.cn/";
	private static Queue<IpProxyBean> ipQueue_http = new LinkedList<IpProxyBean>();
	private static Queue<IpProxyBean> ipQueue_https = new LinkedList<IpProxyBean>();
	private static Queue<IpProxyBean> ipQueue_all = new LinkedList<IpProxyBean>();
	public static IpProxyBean getIpProxyBean() {
		return ipQueue_all.poll();
	}

	public static Boolean setIpProxyBean(IpProxyBean ipBean) {
		return ipQueue_all.offer(ipBean);
	}

	/**
	 * ip代理池获取IP代理
	 * 
	 */
	public static IpProxyBean getIpProxyBean(String type) {
		if (type.toUpperCase().equals("HTTP") && IpProxyPool.ipQueue_http.size() > 0) {
			return ipQueue_http.poll();
		} else if (type.toUpperCase().equals("HTTPS") && IpProxyPool.ipQueue_https.size() > 0) {
			return ipQueue_https.poll();
		}
		return null;
	}

	public static boolean setIpProxyBean(IpProxyBean ipProxy, String type) {
		if (type.toUpperCase().equals("HTTP") && IpProxyPool.ipQueue_http.size() > 0) {
			return ipQueue_http.offer(ipProxy);
		} else if (type.toUpperCase().equals("HTTPS") && IpProxyPool.ipQueue_https.size() > 0) {
			return ipQueue_https.offer(ipProxy);
		} else {
			return false;
		}

	}

	/**
	 * 爬虫获取IP代理
	 * 
	 * @throws Exception
	 * 
	 */
	public static IpProxyBean getIpProxyBeanByOnline() throws Exception {
		System.out.println("[IP池回复:] 网络获取实时ip");
		String html = HttpRequestUtil.getHttp(PROXYURL_ALL, null);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select(".layui-table tbody tr");
		for (Element element : elements) {
			Elements elements_tr = element.getElementsByTag("td");
			if (elements_tr != null && elements_tr.size() > 2) {
				String ip = elements_tr.get(0).text();
				String port = elements_tr.get(1).text();
				IpProxyBean ipProxyBean = new IpProxyBean();
				ipProxyBean.setIp(ip);
				ipProxyBean.setPort(Integer.parseInt(port));
				ipProxyBean.setType("ALL");
				if (isValid(ipProxyBean)) {
					return ipProxyBean;
				}
			}
		}
		return null;
	}

	/**
	 * 通用代理池注入数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean allIpToQueue() throws Exception {

		return IpProxyByOnlineToQueueAll(ipQueue_all);
	}

	/**
	 * http代理池注入数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean httpIpToQueue() throws Exception {

		return IpProxyByOnlineToQueue(PROXYURL_HTTP, ipQueue_http);
	}

	/**
	 * 代理池注入数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean httpsIpToQueue() throws Exception {

		return IpProxyByOnlineToQueue(PROXYURL_HTTPS, ipQueue_https);
	}

	/**
	 * 代理池注入数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean IpProxyByOnlineToQueue(String url, Queue<IpProxyBean> ipQueue) throws Exception {
		String html = HttpRequestUtil.getHttp(url, null);
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("#ip_list .odd");
		for (Element element : elements) {
			Elements elements_tr = element.getElementsByTag("td");
			if (elements_tr != null && elements_tr.size() > 6) {
				String ip = elements_tr.get(1).text().trim();
				String port = elements_tr.get(2).text().trim();
				String type = elements_tr.get(5).text().trim();
				IpProxyBean ipProxyBean = new IpProxyBean();
				ipProxyBean.setIp(ip);
				ipProxyBean.setPort(Integer.parseInt(port));
				ipProxyBean.setType(type);
				if (isValid(ipProxyBean)) {
					ipQueue.offer(ipProxyBean);
				}
			}
		}
		if (ipQueue.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 检测代理ip是否有效
	 *
	 * @param ipBean
	 * @return
	 */
	public static boolean isValid(IpProxyBean ipBean) {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipBean.getIp(), ipBean.getPort()));
		try {

			URLConnection httpCon = new URL("https://www.baidu.com/").openConnection(proxy);
			httpCon.setRequestProperty("content-Type", "text/html; charset=UTF-8");
			httpCon.setRequestProperty("charet", "utf-8");
			httpCon.setConnectTimeout(5000);
			httpCon.setReadTimeout(5000);
			int code = ((HttpURLConnection) httpCon).getResponseCode();
			System.out.println("[IP池回复:]ip测试有效:" + ipBean.toString());
			return code == 200;
		} catch (Exception e) {
			System.out.println("[IP池回复:]ip测试无效:" + ipBean.toString());
		}
		return false;
	}

	/**
	 * 通用代理池注入数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static boolean IpProxyByOnlineToQueueAll(Queue<IpProxyBean> ipQueue) throws Exception {
		String href = PROXYURL_ALL;
		while (true) {
			String html = HttpRequestUtil.getHttp(href, null);
			if(StringUtils.isNotBlank(html)) {
				Document doc = Jsoup.parse(html);
				Elements page_next = doc.select(".layui-laypage-next");
				href = PROXYURL_ALL+page_next.get(page_next.size() - 1).attr("href");
				Elements elements = doc.select(".layui-table tbody tr");
				for (Element element : elements) {
					Elements elements_tr = element.getElementsByTag("td");
					if (elements_tr != null && elements_tr.size() > 2) {
						String ip = elements_tr.get(0).text().trim();
						String port = elements_tr.get(1).text().trim();
						IpProxyBean ipProxyBean = new IpProxyBean();
						ipProxyBean.setIp(ip);
						ipProxyBean.setPort(Integer.parseInt(port));
						ipProxyBean.setType("ALL");
						if (isValid(ipProxyBean)) {
							ipQueue.offer(ipProxyBean);
							System.out.println("[IP池回复：] 目前通用代理池代理数量："+ipQueue.size());
						}
					}
				}
				if (ipQueue.size() >= 5) {
					System.out.println("[IP池回复：]通用代理池注满。。。。。：）");
					break;
				}	
			}
		}

		if (ipQueue.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}
