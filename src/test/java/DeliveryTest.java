import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.generateDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeliveryTest {

    @BeforeEach
    void setup() {
       open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 4;
        var firstMeetingDate = generateDate(daysToAddForFirstMeeting);
        var actualDate = firstMeetingDate;
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = generateDate(daysToAddForSecondMeeting);
        $(By.cssSelector("[placeholder='Город']")).sendKeys(validUser.getCity());
        $(By.cssSelector("[name='name']")).sendKeys(validUser.getName());
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $(By.cssSelector("[name='phone']")).sendKeys(validUser.getPhone());
        $(By.cssSelector("[class='checkbox__text']")).click();
        $(By.cssSelector("button.button")).click();
        String text = $("[class='notification__title']").getText();
        if (text.equals("Необходимо подтверждение")){
            $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
            $("[data-test-id='date'] input").setValue(secondMeetingDate);
            $(By.cssSelector("button.button")).click();
            text = $("[class='notification__title']").getText();
            actualDate = secondMeetingDate;
        }
        assertEquals("Успешно!", text);
        $("[data-test-id='success-notification'] [class='notification__content']")
                .shouldHave(Condition.text("Встреча успешно запланирована на " + actualDate))
                .shouldBe(Condition.visible);
    }
}