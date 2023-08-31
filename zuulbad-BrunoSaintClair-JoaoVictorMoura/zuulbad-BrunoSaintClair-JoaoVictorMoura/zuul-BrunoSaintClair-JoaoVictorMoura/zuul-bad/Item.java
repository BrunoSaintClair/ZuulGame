public class Item {
    private String nome;
    private String descricao;
    private Double peso;



    public  Item(String descricao, String nome, Double peso){
        this.descricao = descricao;
        this.nome = nome;
        this.peso = peso;
    }

    public String getDescricao(){
        return descricao;
    }
    public String getNome(){
        return nome;
    }
    public Double getPeso(){return peso;}

}
