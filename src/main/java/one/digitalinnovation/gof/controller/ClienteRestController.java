package one.digitalinnovation.gof.controller;


import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Esse {@link 'RestController'} representa nossa <b>Facade</b>, pois abstrai toda
 * complexidade de integracoes (Banco de dados H2 e API do ViaCEP) em uma
 * interface simples e coesa (API REST).
 *
 * @author  aula - Dio/falvojr -> (Thiago)
 * Modificacoes para insercao de links Hateoas.
 *
 * */
@RestController
@RequestMapping("clientes")
public class ClienteRestController {
    @Autowired
    private final ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService;

    public ClienteRestController(ClienteRepository clienteRepository, ClienteService clienteService) {
        this.clienteRepository = clienteRepository;
        this.clienteService = clienteService;
    }

    @GetMapping()
    ResponseEntity<List<Cliente>> buscarTodos() {
//        return ResponseEntity.ok(clienteService.buscarTodos());
        long idOrder;
        Link linkUri;
        List<Cliente> clienteList = (List<Cliente>) clienteRepository.findAll();
        if (clienteList.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        for (Cliente cliente:clienteList) {
            idOrder = cliente.getId();
            linkUri = linkTo(methodOn(ClienteRestController.class).buscarPorId(idOrder)).withRel("Dados Individuais do Cliente: "+ cliente.getNome());
            cliente.add(linkUri);
            linkUri = linkTo(methodOn(ClienteRestController.class).buscarTodos()).withRel("Todos os Clientes");
            cliente.add(linkUri);
        }
        return new ResponseEntity<List<Cliente>>(clienteList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
        //return ResponseEntity.ok(clienteService.buscarPorId(id));
            Optional<Cliente> clientPointer = clienteRepository.findById(id);
            if (clientPointer.isPresent()) {
                Cliente client = clientPointer.get();
                client.add(linkTo(methodOn(ClienteRestController.class).buscarTodos()).withRel("Todos Os Clientes"));
                return new ResponseEntity<>(client, HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Cliente> inserir(@RequestBody Cliente cliente) {
        clienteService.inserir(cliente);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente){
        clienteService.atualizar(id, cliente);
        return ResponseEntity.ok(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.ok().build();
    }

}
