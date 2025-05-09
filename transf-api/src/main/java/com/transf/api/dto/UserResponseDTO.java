package com.transf.api.dto;

public class UserResponseDTO {
	private Long id;
	private String idCardNumber;
	private String thaiName;
	private String englishName;

	public UserResponseDTO(Long id, String idCardNumber, String thaiName, String englishName) {
		super();
		this.id = id;
		this.idCardNumber = idCardNumber;
		this.thaiName = thaiName;
		this.englishName = englishName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getThaiName() {
		return thaiName;
	}

	public void setThaiName(String thaiName) {
		this.thaiName = thaiName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

}
