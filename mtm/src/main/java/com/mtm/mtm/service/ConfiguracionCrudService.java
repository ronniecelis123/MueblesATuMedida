package com.mtm.mtm.service;

import com.mtm.mtm.model.Configuracion;
import com.mtm.mtm.repository.ConfiguracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracionCrudService {

    @Autowired
    private ConfiguracionRepository repository;

    public List<Configuracion> getAll() {
        return repository.findAll();
    }

    public Configuracion save(Configuracion c) {
        return repository.save(c);
    }

    public Configuracion update(Integer id, Configuracion c) {

        Configuracion existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuracion no encontrada"));

        existente.setLargo(c.getLargo());
        existente.setAncho(c.getAncho());
        existente.setAlto(c.getAlto());
        existente.setPrecioCalculado(c.getPrecioCalculado());

        existente.setUsuario(c.getUsuario());
        existente.setProducto(c.getProducto());
        existente.setMaterial(c.getMaterial());
        existente.setAcabado(c.getAcabado());
        existente.setTipoPata(c.getTipoPata());

        return repository.save(existente);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
    public Configuracion getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Configuracion no encontrada"));
    }
}