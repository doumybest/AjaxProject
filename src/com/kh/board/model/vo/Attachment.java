package com.kh.board.model.vo;

import java.sql.Date;

public class Attachment {

	private int fileNO;
	private int refNo;
	private String originName;
	private String changeName;
	private String filePath;
	private Date uploadDate;
	private int fileLevel; // 사진게시판에 필요
	private String status;
	
	public Attachment() {
		
	}

	public Attachment(int fileNO, int refNo, String originName, String changeName, String filePath, Date uploadDate,
			int fileLevel, String status) {
		super();
		this.fileNO = fileNO;
		this.refNo = refNo;
		this.originName = originName;
		this.changeName = changeName;
		this.filePath = filePath;
		this.uploadDate = uploadDate;
		this.fileLevel = fileLevel;
		this.status = status;
	}

	public int getFileNO() {
		return fileNO;
	}

	public void setFileNO(int fileNO) {
		this.fileNO = fileNO;
	}

	public int getRefNo() {
		return refNo;
	}

	public void setRefNo(int refNo) {
		this.refNo = refNo;
	}

	public String getOriginName() {
		return originName;
	}

	public void setOriginName(String originName) {
		this.originName = originName;
	}

	public String getChangeName() {
		return changeName;
	}

	public void setChangeName(String changeName) {
		this.changeName = changeName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getFileLevel() {
		return fileLevel;
	}

	public void setFileLevel(int fileLevel) {
		this.fileLevel = fileLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Attachment [fileNO=" + fileNO + ", refNo=" + refNo + ", originName=" + originName + ", changeName="
				+ changeName + ", filePath=" + filePath + ", uploadDate=" + uploadDate + ", fileLevel=" + fileLevel
				+ ", status=" + status + "]";
	}
	
	
	
	
	
}
