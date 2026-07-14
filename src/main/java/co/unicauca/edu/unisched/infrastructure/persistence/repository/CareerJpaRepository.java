package co.unicauca.edu.unisched.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import co.unicauca.edu.unisched.infrastructure.persistence.entity.CareerEntity;


public interface CareerJpaRepository  extends JpaRepository<CareerEntity, Long>{
    
}
