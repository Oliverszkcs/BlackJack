/**
 * A kártya rangjait tartalmazó felsorolás.
 */
public enum Rank{
    TWO("TWO",2),THREE("THREE",3),FOUR("FOUR",4),FIVE("FIVE",5),SIX("SIX",6),
    SEVEN("SEVEN",7),EIGHT("EIGHT",8),NINE("NINE",9),TEN("TEN",10),JACK("JACK",10),
    QUEEN("QUEEN",10),KING("KING",10),ACE("ACE",11);
    //A kártyák és értékeik
    /**
     * A kártya értékét tároló egész szám.
     */
    private int value;
    /**
     * A kártya nevét tároló karakterlánc.
     */
    private final String name;
    /**
     * Rank konstruktora. Inicializálja a kártyát a megadott névvel és értékkel.
     *
     * @param kartya a kártya neve
     * @param ertek  a kártya értéke
     */
    Rank(String kartya, int ertek) {
        this.value=ertek;
        this.name=kartya;
    }
    /**
     * Visszaadja a kártya nevét.
     * @return a kártya neve
     */
    String getName(){
        return name;
    }
    /**
     * Visszaadja a kártya értékét.
     * @return a kártya értéke
     */
    int getValue(){
        return value;
    }
    /**
     * Beállítja a kártya értékét a megadott értékre.
     * @param i a beállítandó érték
     */
    void setValue(int i){
        this.value=i;
    }

}