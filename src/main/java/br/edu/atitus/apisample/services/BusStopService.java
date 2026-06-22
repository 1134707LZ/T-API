package br.edu.atitus.apisample.services;

import br.edu.atitus.apisample.entities.BusStop;
import br.edu.atitus.apisample.entities.User;
import br.edu.atitus.apisample.repositories.BusStopRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BusStopService {

    private final BusStopRepository repository;

    public BusStopService(BusStopRepository repository) {
        this.repository = repository;
    }

    private User getLoggedUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private void validateCoordinates(BusStop busStop) throws Exception {
        if (busStop.getLatitude() == null || busStop.getLatitude() < -90 || busStop.getLatitude() > 90)
            throw new Exception("Latitude informada inválida!");

        if (busStop.getLongitude() == null || busStop.getLongitude() < -180 || busStop.getLongitude() > 180)
            throw new Exception("Longitude informada inválida!");

        if (busStop.getLineNumber() == null || busStop.getLineNumber() <= 0)
            throw new Exception("Número da linha informado inválido!");

        if (busStop.getLineName() == null || busStop.getLineName().isBlank())
            throw new Exception("Nome da linha informado inválido!");
    }

    // ── CREATE ──────────────────────────────────────────────
    public BusStop save(BusStop newBusStop) throws Exception {
        if (newBusStop == null)
            throw new Exception("Objeto Nulo!");

        validateCoordinates(newBusStop);

        newBusStop.setUser(getLoggedUser());
        return repository.save(newBusStop);
    }

    // ── READ (somente do usuário logado) ────────────────────
    public List<BusStop> findAllByLoggedUser() {
        return repository.findByUser(getLoggedUser());
    }

    // ── UPDATE (PUT /ws/point/{id}) ─────────────────────────
    public BusStop update(UUID id, BusStop updatedData) throws Exception {
        BusStop existing = repository.findById(id)
                .orElseThrow(() -> new Exception("Ponto não encontrado!"));

        User loggedUser = getLoggedUser();
        if (!existing.getUser().getId().equals(loggedUser.getId()))
            throw new Exception("Você não tem permissão para alterar este ponto!");

        validateCoordinates(updatedData);

        existing.setLatitude(updatedData.getLatitude());
        existing.setLongitude(updatedData.getLongitude());
        existing.setLineNumber(updatedData.getLineNumber());
        existing.setLineName(updatedData.getLineName());
        existing.setDescription(updatedData.getDescription());

        return repository.save(existing);
    }

    // ── DELETE ───────────────────────────────────────────────
    public void delete(UUID id) throws Exception {
        BusStop existing = repository.findById(id)
                .orElseThrow(() -> new Exception("Ponto não encontrado!"));

        User loggedUser = getLoggedUser();
        if (!existing.getUser().getId().equals(loggedUser.getId()))
            throw new Exception("Você não tem permissão para excluir este ponto!");

        repository.delete(existing);
    }
}
