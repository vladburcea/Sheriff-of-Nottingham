package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Bribe extends Base {

    private static final int HIGHBRIBE = 10;
    private static final int LOWBRIBE = 5;
    private static final int HIGHBRIBEASSETS = 5;
    private static final int LOWBRIBEASSETS = 5;

    // Pentru debugging
    Bribe() {
        super();
        this.setType("BRIBED");
    }

    @Override
    public void traderAction() {
        // Verific cate bunuri ilegale am
        List<Asset> illegalAssets = new ArrayList<>();
        for (Asset asset:this.getHandAssets()) {
            if (!asset.isLegal()) {
                illegalAssets.add(asset);
            }
        }

        // Daca nu am asseturi ilegale sau nu am destui bani de mita joc ca basic
        if (illegalAssets.size() == 0 || this.getCoins() < LOWBRIBE) {
            super.traderAction();
            return;
        }

        // Sortez asseturile ilegale in ordine descrescatoare dupa profit
        Collections.sort(illegalAssets);

        // Determin cate asseturi pot baga in functie de coins
        int allowedNo = (this.getCoins() >= HIGHBRIBE) ? HIGHBRIBEASSETS : LOWBRIBEASSETS;

        // Initializez bagul
        if (this.getBagAssets() != null) {
            this.getBagAssets().clear();
        }

        // Adaug in bag asseturile cat timp am voie
        for (Asset asset:illegalAssets) {
            if (allowedNo == 0 || this.getBagAssets().size() == MAXASSETSNO) {
                break;
            }

            this.getBagAssets().add(asset);
            this.getHandAssets().remove(asset);
            allowedNo--;
        }

        // Setez mita
        this.setBribe((this.getBagAssets().size() > 2) ? HIGHBRIBE : LOWBRIBE);

        // Declar ca am doar mere
        this.setAssetsType(0);
    }

    @Override
    public void sheriffAction(final List<Player> players, final List<Integer> inputAssets) {
        // Iau indexul jucatorului care este sherif
        int idx = players.indexOf(this);

        // Adaug in lista jucatorii care sunt in stanga si respectiv dreapta mea
        final List<Player> l = new ArrayList<>();
        l.add(players.get((idx == 0) ? players.size() - 1 : idx - 1));
        l.add(players.get((idx == players.size() - 1) ? 0 : idx + 1));

        // CAZ nefericit
        if (l.get(0) == l.get(1)) {
            l.remove(1);
        }

        // Verific jucatorii
        super.sheriffAction(l, inputAssets);
    }

}
