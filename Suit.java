/**
 * A kártya színeit tartalmazó felsorolás.
 */
public enum Suit {
    CLUBS("CLUBS"), DIAMONDS("DIAMONDS"), HEARTS("HEARTS"), SPADES("SPADES");
    //4 szín
    /**
     * A kártya színét tároló karakterlánc.
     */
    private String name;
    /**
     * Suit felsorolás konstruktora. Inicializálja a kártyát a megadott névvel.
     * @param name a kártya színe
     */
    Suit(String name) {
        this.name=name;
    }
    /**
     * Visszaadja a kártya színének nevét.
     * @return a kártya színe
     */
    String getname(){
        return name;
    }
}
