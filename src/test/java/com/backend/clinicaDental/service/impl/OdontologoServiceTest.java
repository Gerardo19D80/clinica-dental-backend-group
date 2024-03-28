package com.backend.clinicaDental.service.impl;

import com.backend.clinicaDental.dto.entrada.OdontologoEntradaDto;
import com.backend.clinicaDental.dto.salida.OdontologoSalidaDto;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestPropertySource(locations = "classpath:application-test.properties")
class OdontologoServiceTest {

    @Autowired
    OdontologoService odontologoService;

    @Test
    @Order(1)
    void deberiaRegistrarseUnOdontologoDeMatriculaNumero10007_yRetornarSuId() {

        //ARRANGE
        OdontologoEntradaDto odontologoEntradaDto = new OdontologoEntradaDto("10007", "Dr. Ernesto", "Chapatin");

        // ACT | ACTUAR
        OdontologoSalidaDto odontologoSalidaDto = odontologoService.registrarOdontologo(odontologoEntradaDto);

        // ASSERT | COMPROBACIONES
        assertNotNull(odontologoSalidaDto);
        assertNotNull(odontologoSalidaDto.getId());
        assertEquals("10007", odontologoSalidaDto.getMatricula());
    }

    @Test
    @Order(2)
    void deberiaDevolverUnaListaDeOdontologos() {
        List<OdontologoSalidaDto> odontologos = odontologoService.listarOdontologos();
        assertFalse(odontologos.isEmpty());
    }

    @Test
    @Order(3)
    void deberiaModificarUnOdontologoExistente() {

        Long idOdontologoExistente = 1L;
        OdontologoEntradaDto datosNuevos = new OdontologoEntradaDto("10020", "Onto", "Logo");
        OdontologoSalidaDto odontologoModificado = odontologoService.modificarOdontologo(datosNuevos, idOdontologoExistente);

        assertNotNull(odontologoModificado);
        assertEquals(idOdontologoExistente, odontologoModificado.getId());
        assertEquals("10020", odontologoModificado.getMatricula());
        assertEquals("Onto", odontologoModificado.getNombre());
        assertEquals("Logo", odontologoModificado.getApellido());
    }


}