package com.jiqu.object;

import java.io.Serializable;

import com.jiqu.database.Account;

public class AccountInformation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String uid;
	private String username;
	private String nickname;
	private int gender;
	private String phone;
	private String qq;
	private String photo;
	private String brithday;
	private String level;
	private String email;
	
	public synchronized String getUid() {
		return uid;
	}
	public synchronized void setUid(String uid) {
		this.uid = uid;
	}
	public synchronized String getUsername() {
		return username;
	}
	public synchronized void setUsername(String username) {
		this.username = username;
	}
	public synchronized String getNickname() {
		return nickname;
	}
	public synchronized void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public synchronized int getGender() {
		return gender;
	}
	public synchronized void setGender(int gender) {
		this.gender = gender;
	}
	public synchronized String getPhone() {
		return phone;
	}
	public synchronized void setPhone(String phone) {
		this.phone = phone;
	}
	public synchronized String getQq() {
		return qq;
	}
	public synchronized void setQq(String qq) {
		this.qq = qq;
	}
	public synchronized String getPhoto() {
		return photo;
	}
	public synchronized void setPhoto(String photo) {
		this.photo = photo;
	}
	public synchronized String getBrithday() {
		return brithday;
	}
	public synchronized void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	public synchronized String getLevel() {
		return level;
	}
	public synchronized void setLevel(String level) {
		this.level = level;
	}
	public synchronized String getEmail() {
		return email;
	}
	public synchronized void setEmail(String email) {
		this.email = email;
	}
	
	public static Account toAccount(AccountInformation info,String photoUrl){
		Account account = new Account();
		account.setBirthday(info.getBrithday());
		account.setEmail(info.getEmail());
		account.setGender(info.getGender());
		account.setLevel(info.getLevel());
		account.setNickname(info.getNickname());
		account.setPhone(info.getPhone());
		account.setQq(info.getQq());
		account.setUid(info.getUid());
		account.setUsername(info.username);
		account.setPhoto(photoUrl + info.getPhoto());
		return account;
	}
	
	@Override
	public String toString() {
		return "AccountInformation [uid=" + uid + ", username=" + username + ", nickname=" + nickname + ", gender=" + gender + ", phone=" + phone + ", qq=" + qq + ", photo=" + photo + ", brithday=" + brithday + ", level=" + level + ", email=" + email + "]";
	}
}
