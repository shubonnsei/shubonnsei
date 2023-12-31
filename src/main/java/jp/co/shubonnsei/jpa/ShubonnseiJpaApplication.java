package jp.co.shubonnsei.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import jp.co.shubonnsei.jpa.utils.Messages;
import lombok.extern.log4j.Log4j2;

/**
 * Shubonnseiアプリケーション
 *
 * @author shubonnsei
 * @since 1.00
 */
@Log4j2
@SpringBootApplication
@ServletComponentScan
public class ShubonnseiJpaApplication {
	public static void main(final String[] args) {
		SpringApplication.run(ShubonnseiJpaApplication.class, args);
		log.info(Messages.MSG003);
	}
}
