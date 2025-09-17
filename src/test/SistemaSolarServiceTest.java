package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.RegionType;
import co.edu.uniandes.dse.ParcialPractico1_202520.entities.SistemaSolar;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class SistemaSolarServiceTest {

    @Autowired
    private SistemaSolarService sistemaService;

    private SistemaSolar baseValido() {
        SistemaSolar s = new SistemaSolar();
        s.setNombre("TatooineSystem");
        s.setRegion(RegionType.OUTER_RIM);
        s.setRatioMinimo(0.3);
        s.setStormtroopersAsignados(5_001L);
    }

    @Test
    void crearSistema_happy() throws IllegalOperationException {
        SistemaSolar saved = sistemaService.createSistema(baseValido());
        assertNotNull(saved.getId());
        assertEquals(5_001L, saved.getStormtroopersAsignados());
    }

    @Test
    void crearSistema_unhappy_nombreLargo() {
        SistemaSolar s = baseValido();
        s.setNombre("Nombre_super_excesivamente_largo_>_30_chars");
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }

    @Test
    void crearSistema_alternativo_stormtroopersInvalidos() {
        SistemaSolar s = baseValido();
        s.setStormtroopersAsignados(1000L);
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }
}
