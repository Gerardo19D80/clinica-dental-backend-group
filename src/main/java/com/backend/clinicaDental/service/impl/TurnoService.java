package com.backend.clinicaDental.service.impl;

import com.backend.clinicaDental.dto.entrada.TurnoEntradaDto;
import com.backend.clinicaDental.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaDental.dto.salida.PacienteSalidaDto;
import com.backend.clinicaDental.dto.salida.TurnoSalidaDto;

import com.backend.clinicaDental.entity.Turno;
import com.backend.clinicaDental.exceptions.BadRequestException;
import com.backend.clinicaDental.exceptions.ResourceNotFoundException;
import com.backend.clinicaDental.repository.TurnoRepository;
import com.backend.clinicaDental.service.ITurnoService;
import com.backend.clinicaDental.utils.JsonPrinter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class TurnoService implements ITurnoService {

    private final Logger LOGGER = LoggerFactory.getLogger(TurnoService.class);
    private final TurnoRepository turnoRepository;
    private final ModelMapper modelMapper;

    private final PacienteService pacienteService;
    private final OdontologoService odontologoService;

    public TurnoService(TurnoRepository turnoRepository, ModelMapper modelMapper, PacienteService pacienteService, OdontologoService odontologoService) {
        this.turnoRepository = turnoRepository;
        this.modelMapper = modelMapper;
        this.pacienteService = pacienteService;
        this.odontologoService = odontologoService;
    }

    // *** METODO 1 --- REGISTRAR TURNO --- ***
    @Override
    public TurnoSalidaDto registrarTurno(TurnoEntradaDto turnoEntradaDto) throws BadRequestException {
        TurnoSalidaDto turnoSalidaDto;
        PacienteSalidaDto paciente = pacienteService.buscarPacientePorId(turnoEntradaDto.getPacienteId());
        OdontologoSalidaDto odontologo = odontologoService.buscarOdontologoPorId(turnoEntradaDto.getOdontologoId());
        //modelMapper.getConfiguration().setAmbiguityIgnored(true);

        //ahorrando memoria para devolver el mismo mensaje en lugares distintos
        String pacienteNoEnBdd = "El paciente no se encuentra en nuestra base de datos";
        String odontologoNoEnBdd = "El odontologo no se encuentra en nuestra base de datos";
        String ambosNulos = "El paciente y el odontologo no se encuentran en nuestra base de datos";

        if (paciente == null || odontologo == null) {
            if (paciente == null && odontologo == null) {
                LOGGER.error(ambosNulos);
                throw new BadRequestException(ambosNulos);
            } else if (paciente == null) {
                LOGGER.error(pacienteNoEnBdd);
                throw new BadRequestException(pacienteNoEnBdd);
            } else {
                LOGGER.error(odontologoNoEnBdd);
                throw new BadRequestException(odontologoNoEnBdd);
            }

        } else {
            Turno turnoNuevo = turnoRepository.save(modelMapper.map(turnoEntradaDto, Turno.class));
            turnoSalidaDto = entidadADtoSalida(turnoNuevo, paciente, odontologo);
            LOGGER.info("Nuevo turno registrado con exito: {}", turnoSalidaDto);
        }

        return turnoSalidaDto;
    }

    // *** METODO 2 --- LISTAR TURNO --- ***
    @Override
    public List<TurnoSalidaDto> listarTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        List<TurnoSalidaDto> turnosSalidaDto = new ArrayList<>();

        for (Turno turno : turnos) {
            PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(turno.getPaciente().getId());
            OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(turno.getOdontologo().getId());

            TurnoSalidaDto turnoSalidaDto = entidadADtoSalida(turno, pacienteSalidaDto, odontologoSalidaDto);
            turnosSalidaDto.add(turnoSalidaDto);
        }

        LOGGER.info("Listado de todos los turnos: {}", JsonPrinter.toString(turnosSalidaDto));
        return turnosSalidaDto;
    }

    // *** METODO 3 --- BUSCAR TURNO --- ***
    @Override
    public TurnoSalidaDto buscarTurnoPorId(Long id) {
        Turno turnoBuscado = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoEncontrado = null;

        if (turnoBuscado != null) {
            PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(turnoBuscado.getPaciente().getId());
            OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(turnoBuscado.getOdontologo().getId());

            turnoEncontrado = entidadADtoSalida(turnoBuscado, pacienteSalidaDto, odontologoSalidaDto);
            LOGGER.info("Turno encontrado: {}", JsonPrinter.toString(turnoEncontrado));
        } else {
            LOGGER.error("No se ha encontrado el turno con id {}", id);
        }

        return turnoEncontrado;
    }

    // *** METODO 4 --- ELIMINAR TURNO --- ***
    @Override
    public void eliminarTurno(Long id) throws ResourceNotFoundException {
        if (buscarTurnoPorId(id) != null){
            turnoRepository.deleteById(id);
            LOGGER.warn("Se ha eliminado el turno con id {}", id);
        } else {

            throw new ResourceNotFoundException("No existe registro de turno con id " + id);
        }
    }

    // *** METODO 5 --- MODIFICAR TURNO --- ***
    @Override
    public TurnoSalidaDto modificarTurno(TurnoEntradaDto turnoEntradaDto, Long id) {
        Turno turnoRecibido = modelMapper.map(turnoEntradaDto, Turno.class);
        Turno turnoAActualizar = turnoRepository.findById(id).orElse(null);
        TurnoSalidaDto turnoSalidaDto = null;

        if (turnoAActualizar != null) {
            turnoAActualizar.setOdontologo(turnoRecibido.getOdontologo());
            turnoAActualizar.setPaciente(turnoRecibido.getPaciente());
            turnoAActualizar.setFechaYHora(turnoRecibido.getFechaYHora());
            turnoRepository.save(turnoAActualizar);

            PacienteSalidaDto pacienteSalidaDto = pacienteService.buscarPacientePorId(turnoAActualizar.getPaciente().getId());
            OdontologoSalidaDto odontologoSalidaDto = odontologoService.buscarOdontologoPorId(turnoAActualizar.getOdontologo().getId());

            turnoSalidaDto = entidadADtoSalida(turnoAActualizar, pacienteSalidaDto, odontologoSalidaDto);

            LOGGER.warn("Turno actualizado: {}", turnoSalidaDto);

        } else {
            LOGGER.error("No fue posible actualizar los datos ya que el turno no se encuentra registrado");
        }

        return turnoSalidaDto;
    }

    private TurnoSalidaDto entidadADtoSalida(Turno turno, PacienteSalidaDto pacienteSalidaDto, OdontologoSalidaDto odontologoSalidaDto) {
        TurnoSalidaDto turnoSalidaDto = modelMapper.map(turno, TurnoSalidaDto.class);
        turnoSalidaDto.setPacienteSalidaDto(pacienteSalidaDto);
        turnoSalidaDto.setOdontologoSalidaDto(odontologoSalidaDto);
        return turnoSalidaDto;
    }

}
