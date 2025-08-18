package br.com.exemplo;

import com.google.gson.Gson;

public class App {
    public static void main(String[] args) {
        Pessoa p = new Pessoa("Otávio", 24);
        Gson gson = new Gson();
        String json = gson.toJson(p);
        System.out.println("Objeto em JSON: " + json);
    }
}

class Pessoa {
    private String nome;
    private int idade;

    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }
}
