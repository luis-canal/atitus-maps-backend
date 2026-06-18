package br.edu.atitus.luis_canal.apisample.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.atitus.luis_canal.apisample.entities.PointEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<PointEntity, UUID>{

}
