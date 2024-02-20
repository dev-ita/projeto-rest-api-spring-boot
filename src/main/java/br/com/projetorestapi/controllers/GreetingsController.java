package br.com.projetorestapi.controllers;

import br.com.projetorestapi.model.Usuario;
import br.com.projetorestapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GreetingsController {
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private GreetingsController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @RequestMapping(value = "/hello/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String greetingText(@PathVariable String name) {
        return "Hello Spring Boot " + name + "!";
    }

    @RequestMapping(value = "/save/{name},{idade}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String saveUser(@PathVariable String name, @PathVariable int idade) {
        usuarioRepository.save(new Usuario(name, idade));
        return "Hello, World " + name + "!";
    }

    @GetMapping(value = "/listarTodos")
    @ResponseBody // vai retornar os dados para o corpo da resposta;
    // a ResponseEntity é uma classe para construir uma resposta HTTP personalizada
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping(value = "/salvar")
    @ResponseBody // fazer um retorno a requisição após salvar.
    public ResponseEntity<?> salvar(@RequestBody Usuario usuario) { // recebe os dados para salvar
        if (usuario.getNome().isEmpty() || usuario.getIdade() == 0) {
            return new ResponseEntity<>("Erro ao salvar usuário, verifique os campos", HttpStatus.NOT_ACCEPTABLE);
        }
        Usuario u = usuarioRepository.save(usuario);
        return new ResponseEntity<>(u, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/deletar")
    @ResponseBody
    public ResponseEntity<String> delete(@RequestParam Long iduser) {
        usuarioRepository.deleteById(iduser);
        return new ResponseEntity<>("Usuario deletado", HttpStatus.OK);
    }

    @DeleteMapping(value = "/deletar/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteByPath(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return new ResponseEntity<>("Usuario deletado", HttpStatus.OK);
    }

    @GetMapping(value = "/buscarUsuario")
    @ResponseBody
    public ResponseEntity<Usuario> buscarUsuarioPorId(@RequestParam(name = "id") Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario != null) {
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(value = "/atualizarUsuario")
    @ResponseBody
    public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario user) {
        if (usuarioRepository.findById(user.getId()).isEmpty()) {
            return new ResponseEntity<>("Usuario não existe ou Id não informado", HttpStatus.NOT_FOUND);
        }
        Usuario usuario = usuarioRepository.saveAndFlush(user);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @GetMapping(value = "/buscarPorNome")
    @ResponseBody
    public ResponseEntity<List<Usuario>> buscarPorNome(@RequestParam(name = "nome") String nome) {
        List<Usuario> usuarios = usuarioRepository.buscarPorNome(nome.trim().toUpperCase());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(value = "/buscarPorNome/{nome}")
    @ResponseBody
    public ResponseEntity<List<Usuario>> buscarPorNomeByUrl(@PathVariable String nome) {
        List<Usuario> usuarios = usuarioRepository.buscarPorNome(nome.trim().toUpperCase());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
}