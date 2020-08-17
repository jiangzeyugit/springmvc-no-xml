package com.nest.zerg.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.nest.currency.bean.InfoMsgBean;
import com.nest.currency.bean.ZergBean;
import com.nest.currency.bean.ZergPageActionBean;
import com.nest.currency.bean.ZergRouteActionBean;
import com.nest.currency.utils.HttpRequestUtil;
import com.nest.currency.utils.JsonUtil;

/**
 * 产生和维护实例爬虫的类
 * 
 **/
@Service
public class ZergNestService {
	private final static Logger log = LoggerFactory.getLogger(ZergNestService.class);
	// 当前运行的的爬虫集合
	private final ConcurrentHashMap<String, ScheduledFuture<Object>> zergMap = new ConcurrentHashMap<String, ScheduledFuture<Object>>(
			5);

	// 获取爬虫集合
	public Map<String, ScheduledFuture<Object>> getJobsMap() {
		return zergMap;
	}

	// 根据勾勒的爬虫画像信息实例生成爬虫
	public InfoMsgBean produceZergByzergId(ZergBean ZergInfo) {
		InfoMsgBean info = new InfoMsgBean();

		return info;
	}

	// 停止爬虫
	public InfoMsgBean stopZerg(String zergId, String zergName) {
		InfoMsgBean info = new InfoMsgBean();
		ScheduledFuture<Object> scheduledFuture = zergMap.get("zerg-" + zergId);
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			log.info(zergName + "任务停止");
		} else {
			log.info("当前无该任务");
		}
		return info;
	}

	// 启动爬虫
	@Async
	public InfoMsgBean startZerg(ZergBean zergInfo) {
		InfoMsgBean info = new InfoMsgBean();
		String zergName = zergInfo.getZergName();
		log.info("开始唤醒 " + zergName);
		List<ZergRouteActionBean> zergRouteActions = zergInfo.getRouteActions();
		try {
			// 遍历当前爬虫设定了多少行为
			log.info("【" + zergName + "回复】:任务行为数量：" + zergRouteActions.size());
			for (ZergRouteActionBean zergRoute : zergRouteActions) {
				String startUrl = zergRoute.getStartPageUrl();
				log.info("【" + zergName + "回复】:开始爬取路径: " + startUrl);
				while (true) {
					String html = HttpRequestUtil.getHttp(startUrl, null, true, true);
					log.info("【" + zergName + "回复】:开始页面解析 ");
					List<ZergPageActionBean> zergPageActions = zergRoute.getPageActions();
					// 遍历页面解析行为
					if (StringUtils.isNotBlank(html)) {
						Document doc = Jsoup.parse(html);
						for (ZergPageActionBean zergPageAction : zergPageActions) {
							String dataActionCodes = zergPageAction.getDataActionCodes();
							if (StringUtils.isNotBlank(dataActionCodes)) {
								Map<String, String> dataActionCodeList = JsonUtil.toBean(dataActionCodes, Map.class);
								// 解析行为语言dataActionCode 获取数据内容
								this.documentAnalysis(doc, dataActionCodeList);
							}
						}
						if (zergRoute.getActionStatus() == 1) {
							String nextPageCode = zergRoute.getNextPageUrlCode();

						} else {
							break;
						}
					} else {
						break;
					}

					Thread.sleep(5000L);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		return info;
	}

	// 解析dataActionCode获取数据 一组成员数据
	private List<Map<String, String>> documentAnalysis(Document doc, Map<String, String> dataActionCodeList) {
		for (Map.Entry<String, String> entry : dataActionCodeList.entrySet()) {
			Elements elements_doc = doc.children();
			// 获取选择器层级 a .dd #gg child(n); a .dd #gg child(n)
			String[] code_fs_arr = entry.getValue().split(";");
			for (int i = 0; i < code_fs_arr.length; i++) {
				// 获取选择器层级 a .dd #gg child(n)
				String[] code_fs = code_fs_arr[i].split("\\s+");
				if (elements_doc.size() <= 0) {
					return null;
				}
				// 获取选择器层级 a .dd #gg child(n)
				for (int y = 0; y < code_fs.length; y++) {
					String code_fs_str = code_fs[y].toLowerCase();
					if (code_fs_str.startsWith("child(")) {
						String child_index_str = code_fs[y].substring(code_fs[y].indexOf("(") + 1,
								code_fs[y].indexOf(")"));
						if (StringUtils.isNotBlank(child_index_str) && this.isInteger(child_index_str)) {
							int child_index = Integer.parseInt(child_index_str);
							Element element = elements_doc.get(0).child(child_index);
							elements_doc.clear();
							elements_doc.add(element);
						}

					} else if (code_fs_str.startsWith("get(")) {
						String child_index_str = code_fs[y].substring(code_fs[y].indexOf("(") + 1,
								code_fs[y].indexOf(")"));
						if (StringUtils.isNotBlank(child_index_str) && this.isInteger(child_index_str)) {
							int child_index = Integer.parseInt(child_index_str);
							Element element = elements_doc.get(child_index);
							elements_doc.clear();
							elements_doc.add(element);
						}
					} else if (code_fs_str.startsWith(".")) {
						elements_doc = elements_doc.select(code_fs_str);

					} else if (code_fs_str.startsWith("#")) {
						elements_doc = elements_doc.select(code_fs_str);

					} else if (!code_fs_str.equals("text") && !code_fs_str.startsWith("attr(")) {
						elements_doc = elements_doc.select(code_fs_str);

					} else if (code_fs_str.equals("text") || code_fs_str.startsWith("attr(")) {

					}
				}

				if (i == code_fs_arr.length - 1) {
					for (Element el : elements_doc) {
						String wantget = "";
						if (code_fs[code_fs.length - 1].toLowerCase().equals("text")) {
							wantget = elements_doc.get(0).text();
						} else if (code_fs[code_fs.length - 1].toLowerCase().startsWith("attr(")) {
							String attr_str = code_fs[i].substring(code_fs[i].indexOf("(") + 1,
									code_fs[i].indexOf(")"));
							wantget = elements_doc.get(0).attr(attr_str);
						}
					}

				}
			}

		}

		return null;
	}

	/**
	 * 数据的提取以及存储
	 * 
	 * 
	 */

	public boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

}
