package dev.matheushnt.url_shortener.repository;

import dev.matheushnt.url_shortener.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortCodeAndUserId(String shortCode, UUID userId);
}
