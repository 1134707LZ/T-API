package br.edu.atitus.apisample.repositories;

import br.edu.atitus.apisample.entities.BusStop;
import br.edu.atitus.apisample.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BusStopRepository extends JpaRepository<BusStop, UUID> {

    // Requisito: GET /ws/point (busstop) deve retornar somente
    // os pontos pertencentes ao usuário autenticado
    List<BusStop> findByUser(User user);
}
