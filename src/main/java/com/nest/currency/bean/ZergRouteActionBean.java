package com.nest.currency.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ZergRouteActionBean {

	private String actId;

	private String zergId;

	private String startPageUrl;

	private Integer actionStatus;

	private Integer pageNo;

	private String nextPageUrlCode;

	private Date createTime;

	private Integer resultMode;

	private List<ZergPageActionBean> pageActions = new ArrayList<ZergPageActionBean>();

	public List<ZergPageActionBean> getPageActions() {
		return pageActions;
	}

	public void setPageActions(List<ZergPageActionBean> pageActions) {
		this.pageActions = pageActions;
	}

	public String getActId() {
		return actId;
	}

	public void setActId(String actId) {
		this.actId = actId;
	}

	public String getZergId() {
		return zergId;
	}

	public void setZergId(String zergId) {
		this.zergId = zergId;
	}

	public String getStartPageUrl() {
		return startPageUrl;
	}

	public void setStartPageUrl(String startPageUrl) {
		this.startPageUrl = startPageUrl;
	}

	public Integer getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(Integer actionStatus) {
		this.actionStatus = actionStatus;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public String getNextPageUrlCode() {
		return nextPageUrlCode;
	}

	public void setNextPageUrlCode(String nextPageUrlCode) {
		this.nextPageUrlCode = nextPageUrlCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getResultMode() {
		return resultMode;
	}

	public void setResultMode(Integer resultMode) {
		this.resultMode = resultMode;
	}

}
