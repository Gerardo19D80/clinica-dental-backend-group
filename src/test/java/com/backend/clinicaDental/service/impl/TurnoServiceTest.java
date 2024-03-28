package com.backend.clinicaDental.service.impl;


import com.backend.clinicaDental.dto.entrada.DomicilioEntradaDto;
import com.backend.clinicaDental.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaDental.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaDental.dto.entrada.TurnoEntradaDto;

import com.backend.clinicaDental.dto.salida.OdontologoSalidaDto;
import com.backend.clinicaDental.dto.salida.PacienteSalidaDto;
import com.backend.clinicaDental.dto.salida.TurnoSalidaDto;

import com.backend.clinicaDental.exceptions.BadRequestException;
import com.backend.clinicaDental.exceptions.ResourceNotFoundException;

import com.backend.clinicaDental.repository.OdontologoRepository;
import com.backend.clinicaDental.repository.PacienteRepository;
import com.backend.clinicaDental.service.IPacienteService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {

    @Autowired
    TurnoService turnoService;

    @Autowired
    PacienteService pacienteService;

    @Autowired
    OdontologoService odontologoService;


    @Test
    @Order(1)
    void deberiaRegistrarUnTurnoYRetornarSuId() {

        TurnoSalidaDto turnoSalidaDto = null;

        PacienteEntradaDto paciente = new PacienteEntradaDto();
        OdontologoEntradaDto odontologo = new OdontologoEntradaDto();

        paciente.setApellido("Maradona");
        odontologo.setApellido("Saludable");

        PacienteSalidaDto pacienteRegistrado = pacienteService.registrarPaciente(paciente);
        OdontologoSalidaDto odontologoRegistrado = odontologoService.registrarOdontologo(odontologo);

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto();
        turnoEntradaDto.setPacienteId(pacienteRegistrado.getId());
        turnoEntradaDto.setOdontologoId(odontologoRegistrado.getId());

        try {
            turnoSalidaDto = turnoService.registrarTurno(turnoEntradaDto);
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        assertNotNull(turnoSalidaDto.getId());
    }


    @Test
    @Order(2)
    void deberiaEliminarseElTurnoConId1YLanzarExcepcionAlIntentarEliminarloNuevamente(){

        TurnoEntradaDto turnoEntradaDto = new TurnoEntradaDto(1L,1L, LocalDateTime.of (2024,5,23, 12,50 ));

        try{
            turnoService.eliminarTurno(1L);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertThrows(ResourceNotFoundException.class,() -> turnoService.eliminarTurno(1L));
    }

    @Test
    @Order(3)
    void deberiaDevolverUnaListaVaciaDeTurnos() {
        List<TurnoSalidaDto> turnos = turnoService.listarTurnos();
        assertTrue(turnos.isEmpty());
    }

}