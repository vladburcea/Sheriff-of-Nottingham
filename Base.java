package main;

import java.util.ArrayList;
import java.util.List;

class Base extends Player {
    private static final int MAXASSETSINBAG = 5;
    private static final int ALLASSETSTYPES = 7;

    // Returneaza true daca toate asset-urile din mana sunt ilegale
    private boolean areAllAssetsIllegal() {
        for (Asset asset:this.getHandAssets()) {
            if (asset.isLegal()) {
                return false;
            }
        }

        return true;
    }

    Base() {
        super();
        this.setType("BASIC");
    }

    @Override
    public void traderAction() {
        // Verific daca am doar bunuri ilegale si aplic strategia corecta
        if (areAllAssetsIllegal()) {
            int maxProfit = this.getHandAssets().get(0).getProfit();
            int assetType = this.getHandAssets().get(0).getId();

            // Imi aleg assetul ilegal cu profit cel mai mare
            for (Asset handAsset : this.getHandAssets()) {
                if (maxProfit < handAsset.getProfit()) {
                    maxProfit = handAsset.getProfit();
                    assetType = handAsset.getId();
                }
            }

            // Golesc lista facuta runda trecuta si initializez una noua
            if (this.getBagAssets() != null) {
                this.getBagAssets().clear();
            }

            // Adaug asset-ul in lista si-l elimin din mana
            if (this.getBagAssets() != null) {
                this.getBagAssets().add(new Asset(assetType));
            }
            this.getHandAssets().remove(new Asset(assetType));

            // Declar ca tipul asset-ului este "Apple"
            this.setAssetsType(0);

            // Nu dau mita pentru ca incerc sa minimizez fapta ilegala
            this.setBribe(0);

            return;
        }

        // Cazul in care am si carti legale si ilegale

        // Creez vectorul de frecvente
        int[] freq = new int[ALLASSETSTYPES];

        for (Asset a : this.getHandAssets()) {
            freq[a.getId()]++;
        }

        // Gasesc cea mai mare frecventa & verific daca sunt mai multe in acelasi caz
        int maxFreq = freq[0];
        int count = 1;
        int assetType = 0;

        for (int i = 1; i < freq.length; i++) {
            if (new Asset(i).isLegal() && (maxFreq < freq[i])) {
                // Cand gasesc o frecventa mai mare reinitializez cele 3 variabile ajutatoare
                maxFreq = freq[i];
                count = 1;
                assetType = i;
            } else if (maxFreq == freq[i] && new Asset(i).isLegal()) {
                // In cazul in care mai gasesc o frecventa maxima incrementez contorul
                count++;
            }
        }

        // Daca sunt mai multe frecvente la fel de mari
        // o iau pe cea cu cel mai mare profit
        if (count != 1) {
            int maxProfit = new Asset(assetType).getProfit();

            // Aflu cel mai mare profit din asseturile cu frecventa maxima
            // Aplic algoritmul doar pe asseturi legale
            for (int i = 0; i < freq.length; i++) {
                if ((maxFreq == freq[i]) && new Asset(i).isLegal()) {
                    if (maxProfit < new Asset(i).getProfit()) {
                        maxProfit = new Asset(i).getProfit();
                        assetType = i;
                    }
                }
            }

            // Aleg primul asset din mana care are profit maxim doar
            // daca sunt mai multe cu aceeasi freq si maxProfit
            for (Asset a : this.getHandAssets()) {
                if (a.isLegal() && a.getProfit() * freq[a.getId()] == maxProfit * maxFreq) {
                    assetType = a.getId();
                    break;
                }
            }
        }

        // Golesc lista facuta runda trecuta si initializez una noua
        if (this.getBagAssets() != null) {
            this.getBagAssets().clear();
        }

        // Declar asset-urile
        this.setAssetsType(assetType);

        // Nu dau mita
        this.setBribe(0);

        // Nu am voie sa aduag in sac mai mult de 5 asseturi
        if (freq[assetType] > MAXASSETSINBAG) {
            freq[assetType] = MAXASSETSINBAG;
        }

        // Adaug elementele in sac si elimin elementele din mana
        while (freq[assetType] != 0) {
            freq[assetType]--;
            this.getBagAssets().add(new Asset(assetType)); // Il adaug in bag
            this.getHandAssets().remove(new Asset(assetType)); // Il scot din mana
        }
    }

    @Override
    public void sheriffAction(final List<Player> players, final List<Integer> inputAssets) {
        List<Asset> discardedAssets = new ArrayList<>();

        // Parcurg fiecare jucator si il verific
        for (Player p: players) {
            boolean honest = true;

            // Daca jucatorul este serfi tura asta inseamna ca este fix cel care face verificarea
            // asa ca il sar
            if (p.isSheriff()) {
                continue;
            }

            // Daca assetul este ilegal/difera fata de cel declarat il sanctionez pe comerciant
            for (Asset a:p.getBagAssets()) {
                if (a.getId() != p.getAssetsType() || !a.isLegal()) {
                    honest = false; // Inseamna ca il pedepsesc pe comerciant

                    // Il adaug in lista de inputuri pentru a-l readauga in joc
                    inputAssets.add(a.getId());

                    // Il bag intr-o lista ajutatoare pentru a-l scoate din bag
                    discardedAssets.add(a);

                    this.addCoins(a.getPenalty()); // Recompensez seriful
                    p.subCoins(a.getPenalty()); // Il pedepsesc pe comerciant
                }
            }

            // Daca jucatorul este onest seriful plateste daune
            if (honest) {
                // Comerciantul onest primeste banii de la verificarea incorecta
                // iar serifului i se scad banii
                p.addCoins(new Asset(p.getAssetsType()).getPenalty() * p.getBagAssets().size());
                this.subCoins(new Asset(p.getAssetsType()).getPenalty() * p.getBagAssets().size());

                continue; // Nu am asseturi care trebuie readaugate in joc
            }

            // Readaug in joc asseturile confiscate
            for (Asset a:discardedAssets) {
                p.getBagAssets().remove(a);
            }

        }
    }
}
