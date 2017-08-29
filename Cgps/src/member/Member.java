package member;

public class Member {
	private String mbId;
	private String mbPass;
	private String mbName;
	private String mbBirth;
	private String mbAddress;
	private String mbEmail;
	private String mbPhone;
	private String mbRegdate;
	private String mbAdmin;
	private String mbSex;
	
	public String getMbId() {
		return mbId;
	}
	public void setMbId(String mbId) {
		this.mbId = mbId;
	}
	public String getMbPass() {
		return mbPass;
	}
	public void setMbPass(String mbPass) {
		this.mbPass = mbPass;
	}
	public String getMbName() {
		return mbName;
	}
	public void setMbName(String mbName) {
		this.mbName = mbName;
	}
	public String getMbBirth() {
		return mbBirth;
	}
	public void setMbBirth(String mbBirth) {
		this.mbBirth = mbBirth;
	}
	public String getMbAddress() {
		return mbAddress;
	}
	public void setMbAddress(String mbAddress) {
		this.mbAddress = mbAddress;
	}
	public String getMbEmail() {
		return mbEmail;
	}
	public void setMbEmail(String mbEmail) {
		this.mbEmail = mbEmail;
	}
	public String getMbPhone() {
		return mbPhone;
	}
	public void setMbPhone(String mbPhone) {
		this.mbPhone = mbPhone;
	}
	public String getMbRegdate() {
		return mbRegdate;
	}
	public void setMbRegdate(String mbRegdate) {
		this.mbRegdate = mbRegdate;
	}
	public String getMbAdmin() {
		return mbAdmin;
	}
	public void setMbAdmin(String mbAdmin) {
		this.mbAdmin = mbAdmin;
	}
	public String getMbSex() {
		return mbSex;
	}
	public void setMbSex(String mbSex) {
		this.mbSex = mbSex;
	}
	@Override
	public String toString() {
		return "Member [mbId=" + mbId + ", mbPass=" + mbPass + ", mbName=" + mbName + ", mbBirth=" + mbBirth
				+ ", mbAddress=" + mbAddress + ", mbEmail=" + mbEmail + ", mbPhone=" + mbPhone + ", mbRegdate="
				+ mbRegdate + ", mbAdmin=" + mbAdmin + ", mbSex=" + mbSex + "]";
	}
	
	
	
	
}
