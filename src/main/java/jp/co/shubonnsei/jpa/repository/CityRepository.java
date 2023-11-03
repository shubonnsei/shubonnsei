package jp.co.shubonnsei.jpa.repository;

import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jp.co.shubonnsei.jpa.entity.City;

/**
 * 都市リポジトリ
 *
 * @author shubonnsei
 * @since 1.03
 */
public interface CityRepository extends JpaRepository<City, Integer>, JpaSpecificationExecutor<City> {

	/**
	 * 論理削除
	 *
	 * @param id id of the selected city
	 */
	@Modifying
	@Transactional(rollbackFor = PSQLException.class)
	void removeById(@Param("id") Integer id);

	/**
	 * 採番を行います
	 *
	 * @return 採番値
	 */
	Integer saiban();
}