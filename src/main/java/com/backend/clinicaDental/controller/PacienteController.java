package com.backend.clinicaDental.controller;

import com.backend.clinicaDental.dto.entrada.PacienteEntradaDto;
import com.backend.clinicaDental.dto.salida.PacienteSalidaDto;
import com.backend.clinicaDental.exceptions.ResourceNotFoundException;
import com.backend.clinicaDental.service.IPacienteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

//La anotación @Controller indica que una clase particular cumple la función de un controlador.
//El patrón Controlador sirve como intermediario entre una interfaz y el algoritmo que implementa, de tal forma que es la que recibe los datos del usuario y la que los envía a las distintas clases según el método llamado.

@RestController // @Rest controller = @ResponseBody + @Controller
@RequestMapping("/pacientes")
public class PacienteController {
    private IPacienteService pacienteService;
    public PacienteController(IPacienteService pacienteService) {

        this.pacienteService = pacienteService;
    }

    //GET

    @GetMapping() // opcional ("/listar")
    public ResponseEntity<List<PacienteSalidaDto>> listarPacientes(){
        return new ResponseEntity<>(pacienteService.listarPacientes(), HttpStatus.OK);
    }

    @GetMapping("/{id}") //localhost:8085/pacientes/x
    public ResponseEntity<PacienteSalidaDto> buscarPacientePorId(@PathVariable Long id){
        return new ResponseEntity<>(pacienteService.buscarPacientePorId(id), HttpStatus.OK);
    }


    //POST
    @PostMapping("/registrar")
    public ResponseEntity<PacienteSalidaDto> registrarPaciente(@RequestBody @Valid PacienteEntradaDto paciente){
        return new ResponseEntity<>(pacienteService.registrarPaciente(paciente), HttpStatus.CREATED);
    }


    //PUT
    @PutMapping("/actualizar/{id}")//localhost:8085/pacientes/actualizar/x
    public ResponseEntity<PacienteSalidaDto> actualizarPaciente(@RequestBody @Valid PacienteEntradaDto paciente, @PathVariable Long id) throws ResourceNotFoundException {
        return new ResponseEntity<>(pacienteService.modificarPaciente(paciente, id), HttpStatus.OK);
    }

    //DELETE
    @DeleteMapping("/eliminar")//localhost:8085/pacientes/eliminar?id=x
    public ResponseEntity<?> eliminarPaciente(@RequestParam Long id) throws ResourceNotFoundException {
        pacienteService.eliminarPaciente(id);
        return new ResponseEntity<>("Paciente eliminado correctamente", HttpStatus.NO_CONTENT);
    }



    //Otro tema | clase 15:

    /* Ejercicio para vincular con el HTML. Se vio en una de las clases y apra ello creamos el template paciente.HTML.

    @GetMapping("/buscarPorId")
    public String buscarPacientePorId (Model model, @RequestParam int id){
        Paciente paciente = pacienteService.buscarPacientePorId(id);
        model.addAttribute("nombre", paciente.getNombre());
        model.addAttribute("apellido", paciente.getApellido());

        return "paciente";
    }
    */

}
