import java.io.*;

public class Doidao {
    public static void main(String[] args) {

        String[] entrada = new String[1000];
        int numEntrada = 0;
        // Leitura da entrada padrao
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        int ids[] = new int[numEntrada];

        for (int i = 0; i < numEntrada; i++) {
            ids[i] = Integer.parseInt(entrada[i]); // transformando id em inteiro
        }

        String[] entrada2 = new String[4000];

        try {
            entrada2 = ler(); // leitura das linhas do arquivo
        } catch (Exception e) {
            MyIO.println("Erro ao ler arquivo");
        }

        TabelaHash hash = new TabelaHash();

        for (int i = 0; i < numEntrada; i++) {
            Jogador j = new Jogador(entrada2[ids[i]]);
            hash.inserir(j);
        }

        numEntrada = 0;
        do {
            entrada[numEntrada] = MyIO.readLine();
        } while (isFim(entrada[numEntrada++]) == false);
        numEntrada--; // Desconsiderar ultima linha contendo a palavra FIM

        for (int i = 0; i < numEntrada; i++) {
            if (hash.pesquisar(entrada[i]))
                MyIO.println(" SIM");
            else
                MyIO.println(" NAO");
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
            this.cidadeNascimento = (campos[6].isEmpty()) ? "nao informado" : campos[6];
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

    public void setAnoNascimento(int anoNascimento) {
        this.anoNascimento = anoNascimento;
    }

    public int getAnoNascimento() {
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
        String str = "[" + getId() + " ## " + getNome() + " ## " + getAltura() + " ## " + getPeso() + " ## "
                + getAnoNascimento() + " ## " + getUniversidade() + " ## " + getCidadeNascimento() + " ## "
                + getEstadoNascimento() + "]";
        return str;
    }
}

class Celula {
    public String elemento; // Elemento inserido na celula.
    public Celula prox; // Aponta a celula prox.

    public Celula() {
        this("");
    }

    public Celula(String elemento) {
        this.elemento = elemento;
        this.prox = null;
    }
}

class No {
    public String elemento; // Conteudo do no.
    public No esq, dir; // Filhos da esq e dir.

    public No(String elemento) {
        this(elemento, null, null);
    }

    public No(String elemento, No esq, No dir) {
        this.elemento = elemento;
        this.esq = esq;
        this.dir = dir;
    }
}

class TabelaHash {

    private String tabela[] = new String[11];
    private String T2[] = new String[9];
    private Lista lista = new Lista();
    private ArvoreBinaria arvore = new ArvoreBinaria();

    private int hash(int altura) {
        return (int) altura % tabela.length;
    }

    private int rehash(int altura) {
        return (int) ++altura % tabela.length;
    }

    public void inserir(Jogador j) {
        inserir(j.getNome(), j.getAltura());
    }

    private void inserir(String s, int a) {

        int x = hash(a);

        if (tabela[x] == null) {
            tabela[x] = s;
        } else {
            if (a % 3 == 0) {
                inserirT2(s, a);
            } else if (a % 3 == 1) {
                lista.inserirFim(s);
            } else if (a % 3 == 2) {
                arvore.inserir(s);
            }
        }
    }

    private void inserirT2(String s, int a) {

        int x = hash(a);

        if (x < T2.length && T2[x] == null) {
            T2[x] = s;
        } else {
            x = rehash(a);
            if (x < T2.length && T2[x] == null)
            T2[x] = s;
        }
    }

    public boolean pesquisarT2(String s) {
        boolean resp = false;
        for (int i = 0; i < T2.length; i++) {
            if (T2[i] != null && T2[i].compareTo(s) == 0) {
                resp = true;
                i = T2.length;
            }
        }
        return resp;
    }

    public boolean pesquisar(String s) {
        MyIO.print(s);
        boolean resp = false;
        for (int i = 0; i < tabela.length; i++) {
            if (tabela[i] != null && tabela[i].compareTo(s) == 0) {
                resp = true;
                i = tabela.length;
            }
        }
        if (resp == false) {
            resp = pesquisarT2(s) || lista.pesquisar(s) || arvore.pesquisar(s);
        }
        return resp;
    }
}

class ArvoreBinaria {
    private No raiz;

    ArvoreBinaria(){
        raiz = null;
    }

    public boolean pesquisar(String x) {
        MyIO.print(" raiz");
        return pesquisar(x, raiz);
    }

    private boolean pesquisar(String x, No i) {
        boolean resp;
        if (i == null) {
            resp = false;
        } else if (x.compareTo(i.elemento) == 0) {
            resp = true;
        } else if (x.compareTo(i.elemento) < 0) {
            MyIO.print(" esq");
            resp = pesquisar(x, i.esq);
        } else {
            MyIO.print(" dir");
            resp = pesquisar(x, i.dir);
        }
        return resp;
    }

    public void inserir(String x) {
        raiz = inserir(x, raiz);
    }

    private No inserir(String x, No i) {
        if (i == null) {
            i = new No(x);
        } else if (x.compareTo(i.elemento) < 0) {
            i.esq = inserir(x, i.esq);
        } else if (x.compareTo(i.elemento) > 0) {
            i.dir = inserir(x, i.dir);
        }
        return i;
    }
}

class Lista {
    private Celula primeiro;
    private Celula ultimo;

    public Lista() {
        primeiro = new Celula();
        ultimo = primeiro;
    }

    public void inserirFim(String x) {
        ultimo.prox = new Celula(x);
        ultimo = ultimo.prox;
    }

    public boolean pesquisar(String x) {
        boolean resp = false;
        for (Celula i = primeiro.prox; i != null; i = i.prox) {
            if (i.elemento.compareTo(x) == 0) {
                resp = true;
                i = ultimo;
            }
        }
        return resp;
    }
}
