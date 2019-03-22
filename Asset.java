package main;

import java.util.ArrayList;
import java.util.List;

// De fiecare data cand ma voi referi la asseturi ma voi referi la id-ul lor
class Asset implements Comparable<Asset> {
    // Asset bonuses
    private static final int SILK = 4;
    private static final int SILK_BONUS = 3;
    private static final int OTHER_BONUS = 2;

    private static final int APPLE_KING = 20;
    private static final int APPLE_QUEEN = 10;
    private static final int CHEESE_KING = 15;
    private static final int CHEESE_QUEEN = 10;
    private static final int BREAD_KING = 15;
    private static final int BREAD_QUEEN = 10;
    private static final int CHICKEN_KING = 10;
    private static final int CHICKEN_QUEEN = 5;

    // Diferenta de index dintre asseturile legale si cele ilegale
    private static final int OFFSET = 6;

    // Assets ids
    private static final int APPLE_ID = 0;
    private static final int CHEESE_ID = 1;
    private static final int BREAD_ID = 2;
    private static final int CHICKEN_ID = 3;
    private static final int SILK_ID = 4;
    private static final int PEPPER_ID = 5;

    // Assets penalties
    private static final int LEGALPENALTY = 2;
    private static final int ILLEGALPENALTY = 4;

    // Assets profits
    private static final int APPLE_PFT = 2;
    private static final int CHEESE_PFT = 3;
    private static final int BREAD_PFT = 4;
    private static final int CHICKEN_PFT = 4;
    private static final int SILK_PFT = 9;
    private static final int PEPPER_PFT = 8;
    private static final int BARREL_PFT = 7;

    // Assets properties
    private final int id;
    private final boolean legal;
    private final int profit;
    private final int penalty;
    private final int kingBonus;
    private final int queenBonus;
    private final int bonus;  // tipul assetului primit bonus

    // Initializarea unui asset
    Asset(final int id) {
        if (id > OFFSET) {
            this.id = id - OFFSET;
        } else {
            this.id = id;
        }

        switch (this.id) {
            case APPLE_ID: // Apple
                this.legal = true;
                this.profit = APPLE_PFT;
                this.penalty = LEGALPENALTY;
                this.bonus = -1;
                this.kingBonus = APPLE_KING;
                this.queenBonus = APPLE_QUEEN;
                break;

            case CHEESE_ID: // Cheese
                this.legal = true;
                this.profit = CHEESE_PFT;
                this.penalty = LEGALPENALTY;
                this.bonus = -1;
                this.kingBonus = CHEESE_KING;
                this.queenBonus = CHEESE_QUEEN;
                break;

            case BREAD_ID: // Bread
                this.legal = true;
                this.profit = BREAD_PFT;
                this.penalty = LEGALPENALTY;
                this.bonus = -1;
                this.kingBonus = BREAD_KING;
                this.queenBonus = BREAD_QUEEN;
                break;

            case CHICKEN_ID: // Chicken
                this.legal = true;
                this.profit = CHICKEN_PFT;
                this.penalty = LEGALPENALTY;
                this.bonus = -1;
                this.kingBonus = CHICKEN_KING;
                this.queenBonus = CHICKEN_QUEEN;
                break;

            case SILK_ID: // Silk
                this.legal = false;
                this.profit = SILK_PFT;
                this.penalty = ILLEGALPENALTY;
                this.bonus = CHEESE_ID;
                this.kingBonus = 0;
                this.queenBonus = 0;
                break;

            case PEPPER_ID: // Pepper
                this.legal = false;
                this.profit = PEPPER_PFT;
                this.penalty = ILLEGALPENALTY;
                this.bonus = CHICKEN_ID;
                this.kingBonus = 0;
                this.queenBonus = 0;
                break;

            default: // Barrel
                this.legal = false;
                this.profit = BARREL_PFT;
                this.penalty = ILLEGALPENALTY;
                this.bonus = BREAD_ID;
                this.kingBonus = 0;
                this.queenBonus = 0;
                break;
        }
    }

    // Gettere
    int getId() {
        return this.id;
    }

    int getProfit() {
        return this.profit;
    }

    int getPenalty() {
        return this.penalty;
    }

    boolean isLegal() {
        return this.legal;
    }

    List<Asset> getBonus() {
        List<Asset> l = new ArrayList<>();

        // Doar silkul are o cantitate diferita fata de celelalte la bonus
        int bonusAssetsNo = (this.id == SILK) ? SILK_BONUS : OTHER_BONUS;

        // Adaug cate asseturi trebuie pentru bonus in lista
        for (int i = 0; i < bonusAssetsNo; i++) {
            l.add(new Asset(this.bonus));
        }

        return l;
    }

    int getKingBonus() {
        return this.kingBonus;
    }

    int getQueenBonus() {
        return this.queenBonus;
    }


    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Asset)) {
            return false;
        }

        return ((Asset) o).id == this.id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public int compareTo(final Asset o) {
        return (this.getProfit() < o.getProfit()) ? 1 : -1;
    }
}
