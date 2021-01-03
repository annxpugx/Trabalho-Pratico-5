import java.io.*;

public class Rehash {
    public static void main(String[] args){

        String[] entrada = new String[1000];
        int numEntrada = 0;       
        // Leitura da entrada padrao
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids[] = new int[numEntrada];

        for(int i = 0; i < numEntrada; i++){
            ids[i] = Integer.parseInt(entrada[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[4000];

        try{
            entrada2 = ler(); // leitura das linhas do arquivo
        }catch(Exception e){
            MyIO.println("Erro ao ler arquivo");
        }

        TabelaHash hash = new TabelaHash(); 

        for(int i = 0; i < numEntrada; i++){
            Jogador j = new Jogador(entrada2[ids[i]]); 
            hash.inserir(j);
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        for(int i = 0; i < numEntrada; i++){
            if(hash.pesquisar(entrada[i]))
                MyIO.println(entrada[i] + " SIM");
            else
                MyIO.println(entrada[i] + " NAO");
        }

    }

    public static boolean isFim(String s) {
        return s.equals("FIM");
    }

    public static String[] ler() throws Exception {

        String[] entrada = new String[4000];
        int numEntrada = 0;
        File file = new File("/tmp/players.csv");

        BufferedReader br = new BufferedReader(new FileReader(file));
        // Leitura da entrada padrao
        String lixo = br.readLine();
        do {
            entrada[numEntrada] = br.readLine();
        } while (entrada[numEntrada++] != null);
        numEntrada--;

        br.close();
        return entrada;
    }
}

class Jogador {
    private int id;
    private String nome;
    private int altura;
    private int peso;
    private String universidade;
    private int anoNascimento;
    private String cidadeNascimento;
    private String estadoNascimento;

    public Jogador() {
    }

    public Jogador(String linha) {
        String campos[] = linha.split(",");
        this.id = Integer.parseInt(campos[0]);
        this.nome = campos[1];
        this.altura = Integer.parseInt(campos[2]);
        this.peso = Integer.parseInt(campos[3]);
        this.universidade = (campos[4].isEmpty()) ? "nao informado" : campos[4];
        this.anoNascimento = Integer.parseInt(campos[5]);
        if (campos.length > 6) {
            this.cidadeNascimento = (campos[6].isEmpty())? "nao informado": campos[6];
            if (campos.length < 8) {
                this.estadoNascimento = "nao informado";
            } else {
                this.estadoNascimento = campos[7];
            }
        } else {
            this.cidadeNascimento = "nao informado";
            this.estadoNascimento = "nao informado";
        }
    }

    // id,Player,height,weight,collage,born,birth_city,birth_state
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public void setAnoNascimento(int anoNascimento){
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento(){
        return anoNascimento;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public String getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(String cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public String getEstadoNascimento() {
        return estadoNascimento;
    }

    public void setEstadoNascimento(String estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public void clone(Jogador J) {

        this.setId(J.getId());
        this.setCidadeNascimento(J.getCidadeNascimento());
        this.setEstadoNascimento(J.getEstadoNascimento());
        this.setNome(J.getNome());
        this.setAltura(J.getAltura());
        this.setPeso(J.getPeso());
        this.setAnoNascimento(J.getAnoNascimento());
        this.setUniversidade(J.getUniversidade());

    }

    public String toString() {
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## " +  getAnoNascimento()
        + " ## " + getUniversidade()+ " ## " + getCidadeNascimento() + " ## " + getEstadoNascimento() + "]";
        return str;
    }
}

class TabelaHash {

    private String tabela[] = new String[21];

    private int hash(int altura){
        return (int) altura % tabela.length;
    }

    private int rehash(int altura){
        return (int) (++altura) % tabela.length;
    }

    public void inserir(Jogador j){
        inserir(j.getNome(), j.getAltura());
    }

    private void inserir(String s, int a){

        int x = hash(a);

        if(x < tabela.length && tabela[x] == null){
            tabela[x] = s;
        } else {
            x = rehash(a);
            if(x < tabela.length && tabela[x] == null)
                tabela[x] = s;
        }
    }

    public boolean pesquisar(String s){

        boolean resp = false;

        for(int i = 0; i < tabela.length; i++){

            if(tabela[i] != null && tabela[i].compareTo(s) == 0){
                resp = true;
                i = tabela.length;
            }

        }

        return resp;
    }
}

