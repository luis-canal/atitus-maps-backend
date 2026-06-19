package br.edu.atitus.luis_canal.apisample.services;

import br.edu.atitus.luis_canal.apisample.entities.PointEntity;
import br.edu.atitus.luis_canal.apisample.entities.User;
import br.edu.atitus.luis_canal.apisample.repositories.PointRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PointService {

    private final PointRepository repository;

    public PointService(PointRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PointEntity save(PointEntity point) throws Exception {

        if (point == null)
            throw new Exception("Ponto nulo");

        if (point.getDescription() == null || point.getDescription().isEmpty())
            throw new Exception("Descrição inválida");

        if (point.getLatitude() < -90 || point.getLatitude() > 90)
            throw new Exception("Latitude inválida.");

        if (point.getLongitude() < -180 || point.getLongitude() > 180)
            throw new Exception("Longitude inválida.");

        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        point.setUser(userAuth);

        return repository.save(point);
    }

    @Transactional
    public PointEntity update(UUID id, PointEntity pointData) throws Exception {
        var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Ponto não localizado"));
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!pointInBD.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Você não tem permissão para esta ação.");

        if (pointData.getDescription() == null || pointData.getDescription().isEmpty())
            throw new Exception("Descrição inválida");

        if (pointData.getLatitude() < -90 || pointData.getLatitude() > 90)
            throw new Exception("Latitude inválida.");

        if (pointData.getLongitude() < -180 || pointData.getLongitude() > 180)
            throw new Exception("Longitude inválida.");

        pointInBD.setDescription(pointData.getDescription());
        pointInBD.setLatitude(pointData.getLatitude());
        pointInBD.setLongitude(pointData.getLongitude());

        return repository.save(pointInBD);
    }

    @Transactional
    public void deleteById(UUID id) throws Exception {
        var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Ponto não localizado"));
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!pointInBD.getUser().getId().equals(userAuth.getId()))
            throw new Exception("Você não tem permissão para esta ação.");

        repository.deleteById(id);
    }

    public List<PointEntity> findAll() {
        return repository.findAll();
    }

    public List<PointEntity> findAllByAuthenticatedUser() {
        User userAuth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return repository.findByUser(userAuth);
    }
}

