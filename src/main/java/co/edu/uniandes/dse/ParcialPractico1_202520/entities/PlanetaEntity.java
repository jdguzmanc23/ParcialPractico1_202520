package co.edu.uniandes.dse.ParcialPractico1_202520.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
public class PlanetaEntity extends BaseEntity {

    private String nombre;
    private Long poblacion;
    private Integer diametroKm;

    @ManyToOne
    private SistemaSolar sistema;
}
