package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.util.OptimizedArea;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;

public interface OptimizedAreaService {
    void addOrUpdate(Long id, OptimizedArea area);

    OptimizedArea getById(Long id) throws NotFoundException;
}
