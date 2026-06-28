package org.devsync.spring.search.projection;

import java.util.UUID;

public interface UserSearchProjection {

    UUID getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    Float getRank();
}
