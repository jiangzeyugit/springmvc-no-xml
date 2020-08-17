package com.nest.currency.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * 爬虫画像
 * 
 * @author jiangzeyu3 @
 */
public class ZergBean {
	
	private String zergId;
	
	private String zergName;
	
	private Integer propagateNum;
	
	private Integer isPropagate;
	
	private Integer flagUse;
	
	private Integer isUsePool;
	
	private Integer isUseSleep;
	
	private Integer sleepTime;
	
	private Date updateTime;
	
	private Date createTime;
	
	private String createUserId;
	
	private String remarks;
	
	private List<ZergRouteActionBean> routeActions=new ArrayList<ZergRouteActionBean>();
    
	public List<ZergRouteActionBean> getRouteActions() {
		return routeActions;
	}

	public void setRouteActions(List<ZergRouteActionBean> routeActions) {
		this.routeActions = routeActions;
	}

	public String getZergId() {
		return zergId;
	}

	public void setZergId(String zergId) {
		this.zergId = zergId;
	}

	public String getZergName() {
		return zergName;
	}

	public void setZergName(String zergName) {
		this.zergName = zergName;
	}

	public Integer getPropagateNum() {
		return propagateNum;
	}

	public void setPropagateNum(Integer propagateNum) {
		this.propagateNum = propagateNum;
	}

	public Integer getIsPropagate() {
		return isPropagate;
	}

	public void setIsPropagate(Integer isPropagate) {
		this.isPropagate = isPropagate;
	}

	public Integer getFlagUse() {
		return flagUse;
	}

	public void setFlagUse(Integer flagUse) {
		this.flagUse = flagUse;
	}

	public Integer getIsUsePool() {
		return isUsePool;
	}

	public void setIsUsePool(Integer isUsePool) {
		this.isUsePool = isUsePool;
	}

	public Integer getIsUseSleep() {
		return isUseSleep;
	}

	public void setIsUseSleep(Integer isUseSleep) {
		this.isUseSleep = isUseSleep;
	}

	public Integer getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(Integer sleepTime) {
		this.sleepTime = sleepTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

}
