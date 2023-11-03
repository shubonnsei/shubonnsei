package jp.co.shubonnsei.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.co.shubonnsei.jpa.entity.Language;

/**
 * 言語リポジトリ
 *
 * @author shubonnsei
 * @since 1.03
 */
public interface LanguageRepository extends JpaRepository<Language, String>, JpaSpecificationExecutor<Language> {
}