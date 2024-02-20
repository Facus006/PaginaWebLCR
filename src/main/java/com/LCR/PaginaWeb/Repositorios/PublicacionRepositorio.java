package com.LCR.PaginaWeb.Repositorios;

import com.LCR.PaginaWeb.Entidades.Publicacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepositorio extends JpaRepository<Publicacion, String> {

    @Query("SELECT p FROM Publicacion p WHERE p.categoria = :categoria")
    public Publicacion buscarPorCategoria(@Param("categoria") String categoria);

    @Query("SELECT p FROM Publicacion p WHERE p.destacada = true")
    List<Publicacion> obtenerPublicacionesDestacadas();

}
