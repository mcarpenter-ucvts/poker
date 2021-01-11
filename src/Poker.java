import java.util.*;

public class Poker {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private List<Card> deck;
    private final Scanner in;
    private int chips = 25;
    private int wager;
    private int cardsChosen;
    private int remove = -1;

    public Poker() {
        this.player = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        wager();
        clearHand();
        clearInfo();
        shuffleAndDeal();
        showHand();
        takeTurn();
        endRound();
    }

    private void wager() {
        do {
            System.out.println("Your total chips right now: " + chips);
            System.out.println("How many chips would you like to wager(Min: 1, Max: 25)");
            wager = in.nextInt();
            if (wager > chips) {
                System.out.println("Sorry, you only have " + chips + " chips remaining, but you wagered " + wager + " chips.");
            }
        } while (wager < 1 || wager > 25 || wager > chips);
        in.nextLine();
    }

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck
        while (player.getHand().size() < 5) {
            player.takeCard(deck.remove(0));    // deal 2 cards to the player
        }
    }

    ////////// PRIVATE METHODS /////////////////////////////////////////////////////
    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private void takeTurn() {
        cardsChosen = -1;
        do {
            System.out.println("How many cards would you like to switch(Min: 0, Max: 3)");
            cardsChosen = in.nextInt();
        } while (cardsChosen <= -1 || cardsChosen > 3);
        for (int k = 0; k < cardsChosen; k++) {
            do {
                System.out.println("Choose the index of the card you would like to remove(First card has an index of 0)");
                remove = in.nextInt();
            } while (remove <= -1 || remove > player.hand.size());
            player.hand.remove(remove);
            player.handRanks.remove(remove);
            player.handSuits.remove(remove);
            showHand();
        }
        shuffleAndDeal();
        showHand();

    }

    private void endRound() {
        player.calculateHandType();

        switch (player.handType) {
            case "RoyalFlush":
                chips += wager * 100;
                System.out.println("Congratulations, you have just won with a Royal Flush!");
                break;
            case "StraightFlush":
                chips += wager * 50;
                System.out.println("Congratulations, you have just won with a Straight Flush!");
                break;
            case "FourOfAKind":
                chips += wager * 25;
                System.out.println("Congratulations, you have just won with a Four of a Kind!");
                break;
            case "FullHouse":
                chips += wager * 15;
                System.out.println("Congratulations, you have just won with a Full House!");
                break;
            case "Flush":
                chips += wager * 10;
                System.out.println("Congratulations, you have just won with a Flush!");
                break;
            case "Straight":
                chips += wager * 5;
                System.out.println("Congratulations, you have just won with a Straight!");
                break;
            case "ThreeOfAKind":
                chips += wager * 3;
                System.out.println("Congratulations, you have just won with a Three of a Kind!");
                break;
            case "TwoPair":
                chips += wager * 2;
                System.out.println("Congratulations, you have just won with a Two Pair!");
                break;
            case "HighPair":
                chips += wager * 1;
                System.out.println("Congratulations, you have just won with a Pair!");
                break;
            default:
                chips -= wager;
                System.out.println("Unfortunately, you have lost this round. Better luck next time.");
                break;
        }

        if (chips <= 0) {
            endGame();
        }
        in.nextLine();
        String answer = "";
        do {
            System.out.println("Would you like to end the game here or continue playing(E or C)");
            answer = in.nextLine().toUpperCase();
        } while (answer.equals("E") == false && answer.equals("C") == false);
        if (answer.equals("E")) {
            endGame();
        } else {
            play();
        }

    }

    private void showHand() {
        System.out.println("\nPLAYER hand: " + player.getHand()); //shows player's full hand
    }

    private void clearHand() {
        player.clearHand();
    }

    private void clearInfo() {
        player.clearInfo();
    }

    private void endGame() {
        if (chips <= 0) {
            System.out.println("You ran out of chips and lost.");
        } else {
            System.out.println("You ended up with " + chips + " chips. Good job!");
        }
        System.exit(0);
    }
    

    public static void main(String[] args) {
        System.out.println("POKER");
        new Poker().play();
    }
}