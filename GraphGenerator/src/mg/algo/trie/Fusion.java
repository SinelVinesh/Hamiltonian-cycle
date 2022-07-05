package trie;
public class Fusion{
    public Fusion(){

    }
    public static int[] trier(int[] tab,int debut,int fin){
        int[] res = new int[0];
        int taille = 0;
        if(fin <= 1){
            res = tab;
        }else{
            taille = fin;
            if(fin%2 != 0){
                taille = fin + 1;
            }
            int[] tab1 = divise(tab,0,taille/2);
            int[] tab2 = divise(tab,taille/2,fin);
            res = fusionner(trier(tab1,0,tab1.length),trier(tab2,0,tab2.length));
        }
        return res;
    }
    private static int[] fusionner(int[] tab1,int[] tab2){
        int taille = tab1.length + tab2.length;
        int[] resultat = new int[taille];
        int v = 0;
        int i = 0 , j = 0;
        while((i < tab1.length)&&(j < tab2.length)){
            if(tab1[i] <= tab2[j]){
                resultat[v] = tab1[i];
                i ++;
                v++;
            }else{
                resultat[v] = tab2[j];
                j++;
                v++;
            }
        }
        while(i < tab1.length){
            resultat[v] = tab1[i];
            i ++;
            v ++;
        }
        while( j  < tab2.length){
            resultat[v] = tab2[j];
            j ++;
            v ++;
        }
        return resultat;
    }
    private static int[] divise(int[] tab,int deb,int fin){
        int[] vao = new int[fin - deb];
        int i = 0 , b = 0;
        for(i = deb ; i < fin ;i++){
            vao[b] = tab[i];
            b++;
        }
        return vao;
    }
    public static void main(String[] args) {
        int[] d ={1,5,6,10,2,4,8,9};
        int[] val = Fusion.trier(d,0,d.length);
        for (int i = 0; i < val.length; i++) {
            System.out.print("  "+val[i]);
        }
    }
}