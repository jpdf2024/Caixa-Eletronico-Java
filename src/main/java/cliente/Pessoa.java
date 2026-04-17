package cliente;

import database.ContaDAO.ValidadorTelefone;
import java.util.Scanner;

public abstract class Pessoa {
    protected String telefone;
    protected String nomePessoa;
    protected Endereco e = new Endereco();

    // Métodos
    public void cadastra(Scanner tec) {
        /*correcao IA - Copilot 08.02.26 */
        /*Scanner tec = new Scanner(System.in);*/
        System.out.println("Digite o nome");
        nomePessoa = tec.nextLine();
       
        //Atualizado para validar telefone 16.04.26
        //System.out.println("Digite o telefone");
        //telefone = tec.nextLine();
        
        do {
            System.out.print("Digite o telefone (com DDD): ");
            telefone = tec.nextLine();

            // Chamamos o validador. "BR" é o código para o Brasil.
            if (!ValidadorTelefone.validarTelefone(telefone, "BR")) {
                System.out.println("Telefone inválido! Use o formato (43) 99999-9999");
            }
        } while (!ValidadorTelefone.validarTelefone(telefone, "BR"));

        // Se chegou aqui, o telefone é válido!
        this.telefone = telefone;
        
        
        //chamar o método para validar o número do telefone
        e.cadastra(tec);
    }

    public void imprime() {
        /* correcao IA - Copilot 08.02.26        
        System.out.println("Nome: " + getNomePessoa());*/
        System.out.println("Nome: " + nomePessoa);
        System.out.println("Telefone: " + telefone);
        System.out.println("Endereço: " + e.getRua() + ", " + e.getNumero() + ", " + e.getCidade() + "/" + e.getUf());        
    }

    // Getters e Setters
    public Endereco getE() {
        return e;
    }
    public void setE(Endereco e) {
        this.e = e;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }
    
    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

}