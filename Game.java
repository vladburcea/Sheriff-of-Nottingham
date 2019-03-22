package main;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

class Game {
    private List<Player> playerList;
    private List<Integer> assetList;
    private static final int LEGALASSETSNO = 4;

    Game() {
        playerList = null;
        assetList = null;
    }

    private static void kingQueenBonus(final List<Player> playersList) {
        Map<Player, Integer> freq = new HashMap<>();

        for (int i = 0; i < LEGALASSETSNO; i++) {
            // Retin cate asseturi de tipul asta are fiecare jucator
            for (Player p:playersList) {
                freq.put(p, p.noOfAssets(i));
            }

            // Sortez descrescator
            Set<Map.Entry<Player, Integer>> s = freq.entrySet();
            List<Map.Entry<Player, Integer>> auxList = new ArrayList<>(s);

            auxList.sort((o1, o2) -> (o2.getValue()) - (o1.getValue()));

            // Dau bonusul de king tuturor jucatorilor cu frecventa maxima
            auxList.get(0).getKey().addCoins(new Asset(i).getKingBonus());
            int j = 1;

            while ((j < playersList.size())
                    && auxList.get(0).getValue().equals(auxList.get(j).getValue())) {
                auxList.get(j).getKey().addCoins(new Asset(i).getKingBonus());
                j++;
            }

            // Daca toti jucatorii au fost la egalitate primesc toti king si ies din metoda
            if (j == playersList.size()) {
                continue;
            }

            // Dau bonusul de queen tuturor jucatorilor cu a doua frecventa maxima
            auxList.get(j).getKey().addCoins(new Asset(i).getQueenBonus());
            int q = j;
            j++;

            while ((j < playersList.size())
                    && auxList.get(q).getValue().equals(auxList.get(j).getValue())) {
                auxList.get(j).getKey().addCoins(new Asset(i).getQueenBonus());
                j++;
            }

        }
    }

    void startGame(final GameInput gameInput) {
        assetList = gameInput.getAssetIds();
        List<String> playerOrder = gameInput.getPlayerNames();

         // Citesc lista cu Asseturi
        playerList = new ArrayList<>();

        // Imi creez lista cu jucatori in ordine
        for (String playerType : playerOrder) {
            switch (playerType) {
                default: // Cazul basic
                    playerList.add(new Base());
                    break;

                case "greedy":
                    playerList.add(new Greedy());
                    break;

                case "wizard":
                    playerList.add(new Wizard());
                    break;

                case "bribed":
                    playerList.add(new Bribe());
                    break;
            }
        }
    }

    void playGame() {
        // Jocul va avea noOfPlayers * 2 runde, ca fiecare jucator sa poata fi serif de 2 ori
        for (int i = 0; i < playerList.size() * 2; i++) {
            // Aleg seriful tura asta
            playerList.get(i % playerList.size()).setRole(true);

            // Adaug in mana fiecarui jucator asseturile necesare
            for (Player p:playerList) {
                p.addAssetsToHand(assetList);
            }

            // Isi joaca traderii tura
            for (Player p : playerList) {
                if (!p.isSheriff()) {
                    p.traderAction();
                }
            }

            // Joaca seriful
            playerList.get(i % playerList.size()).sheriffAction(playerList, assetList);

            // In urma verificarii adaug pe stand asseturile care au trecut
            for (Player p:playerList) {
                if (!p.isSheriff()) {
                    p.transferAssetsFromHandToStand();
                }
            }

            // Readuc seriful la normal
            playerList.get(i % playerList.size()).setRole(false);
        }
    }

    void endGame() {
        // Fac scorul final cu tot cu profitul de pe asseturi
        for (Player p:playerList) {
            for (Asset a:p.getStandAssets()) {
                p.addCoins(a.getProfit());
            }
        }

        // Aplic bonusul de king & queen
        kingQueenBonus(playerList);

        // Sortez lista jucatorilor dupa calcularea scorului final, utilizand o expresie lambda
        playerList.sort((o1, o2) -> (o2.getCoins() - o1.getCoins() == 0)
                ? o1.getType().compareTo(o2.getType()) : o2.getCoins() - o1.getCoins());

        // Afisez lista jucatorilor in ordine descrescatoare
        for (Player p:playerList) {
            System.out.println(p.getType() + ": "  + p.getCoins());
        }
    }
}
