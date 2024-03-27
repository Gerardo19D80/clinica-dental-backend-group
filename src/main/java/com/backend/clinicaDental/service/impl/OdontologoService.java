package com.backend.clinicaDental.service.impl;

import com.backend.clinicaDental.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaDental.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaDental.entity.Odontologo;

import com.backend.clinicaDental.repository.OdontologoRepository;
import com.backend.clinicaDental.service.IOdontologoService;

import com.backend.clinicaDental.utils.JsonPrinter;
import org.modelmapper.ModelMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OdontologoService implements IOdontologoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OdontologoService.class);

    private OdontologoRepository odontologoRepository;
    private ModelMapper modelMapper;

    public OdontologoService(OdontologoRepository odontologoRepository, ModelMapper modelMapper) {
        this.odontologoRepository = odontologoRepository;
        this.modelMapper = modelMapper;
    }

    // *** METODO 1 --- REGISTRAR ODONTOLOGO --- ***
    @Override
    public OdontologoSalidaDto registrarOdontologo(OdontologoEntradaDto odontologo) {

        LOGGER.info("OdontologoEntradaDto: " + JsonPrinter.toString(odontologo));

        Odontologo odontologoEntidad = modelMapper.map(odontologo, Odontologo.class);
        Odontologo odontologoEntidadconId = odontologoRepository.save(odontologoEntidad);
        OdontologoSalidaDto odontologoSalidaDto = modelMapper.map(odontologoEntidadconId, OdontologoSalidaDto.class);
        LOGGER.info("OdontologoentradaDto: " + JsonPrinter.toString(odontologoSalidaDto));
        return odontologoSalidaDto;
    }

    // *** METODO 2 --- LISTAR ODONTOLOGOS --- ***
    @Override
    public List<OdontologoSalidaDto> listarOdontologos() {
        List<OdontologoSalidaDto> odontologosSalidaDto = odontologoRepository.findAll()
                .stream()
                .map(odontologo -> modelMapper.map(odontologo, OdontologoSalidaDto.class))
                .toList();
        LOGGER.info("Listado de todos los odontologos: {}", JsonPrinter.toString(odontologosSalidaDto));

        return odontologosSalidaDto;
    }

    // *** METODO 3 --- BUSCAR ODONTOLOGO POR ID --- ***
    @Override
    public OdontologoSalidaDto buscarOdontologoPorId(Long id) {
        Odontologo odontologoBuscado = odontologoRepository.findById(id).orElse(null);
        OdontologoSalidaDto odontologoEncontrado = null;

        if(odontologoBuscado != null){
            odontologoEncontrado = modelMapper.map(odontologoBuscado, OdontologoSalidaDto.class);
            LOGGER.info("Odontologo encontrado: {}", JsonPrinter.toString(odontologoEncontrado));
        } else LOGGER.error("El ID no se encuentra registrado en la base de datos");

        return odontologoEncontrado;
    }


    // *** METODO 4 --- ELIMINAR ODONTOLOGO --- ***
    @Override
    public void eliminarOdontologo (Long id) {
        if (buscarOdontologoPorId(id) != null) {
            odontologoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el odontologo con id: {}", id);
        } else {
            LOGGER.error("No se ha encontrado el odontologo con id {}", id);

        }
    }

    // *** METODO 5 --- MODIFICAR ODONTOLOGO --- ***

    @Override
    public OdontologoSalidaDto modificarOdontologo (OdontologoEntradaDto odontologoEntradaDto, Long id) {
        Odontologo odontologoRecibido = modelMapper.map(odontologoEntradaDto, Odontologo.class);
        Odontologo odontologoAActualizar = odontologoRepository.findById(id).orElse(null);
        OdontologoSalidaDto odontologoSalidaDto = null;

        if (odontologoAActualizar != null) {

            odontologoAActualizar.setNombre(odontologoRecibido.getNombre());
            odontologoAActualizar.setApellido(odontologoRecibido.getApellido());
            odontologoAActualizar.setMatricula(odontologoRecibido.getMatricula());
            odontologoRepository.save(odontologoAActualizar);

            odontologoSalidaDto = modelMapper.map(odontologoAActualizar, OdontologoSalidaDto.class);

            LOGGER.warn("Odontologo actualizado: {}", odontologoSalidaDto);

        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el odontologo no se encuentra registrado");

        }

        return odontologoSalidaDto;
    }

}