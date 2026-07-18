package co.unicauca.edu.unisched.mapper.subjects;

import org.springframework.stereotype.Component;

import co.unicauca.edu.unisched.domain.model.Career;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.CareerEntity;

@Component
public class CareerMapper {

    public Career toDomain(CareerEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Career(
                entity.getId(),
                entity.getName()
        );
    }
}