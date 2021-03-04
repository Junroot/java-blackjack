package blackjack.view;

import blackjack.domain.GameResult;
import blackjack.domain.Players;
import blackjack.domain.ResultType;
import blackjack.domain.card.Card;
import blackjack.domain.participant.Dealer;
import blackjack.domain.participant.Participant;
import blackjack.domain.participant.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OutputView {

    private static final String NAME_DELIMITER = ", ";

    public static void printInputNames() {
        System.out.println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)");
    }

    private static List<Participant> concatenateParticipants(Dealer dealer, Players players) {
        List<Participant> participants = new ArrayList<>(Collections.singletonList(dealer));
        participants.addAll(players.unwrap());
        return participants;
    }

    public static void printGameInitializeMessage(Dealer dealer, Players players,
        int startingCardCount) {
        List<Participant> participants = concatenateParticipants(dealer, players);
        String participantNames = participants.stream()
            .map(Participant::getName)
            .collect(Collectors.joining(NAME_DELIMITER));
        System.out.println("\n" + participantNames + "에게 " + startingCardCount + "장의 카드를 나누었습니다.");
    }

    public static void printParticipantsStatus(Dealer dealer, Players players, boolean withScore) {
        List<Participant> participants = concatenateParticipants(dealer, players);
        participants.forEach(participant -> printParticipantStatus(participant, withScore));
        System.out.println();
    }

    public static void printParticipantStatus(Participant participant, boolean withScore) {
        String cardNames = participant.getHand().unwrap().stream()
            .map(Card::getCardName)
            .collect(Collectors.joining(NAME_DELIMITER));

        String scoreMessage = makeMessageByScore(participant, withScore);
        System.out.println(participant.getName() + "카드: " + cardNames + scoreMessage);
    }

    private static String makeMessageByScore(Participant participant, boolean withScore) {
        String scoreMessage = "";
        if (withScore) {
            scoreMessage = makeMessageByBust(participant);
        }
        return scoreMessage;
    }

    private static String makeMessageByBust(Participant participant) {
        String scoreMessage = " - 결과: " + participant.getScore();
        if (participant.isBust()) {
            scoreMessage = " - 결과: BUST";
        }
        return scoreMessage;
    }

    public static void willDrawCard(Player player) {
        System.out.println(player.getName() + "는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)");
    }

    public static void printNewLine() {
        System.out.println();
    }

    public static void printDealerDrawCard(Dealer dealer) {
        System.out
            .println("\n" + dealer.getName() + "는 " + Dealer.DEALER_LIMIT + "이하라 한장의 카드를 더 받았습니다.");
    }

    public static void printResult(GameResult gameResult) {
        Map<ResultType, Integer> statistics = gameResult.getStatistics();
        System.out.print("\n## 최종 승패\n딜러: ");
        printDealerResult(statistics);
        printPlayerResult(gameResult);
    }

    private static void printDealerResult(Map<ResultType, Integer> statistics) {
        for (ResultType resultType : statistics.keySet()) {
            System.out.print(statistics.get(resultType) + resultType.opposite().getName() + " ");
        }
        System.out.println();
    }

    private static void printPlayerResult(GameResult gameResult) {
        Map<Player, ResultType> unwrappedResult = gameResult.unwrap();
        unwrappedResult.keySet()
            .forEach(player -> System.out
                .println(player.getName() + ": " + unwrappedResult.get(player).getName()));
    }
}
