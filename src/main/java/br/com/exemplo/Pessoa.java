package br.com.exemplo;
import com.google.gson.Gson;
public class Pessoa {
    private String nome;
    private int idade;
    private int passosDados;
    private String profissao;

    public Pessoa(String nome, int idade, String profissao) {
        this.nome = nome;
        this.idade = idade;
        this.passosDados = 0;
        this.profissao = profissao;
    }
    public String criarJson(Pessoa pessoa){
        Gson gson = new Gson();
        String json = gson.toJson(pessoa);
        System.out.println("Objeto em JSON: " + json);
        return json;
    };
    public int getIdade(){
      return this.idade;
    };
    public String getName(){
      return this.nome;
    };

    public String falar(String mensagem) {
        return nome + " diz: \"" + mensagem + "\"";
    }

    public void andar(int passos) {
        this.passosDados += passos;
    }

    public int getPassosDados() {
        return this.passosDados;
    }

    public String comer(String comida) {
        return nome + " está comendo " + comida + ".";
    }

    public void dormir(int horas) {
        if (horas < 0) {
            throw new IllegalArgumentException("Horas de sono não podem ser negativas.");
        }
    }

    public void fazerAniversario() {
        this.idade++;
    }

    public String estudar(String materia) {
        return nome + " está estudando " + materia + ".";
    }

    public String trabalhar() {
        return nome + " está trabalhando como " + this.profissao + ".";
    }

}