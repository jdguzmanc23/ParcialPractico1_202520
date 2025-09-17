package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.PlanetaEntity;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.ParcialPractico1_202520.repositories.PlanetaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PlanetaService {

    private final PlanetaRepository planetaRepo;
//miramos en este caso si cumplimos con las reglas presupuestadas aunque toca tener en cuenta el Happy unhappy path en el service de los tests
//INSTRUCCIONES DEL PARLCIAL
    // eL NOMBRE DE TODO PLANETA DEBE TERMINAR CON LOS NUMEROS ROMANOS I, II, III
    // La población del planeta debe ser un número positivo mayor que 0
    public PlanetaEntity createPlaneta(PlanetaEntity planeta) throws IllegalOperationException {
        log.info("Creando Planeta: {}", planeta);

        if (planeta.getNombre() == null || !planeta.getNombre().matches(".*(I|II|III)$")) {
            throw new IllegalOperationException("El nombre del planeta debe terminar en I, II o III.");
        }
        if (planeta.getPoblacion() == null || planeta.getPoblacion() <= 0) {
            throw new IllegalOperationException("La población del planeta debe ser mayor que 0.");
        }

        planeta.setSistema(null);

        return planetaRepo.save(planeta);
    }
}

