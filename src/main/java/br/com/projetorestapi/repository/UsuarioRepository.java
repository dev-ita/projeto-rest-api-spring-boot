package br.com.projetorestapi.repository;

import br.com.projetorestapi.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Transactional
    @Query(value = "select u from Usuario u where upper(trim(u.nome)) like %:nome%")
    List<Usuario> buscarPorNome(@Param("nome") String nome);
}