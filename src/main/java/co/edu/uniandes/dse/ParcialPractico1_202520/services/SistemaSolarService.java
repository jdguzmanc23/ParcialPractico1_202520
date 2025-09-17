package co.edu.uniandes.dse.ParcialPractico1_202520.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uniandes.dse.ParcialPractico1_202520.entities.SistemaSolar;
import co.edu.uniandes.dse.ParcialPractico1_202520.exceptions.IllegalOperationException;
import co.edu.uniandes.dse.ParcialPractico1_202520.repositories.SistemaSolarRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SistemaSolarService {

    private final SistemaSolarRepository sistemaRepo;
//miramos en este caso si cumplimos con las reglas presupuestadas aunque toca tener en cuenta el Happy unhappy path en el service de los tests
//INSTRUCCIONES DEL PARLCIAL
    // nOS PIDEN QUE EL NOMBRE DE UN SISTEMA SOLAR DEBE TENER SER MENOS A LOS 31 CARACTERES
   // eL RATIO DEL SISTEMA SOLAR DEBE ESTAR EN UN RANGO MAYOR O IGUAL A 0,2 Y MENOR O IGUAL A 0,6
   //nUM DE STORMTROOPERS ASIGNADOS A UN SISTEMA SOLAR DEBE SER MAYOR A 1000 UNIDADES
    public SistemaSolar createSistema(SistemaSolar sistema) throws IllegalOperationException {
        log.info("Creando SistemaSolar: {}", sistema);

        if (sistema.getNombre() == null || sistema.getNombre().length() >= 31) {
            throw new IllegalOperationException("El nombre del sistema debe tener menos de 31 caracteres.");
        }
        if (sistema.getRatioMinimo() == null
                || sistema.getRatioMinimo() < 0.2
                || sistema.getRatioMinimo() > 0.6) {
            throw new IllegalOperationException("El ratio mínimo debe estar entre 0.2 y 0.6 (estos mismo  incluidos).");
        }
        if (sistema.getStormtroopersAsignados() == null || sistema.getStormtroopersAsignados() <= 1000) {
            throw new IllegalOperationException("Los stormtroopers asignados deben ser > 1000.");
        }

        return sistemaRepo.save(sistema);
    }
}
