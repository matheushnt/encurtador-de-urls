package dev.matheushnt.url_shortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.matheushnt.url_shortener.model.ShortLink;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
}
