package com.LCR.PaginaWeb.Controladores;

import com.LCR.PaginaWeb.Entidades.Usuario;
import com.LCR.PaginaWeb.Errores.MyException;
import com.LCR.PaginaWeb.Servicios.PublicacionServicio;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/publicacion")
public class PublicacionControlador {

    @Autowired
    private PublicacionServicio publicacionServicio;

    //Esta ruta es para Crear una publicacion
    @GetMapping("/crear") //localhost:8080
    public String crear() {
        return "publicacion_registro.html";
    }

    //crear publicacion
    @PostMapping("/crear")
    public String crear(@RequestParam(required = false) String descripcion, @RequestParam String titulo, @RequestParam Integer precio, HttpSession session,
            @RequestParam List<MultipartFile> archivos, @RequestParam String categoria, ModelMap modelo) {

        try {
            publicacionServicio.registrarPublicacion(titulo, precio, descripcion, (Usuario) session.getAttribute("usuariosession"), categoria.toUpperCase(), archivos);
            modelo.put("exito", "Plublicacion ok");
            return "publicacion_registro.html";

        } catch (MyException e) {

            modelo.put("error", e.getMessage());
            return "publicacion_registro.html";

        }

    }

    //Esta ruta es para Eliminar una publicacion
    @PostMapping("/borrar/{id}")
    public String eliminar(@PathVariable("id") String id, ModelMap modelo) throws MyException {
        try {
            publicacionServicio.eliminarPublicacionPorId(id);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "redirect:/";
    }

    //Esta ruta es para agegar una publicacion a Destacadas
    @GetMapping("/destacar/{id}")
    public String agregarDestacada(@PathVariable("id") String id, ModelMap modelo) throws MyException {
        try {
            publicacionServicio.agregarDestacada(id);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "redirect:/publicacion/listar";
    }

    //Esta ruta es para eliminar una publicacion de Destacadas
    @GetMapping("/eliminarDestacada/{id}")
    public String eliminarDestacada(@PathVariable("id") String id, ModelMap modelo) throws MyException {
        try {
            publicacionServicio.eliminarDestacada(id);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "redirect:/publicacion/listar";
    }

    //Esta ruta hace una lista de las publicaciones que solo puede acceder el admin(Es para eliminar o Destacar)
    @GetMapping("/listar") //localhost:8080
    public String Lista(ModelMap modelo) {
        modelo.addAttribute("lista", publicacionServicio.listarPublicaciones());
        return "ListaPublicaciones.html";
    }

    @GetMapping("/editar/{id}") //localhost:8080
    public String editar(@PathVariable("id") String id, ModelMap modelo) {
        modelo.addAttribute("publicacion", publicacionServicio.getOne(id));
        return "editarPublicacion.html";
    }

    @PostMapping("/editar/{id}") //localhost:8080
    public String editar(@PathVariable("id") String id, @RequestParam String titulo,
            @RequestParam String descripcion, @RequestParam Integer precio,
            @RequestParam String categoria, ModelMap modelo) {
        modelo.addAttribute("publicacion", publicacionServicio.getOne(id));
        try {
            publicacionServicio.editar(id, titulo, descripcion, precio, categoria);
            modelo.put("exito", "Altoque.");
            return "redirect:/inicio";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
        }
        return "ListaPublicaciones.html";
    }

}
