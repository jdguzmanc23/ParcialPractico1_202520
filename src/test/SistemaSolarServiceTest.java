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
        s.setStormtroopersAsignados(5_000L);
        return s;
    }

    @Test
    void crearSistema_ok() throws IllegalOperationException {
        SistemaSolar saved = sistemaService.createSistema(baseValido());
        assertNotNull(saved.getId());
    }

    @Test
    void crearSistema_fallaNombreLargo() {
        SistemaSolar s = baseValido();
        s.setNombre("Nombre_excesivamente_largo_de_mas_de_30_caracteres");
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }

    @Test
    void crearSistema_fallaRatioMenorA02() {
        SistemaSolar s = baseValido();
        s.setRatioMinimo(0.199);
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }

    @Test
    void crearSistema_fallaRatioMayorA06() {
        SistemaSolar s = baseValido();
        s.setRatioMinimo(0.61);
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }

    @Test
    void crearSistema_fallaStormtroopersNoMayoresA1000() {
        SistemaSolar s = baseValido();
        s.setStormtroopersAsignados(1000L);
        assertThrows(IllegalOperationException.class, () -> sistemaService.createSistema(s));
    }

    @Test
    void crearSistema_okNombre30Chars() throws IllegalOperationException {
        SistemaSolar s = baseValido();
        s.setNombre("ABCDEFGHIJABCDEFGHIJABCDEFGHIJ");
        SistemaSolar saved = sistemaService.createSistema(s);
        assertNotNull(saved.getId());
    }

    @Test
    void crearSistema_okRatioLimites() throws IllegalOperationException {
        SistemaSolar s1 = baseValido();
        s1.setRatioMinimo(0.2);
        assertNotNull(sistemaService.createSistema(s1).getId());

        SistemaSolar s2 = baseValido();
        s2.setNombre("OtroSystem");
        s2.setRatioMinimo(0.6);
        assertNotNull(sistemaService.createSistema(s2).getId());
    }

    @Test
    void crearSistema_okStormtroopersGrandes() throws IllegalOperationException {
        SistemaSolar s = baseValido();
        s.setStormtroopersAsignados(1_000_000L);
        assertNotNull(sistemaService.createSistema(s).getId());
    }
}
