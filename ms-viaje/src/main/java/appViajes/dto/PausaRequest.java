package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PausaRequest {
    private Long idViaje;

    // Con esto encapsulamos los datos necesarios para pausar un viaje
}
