package com.backend.clinicaDental.service.impl;


import com.backend.clinicaDental.dto.entrada.TurnoEntradaDto;

import com.backend.clinicaDental.dto.salida.TurnoSalidaDto;

import com.backend.clinicaDental.exceptions.BadRequestException;
import com.backend.clinicaDental.exceptions.ResourceNotFoundException;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class TurnoServiceTest {

    @Autowired
    TurnoService turnoService;



    @Test
    @Order(1)
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
    @Order(2)
    void deberiaDevolverUnaListaVaciaDeTurnos() {
        List<TurnoSalidaDto> turnos = turnoService.listarTurnos();
        assertTrue(turnos.isEmpty());
    }

}