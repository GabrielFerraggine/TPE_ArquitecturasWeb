package repository;

import java.util.List;
import java.util.Optional;

import DTO.ReporteDTO;
import entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("RepositoryMonopatin")
public interface RepositoryMonopatin extends JpaRepository<Monopatin, Integer> {

    @Query("SELECT m FROM Monopatin m WHERE m.idMonopatin = :id")
    Monopatin buscarPorId(int id);

    @Modifying
    @Query("UPDATE Monopatin m SET m.estado = :estado WHERE m.idMonopatin = :idMonopatin")
    int setEstado(int idMonopatin, String estado);

    @Query("SELECT m FROM Monopatin m")
    List<Monopatin> traerTodos();

    @Query("SELECT new DTO.ReporteDTO(m.idMonopatin, m.kmRecorridos, null, null) " +
            "FROM Monopatin m ")
    List<ReporteDTO> getReportePorKmRecorridos();

    @Query("SELECT new DTO.ReporteDTO(m.idMonopatin, null, m.tiempoDeUsoTotal, null) " +
            "FROM Monopatin m ")
    List<ReporteDTO>getReportePorTiempoDeUsoTotal();

    @Query("SELECT new DTO.ReporteDTO(m.idMonopatin, null, null, m.tiempoDePausas) " +
            "FROM Monopatin m ")
    List<ReporteDTO>getReportePorTiempoDePausas();

    @Query("SELECT new DTO.ReporteDTO(m.idMonopatin, m.kmRecorridos, m.tiempoDeUsoTotal, m.tiempoDePausas) " +
            "FROM Monopatin m ")
    List<ReporteDTO>getReporteCompleto();

    @Query("UPDATE Monopatin m SET m.kmRecorridos = :kmRecorridos, m.tiempoDeUsoTotal = :tiempoDeUsoTotal, m.tiempoDePausas = :tiempoDePausas WHERE m.idMonopatin = :idMonopatin")
    int finalizarRecorrido(int idMonopatin, double kmRecorridos, int tiempoDeUsoTotal, int tiempoDePausas);
}