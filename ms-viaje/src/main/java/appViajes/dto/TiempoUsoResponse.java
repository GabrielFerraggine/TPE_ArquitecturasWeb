package appViajes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TiempoUsoResponse {
    private Long idUsuario;
    private Integer tiempoUsoTotalMinutos;
    private String periodo;
    private List<UsuarioTiempoInfo> usuariosRelacionados;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UsuarioTiempoInfo {
        private Long idUsuario;
        private String nombreUsuario;
        private Integer tiempoUsoMinutos;
        private Integer cantidadViajes;

    }
}
