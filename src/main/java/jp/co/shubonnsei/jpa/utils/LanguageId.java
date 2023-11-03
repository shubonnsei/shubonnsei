package jp.co.shubonnsei.jpa.utils;

import java.io.Serializable;

import lombok.Data;

/**
 * 言語テーブル複数プライマリーキーの永続化するクラス
 *
 * @author shubonnsei
 * @since 1.03
 */
@Data
public class LanguageId implements Serializable {

	/**
	 * This field corresponds to the database column COUNTRY_CODE
	 */
	private String countryCode;

	/**
	 * This field corresponds to the database column LANGUAGE
	 */
	private String name;
}
