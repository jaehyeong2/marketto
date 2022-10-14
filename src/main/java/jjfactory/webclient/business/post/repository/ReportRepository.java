package jjfactory.webclient.business.post.repository;

import jjfactory.webclient.business.post.domain.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
