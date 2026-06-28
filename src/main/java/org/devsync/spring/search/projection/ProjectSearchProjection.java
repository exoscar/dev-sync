package org.devsync.spring.search.projection;

import java.util.UUID;

public interface ProjectSearchProjection {

    UUID getId();

    String getName();

    Float getRank();
}