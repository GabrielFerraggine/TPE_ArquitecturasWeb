package DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class DTOTiempoDeViaje {
    private int minutosDeViaje;
    private List<String> dnis;
}