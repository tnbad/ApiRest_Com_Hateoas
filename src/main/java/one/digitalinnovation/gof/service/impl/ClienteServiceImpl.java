package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementacao da Strategy {@link 'ClienteService'}, a qual pode ser
 * injetada pelo spring (via {@link 'Autowired'}). Com isso, como essa clase é um
 * {@link 'Service'}, ela sera tratada como um Singleton.
 *
 * @author aula - Dio/falvojr -> (Thiago)
 */

@Service
public class ClienteServiceImpl implements ClienteService {
    //Todo Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    //Todo Strategy: Implementar os métodos definidos na interface.
    //Todo Facade: Abstrair as integracoes com subsistemas, provendo uma interface simples.
    @Override
    public Iterable<Cliente> buscarTodos() {
        // Buscar todos Clientes.
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        // Buscar cliente por id
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        // buscar Cliente por id, caso exista:
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            // Verificar se o endereco do cliente ja existe (pelo CEP).
            // Caso nao exista, intergrar com o viaCEP e persistir o retorno.
            // Alterar Cliente, vinculando o endereco (novo ou exixtente).
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // deletar cliente pelo id.
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        // Verificar se o cliente ja existe (pelo cep).
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // caso nao exista, integrar com o viaCEP e persistir o retorno.
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);

        // Inserir o cliente, cinculando o endereco (novo ou existente).
        clienteRepository.save(cliente);
    }

}
