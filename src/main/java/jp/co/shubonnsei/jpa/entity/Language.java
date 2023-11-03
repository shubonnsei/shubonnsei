package jp.co.shubonnsei.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jp.co.shubonnsei.jpa.utils.LanguageId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 言語テーブルのエンティティ
 *
 * @author shubonnsei
 * @since 1.00
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(LanguageId.class)
@Table(name = "language")
public final class Language implements Serializable {

	/**
	 * This field corresponds to the database column COUNTRY_CODE
	 */
	@Id
	private String countryCode;

	/**
	 * This field corresponds to the database column LANGUAGE
	 */
	@Id
	@Column(name = "language")
	private String name;

	/**
	 * This field corresponds to the database column IS_OFFICIAL
	 */
	@Column(nullable = false)
	private String isOfficial;

	/**
	 * This field corresponds to the database column PERCENTAGE
	 */
	@Column(nullable = false, precision = 5, scale = 2)
	private BigDecimal percentage;

	/**
	 * This field corresponds to the database column LOGIC_DELETE_FLG
	 */
	@Column(nullable = false)
	private String deleteFlg;
}