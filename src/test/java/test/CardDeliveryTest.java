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

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class CardDeliveryTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("Allure", new AllureSelenide());
    }
    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }
    @Test
    void shouldPassWhenNameWithHyphenTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Санкт-Петербург");
        form.$("[data-test-id=name] .input__control").setValue("Салтыков-Щедрин Юрок");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(12));
        $(".notification__title").shouldHave(exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generateDate(3, "dd.MM.yyyy")))
                .shouldBe(Condition.visible);
    }

    // тесты на 1 поле Город
    @Test
    //в названии города дефис
    void shouldPassWhenCityWithHyphenTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Ростов-на-Дону");
        form.$("[data-test-id=name] .input__control").setValue("Васильев Андрей");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(12));
        $(".notification__title").shouldHave(exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generateDate(3, "dd.MM.yyyy")))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldNotPassWhenCityIsNotFromListTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Белогорск");
        form.$("[data-test-id=name] .input__control").setValue("Васильев Андрей");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldNotPassWhenCityNotInRussianTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Moscow");
        form.$("[data-test-id=name] .input__control").setValue("Васильев Андрей");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=city] .input__sub").shouldHave(exactText("Доставка в выбранный город недоступна"));
    }

    // тесты на 2 поле Дата встречи
    @Test
    void shouldPassWhenDatePlusThreeDaysTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Благовещенск");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control").setValue(DataGenerator.generateDate(6, "dd.MM.yyyy"));
        form.$("[data-test-id=name] .input__control").setValue("Кошечкин");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=success-notification]").shouldBe(visible, Duration.ofSeconds(12));
        $(".notification__title").shouldHave(exactText("Успешно!"));
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + DataGenerator.generateDate(6, "dd.MM.yyyy")))
                .shouldBe(Condition.visible);
    }

    @Test
    void shouldNotPassWhenDateFromThePastTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Москва");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control").setValue(DataGenerator.generateDate(-10, "dd.MM.yyyy"));
        form.$("[data-test-id=name] .input__control").setValue("Кошечкин молодец");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldNotPassWhenDateIsTodayTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Москва");
        form.$("[data-test-id=date] .input__control").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        form.$("[data-test-id=date] .input__control").setValue(DataGenerator.generateDate(0, "dd.MM.yyyy"));
        form.$("[data-test-id=name] .input__control").setValue("Кошечкин молодец");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567891");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=date] .input__sub").shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    // тесты на 3 поле Имя и Фамилия
    @Test
    void shouldNotPassWhenNameNotRussianTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Москва");
        form.$("[data-test-id=name] .input__control").setValue("Frank Кошечкин");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldNotPassWhenNameWithOtherSymbolsTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Москва");
        form.$("[data-test-id=name] .input__control").setValue("Кошечкин молодец.");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".checkbox").click();
        form.$(".button").click();
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue(",");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("#");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue(";");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("\'");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("\"");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("?");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("(");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue(")");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("_");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("+");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("=");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("~");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("`");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("#");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("@");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("&");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("/");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("\\");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("<");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue(">");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("*");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("%");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
        form.$("[data-test-id=name] .input__control").sendKeys(Keys.BACK_SPACE);
        form.$("[data-test-id=name] .input__control").setValue("$");
        $("[data-test-id=name] .input__sub").shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    // тест на чекбокс

    @Test
    void shouldNotPassWhenCheckboxDisabledTest() {
        open("http://localhost:9999");
        SelenideElement form = $("form");
        form.$("[data-test-id=city] .input__control").setValue("Абакан");
        form.$("[data-test-id=name] .input__control").setValue("Кошечкин Фрэнк");
        form.$("[data-test-id=phone] .input__control").setValue("+79234567890");
        form.$(".button").click();
        $("[data-test-id='agreement'].input_invalid").shouldBe(visible);
    }

}