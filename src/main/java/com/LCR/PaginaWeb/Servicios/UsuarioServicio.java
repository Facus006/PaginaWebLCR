package com.LCR.PaginaWeb.Servicios;

import com.LCR.PaginaWeb.Entidades.Rol;
import com.LCR.PaginaWeb.Entidades.Usuario;
import com.LCR.PaginaWeb.Errores.MyException;
import com.LCR.PaginaWeb.Repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuariorepo;

    @Transactional
    public void registrar(String nombre, String apellido, String email,
            String nombreUsuario, String password, String password2)
            throws MyException {

        validar(nombre, apellido, email, nombreUsuario, password, password2);
        validar2(email, nombreUsuario);

        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setEmail(email);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        usuariorepo.save(usuario);

    }

    private void validar(String nombre, String apellido, String email, String nombreUsuario,
            String password, String password2) throws MyException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MyException("EL nombre no puede estar vacio.");
        }
        if (apellido.isEmpty() || apellido == null) {
            throw new MyException("EL apellido no puede estar vacio.");
        }
        if (email.isEmpty() || email == null) {
            throw new MyException("EL email no puede estar vacio.");
        }
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MyException("EL nombreUsuario no puede estar vacio.");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MyException("La contraseña no puede estar vacia y tiene que tener mas de 5 caracteres.");
        }
        if (!password.equals(password2)) {
            throw new MyException("Las contraseñas no coiciden.");
        }

    }

    public void validar2(String email, String nombreUsuario) throws MyException {
        if (usuariorepo.buscarPorEmail(email) != null) {
            throw new MyException("ERROR, EMAIL YA SE ENCUENTRA EN USO!");
        }
        if (usuariorepo.buscarPorNombreUsuario(nombreUsuario) != null) {
            throw new MyException("ERROR, EL NOMBRE DE USUARIO YA SE ENCUENTRA EN USO!");
        }

    }

    public Usuario getOne(String id) {
        return usuariorepo.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = usuariorepo.findAll();

        return usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuariorepo.buscarPorEmail(email);

        List<GrantedAuthority> permisos = new ArrayList();
        GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
        permisos.add(p);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession();
        session.setAttribute("usuariosession", usuario);

        return new User(usuario.getEmail(), usuario.getPassword(), permisos);

    }

}
