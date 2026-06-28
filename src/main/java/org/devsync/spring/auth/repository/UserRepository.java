package org.devsync.spring.auth.repository;

import org.devsync.spring.auth.entity.User;
import org.devsync.spring.search.projection.UserSearchProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query(value = """
            SELECT
                u.id,
                u.first_name AS firstName,
                u.last_name AS lastName,
                u.email AS email,
                ts_rank(
                    u.search_vector,
                    websearch_to_tsquery('english', :query)
                ) AS rank
            FROM users u
            JOIN workspace_member wm
                ON wm.user_id = u.id
            WHERE wm.workspace_id = :workspaceId
            AND u.search_vector @@ websearch_to_tsquery('english', :query)
            ORDER BY rank DESC
            LIMIT 15
            """, nativeQuery = true)
    List<UserSearchProjection> searchUsers(@Param("workspaceId") UUID workspaceId, @Param("query") String query);
}