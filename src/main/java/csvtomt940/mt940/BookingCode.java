/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csvtomt940.mt940;

/**
 *
 * @author fr
 */
public class BookingCode {

	private final String code;
	private final String swift;

	public BookingCode(String code, String swift) {
		this.code = code;
		this.swift = swift;
	}

	public String getCode() {
		return code;
	}

	public String getSwift() {
		return swift;
	}
}
