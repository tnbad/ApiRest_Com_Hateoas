package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.model.Cliente;

/**
 * Interface que define o padrao Strategy no dominio de cliente.
 * Com isso, se necessario, pdemos ter multiplas inplementacoes dessa mesma interface.
 *
 * @author aula - Dio/falvojr -> (Thiago)
 */

public interface ClienteService {
    Iterable<Cliente> buscarTodos();
    Cliente buscarPorId(Long id);

    void inserir(Cliente cliente);
    void atualizar(Long id, Cliente cliente);
    void deletar(Long id);
}
