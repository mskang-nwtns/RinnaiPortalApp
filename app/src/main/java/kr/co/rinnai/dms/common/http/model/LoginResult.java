package kr.co.rinnai.dms.common.http.model;


/**
 * 
 * @author mini3248
 * 로그인 결과
 */
public class LoginResult {

	private String password = null;
	private String email = null;
	private String gwMail = null;


	private String userNo = null;
	private String userName = null;
	private String codeDept = null;
	private String deptName = null;


	private String loginType;
	private String passType;

	private String appVersion = null;


	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGwMail() {
		return gwMail;
	}
	public void setGwMail(String gwMail) {
		this.gwMail = gwMail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getLoginType() {
		return loginType;
	}
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}
	public String getPassType() {
		return passType;
	}
	public void setPassType(String passType) {
		this.passType = passType;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}

