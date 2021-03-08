package blackjack.domain.participants;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    @DisplayName("이름 공백 예외처리")
    void name() {
        assertThatThrownBy(() -> new Name(""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("공백은 이름으로 사용할 수 없습니다.");
    }
}
