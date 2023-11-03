package jp.co.shubonnsei.jpa.repository;

import java.util.List;

import org.postgresql.util.PSQLException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jp.co.shubonnsei.jpa.entity.CityInfo;

/**
 * 都市情報リポジトリ
 *
 * @author shubonnsei
 * @since 1.03
 */
public interface CityInfoRepository extends JpaRepository<CityInfo, Integer>, JpaSpecificationExecutor<CityInfo> {

	/**
	 * 人口数量降順で都市情報を検索する
	 *
	 * @param sort ソート
	 * @return List<City>
	 */
	@Query(value = "select cnf.id, cnf.name, cnf.continent, cnf.nation, cnf.district, cnf.population, cnf.language from city_info as cnf "
			+ "where cn.delete_flg = 'visible' order by cn.population desc limit :sortNumber", nativeQuery = true)
	List<CityInfo> findMaximumRanks(@Param("sortNumber") Integer sort);

	/**
	 * 人口数量昇順で都市情報を検索する
	 *
	 * @param sort ソート
	 * @return List<City>
	 */
	@Query(value = "select cnf.id, cnf.name, cnf.continent, cnf.nation, cnf.district, cnf.population, cnf.language from city_info as cnf "
			+ "where cn.delete_flg = 'visible' order by cn.population asc limit :sortNumber", nativeQuery = true)
	List<CityInfo> findMinimumRanks(@Param("sortNumber") Integer sort);

	/**
	 * 国名によって公用語を取得する
	 *
	 * @param nationVal 国名
	 * @return String
	 */
	@Query(value = "select max(cnf.language) from CityInfo as cnf where cnf.nation =:nation group by cnf.nation")
	String getLanguage(@Param("nation") String nationVal);

	/**
	 * ビューリフレッシュ
	 */
	@Modifying
	@Transactional(rollbackFor = PSQLException.class)
	@Query(value = "refresh materialized view city_info", nativeQuery = true)
	void refresh();
}
