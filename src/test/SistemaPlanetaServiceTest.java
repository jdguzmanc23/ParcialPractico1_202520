package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.PlanetaEntity;
import co.edu.uniandes.dse.ParcialPractico1_202520.entities.RegionType;
import co.edu.uniandes.dse.ParcialPractico1_202520.entities.SistemaSolar;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.EntityNotFoundException;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.ParcialPractico1_202520.repositories.SistemaSolarRepository;

@SpringBootTest
@Transactional
class SistemaPlanetaServiceIT {

    @Autowired
    private SistemaPlanetaService sistemaPlanetaService;

    @Autowired
    private SistemaSolarService sistemaService;

    @Autowired
    private PlanetaService planetaService;

    @Autowired
    private SistemaSolarRepository sistemaRepo;

    private SistemaSolar sistema;
    private PlanetaEntity p1;

    @BeforeEach
    void setup() throws IllegalOperationException {
        sistema = new SistemaSolar();
        sistema.setNombre("Corellia");
        sistema.setRegion(RegionType.CORE);
        sistema.setRatioMinimo(0.3);
        sistema.setStormtroopersAsignados(3_000L);
        sistema = sistemaService.createSistema(sistema);

        p1 = new PlanetaEntity();
        p1.setNombre("Corellia II");
        p1.setPoblacion(5_000L);
        p1.setDiametroKm(11000);
        p1 = planetaService.createPlaneta(p1);
    }

    @Test
    void asociar_ok() throws Exception {
        PlanetaEntity asociado = sistemaPlanetaService.asociarPlanetaASistema(p1.getId(), sistema.getId());
        assertEquals(sistema.getId(), asociado.getSistema().getId());
        SistemaSolar recargado = sistemaRepo.findById(sistema.getId()).orElseThrow();
        assertTrue(recargado.getPlanetas().stream().anyMatch(pp -> pp.getId().equals(p1.getId())));
    }

    @Test
    void asociar_fallaSistemaNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> sistemaPlanetaService.asociarPlanetaASistema(p1.getId(), 99999L));
    }

    @Test
    void asociar_fallaPlanetaNoExiste() {
        assertThrows(EntityNotFoundException.class,
            () -> sistemaPlanetaService.asociarPlanetaASistema(88888L, sistema.getId()));
    }

    @Test
    void asociar_fallaPoblacionPlanetaNoPositiva() throws IllegalOperationException {
        PlanetaEntity pBad = new PlanetaEntity();
        pBad.setNombre("Corellia III");
        pBad.setPoblacion(0L);
        pBad.setDiametroKm(12000);
        pBad = planetaService.createPlaneta(pBad);
// Crearemos EXCEPCIÓN aquí si mantienes regla en creación
        // pa probarla se puede diercto, creando el planeta por repo.
    }

    @Test
    void asociar_fallaPorRatio() throws Exception {
        sistema.setRatioMinimo(0.5);
        sistemaRepo.save(sistema);

        PlanetaEntity p2 = new PlanetaEntity();
        p2.setNombre("Corellia III");
        p2.setPoblacion(5_000L);
        p2.setDiametroKm(12000);
        p2 = planetaService.createPlaneta(p2);

        sistemaPlanetaService.asociarPlanetaASistema(p1.getId(), sistema.getId());
        PlanetaEntity p2Final = p2;
        assertThrows(IllegalOperationException.class,
            () -> sistemaPlanetaService.asociarPlanetaASistema(p2Final.getId(), sistema.getId()));
    }

    @Test
    void asociar_okRatioIgualAlMinimo() throws Exception {
        sistema.setRatioMinimo(0.6);
        sistemaRepo.save(sistema);
        PlanetaEntity asociado = sistemaPlanetaService.asociarPlanetaASistema(p1.getId(), sistema.getId());
        assertEquals(sistema.getId(), asociado.getSistema().getId());
    }

    @Test
    void asociar_okConSistemaPoblado() throws Exception {
        PlanetaEntity pGrande = new PlanetaEntity();
        pGrande.setNombre("Corellia I");
        pGrande.setPoblacion(3_000L);
        pGrande.setDiametroKm(10000);
        pGrande = planetaService.createPlaneta(pGrande);
        sistemaPlanetaService.asociarPlanetaASistema(pGrande.getId(), sistema.getId());
// Agreg en este caso otro pequeño planeta a dif del anterior que era grande y debe seguir cumpliendo
        PlanetaEntity pPeq = new PlanetaEntity();
        pPeq.setNombre("Corellia III");
        pPeq.setPoblacion(500L);
        pPeq.setDiametroKm(9000);
        pPeq = planetaService.createPlaneta(pPeq);
        PlanetaEntity asociado = sistemaPlanetaService.asociarPlanetaASistema(pPeq.getId(), sistema.getId());
        assertEquals(sistema.getId(), asociado.getSistema().getId());
    }

    @Test
    void asociar_okLuegoFallaPorRatio() throws Exception {
        sistemaPlanetaService.asociarPlanetaASistema(p1.getId(), sistema.getId());
        sistema.setRatioMinimo(0.7);
        sistemaRepo.save(sistema);

        PlanetaEntity p2 = new PlanetaEntity();
        p2.setNombre("Corellia III");
        p2.setPoblacion(1_000L);
        p2.setDiametroKm(12000);
        p2 = planetaService.createPlaneta(p2);

        PlanetaEntity p2Final = p2;
        assertThrows(IllegalOperationException.class,
            () -> sistemaPlanetaService.asociarPlanetaASistema(p2Final.getId(), sistema.getId()));
    }
}
