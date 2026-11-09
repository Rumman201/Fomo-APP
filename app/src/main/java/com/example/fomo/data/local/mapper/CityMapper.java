package com.example.fomo.data.local.mapper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.fomo.data.local.entity.CityEntity;
import com.example.fomo.data.remote.dto.GeocodingResultDto;
import com.example.fomo.domain.model.City;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mapper utility converting between City entities, DTOs, and Domain models.
 */
public final class CityMapper {

    private CityMapper() {
    }

    @Nullable
    public static City toDomain(@Nullable CityEntity entity) {
        if (entity == null) return null;
        return new City(
                entity.id,
                entity.name,
                entity.country,
                entity.adminRegion,
                entity.latitude,
                entity.longitude,
                entity.isFavorite,
                entity.selectedAt
        );
    }

    @NonNull
    public static List<City> toDomainList(@Nullable List<CityEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        List<City> list = new ArrayList<>(entities.size());
        for (CityEntity entity : entities) {
            City domain = toDomain(entity);
            if (domain != null) {
                list.add(domain);
            }
        }
        return list;
    }

    @Nullable
    public static CityEntity toEntity(@Nullable City domain) {
        if (domain == null) return null;
        return new CityEntity(
                domain.getId(),
                domain.getName(),
                domain.getCountry(),
                domain.getAdminRegion(),
                domain.getLatitude(),
                domain.getLongitude(),
                domain.isFavorite(),
                domain.getSelectedAt()
        );
    }

    @Nullable
    public static City fromGeocodingDto(@Nullable GeocodingResultDto dto) {
        if (dto == null) return null;
        return new City(
                0,
                dto.getName() != null ? dto.getName() : "Unknown",
                dto.getCountry() != null ? dto.getCountry() : "",
                dto.getAdmin1(),
                dto.getLatitude(),
                dto.getLongitude(),
                false,
                0
        );
    }

    @NonNull
    public static List<City> fromGeocodingDtoList(@Nullable List<GeocodingResultDto> dtos) {
        if (dtos == null || dtos.isEmpty()) return Collections.emptyList();
        List<City> list = new ArrayList<>(dtos.size());
        for (GeocodingResultDto dto : dtos) {
            City city = fromGeocodingDto(dto);
            if (city != null) {
                list.add(city);
            }
        }
        return list;
    }
}

