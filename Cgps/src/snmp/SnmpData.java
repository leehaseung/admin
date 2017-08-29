package snmp;

public class SnmpData {

	public String svrNo;
	public String cpu1Load;
	public String cpu5Load;
	public String cpu15Load;

	public String userCpu;
	public String sysCpu;
	public String idleCpu;

	// static String totalSwap=".1.3.6.1.4.1.2021.4.3.0";
	public String availSwap;
	public String totalRam;
	public String usedRam;
	public String freeRam; // integer

	public String availDsik; // integer
	public String usedDisk; // integer

	public double ifHcin; // counter64
	public double ifHcout; // counter64
	
	private String sifHcin;
	private String sifHcout;
	
	public String sDate;
//평균데이터 산
	private String rsifHcin;
	private String rsifHcout;
	public String rsDate;
	
	
	public String getRsDate() {
		return rsDate;
	}

	public void setRsDate(String rsDate) {
		this.rsDate = rsDate;
	}

	public String getsDate() {
		return sDate;
	}

	public void setsDate(String sDate) {
		this.sDate = sDate;
	}

	public String getRsifHcin() {
		return rsifHcin;
	}

	public void setRsifHcin(String rsifHcin) {
		this.rsifHcin = rsifHcin;
	}

	public String getRsifHcout() {
		return rsifHcout;
	}

	public void setRsifHcout(String rsifHcout) {
		this.rsifHcout = rsifHcout;
	}

	public String getSifHcin() {
		return sifHcin;
	}

	public void setSifHcin(String sifHcin) {
		this.sifHcin = sifHcin;
	}

	public String getSifHcout() {
		return sifHcout;
	}

	public void setSifHcout(String sifHcout) {
		this.sifHcout = sifHcout;
	}

	public String getSvrNo() {
		return svrNo;
	}

	public void setSvrNo(String svrNo) {
		this.svrNo = svrNo;
	}

	public String getCpu1Load() {
		return cpu1Load;
	}

	public void setCpu1Load(String cpu1Load) {
		this.cpu1Load = cpu1Load;
	}

	public String getCpu5Load() {
		return cpu5Load;
	}

	public void setCpu5Load(String cpu5Load) {
		this.cpu5Load = cpu5Load;
	}

	public String getCpu15Load() {
		return cpu15Load;
	}

	public void setCpu15Load(String cpu15Load) {
		this.cpu15Load = cpu15Load;
	}

	public String getUserCpu() {
		return userCpu;
	}

	public void setUserCpu(String userCpu) {
		this.userCpu = userCpu;
	}

	public String getSysCpu() {
		return sysCpu;
	}

	public void setSysCpu(String sysCpu) {
		this.sysCpu = sysCpu;
	}

	public String getIdleCpu() {
		return idleCpu;
	}

	public void setIdleCpu(String idleCpu) {
		this.idleCpu = idleCpu;
	}

	public String getAvailSwap() {
		return availSwap;
	}

	public void setAvailSwap(String availSwap) {
		this.availSwap = availSwap;
	}

	public String getTotalRam() {
		return totalRam;
	}

	public void setTotalRam(String totalRam) {
		this.totalRam = totalRam;
	}

	public String getUsedRam() {
		return usedRam;
	}

	public void setUsedRam(String usedRam) {
		this.usedRam = usedRam;
	}

	public String getFreeRam() {
		return freeRam;
	}

	public void setFreeRam(String freeRam) {
		this.freeRam = freeRam;
	}

	public String getAvailDsik() {
		return availDsik;
	}

	public void setAvailDsik(String availDsik) {
		this.availDsik = availDsik;
	}

	public String getUsedDisk() {
		return usedDisk;
	}

	public void setUsedDisk(String usedDisk) {
		this.usedDisk = usedDisk;
	}

	public double getIfHcin() {
		return ifHcin;
	}

	public void setIfHcin(double ifHcin) {
		this.ifHcin = ifHcin;
		/*if(ifHcin > 1024*1024*1024){
		sifHcin =ifHcin / 1024*1024*1024 + "GB";
		}else if*/
		
	}

	public double getIfHcout() {
		return ifHcout;
	}

	public void setIfHcout(double ifHcout) {
		this.ifHcout = ifHcout;
	}

	public String getSDate() {
		return sDate;
	}

	public void setSDate(String sDate) {
		this.sDate = sDate;
	}

	@Override
	public String toString() {
		return "SnmpData [svrNo=" + svrNo + ", cpu1Load=" + cpu1Load + ", cpu5Load=" + cpu5Load + ", cpu15Load="
				+ cpu15Load + ", userCpu=" + userCpu + ", sysCpu=" + sysCpu + ", idleCpu=" + idleCpu + ", availSwap="
				+ availSwap + ", totalRam=" + totalRam + ", usedRam=" + usedRam + ", freeRam=" + freeRam
				+ ", availDsik=" + availDsik + ", usedDisk=" + usedDisk + ", ifHcin=" + ifHcin + ", ifHcout=" + ifHcout
				+ ", sifHcin=" + sifHcin + ", sifHcout=" + sifHcout + ", sDate=" + sDate + ", rsifHcin=" + rsifHcin
				+ ", rsifHcout=" + rsifHcout + ", rsDate=" + rsDate + "]";
	}

	
	

}
