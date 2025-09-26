import br.com.exemplo.Pessoa;
import org.junit.Test;
import com.google.gson.Gson;
import static org.junit.Assert.*;

public class PessoaTest {
    @Test
    public void testeGetIdade(){
        Pessoa p = new Pessoa("Otavio",24);
        int a = p.getIdade();
        assertEquals(24,a);
    }
    @Test
    public void testeGetNome(){
        Pessoa p = new Pessoa("Otavio",24);
        String a = p.getName();
        assertEquals("Otavio",a);
    };
    @Test
    public void deveSerializarObjetoSimples() {
        Pessoa p = new Pessoa("Ana", 30);
        String json = p.criarJson(p);
        assertTrue(json.contains("\"nome\":\"Ana\""));
        assertTrue(json.contains("\"idade\":30"));
    }

    @Test
    public void deveDeserializarObjetoSimples() {
        String json = "{\"nome\":\"Carlos\",\"idade\":25}";
        Gson gson = new Gson();
        Pessoa p = gson.fromJson(json, Pessoa.class);
        assertEquals("Carlos", p.getName());
        assertEquals(25, p.getIdade());
    }

    @Test
    public void testFalar() {
        Pessoa p = new Pessoa("João", 20);
        assertEquals("João diz: \"Olá!\"", p.falar("Olá!"));
    }

    @Test
    public void testAndar() {
        Pessoa p = new Pessoa("Maria", 30);
        p.andar(100);
        assertEquals(100, p.getPassosDados());
        p.andar(50);
        assertEquals(150, p.getPassosDados());
    }

    @Test
    public void testFazerAniversario() {
        Pessoa p = new Pessoa("Carlos", 40);
        p.fazerAniversario();
        assertEquals(41, p.getIdade());
    }

    @Test
    public void testComer() {
        Pessoa p = new Pessoa("Lucas", 18);
        assertEquals("Lucas está comendo pizza.", p.comer("pizza"));
    }

    @Test
    public void testEstudar() {
        Pessoa p = new Pessoa("Paula", 22);
        assertEquals("Paula está estudando Matemática.", p.estudar("Matemática"));
    }

    @Test
    public void testTrabalhar() {
        Pessoa p = new Pessoa("Jorge", 35);
        p.setProfissao("Engenheiro");
        assertEquals("Jorge está trabalhando como Engenheiro.", p.trabalhar());
    }

    @Test
    public void testDormirHorasInvalidas() {
        Pessoa p = new Pessoa("Pedro", 28);
        assertThrows(IllegalArgumentException.class, () -> p.dormir(-3));
    }

    //Falhas
    @Test
    public void deveFalharSeJsonInvalido() {
        String json = "{\"nome\": \"Joao\", \"idade\": \"invalid\"}";
        Gson gson = new Gson();
        assertThrows(Exception.class, () -> gson.fromJson(json, Pessoa.class));
    }

    @Test
    public void deveFalharAoAcessarCampoNulo() {
        String json = "{\"nome\":null,\"idade\":20}";
        Gson gson = new Gson();
        Pessoa p = gson.fromJson(json, Pessoa.class);
        assertNull(p.getName());
        assertThrows(NullPointerException.class, () -> p.getName().length());
    }
    @Test
    public void testeErroGetIdade(){
        Pessoa p = new Pessoa("Otavio",24);
        int a = p.getIdade();
        assertNotEquals(26,a);
    }
    @Test
    public void testeErroGetNome(){
        Pessoa p = new Pessoa("Otavio",24);
        String a = p.getName();
        assertNotEquals("Davi",a);
    };
    @Test
    public void testFalarComFalha() {
        Pessoa p = new Pessoa("João", 20);
        assertNotEquals("João fala: \"Olá!\"", p.falar("Olá!")); // errado
    }

    @Test
    public void testAndarComFalha() {
        Pessoa p = new Pessoa("Maria", 30);
        p.andar(50);
        assertNotEquals(100, p.getPassosDados()); // errado
    }

    @Test
    public void testDormirComFalha() {
        Pessoa p = new Pessoa("Pedro", 28);
        assertThrows(IllegalArgumentException.class, () -> p.dormir(-5));
    }

    @Test
    public void testFazerAniversarioComFalha() {
        Pessoa p = new Pessoa("Carlos", 40);
        p.fazerAniversario();
        assertNotEquals(50, p.getIdade());
        // errado
    }
    @Test
    public void testErroTrabalhar() {
        Pessoa p = new Pessoa("Jorge", 35);
        p.setProfissao("Engenheiro");
        assertNotEquals("Rodolfo está trabalhando como Açougueiro.", p.trabalhar());
    }
}
