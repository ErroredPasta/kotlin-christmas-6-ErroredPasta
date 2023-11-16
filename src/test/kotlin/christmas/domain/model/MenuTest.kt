package christmas.domain.model

import christmas.domain.exception.MenuNotExistException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class MenuTest {
    @ParameterizedTest
    @MethodSource("provideMenuNameAndMenu")
    @DisplayName("존재하는 메뉴 이름이 주어지면 해당 메뉴를 return")
    fun getMenuByMenuName_menuExists_returnCorrectMenu(menuName: String, expected: Menu) {
        // when
        val result = Menu.getMenuByMenuName(menuName = menuName)

        // then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    @DisplayName("존재하지 않은 메뉴 이름이 주어지면 예외 발생")
    fun getMenuByMenuName_menuNotExists_returnCorrectMenu() {
        // given
        val menuNameNotExists = "크림파스타"

        val exception = assertThrows<MenuNotExistException> {
            Menu.getMenuByMenuName(menuName = menuNameNotExists)
        }

        // then
        assertThat(exception.message).contains(Menu.MENU_NOT_EXIST_MESSAGE)
    }

    companion object {
        @JvmStatic
        private fun provideMenuNameAndMenu(): Stream<Arguments> = Stream.of(
            Arguments.of("양송이수프", Menu.BUTTON_MUSHROOM_SOUP),
            Arguments.of("티본스테이크", Menu.T_BONE_STEAK),
            Arguments.of("초코케이크", Menu.CHOCOLATE_CAKE),
            Arguments.of("제로콜라", Menu.ZERO_COKE),
        )
    }
}