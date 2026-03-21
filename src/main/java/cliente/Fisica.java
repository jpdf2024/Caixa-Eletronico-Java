package cliente;

import java.util.Scanner;

public class Fisica extends Pessoa {
    private String cpf;
    
    
    // Sobrescrita do método cadastra
    @Override
    /*correcao IA - Copilot 08.02.26 */
    public void cadastra(Scanner tec) {
        System.out.println("----Cadastro de Pessoa Fisica----");
        super.cadastra(tec);
        /*Scanner tec = new Scanner(System.in);*/
        System.out.println("Digite o cpf");
        cpf = tec.nextLine();
    }

    @Override
    public void imprime() {
        System.out.println("Pessoa Fisica!");
        super.imprime();
        System.out.println("CPF: " + cpf);
    }

    /*incluso getters and setters 05.02.2026 */
    public Fisica() {
        super();
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


}


