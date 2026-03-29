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
        //System.out.println("Digite o cpf");
        //cpf = tec.nextLine();//04802936940
        
        //atualizado 29.03.2026 para validar quantidade de dígitos cpf
        boolean cpfValido = false;
        
        while (!cpfValido) {
            System.out.println("Digite o CPF (apenas 11 números):");
            String entrada = tec.nextLine();
            
            // Remove espaços ou pontos caso o usuário digite (opcional, mas bom)
            entrada = entrada.replaceAll("\\D", "");
            
            if (entrada.length() != 11) {
            System.out.println("Erro: O CPF deve ter exatamente 11 dígitos! Você digitou: " + entrada.length());
            } else {
            this.cpf = entrada; // Salva na variável da classe
            cpfValido = true;   // Sai do loop
            }
        }
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


