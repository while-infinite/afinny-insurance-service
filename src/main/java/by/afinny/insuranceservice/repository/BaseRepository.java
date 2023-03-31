package by.afinny.insuranceservice.repository;

import by.afinny.insuranceservice.entity.Base;
import by.afinny.insuranceservice.entity.constant.CategoryGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseRepository extends JpaRepository<Base, CategoryGroup> {
}
