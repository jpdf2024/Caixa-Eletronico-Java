package cliente;

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
        System.out.println("Digite o telefone");
        telefone = tec.nextLine();
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