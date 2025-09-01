package br.com.exemplo;
import com.google.gson.Gson;
public class Pessoa {
    private String nome;
    private int idade;

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
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
}
