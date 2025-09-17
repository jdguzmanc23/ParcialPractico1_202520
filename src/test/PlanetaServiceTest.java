package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.PlanetaEntity;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class PlanetaServiceTest {

    @Autowired
    private PlanetaService planetaService;

    private PlanetaEntity baseValido(String nombre) {
        PlanetaEntity p = new PlanetaEntity();
        p.setNombre(nombre);
        p.setPoblacion(1_000_000L);
        p.setDiametroKm(12000);
        return p;
    }

    @Test
    void crearPlaneta_ok() throws IllegalOperationException {
        PlanetaEntity saved = planetaService.createPlaneta(baseValido("Dathomir II"));
        assertNotNull(saved.getId());
        assertNull(saved.getSistema());
    }

    @Test
    void crearPlaneta_fallaNombreNoRomano() {
        PlanetaEntity p = baseValido("Dathomir 2");
        assertThrows(IllegalOperationException.class, () -> planetaService.createPlaneta(p));
    }

    @Test
    void crearPlaneta_fallaPoblacionNoPositiva() {
        PlanetaEntity p = baseValido("Taris I");
        p.setPoblacion(0L);
        assertThrows(IllegalOperationException.class, () -> planetaService.createPlaneta(p));
    }

    // Alternativos
    @Test
    void crearPlaneta_okRomanoI_II_III() throws IllegalOperationException {
        assertNotNull(planetaService.createPlaneta(baseValido("Naboo I")).getId());
        assertNotNull(planetaService.createPlaneta(baseValido("Naboo II")).getId());
        assertNotNull(planetaService.createPlaneta(baseValido("Naboo III")).getId());
    }

    @Test
    void crearPlaneta_okDiametroEdgeCases() throws IllegalOperationException {
        PlanetaEntity p1 = baseValido("Kamino II");
        p1.setDiametroKm(0);
        assertNotNull(planetaService.createPlaneta(p1).getId());

        PlanetaEntity p2 = baseValido("Geonosis III");
        p2.setDiametroKm(200_000);
        assertNotNull(planetaService.createPlaneta(p2).getId());
    }

    @Test
    void crearPlaneta_fallaRomanoSeparadoPorEspacio() {
        PlanetaEntity p = baseValido("Naboo  II");
        assertThrows(IllegalOperationException.class, () -> planetaService.createPlaneta(p));
    }
}
