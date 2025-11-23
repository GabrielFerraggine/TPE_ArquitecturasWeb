package Aplicacion.repository;

import java.util.List;

import Aplicacion.DTO.*;
import Aplicacion.entity.*;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

@Repository("RepositoryMonopatin")
public interface RepositoryMonopatin extends MongoRepository<Monopatin, Long> {

    Monopatin findByIdMonopatin(Long id);

    @Query("{ 'latitud': { $gte: ?0, $lte: ?1 }, 'longitud': { $gte: ?2, $lte: ?3 } }")
    List<Monopatin> getMonopatinesCercanos(double latitudMin, double latitudMax, double longitudMin, double longitudMax);

    @Query(value = "{ 'idMonopatin': ?0 }", fields = "{ 'latitud': 1, '_id': 0 }")
    Monopatin getLatitud(Long idMonopatin);

    @Query(value = "{ 'idMonopatin': ?0 }", fields = "{ 'longitud': 1, '_id': 0 }")
    Monopatin getLongitud(Long idMonopatin);

    @Query("{}")
    List<Monopatin> traerTodos();

    @Query(value = "{}", fields = "{ 'idMonopatin' : 1, 'kmRecorridos' : 1 }")
    List<Monopatin> findAllDataParaReporteKm();

    @Query(value = "{}", fields = "{ 'idMonopatin': 1, 'kmRecorridos': 1, 'tiempoDePausas': 1 }")
    List<Monopatin> findAllDataReportePausasyKm();

    @Query(value = "{}", fields = "{'idMonopatin' : 1, 'tiempoDeUsoTotal': 1}")
    List<Monopatin> findAllDataReportePorTiempoDeUsoTotal();

    @Query(value = "{}", fields = "{'idMonopatin':  1, 'tiempoDePausas' : 1 }")
    List<Monopatin> findAllDataReportePorTiempoDePausas();

    @Query(value = "{}", fields = "{'idMonopatin': 1, 'kmRecorridos': 1, 'tiempoDeUsoTotal': 1, 'tiempoDePausas': 1}")
    List<Monopatin> findAllDataReporteCompleto();
} 