package com.nest.currency.bean;

public class IpProxyBean {
	
	//ip地址
	private String ip;
	
	//端口
	private Integer port;
	
	//代理类型
	private String type;
    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "IpProxyBean [ip=" + ip + ", port=" + port + ", type=" + type + "]";
	}
 

}
