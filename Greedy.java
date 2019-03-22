package main;

import java.util.ArrayList;
import java.util.List;

class Greedy extends Base {

    // Pentru debugging
    Greedy() {
        super();
        this.setType("GREEDY");
    }

    private static int roundNo = 0;
    private static final int MAXASSETS = 5;

    @Override
    public void traderAction() {
        // Oricum ar fi eu initial joc basic
        super.traderAction();
        roundNo++;

        // Daca sunt pe runda para joc doar basic
        if (roundNo % 2 != 0) {
            return;
        }

        if (this.getBagAssets().size() < MAXASSETS) {
            int maxProfit = Integer.MIN_VALUE;
            int assetType = -1;


            for (Asset asset:this.getHandAssets()) {
                if (!asset.isLegal() && asset.getProfit() > maxProfit) {
                    maxProfit = asset.getProfit();
                    assetType = asset.getId();
                }
            }

            // Daca nu am gasit niciun asset ilegal ies
            if (assetType == -1) {
                return;
            }

            // Adaug in lista assetul ilegal cu cel mai mare profit
            this.getBagAssets().add(new Asset(assetType));
            this.getHandAssets().remove(new Asset(assetType));
        }

    }

    @Override
    public void sheriffAction(final List<Player> players, final List<Integer> inputAssets) {
        List<Player> honestPlayers = new ArrayList<>();

        for (Player p:players) {
            if (p.isSheriff()) {
                continue;
            }

            // Adaug mita si o sustrag din mana
            this.addCoins(p.getBribe());
            p.subCoins(p.getBribe());

            // Imi fac lista cu jucatorii care nu mi-au dat mita ca sa-i verific
            if (p.getBribe() == 0) {
                honestPlayers.add(p);
            }
        }

        // Verific jucatorii
        super.sheriffAction(honestPlayers, inputAssets);
    }
}

