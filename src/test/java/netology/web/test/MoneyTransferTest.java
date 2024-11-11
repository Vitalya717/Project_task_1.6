package netology.web.test;

import com.codeborne.selenide.Selenide;
import netology.web.data.DataHelper;
import netology.web.page.DashboardPage;
import netology.web.page.LoginPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static netology.web.data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MoneyTransferTest {

    DashboardPage dashboardPage;
    DataHelper.CardInfo firstCardInfo;
    DataHelper.CardInfo secondCardInfo;
    int firstCardBalance;
    int secondCardBalance;


    @BeforeEach
    void setup() {
        Selenide.open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
        firstCardInfo = getFirstCardInfo();
        secondCardInfo = getSecondCardInfo();
        firstCardBalance = dashboardPage.getCardBalance(0);
        secondCardBalance = dashboardPage.getCardBalance(1);
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsFS() {

        int sum = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) - sum;
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) + sum;
        var transMoney = dashboardPage.selectCardForTransfer(secondCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(sum), firstCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }

    @Test
    void shouldTransferMoneyBetweenOwnCardsSF() {
        int sum = generateValidAmount(firstCardBalance);
        var expectedBalanceSecondCard = dashboardPage.getCardBalance(1) - sum;
        var expectedBalanceFirstCard = dashboardPage.getCardBalance(0) + sum;
        var transMoney = dashboardPage.selectCardForTransfer(firstCardInfo);
        dashboardPage = transMoney.toTransMoney(String.valueOf(sum), secondCardInfo);
        dashboardPage.reloadDashboardPage();
        var actualBalanceForSecondCard = dashboardPage.getCardBalance(1);
        var actualBalanceForFirstCard = dashboardPage.getCardBalance(0);
        assertAll(() -> assertEquals(expectedBalanceFirstCard,
                        actualBalanceForFirstCard),
                () -> assertEquals(expectedBalanceSecondCard, actualBalanceForSecondCard));
    }



}