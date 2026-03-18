package com.getit.domain.admin.assignment.dto.internal;

import org.springframework.core.io.Resource;

public record FileDownloadDto(
        Resource resource,
        String originFileName
) {}
