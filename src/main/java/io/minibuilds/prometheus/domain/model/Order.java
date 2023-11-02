package io.minibuilds.prometheus.domain.model;

import java.time.LocalDateTime;
import java.util.List;

public record Order(String id, List<String> items, LocalDateTime createdDateTime) {

}
