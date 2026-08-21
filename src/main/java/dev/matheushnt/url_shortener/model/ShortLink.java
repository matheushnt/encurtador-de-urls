package dev.matheushnt.url_shortener.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "short_links")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShortLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "short_code", nullable = false, unique = true)
    private String shortCode;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Override
    public String toString() {
        return "ShortLink [id=" + id + ", shortCode=" + shortCode + ", originalUrl=" + originalUrl + ", createdAt="
                + createdAt + ", expiresAt=" + expiresAt + "]";
    }

    public static ShortLink create(
        String shortCode,
        String originalUrl,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        User user
    ) {
        var shortLink = new ShortLink();
        shortLink.setShortCode(shortCode);
        shortLink.setOriginalUrl(originalUrl);
        shortLink.setCreatedAt(createdAt);
        shortLink.setExpiresAt(expiresAt);
        shortLink.setUser(user);

        return shortLink;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShortLink other = (ShortLink) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
