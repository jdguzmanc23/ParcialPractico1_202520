package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.PlanetaEntity;
import co.edu.uniandes.dse.ParcialPractico1_202520.entities.SistemaSolar;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.ParcialPractico1_202520.repositories.PlanetaRepository;
import co.edu.uniandes.dse.ParcialPractico1_202520.repositories.SistemaSolarRepository;

import java.util.List;

@Service
@Transactional
public class SistemaPlanetaService {

    private final SistemaSolarRepository sistemaRepo;
    private final PlanetaRepository planetaRepo;

    public SistemaPlanetaService(SistemaSolarRepository sistemaRepo, PlanetaRepository planetaRepo) {
        this.sistemaRepo = sistemaRepo;
        this.planetaRepo = planetaRepo;
    }

    public PlanetaEntity asociarPlanetaASistema(Long planetaId, Long sistemaId)
            throws EntityNotFoundException, IllegalOperationException {

        PlanetaEntity planeta = planetaRepo.findById(planetaId).orElse(null);
        if (planeta == null) {
            throw new EntityNotFoundException("El planeta con id " + planetaId + " no existe.");
        }

        SistemaSolar sistema = sistemaRepo.findById(sistemaId).orElse(null);
        if (sistema == null) {
            throw new EntityNotFoundException("El sistema con id " + sistemaId + " no existe.");
        }

        if (planeta.getPoblacion() == null || planeta.getPoblacion() <= 0) {
            throw new IllegalOperationException("El planeta debe tener una población mayor que 0.");
        }
        long poblacionActual = 0;
        List<PlanetaEntity> planetas = sistema.getPlanetas();
        for (PlanetaEntity p : planetas) {
            if (p.getPoblacion() != null) {
                poblacionActual += p.getPoblacion();
            }
        }
        long poblacionConNuevo = poblacionActual + planeta.getPoblacion();
        double ratioActual = (double) sistema.getStormtroopersAsignados()/(double) poblacionConNuevo;

        if (ratioActual < sistema.getRatioMinimo()) {
            throw new IllegalOperationException("El ratio actual (" + ratioActual + 
                ") es menor al mínimo permitido (" + sistema.getRatioMinimo() + ").");
        }

        planeta.setSistema(sistema);
        sistema.getPlanetas().add(planeta);

        return planetaRepo.save(planeta);
    }
}
