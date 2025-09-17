package co.edu.uniandes.dse.ParcialPractico1_202520.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class SistemaSolar extends BaseEntity {

    private String nombre;

    @Enumerated(EnumType.STRING)
    private RegionType region;

    private Double ratioMinimo;

    private Long stormtroopersAsignados;

    @OneToMany(mappedBy = "sistemaSolar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlanetaEntity> planetas = new ArrayList<>();
}
