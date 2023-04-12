package com.arsuhinars.animals_chipization.area.service;

import com.arsuhinars.animals_chipization.area.model.Area;
import com.arsuhinars.animals_chipization.area.repository.AreaRepository;
import com.arsuhinars.animals_chipization.area.util.OptimizedArea;
import com.arsuhinars.animals_chipization.core.exception.NotFoundException;
import com.arsuhinars.animals_chipization.core.util.ErrorDetailsFormatter;
import com.arsuhinars.animals_chipization.core.util.GeoPosition;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OptimizedAreaServiceImpl implements OptimizedAreaService {
    private final AreaRepository repository;
    private final OptimizedAreaFactory areaFactory;
    private final HashMap<Long, OptimizedArea> areasMap;

    public OptimizedAreaServiceImpl(AreaRepository repository, OptimizedAreaFactory areaFactory) {
        this.repository = repository;
        this.areaFactory = areaFactory;
        this.areasMap = new HashMap<>();
    }

    @Override
    public void addOrUpdate(Long id, OptimizedArea area) {
        areasMap.put(id, area);
    }

    @Override
    public OptimizedArea getById(Long id) throws NotFoundException {
        if (!areasMap.containsKey(id)) {
            var area = repository.findById(id).orElse(null);
            if (area == null) {
                throw new NotFoundException(
                    ErrorDetailsFormatter.formatNotFoundError(Area.class, id)
                );
            }
            var optimizedArea = areaFactory.createArea(
                area.getPoints().stream().map(GeoPosition::toVector2d).toList()
            );
            areasMap.put(id, optimizedArea);
        }
        return areasMap.get(id);
    }
}
