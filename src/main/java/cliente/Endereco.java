
package cliente;

import java.util.Scanner;

/*inclusas atributos da classe, getters and setters 05.02.2026 */
public class Endereco {
    private String rua;
    private int numero;
    private String cidade;
    private String uf;

    public Endereco() {
        super();
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    /* Métodos 06.02.26 */
    public void cadastra(Scanner tec) {
        /*correcao IA - Copilot 08.02.26 */
        /*Scanner tec = new Scanner(System.in);*/
        System.out.println("Digite a rua");
        rua = tec.nextLine();
        System.out.println("Digite o número");
        numero = tec.nextInt();
        /*correcao IA - Copilot 08.02.26 */
        tec.nextLine(); // consome o Enter
        System.out.println("Digite a cidade");
        cidade = tec.nextLine();
        System.out.println("Digite a uf");
        uf = tec.nextLine();
    }

}