package cliente;

import java.util.Scanner;

public class Juridica extends Pessoa {
    private String cnpj;

    @Override
    /*correcao IA - Copilot 08.02.26 */
    public void cadastra(Scanner tec) {
        System.out.println("----Cadastro de Pessoa Juridica----");
        super.cadastra(tec);
        /*Scanner tec = new Scanner(System.in);*/
        System.out.println("Digite o cnpj");
        cnpj = tec.nextLine();
    }

    @Override
    public void imprime() {
        System.out.println("Pessoa Juridica!");
        super.imprime();
        System.out.println("CNPJ: " + cnpj);
    }

    public Juridica() { super(); }

    /*getters and setters 05.02.2026 */
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
