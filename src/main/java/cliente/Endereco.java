
package cliente;

import database.ContaDAO;
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
    //public void cadastra(Scanner tec) {
    //    /*correcao IA - Copilot 08.02.26 */
    //    /*Scanner tec = new Scanner(System.in);*/
    //    System.out.println("Digite a rua");
    //    rua = tec.nextLine();
    //    System.out.println("Digite o número");
    //    numero = tec.nextInt();
    //    /*correcao IA - Copilot 08.02.26 */
    //    tec.nextLine(); // consome o Enter
    //    System.out.println("Digite a cidade");
    //    cidade = tec.nextLine();
    //    System.out.println("Digite a uf");
    //    uf = tec.nextLine();
    //}
    
    /* Métodos 06.02.26 - Atualizado em 24.03.26 */
    public void cadastra(Scanner tec) {
        System.out.println("Digite a rua");
        rua = tec.nextLine();

        boolean numeroValido = false;
        while (!numeroValido) {
            System.out.println("Digite o número");
            String entrada = tec.nextLine(); // Lemos como String para evitar erro de buffer

            try {
                numero = Integer.parseInt(entrada); // Tenta converter o texto em número
                numeroValido = true; // Se chegar aqui, o número é válido e sai do loop
            } catch (NumberFormatException e) {
                System.out.println("Erro: Digite apenas números inteiros (ex: 123).");
            }
        }

        System.out.println("Digite a cidade");
        cidade = tec.nextLine();
        
         //inclusão de método para validar a UF 07.04.2026
        String uf;
            do {
                System.out.print("Digite a UF (ex: PR): ");
                uf = tec.nextLine().toUpperCase();
        
                if (!ContaDAO.validarUF(uf)) {
                    System.out.println("UF inválida! Tente novamente.");
                }       
            } while (!ContaDAO.validarUF(uf)); // O loop continua enquanto a UF for inválida
    
            this.uf = uf;
        //System.out.println("Digite a uf");
        //uf = tec.nextLine();
    }

}