import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Játék megvalósításának osztálya(grafikusan)
 */
public class Game extends JFrame {
    /**
     * Hit gomb(lap húzása)
     */
    private final JButton HitButton;
    /**
     * Stand gomb(Megmaradás,azaz nem húz a játékos)
     */
    private final JButton StandButton;
    /**
     * Double gomb(Tét megduplázása és 1 lap húzása)
     */
    private final JButton DoubleButton;
    /**
     * Háttér Jlabal-je
     */
    private JLabel backgroundlabel;
    /**
     * A játék során az aktuális játékos sorszáma
     */
    public static int playercount=0;
    /**
     * Összes játékos tömbje
     */
    public static ArrayList<Player> players=new ArrayList<>();
    /**
     * Éppen játszó játákosok tömbje
     */
    public static ArrayList<Player> playing = new ArrayList<>();
    /**
     * A pakli
     */
    private static Deck deck;
    /**
     * Középső kéz(aktuális kéz)
     */
    private static Hand mainHand;

    /**
     * Létrehozza a játékot és a hozzá szükséges grafikus elemeket, itt játszódik le a játékmenet is
     * @throws Exception
     */
    Game() throws Exception {
        deck=new Deck();
        deck.shuffle();
        //új paklit hozunk létre és megkeverjük

        Player dealer = new Player(0, "Dealer", 1); //dealert létrehozzuk és adunk neki 1 lapot
        dealer.getHands().get(0).addcard(deck.Deal());
        players= Player.loadPlayers("mentes.txt");//betöltjük a mentett játékosokat

        JLayeredPane menu = new JLayeredPane();
        menu.setPreferredSize(new Dimension(1280, 720));
        //layeredpanet használtam hogy tudjak háttérképet és egyéb dolgokat rakni

        JFrame frame = new JFrame("Blackjack game");
        frame.setSize(1280, 720);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //blackjack ablak létrehozása

        JTextField name=generateblanktf();
        name.setBounds(500,250,200,100);
        //játékos nevének csinálok egy szövegmezőt

        JTextField cardvalue=generateblanktf();
        cardvalue.setBounds(500,230,300,200);
        //Kéz értékénének egy szövegmező

        ImageIcon background = new ImageIcon("ingamebackground.png");
        Image image = background.getImage();
        Image smallbackground = image.getScaledInstance(1280, 720, Image.SCALE_SMOOTH);
        background = new ImageIcon(smallbackground);
        backgroundlabel = new JLabel(background);
        backgroundlabel.setBounds(0, 0, 1280, 720);
        //beállítom a háttérképet és a mértetét

        JButton addnew=new JButton("Add player to table");//Játékos hozzáadása gomb
        addnew.setBounds(50, 150, 150, 30);
        addnew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int balance=0;
                String playername = JOptionPane.showInputDialog("Please add the player's name");//bekérjük a nevét
                if (containsplayer(playername) == null) {
                     balance = Integer.parseInt(JOptionPane.showInputDialog("Balance"));//Ha még nem létezik ilyen nevű játékos akkor újat csinálunk
                }else {
                    JOptionPane.showMessageDialog(null,"Player already exists, loaded informations. Balance: "+containsplayer(playername).getBalance(),"Playername" ,JOptionPane.PLAIN_MESSAGE);
                    balance=containsplayer(playername).getBalance();//találtunk ilyen játékost mentve , betöltjük hogy mennyi pénze van
                }
                int handcount = Integer.parseInt(JOptionPane.showInputDialog("Enter number of hands. Overall handcount is maxed at 6"));
                int betamount = Integer.parseInt(JOptionPane.showInputDialog("Enter bet amount"));//bekérünk minden más szükséges értéket
                if(betamount*handcount>balance){//ellenőrzés hogy tényleg tud-e ennyi tétet rakni
                    JOptionPane.showMessageDialog(null,"You dont have enough balance to do that","Not enough balance",JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Player newplayer = new Player(balance, playername, handcount);//létrehozzuk az új játékost
                if((containsplayer(playername)!=null)){
                    Player playeradding=containsplayer(playername);
                    newplayer=new Player(playeradding.getBalance(),playeradding.getName(),handcount);//ha viszont sikerült betölteni akkor felülírjuk és a már mentett adatokkal játszhat
                    players.remove(containsplayer(playername));
                }
                players.add(newplayer);
                for (Hand hand : newplayer.getHands()) {
                    hand.setBet(betamount);//beállítjuk az téteket az összes kéznek
                }
                playing.add(newplayer);
                paintdealerscards(dealer, menu);
                HitButton.setEnabled(true); //Ha már létezik 1 játékos az asztalnál akkor lehet elindítani a játékot
                StandButton.setEnabled(true);
                DoubleButton.setEnabled(true);
                cardvalue.setText("Value: ");
            }
        });
        JButton newgamebutton=new JButton("Start a new game");
        newgamebutton.setEnabled(false);
        newgamebutton.setBounds(50, 100, 150, 30);
        newgamebutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playercount=0; //új játékot lehet kezdeni ezzel a gombbal ezért minden értéket nullázunk vagy újra kezdeti állapotba állítjuk
                deck=null;
                deck=new Deck();    //új pakli
                deck.shuffle();
                StandButton.setEnabled(false);//újra kell legalább 1 játékos az asztalnál ezért kikapcsoljuk a játékhoz szükgséges gombokat
                HitButton.setEnabled(false);
                DoubleButton.setEnabled(false);
                addnew.setEnabled(true);//új játékos hozzáadása bekapcsolva
                playing.clear();    //játékosok tömbje törölve
                dealer.getHands().get(0).getHand().clear(); //dealernek új kártya
                dealer.getHands().get(0).addcard(deck.Deal());
                removeCardLabels(menu); //előző játék kártyáinak megjelenése eltüntetve
                removeJTextFields(menu,cardvalue,name); //különböző szövegmezők törlése
                menu.add(backgroundlabel,JLayeredPane.DEFAULT_LAYER); //háttér visszaállítása
                players= Player.loadPlayers("mentes.txt");//játékosok betöltése
                newgamebutton.setEnabled(false);
                name.setText("");
                cardvalue.setText("");
            }
        });

        HitButton = new JButton("Hit");
        HitButton.setBounds(50, 200, 150, 30);
        HitButton.addActionListener(new ActionListener() {
            /**
             * Ez a metódus kezeli a Hit gombra kattintáskor való történéseket
             * @param e Eseményobjektum ami tartalmazza az adott esemény részleteit
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                addnew.setEnabled(false);//elindul a játék nem lehet több játékos hozzáadni
                StandButton.setEnabled(true);
                DoubleButton.setEnabled(true);
                if (playercount <= playing.size()-1) {
                    name.setText(playing.get(playercount).getName());//játékos nevének megjelenítése
                    mainHand = playing.get(playercount).getHands().get(playing.get(playercount).getActualhand());//A középen megjelenített "aktuális kéz"
                    playing.get(playercount).Hit(deck);//húzunk
                    Player player = playing.get(playercount);  //aktuális játékos
                    ArrayList<Hand> hands = player.getHands();//jatekos kezei
                    Hand aktualis = hands.get(player.getActualhand());    //adott kez
                    Card actualcard = aktualis.getcardat(aktualis.getHand().size() - 1);// adott kéz utolsó lapja

                    JLabel newcard = genaratenewcard(actualcard.nameforpng(), aktualis.getHand().size() - 1);//képet generálunk
                    menu.add(newcard, JLayeredPane.PALETTE_LAYER);
                    menu.moveToFront(newcard);
                    cardvalue.setText("Value: " + playing.get(playercount).getHands().get(playing.get(playercount).getActualhand()).getvalue());
                    //hozzáadjuk a lapot és megjelenítjük
                    if (aktualis.getvalue() > 21) {
                        repainteverything(dealer,menu);//ha több mint 21 akkor új kéz jön
                        DoubleButton.setEnabled(false);
                        StandButton.setEnabled(false);
                        cardvalue.setText("Value: " + playing.get(playercount).getHands().get(playing.get(playercount).getActualhand()).getvalue());

                        if (player.getHands().size() > player.getActualhand()) {
                            player.setActualhand(player.getActualhand() + 1);//ha  az adott játékosnak még van másik keze akkor az jön
                            if((playercount!=playing.size()-1) && (aktualis.equals(player.getHands().get(player.getHands().size()-1).getHand()))) {
                                mainHand = playing.get(playercount).getHands().get(playing.get(playercount).getActualhand());
                                removeCardLabels(menu);
                                repainteverything(dealer,menu);
                            }
                        }
                        if (player.getHands().size() == player.getActualhand()) {//ha nincs akkor új játékos
                            playercount++;
                            if ((playercount != playing.size() - 1) && (aktualis.equals(player.getHands().get(player.getHands().size() - 1).getHand()))) {
                                mainHand = playing.get(playercount).getHands().get(playing.get(playercount).getActualhand());
                                removeCardLabels(menu);
                                repainteverything(dealer,menu);
                            }
                        }
                    }
                    removeCardLabels(menu);
                    menu.add(backgroundlabel);
                    repainteverything(dealer,menu);
                }else{//ha már nincs se új kéz se játékos a játéknak vége
                    dorestdealercard(dealer);// dealer húz lapokat
                    paintdealerscards(dealer,menu);
                    repaintallcard(menu); //megjelenít mindent
                    addallvaluesandnames(menu); //megjelenít minden lapot és minden kéz értékét
                    StandButton.setEnabled(false);
                    HitButton.setEnabled(false);    //jatékhoz szükséges gombok kikapcsolása
                    DoubleButton.setEnabled(false);
                    seeresults(dealer,menu);//dealer lapjainak értéke
                    Player.savePlayers(players,"mentes.txt"); //elmentjük a játékosoakt
                    newgamebutton.setEnabled(true);     //új játék indulhat
                }
            }
        });

        DoubleButton = new JButton("Double");
        DoubleButton.setBounds(50, 250, 150, 30);
        for(ActionListener actionListener:HitButton.getActionListeners()){
            DoubleButton.addActionListener(actionListener); //hozzáadjuk a hit actionListener-ét mivel a double ugyanaz csak a tét = tét*2
        }
        DoubleButton.addActionListener(new ActionListener() {
            /**
             * Ez a metódus kezeli a Double gombra kattintáskor való történéseket
             * @param e Eseményobjektum ami tartalmazza az adott esemény részleteit
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if (playercount<=playing.size()-1 ){
                    Player player = playing.get(playercount);
                int overallbalance=0;
                for(int i=0;i<playing.get(playercount).getHands().size();i++){
                    overallbalance+=player.getHands().get(i).getBet();  //kiszámoljuk összesen mennyi tétet rakott
                }
                    if (overallbalance+ player.getHands().get(player.getActualhand()).getBet() <= player.getBalance()) {
                        player.getHands().get(player.getActualhand()).setBet(player.getHands().get(player.getActualhand()).getBet() * 2);//ha van elég pénze akkor duplázza az adott kéz tétét
                    }else{
                        JOptionPane.showMessageDialog(null,"You dont have enough balance to do that, used normal hit ","Not enough balance",JOptionPane.WARNING_MESSAGE);
                        //jelzi ha nincs elég pénz
                    }
                }
            }
        });

        StandButton = new JButton("Stand");
        StandButton.setBounds(50, 300, 150, 30);
        StandButton.addActionListener(new ActionListener() {
            /**
             * Ez a metódus kezeli a Stand gombra kattintáskor való történéseket
             * @param e Eseményobjektum ami tartalmazza az adott esemény részleteit
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                    addnew.setEnabled(false);
                    Player player = playing.get(playercount);
                    if (playercount <= playing.size() - 1 && player.getHands().get(player.getHands().size() - 1) != player.getHands().get(player.getActualhand())) {
                        if (player.getHands().size() - 1 > player.getActualhand()) {//ha vann több keze az adott játékosnak akkro az jön
                            player.setActualhand(player.getActualhand() + 1);
                            mainHand = player.getHands().get(player.getActualhand());
                            repainteverything(dealer, menu);
                            name.setText(playing.get(playercount).getName());
                        }
                    } else if (playing.get(playercount).getHands().size() - 1 == playing.get(playercount).getActualhand()) {
                        mainHand = playing.get(playercount).getHands().get(playing.get(playercount).getActualhand());
                        repainteverything(dealer, menu);// ha nem akkor új játékos
                        if(playing.get(playercount)==playing.get(playing.size()-1)){
                            //ha nincs több kéz vagy játékos akkor vége a játéknak
                            dorestdealercard(dealer);
                            paintdealerscards(dealer,menu);
                            addallvaluesandnames(menu);
                            seeresults(dealer,menu);
                            repainteverything(dealer,menu);
                            StandButton.setEnabled(false);
                            HitButton.setEnabled(false);
                            DoubleButton.setEnabled(false);
                            Player.savePlayers(players,"mentes.txt");
                            newgamebutton.setEnabled(true);
                        }else {
                            playercount++;
                            mainHand = null;
                            repainteverything(dealer, menu);
                            //következő játékos jön
                        }
                    }
            }
        });

        menu.add(backgroundlabel, JLayeredPane.DEFAULT_LAYER);  //hozzáadjuk a hátteret
        menu.add(name,JLayeredPane.PALETTE_LAYER);              //a középső kézhez tartozó ember nevét
        menu.add(newgamebutton,JLayeredPane.PALETTE_LAYER);     //új játék gombot
        menu.add(addnew,JLayeredPane.PALETTE_LAYER);            //új játékos gombot
        menu.add(StandButton, JLayeredPane.PALETTE_LAYER);      //Stand gomb
        menu.add(HitButton, JLayeredPane.PALETTE_LAYER);        //Hit gomb
        menu.add(DoubleButton, JLayeredPane.PALETTE_LAYER);     //Double gomb
        menu.add(cardvalue,JLayeredPane.PALETTE_LAYER);         //középső kéz értéke
        HitButton.setEnabled(false);            //alapból a játékhoz szükséges gombok nem elérhetőek
        StandButton.setEnabled(false);
        DoubleButton.setEnabled(false);

        frame.setContentPane(menu);
        frame.setVisible(true);
        setResizable(false);

    }

    /**
     * Újra megjelenít mindent, hátteret, kártyákat osztó lapjait
     * @param dealer Osztó
     * @param panel Az a pane amin dolgozunk
     */
    private void repainteverything(Player dealer,JLayeredPane panel){// újra rajzolja az összes képet,szövegmezőt
        removeCardLabels(panel);
        panel.add(backgroundlabel, JLayeredPane.DEFAULT_LAYER);
        repaintallcard(panel);
        paintdealerscards(dealer,panel);
    }

    /**
     * Újra megjeleníti a kártyákat(ahhoz szükséges ha változott a helyük)
     * @param panel adot pane amin dolgozunk
     */
    private void repaintallcard(JLayeredPane panel){//összes kártya képét rajzolja újra
        for(int z=0;z<playing.size();z++){
            Player player=playing.get(z);
            for(int j=0;j<player.getHands().size();j++){
                Hand hand=player.getHands().get(j);
                for(int i=0;i<hand.getHand().size();i++){
                    JLabel newcard=genaratenewcard(hand.getcardat(i).nameforpng(),i);
                    if(hand!=mainHand){
                        newcard.setBounds(50+30*i+j*250+z*250*player.getHands().size(),600,70,120);
                    }
                    panel.add(newcard,JLayeredPane.PALETTE_LAYER);
                    panel.moveToFront(newcard);
                }
            }
        }
    }

    /**
     * Megjeleníti az összes érték és név szövegmezőt
     * @param panel Az a pane amihez szeretnénk hozzáadni az összes értéket és nevet
     */
    public void addallvaluesandnames(JLayeredPane panel){//összes érték és név mezőt jeleníti meg
        for(int z=0;z<playing.size();z++){
            Player player=playing.get(z);
            for(int i=0;i<player.getHands().size();i++){
                if(player.getHands().get(i).getHand()==playing.get(playing.size()-1).getHands().get(playing.get(playing.size()-1).getHands().size()-1).getHand()){
                    break;
                }
                JTextField name=generateblanktf();
                name.setText(player.getName());
                name.setBounds(120+235*i+z*240*player.getHands().size(),540,170,30);
                name.setForeground(Color.white);
                name.setFont(new Font("Arial",Font.PLAIN,25));


                JTextField value=generateblanktf();
                value.setText(String.valueOf(player.getHands().get(i).getvalue()));
                value.setBounds(120+240*i+z*245*player.getHands().size(),570,170,30);
                value.setFont(new Font("Arial",Font.PLAIN,25));

                panel.add(name,JLayeredPane.PALETTE_LAYER);
                panel.add(value,JLayeredPane.PALETTE_LAYER);
            }
        }
    }

    /**
     * Az osztó kezét feltölti lapokkal addig ameddig az nincs legalább 17
     * @param dealer Osztó
     */
    public void dorestdealercard(Player dealer){// addig húz a dealernek lapokat ameddig nincs legalább 17 értékű lap a kezében
        while(dealer.getHands().get(0).getvalue()<17){
            dealer.getHands().get(0).addcard(deck.Deal());
        }
    }

    /**
     * Létre hoz egy olyan szövegmezőt aminek a háttere átlátszóm nincs körvonala, nem szerkeszthető és nem kijelölhető
     * @return Megadott feltételeknek megfelelő üres szövegmező
     */
    public JTextField generateblanktf() {//létrehoz egy olyan szövegmezőt aminek nincs háttere és nincs körberajzolva, illetve nem lehet változatni és kijelölnise
        JTextField Newtf=new JTextField();
        Newtf.setOpaque(false);
        Newtf.setOpaque(false);
        Newtf.setBorder(null);
        Newtf.setEditable(false);
        Newtf.setFocusable(false);
        Newtf.setForeground(Color.white);//szöveg fehér legyen
        Newtf.setFont(new Font("Arial",Font.PLAIN,35));//35-ös szövegméret arial típus
        return Newtf;
    }

    /**
     * Az összes szövegmezőt eltávolítja a paraméterként megadott layeredpane-ből(kivétel kettőt)
     * @param layeredPane Megadott pane amiből szeretnénk eltávolítani a szövegmezőket
     * @param cardvalue A középső kéz értéke, ezt szeretnénk meghagyni
     * @param name A középső kéz tulajdonosának neve, ezt se szeretnénk eltávolítani
     */
    private void removeJTextFields(JLayeredPane layeredPane,JTextField cardvalue,JTextField name) {//összes szövegmezőt törli kivéve a 2 megadott kivételt
        Component[] components = layeredPane.getComponents();
        for (Component component : components) {
            if (component instanceof JTextField &&component!=cardvalue &&component!=name) {
                layeredPane.remove(component);
            }
        }
        layeredPane.repaint();
    }

    /**
     * Eltünteti az összes kártya képét
     * @param layeredPane A használt panel
     */
    private void removeCardLabels(JLayeredPane layeredPane) {//káryták képeinek megjelenítésének törlése
        Component[] components = layeredPane.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel) {
                layeredPane.remove(component);
            }
        }
        layeredPane.repaint();
    }

    /**
     * Létrehoz egy Jlabel-t ami az adott lap megjelenítéséhez szükséges
     * @param cardname  Kártya neve
     * @param cardcount Adott kéz hanyadik lapja(elhelyezéshez szükséges)
     * @return Kártya megjelenítéséhez használt Jlabelt adja vissza
     */
    public JLabel genaratenewcard(String cardname,int cardcount){// új kártya képének létrehozása
        ImageIcon card=new ImageIcon("cardpngs/"+cardname+".png");//megkeresi a cardpngs mappában a kártya nevéhez tartozó képet
        Image image1=card.getImage();
        Image smallcard=image1.getScaledInstance(70,120,Image.SCALE_SMOOTH);//méretre szabja
        card=new ImageIcon(smallcard);
        JLabel cardlabel= new JLabel(card);
        cardlabel.setBounds(500+(50*cardcount),360,70,120);//szükséges helyre rakja
        return cardlabel;
    }

    /**
     * Kirajzolja az osztó lapjait
     * @param dealer Osztó
     * @param panel Adott panel ahol szeretnénk megjeleníteni
     */
    public void paintdealerscards(Player dealer,JLayeredPane panel){//dealer lapjainak megjelenítése
        for(int i=0; i< dealer.getHands().get(0).getHand().size();i++){
            Card card=dealer.getHands().get(0).getcardat(i);
            JLabel newdeaelrcard=genaratenewcard(String.valueOf(card.getSuit())+ card.getRank(),dealer.getHands().get(0).getHand().size());
            newdeaelrcard.setBounds(520+(50*i),0,70,120);
            panel.add(newdeaelrcard,JLayeredPane.PALETTE_LAYER);
        }if(dealer.getHands().get(0).getHand().size()!=1){
        JTextField value=generateblanktf();
        value.setBounds(545,130,170,30);
        value.setText(String.valueOf(dealer.getHands().get(0).getvalue()));
        panel.add(value,JLayeredPane.PALETTE_LAYER);
        }
        repaintallcard(panel);
    }

    /**
     * Megkeresi hogy létezik-e az adott nevű játékos
     * @param name Keresett játékos neve
     * @return  Az adott játékos vagy null ha nem létezik ilyen
     */
    public Player containsplayer(String name){//Létezik már egy adott játékos a mentett játékosok között
        for(Player player:players){
            if(player.getName().equals(name)){
                return player;
            }
        }
        return null;
    }

    /**
     * Kiszámolja és megjeleníti minden kéz eredményét az osztó kézéhez viszonyítva.
     * A pozitív értékek nyereményeket, a negatív értékek veszteségeket jelölnek, és a 0 döntetlent.
     * @param dealer Az osztó játékos.
     * @param panel Az eredmények megjelenítésére szolgáló panel.
     */
    public void seeresults(Player dealer,JLayeredPane panel){//Minden kéz esetén kiszámolja hogy nyert-e,vesztett-e és hogy mennyit illetve hogy döntetlen lett-e
        for(int i=0;i<playing.size();i++){
            Player player=playing.get(i);
            for(int j=0;j<player.getHands().size();j++){
                Hand hand=player.getHands().get(j);
                JTextField money=generateblanktf();
                money.setBounds(120+235*j+i*240*player.getHands().size(),500,170,30);
                if(hand==playing.get(playing.size()-1).getHands().get(playing.get(playing.size()-1).getHands().size()-1)){
                    money.setBounds(500,230,170,30);
                }
                if(hand.results(dealer.getHands().get(0))==0){
                    money.setForeground(Color.white);
                    money.setText("+0");//ha döntetlen akkor fehér +0 jelenik meg
                }else if(hand.results(dealer.getHands().get(0))==1){
                    money.setForeground(Color.green);
                    money.setText("+"+hand.getBet());//ha nyert akkor zölddel írja ki hogy mennyit
                    player.setBalance(player.getBalance()+hand.getBet());
                }else if(hand.results(dealer.getHands().get(0))==-1){
                    player.setBalance(player.getBalance()-hand.getBet());
                    money.setForeground(Color.red);//ha veszített akkor pirossal írja hogy mennyit
                    money.setText("-"+hand.getBet());
                }
                panel.add(money,JLayeredPane.PALETTE_LAYER);
            }
        }
    }
}



