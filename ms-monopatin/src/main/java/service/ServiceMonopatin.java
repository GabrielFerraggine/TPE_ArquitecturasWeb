package service;

import entity.Monopatin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.RepositoryMonopatin;

@Service
public class ServiceMonopatin {

    @Autowired
    private RepositoryMonopatin repoMonopatin;

    public Monopatin buscarMonopatinPorId(Long idMonopatin) {
        return repoMonopatin.buscarPorId(idMonopatin).orElseThrow(() -> new RuntimeException("Monopatin no encontrado D:"));
    }
}

