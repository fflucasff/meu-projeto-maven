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
        assertEquals(26,a);
    }
    @Test
    public void testeErroGetNome(){
        Pessoa p = new Pessoa("Otavio",24);
        String a = p.getName();
        assertEquals("Davi",a);
    };
}
