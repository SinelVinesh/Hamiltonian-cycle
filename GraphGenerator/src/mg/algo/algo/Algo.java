package mg.algo.algo;

import mg.algo.trie.Fusion;

public class Algo {
    private int[] verticesCount;
    private int edgesCount;
    public void setVerticesCount(int[] verticesCount) {
        this.verticesCount = verticesCount;
    }

    public int getEdgesCount() {
        return edgesCount;
    }
    public void setEdgesCount(int edgesCount) {
        this.edgesCount = edgesCount;
    }
    // calcul du variance 
     public double variance(int[] sommet,double Moyenne)
     {
         double val = 0;
         for (int i = 0; i < sommet.length; i++) {
             double nb = sommet[i]-Moyenne;
             
             val+=( nb*nb )/sommet.length ;
         }
         //arrondi 4 chiffre apres la virgule fa raha tsy ilaina d commentena fotsn
         val = Math.round(val*1e4)/1e4;
         return val;
     }
     public double ecartype(int[] sommet,double Moyenne) {
        return Math.sqrt(variance(sommet,Moyenne));
    }
      // get le minimum de degre et le max de degre
    public int[] min_max()
    {
        int[] tab = new int[2];
        // Fusion fonction trie fusion le apesaina amnio 
        int[] val = Fusion.trier(verticesCount,0,verticesCount.length);
        //min
        tab[0] = val[0];
        //max
        tab[1] = val[val.length-1];
        return tab;
    }
      // get la nombre de degre pair et impair
      public int[] getNbPariteDegree()
      {
          int[] val = new int[2];
          int i = 0;
          int j = 0;
          for (int k =0;k<verticesCount.length;k++) {
              if (verticesCount[k]%2==0) {
                  i++;
              }else{
                  j++;
              }
          }
          //paire
          val[0] = i;
          //impaire
          val[1] = j;
          return val;
      }
        // get moyenne de degree
    public double getMoyenne()
    {
        double sumDegree = 0;
        for (int k =0;k<verticesCount.length;k++) {
            sumDegree+=verticesCount[k];
        }
        double val = sumDegree/verticesCount.length;
        val = Math.round(val*1e4)/1e4;
        return val;
    }
}
