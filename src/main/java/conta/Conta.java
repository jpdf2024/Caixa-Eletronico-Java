
package conta;

import cliente.Pessoa;
        
public class Conta {
    private float saldo;
    private int numConta;
    private Pessoa titular;
    private int tipoConta;

    public Conta() { super(); }

    public int getNumConta() { return numConta; }

    public void setNumConta(int numConta) { this.numConta = numConta; }

    public float getSaldo() { return saldo; }

    public void setSaldo(float saldo) { this.saldo = saldo; }

    public int getTipoConta() { return tipoConta; }

    public void setTipoConta(int tipoConta) { this.tipoConta = tipoConta; }

    public Pessoa getTitular() { return titular; }

    public void setTitular(Pessoa titular) { this.titular = titular; }
}