package com.LCR.PaginaWeb.Controladores;

import com.LCR.PaginaWeb.Servicios.PublicacionServicio;
import com.LCR.PaginaWeb.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class UserController {

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private PublicacionServicio publicacionServicio;

    @GetMapping("/")
    public String index(ModelMap modelo) {
        modelo.addAttribute("publicaciones", publicacionServicio.obtenerPublicacionesDestacadas());
        return "inicio";
    }

    @GetMapping("/inicio") //este es el inicio despues de logearse
    public String inicio(ModelMap modelo) {
        modelo.addAttribute("publicaciones", publicacionServicio.obtenerPublicacionesDestacadas());
        return "inicio";
    }

    // Vista para mostrar todos los mods
    @GetMapping("/mods")
    public String listarMods(ModelMap modelo) {
        modelo.addAttribute("publicaciones", publicacionServicio.listarPublicaciones());
        return "mods";
    }

    // registro
    @GetMapping("/registrar") // localhost:8080
    public String registrar() {
        return "UsuarioFormRegistro";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String email, @RequestParam String password,
            String password2, @RequestParam String nombreUsuario, ModelMap modelo) {

        try {
            usuarioServicio.registrar(nombre, apellido, email,
                    nombreUsuario, password, password2);
            modelo.put("exito", "El usuario se registro correctamente.");
            return "login";
        } catch (Exception ex) {
            System.out.println("error en " + ex);
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("email", email);
            modelo.put("nombreUsuario", nombreUsuario);

            return "UsuarioFormRegistro.html";
        }
    }

    // login
    @GetMapping("/loguear") // localhost:8080
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválido.");
        }

        return "login";
    }

    //Esta ruta es para Mostrar solo una publicacion.
    @GetMapping("/{id}") //localhost:8080
    public String verPublicacion(@PathVariable("id") String id, ModelMap modelo) {
        modelo.addAttribute("publicacion", publicacionServicio.getOne(id));
        return "Publicacion";
    }

    //Esta ruta es para Mostrar publicaciones por categorias
    @GetMapping("/categorias/{categoria}")
    public String categoriaPublicacion(ModelMap modelo, @PathVariable("categoria") String categoria) {
        modelo.addAttribute("publicaciones", publicacionServicio.publicacionesxCategoria(categoria));
        return "mods";
    }

    //Esta ruta es para hacer una busqueda personalizada
    @GetMapping("/buscar")
    public String buscarusuario(String consulta, ModelMap modelo) {
        modelo.addAttribute("publicaciones", publicacionServicio.busquedaPersonalizada(consulta));
        return "mods";
    }

}
