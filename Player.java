package main;

import java.util.ArrayList;
import java.util.List;

abstract class Player {
    // Valori constante
    static final int MAXASSETSNO = 6;
    private static final int STARTINGCOINS = 50;
    private String type;

    private List<Asset> handAssets;
    private List<Asset> standAssets;
    private int coins;

    // Round related values: (se schimba la fiecare runda)
    private List<Asset> bagAssets;
    private boolean sheriff;
    private int declaredAssets;
    private int bribe;


    // Gettere si settere:
    String getType() {
        return this.type;
    }

    void setType(final String t) {
        this.type = t;
    }

    List<Asset> getHandAssets() {
        return this.handAssets;
    }

    List<Asset> getStandAssets() {
        return this.standAssets;
    }

    List<Asset> getBagAssets() {
        return this.bagAssets;
    }

    void setBribe(final int b) {
        this.bribe = b;
    }

    int getBribe() {
        return this.bribe;
    }

    void setAssetsType(final int typeOfAssets) {
        this.declaredAssets = typeOfAssets;
    }

    int getAssetsType() {
        return this.declaredAssets;
    }

    public void setCoins(final int c) {
        this.coins = c;
    }

    // Functie care adauga c la numarul curent de coins
    void addCoins(final int c) {
        this.coins += c;
    }

    // Functie care scade c din numarul curent de coins
    void subCoins(final int c) {
        this.coins -= c;
    }

    int getCoins() {
        return this.coins;
    }

    // Functie care seteaza rolul unui jucator
    void setRole(final boolean s) {
        this.sheriff = s;
    }

    boolean isSheriff() {
        return this.sheriff;
    }


    // Constructor
    Player() {
        this.handAssets = new ArrayList<>();
        this.standAssets = new ArrayList<>();
        this.coins = STARTINGCOINS;

        this.sheriff = false;
        this.bagAssets = new ArrayList<>();
        this.declaredAssets = 0;
        this.bribe = 0;
    }

    // Functie care adauga asseturile in mana jucatorilor
    void addAssetsToHand(final List<Integer> assetsInputList) {
        // Daca nu am asset-uri in mana atunci initializez lista
        if (this.handAssets == null) {
            this.handAssets = new ArrayList<Asset>(MAXASSETSNO);
        }

        // Adaug in lista asset-uri pana am 6
        while (handAssets.size() < MAXASSETSNO) {
            this.handAssets.add(new Asset(assetsInputList.get(0)));
            assetsInputList.remove(0);
        }
    }

    // Functie care returneaza cate asseturi de tipul type am pe stand:
    int noOfAssets(final int typeOfAsset) {
        if (standAssets == null) {
            return 0;
        }
        int cnt = 0;

        for (Asset a:standAssets) {
            if (a.getId() == typeOfAsset) {
                cnt++;
            }
        }

        return cnt;
    }

    // Functie care adauga asseturile ramase in bag pe stand
    void transferAssetsFromHandToStand() {
        if (this.standAssets == null) {
            this.standAssets = new ArrayList<>();
        }

        // Daca am trecut cu asseturi ilegale trebuie sa dau bonus
        for (Asset a:this.bagAssets) {
            if (!a.isLegal()) {
                this.standAssets.addAll(a.getBonus());
            }
        }

        // Adaug restul asseturilor pe stand
        this.standAssets.addAll(this.bagAssets);

        // Resetez asseturile din hand
        this.bagAssets.clear();
    }

    void traderAction() {
    }

    void sheriffAction(final List<Player> players, final List<Integer> inputAssets) {
    }
}
