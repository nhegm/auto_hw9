package test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class PlanningDateTest {
    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @Test
    void shouldPassWhenNewDateSetTest() {

        DataGenerator.UserInfo user = DataGenerator.Registration.generateUser("ru");
        String planingDate = DataGenerator.generateDate(6, "dd.MM.yyyy");
        String newPlaningDate = DataGenerator.generateDate(9, "dd.MM.yyyy");

        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=name] .input__control").setValue(user.getName());
        form.$("[data-test-id=phone] .input__control").setValue(user.getPhone());
        form.$("[data-test-id=city] .input__control").setValue(user.getCity());
        form.$("[data-test-id=city] .input__control").sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control")
                .setValue(planingDate);
        form.$("[data-test-id=date] .input__control").click();
        form.$(".checkbox").sendKeys(Keys.SPACE);
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + planingDate))
                .shouldBe(Condition.visible);
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue(user.getName());
        form.$("[data-test-id=phone] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=phone] .input__control").setValue(user.getPhone());
        form.$("[data-test-id=city] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=city] .input__control").setValue(user.getCity());
        form.$("[data-test-id=city] .input__control").sendKeys(Keys.ARROW_DOWN, Keys.ARROW_DOWN, Keys.ENTER);
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control")
                .setValue(newPlaningDate);
        form.$(".button").click();
        $("[data-test-id=replan-notification]").shouldBe(visible);
        $("[data-test-id=replan-notification] .notification__title").shouldHave(exactText("Необходимо подтверждение"));
        $("[data-test-id=replan-notification] .notification__content")
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] .button__text").shouldHave(exactText("Перепланировать")).click();
        $("[data-test-id=success-notification]").shouldBe(visible);
        $(".notification__title").shouldHave(exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + newPlaningDate))
                .shouldBe(Condition.visible);
    }

}
