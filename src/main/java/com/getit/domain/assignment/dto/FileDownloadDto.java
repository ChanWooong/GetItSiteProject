package com.getit.domain.assignment.dto;

import org.springframework.core.io.Resource;

public record FileDownloadDto(
        Resource resource,
        String originFileName
) {}
