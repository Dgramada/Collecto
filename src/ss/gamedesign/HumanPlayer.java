package ss.gamedesign;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

//    @Override
//    public int determineSingleMove(Move move) {
//
//        String prompt = "> " + getName() + "'s turn. Please enter single move command: ";
//        System.out.println(prompt);
//
//        int playerMove;
//
//        while (true) {
//            playerMove = TextIO.getlnInt();
//            if (playerMove < 0 || playerMove > 27) {
//                System.out.println("Invalid command. Please try again: ");
//            } else {
//                break;
//            }
//        }
//
//        return playerMove;
//    }
//
//    @Override
//    public int[] determineDoubleMove(Move move) {
//
//        int[] movesArray = new int[2];
//        String prompt = "> " + getName() + "'s turn. Please enter double move command: ";
//        System.out.println(prompt);
//
//        int firstMove;
//        int secondMove;
//
//        while (true) {
//            firstMove = TextIO.getlnInt();
//            secondMove = TextIO.getlnInt();
//            movesArray[0] = firstMove;
//            movesArray[1] = secondMove;
//
//            if (firstMove < 0 || firstMove > 27) {
//                System.out.println("Invalid command. Please try again: ");
//            } else if (secondMove < 0 || secondMove > 27) {
//                System.out.println("Invalid command. Please try again: ");
//            } else {
//                break;
//            }
//        }
//
//        return movesArray;
//    }

}
