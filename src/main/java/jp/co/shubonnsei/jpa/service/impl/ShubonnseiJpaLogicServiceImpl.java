package jp.co.shubonnsei.jpa.service.impl;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import jp.co.shubonnsei.jpa.dto.CityDto;
import jp.co.shubonnsei.jpa.entity.City;
import jp.co.shubonnsei.jpa.entity.CityInfo;
import jp.co.shubonnsei.jpa.repository.CityInfoRepository;
import jp.co.shubonnsei.jpa.repository.CityRepository;
import jp.co.shubonnsei.jpa.repository.CountryRepository;
import jp.co.shubonnsei.jpa.service.ShubonnseiJpaLogicService;
import jp.co.shubonnsei.jpa.utils.Messages;
import jp.co.shubonnsei.jpa.utils.Pagination;
import jp.co.shubonnsei.jpa.utils.StringUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 中央処理サービス実装クラス
 *
 * @author shubonnsei
 * @since 1.00
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ShubonnseiJpaLogicServiceImpl implements ShubonnseiJpaLogicService {

	/**
	 * ページサイズ
	 */
	private static final Integer PAGE_SIZE = 8;

	/**
	 * デフォルトソート値
	 */
	private static final Integer SORT_NUMBER = 100;

	/**
	 * 都市リポジトリ
	 */
	private final CityRepository cityRepository;

	/**
	 * 都市情報リポジトリ
	 */
	private final CityInfoRepository cityInfoRepository;

	/**
	 * 国家リポジトリ
	 */
	private final CountryRepository countryRepository;

	@Override
	public Integer checkDuplicate(final String cityName) {
		final City city = new City();
		city.setName(StringUtils.toHankaku(cityName));
		city.setDeleteFlg(Messages.MSG007);
		final Example<City> example = Example.of(city, ExampleMatcher.matchingAll());
		return this.cityRepository.findAll(example).size();
	}

	@Override
	public List<String> findAllContinents() {
		return this.countryRepository.findAllContinents();
	}

	@Override
	public String findLanguageByCty(final String nationVal) {
		return this.cityInfoRepository.getLanguage(nationVal);
	}

	@Override
	public List<String> findNationsByCnt(final String continentVal) {
		if (StringUtils.isDigital(continentVal)) {
			final Integer id = Integer.parseInt(continentVal);
			final List<String> nations = Lists.newArrayList();
			final CityInfo cityInfo = this.cityInfoRepository.findById(id).orElseGet(CityInfo::new);
			nations.add(cityInfo.getNation());
			final List<String> list = this.countryRepository.findNationsByCnt(cityInfo.getContinent()).stream()
					.filter(a -> StringUtils.isNotEqual(a, cityInfo.getNation())).toList();
			nations.addAll(list);
			return nations;
		}
		return this.countryRepository.findNationsByCnt(continentVal);
	}

	@Override
	public CityDto getCityInfoById(final Integer id) {
		final CityInfo cityInfo = this.cityInfoRepository.findById(id).orElseGet(CityInfo::new);
		return new CityDto(cityInfo.getId(), cityInfo.getName(), cityInfo.getContinent(), cityInfo.getNation(),
				cityInfo.getDistrict(), cityInfo.getPopulation(), cityInfo.getLanguage());
	}

	@Override
	public Pagination<CityDto> getPageInfo(final Integer pageNum, final String keyword) {
		// ページングコンストラクタを宣言する；
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_SIZE, Sort.by(Direction.ASC, "id"));
		// キーワードの属性を判断する；
		if (StringUtils.isNotEmpty(keyword)) {
			final String hankakuKeyword = StringUtils.toHankaku(keyword);
			final int pageMin = PAGE_SIZE * (pageNum - 1);
			final int pageMax = PAGE_SIZE * pageNum;
			int sort = SORT_NUMBER;
			if (hankakuKeyword.startsWith("min(pop)")) {
				final int indexOf = hankakuKeyword.indexOf(")");
				final String keisan = hankakuKeyword.substring(indexOf + 1);
				if (StringUtils.isNotEmpty(keisan)) {
					sort = Integer.parseInt(keisan);
				}
				// 人口数量昇順で最初の15個都市の情報を吹き出します；
				final List<CityDto> minimumRanks = this.cityInfoRepository.findMinimumRanks(sort).stream()
						.map(item -> new CityDto(item.getId(), item.getName(), item.getContinent(), item.getNation(),
								item.getDistrict(), item.getPopulation(), item.getLanguage()))
						.toList();
				if (pageMax >= sort) {
					return Pagination.of(minimumRanks.subList(pageMin, sort), minimumRanks.size(), pageNum, PAGE_SIZE);
				}
				return Pagination.of(minimumRanks.subList(pageMin, pageMax), minimumRanks.size(), pageNum, PAGE_SIZE);
			}
			if (hankakuKeyword.startsWith("max(pop)")) {
				final int indexOf = hankakuKeyword.indexOf(")");
				final String keisan = hankakuKeyword.substring(indexOf + 1);
				if (StringUtils.isNotEmpty(keisan)) {
					sort = Integer.parseInt(keisan);
				}
				// 人口数量降順で最初の15個都市の情報を吹き出します；
				final List<CityDto> maximumRanks = this.cityInfoRepository.findMaximumRanks(sort).stream()
						.map(item -> new CityDto(item.getId(), item.getName(), item.getContinent(), item.getNation(),
								item.getDistrict(), item.getPopulation(), item.getLanguage()))
						.toList();
				if (pageMax >= sort) {
					return Pagination.of(maximumRanks.subList(pageMin, sort), maximumRanks.size(), pageNum, PAGE_SIZE);
				}
				return Pagination.of(maximumRanks.subList(pageMin, pageMax), maximumRanks.size(), pageNum, PAGE_SIZE);
			}
			// ページング検索；
			final CityInfo cityInfo = new CityInfo();
			final String nationCode = this.countryRepository.findNationCode(hankakuKeyword);
			if (StringUtils.isNotEmpty(nationCode)) {
				cityInfo.setNation(hankakuKeyword);
				final Example<CityInfo> example = Example.of(cityInfo, ExampleMatcher.matching());
				final Page<CityInfo> pages = this.cityInfoRepository.findAll(example, pageRequest);
				final List<CityDto> list = pages.getContent().stream()
						.map(item -> new CityDto(item.getId(), item.getName(), item.getContinent(), item.getNation(),
								item.getDistrict(), item.getPopulation(), item.getLanguage()))
						.toList();
				return Pagination.of(list, pages.getTotalElements(), pageNum, PAGE_SIZE);
			}
			cityInfo.setName(hankakuKeyword);
			final ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name",
					GenericPropertyMatchers.contains());
			final Example<CityInfo> example = Example.of(cityInfo, matcher);
			final Page<CityInfo> pages = this.cityInfoRepository.findAll(example, pageRequest);
			final List<CityDto> list = pages.getContent().stream()
					.map(item -> new CityDto(item.getId(), item.getName(), item.getContinent(), item.getNation(),
							item.getDistrict(), item.getPopulation(), item.getLanguage()))
					.toList();
			return Pagination.of(list, pages.getTotalElements(), pageNum, PAGE_SIZE);
		}
		// ページング検索；
		final Page<CityInfo> pages = this.cityInfoRepository.findAll(pageRequest);
		final List<CityDto> list = pages.getContent().stream().map(item -> new CityDto(item.getId(), item.getName(),
				item.getContinent(), item.getNation(), item.getDistrict(), item.getPopulation(), item.getLanguage()))
				.toList();
		return Pagination.of(list, pages.getTotalElements(), pageNum, PAGE_SIZE);
	}

	@Override
	public void removeById(final Integer id) {
		this.cityRepository.removeById(id);
		this.cityInfoRepository.refresh();
	}

	@Override
	public void save(final CityDto cityDto) {
		final String countryCode = this.countryRepository.findNationCode(cityDto.nation());
		final Integer saiban = this.cityRepository.saiban();
		final City city = new City();
		city.setId(saiban);
		city.setName(cityDto.name());
		city.setCountryCode(countryCode);
		city.setDistrict(cityDto.district());
		city.setPopulation(cityDto.population());
		city.setDeleteFlg(Messages.MSG007);
		this.cityRepository.save(city);
		this.cityInfoRepository.refresh();
	}

	@Override
	public void update(final CityDto cityDto) {
		final City city = this.cityRepository.findById(cityDto.id()).orElseGet(City::new);
		final String countryCode = this.countryRepository.findNationCode(cityDto.nation());
		city.setCountryCode(countryCode);
		city.setName(cityDto.name());
		city.setDistrict(cityDto.district());
		city.setPopulation(cityDto.population());
		this.cityRepository.save(city);
		this.cityInfoRepository.refresh();
	}
}
