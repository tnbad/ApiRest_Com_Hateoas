package one.digitalinnovation.gof.model;

import javax.persistence.*;

import org.springframework.hateoas.RepresentationModel;

@Entity
public class Cliente extends RepresentationModel<Cliente>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  String nome;
    @ManyToOne
    private Endereco endereco;

    // geters e seters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
