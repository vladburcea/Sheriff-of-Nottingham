package main;

import java.util.ArrayList;
import java.util.List;


public final class Wizard extends Base {

    private static final int MAXASSETSINBAG = 5;
    private static final int ALLASSETSTYPES = 7;
    private static final int BIGENOUGHNO = 3;

    // Returneaza true daca toate asset-urile din mana sunt ilegale
    private boolean areAllAssetsIllegal() {
        for (Asset asset:this.getHandAssets()) {
            if (asset.isLegal()) {
                return false;
            }
        }

        return true;
    }

    Wizard() {
        super();
        this.setType("WIZARD");
    }

    @Override
    public void traderAction() {
        // Verific daca am doar bunuri ilegale
        if (areAllAssetsIllegal()) {
            int maxProfit = getHandAssets().get(0).getProfit();
            int assetType = getHandAssets().get(0).getId();

            // Sortez descrescator lista de asseturi
            this.getHandAssets().sort((o1, o2) -> o2.getProfit() - o1.getProfit());

            // Adaug in mana doua asseturi ilegale si dau mita
            if (this.getBagAssets() != null) {
                this.getBagAssets().add(this.getHandAssets().get(0));
            }
            this.getHandAssets().remove(0);

            if (this.getBagAssets() != null) {
                this.getBagAssets().add(this.getHandAssets().get(0));
            }
            this.getHandAssets().remove(0);

            // Declar ca tipul asset-ului este "Apple"
            this.setAssetsType(0);

            // Dau mita ca poate ma lasa lumea sa trec
            this.setBribe(1);

            return;
        }

        // Cazul in care am si carti legale si ilegale

        // Creez vectorul de frecvente
        int[] freq = new int[ALLASSETSTYPES];

        for (Asset handAsset : getHandAssets()) {
            freq[handAsset.getId()]++;
        }

        // Gasesc cea mai mare frecventa & verific daca sunt mai multe in acelasi caz
        int maxFreq = freq[0];
        int count = 1;
        int assetType = 0;

        for (int i = 1; i < freq.length; i++) {
            if (new Asset(i).isLegal() && (maxFreq < freq[i])) {
                maxFreq = freq[i];
                count = 1;
                assetType = i;
            } else {
                if (maxFreq == freq[i]) {
                    count++;
                }
            }
        }

        // Daca sunt mai multe frecvente la fel de mari
        // o iau pe cea cu cel mai mare profit
        if (count != 1) {
            int maxProfit = new Asset(assetType).getProfit();

            // Aflu cel mai mare profit din asseturile cu frecventa maxima
            for (int i = 0; i < freq.length; i++) {
                if ((maxFreq == freq[i]) && new Asset(i).isLegal()) {
                    if (maxProfit < new Asset(i).getProfit()) {
                        maxProfit = new Asset(i).getProfit();
                    }
                }
            }
        }

        // Declar asset-urile
        this.setAssetsType(assetType);

        // Nu dau mita pentru ca nu am nimic ilegal
        this.setBribe(0);

        // Adaug elementele in sac si elimin elementele din mana
        // Nu am voie sa aduag in sac mai mult de 5 asseturi
        if (freq[assetType] > MAXASSETSINBAG) {
            freq[assetType] = MAXASSETSINBAG;
        }

        while (freq[assetType] != 0) {
            freq[assetType]--;
            this.getBagAssets().add(new Asset(assetType));
            this.getHandAssets().remove(new Asset(assetType));
        }
    }

    @Override
    public void sheriffAction(final List<Player> players, final List<Integer> inputAssets) {
        List<Player> toBeVerified = new ArrayList<>();

        for (Player p:players) {
            if (p.isSheriff()) {
                continue;
            }

            // Imi fac lista cu toti jucatorii care au mai multe asseturi in bag
            if (p.getBagAssets().size() >= BIGENOUGHNO || p.getAssetsType() == 0) {
                toBeVerified.add(p);
            }
        }

        // Verific jucatorii respectivi
        super.sheriffAction(toBeVerified, inputAssets);
    }
}
