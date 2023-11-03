package jp.co.shubonnsei.jpa.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 都市情報マテリアライズドビューのエンティティ
 *
 * @author shubonnsei
 * @since 1.00
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "city_info")
public final class CityInfo implements Serializable {

	/**
	 * 都市ID
	 */
	@Id
	private Integer id;

	/**
	 * 都市名
	 */
	@Column(nullable = false)
	private String name;

	/**
	 * 大陸
	 */
	@Column(nullable = false)
	private String continent;

	/**
	 * 国家
	 */
	@Column(nullable = false)
	private String nation;

	/**
	 * 地域
	 */
	@Column(nullable = false)
	private String district;

	/**
	 * 人口
	 */
	@Column(nullable = false)
	private Integer population;

	/**
	 * 公用語
	 */
	@Column(nullable = false)
	private String language;
}