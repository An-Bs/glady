package com.glady.backend.challenge;

import com.glady.backend.challenge.dao.CompanyRepository;
import com.glady.backend.challenge.dao.MealDepositRepository;
import com.glady.backend.challenge.dao.UserRepository;
import com.glady.backend.challenge.model.Company;
import com.glady.backend.challenge.model.MealDeposit;
import com.glady.backend.challenge.model.User;
import org.assertj.core.util.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.glady.backend.challenge.utils.ExpirationDateUtils.geExpirationDate;
import static com.glady.backend.challenge.utils.ExpirationDateUtils.makeNewExpirationDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MealDepositServiceTest {
    private static final String FIRST_NAME = "Anass";
    private static final String NAME = "Boudrissi";
    private static final  Integer companyId = 1;
    private static final  Integer userId = 23;

    private static final  Integer expirationMonth = 1;
    private static final LocalDate DATE_2022_JULY_28 = LocalDate.of(2022, 7, 28);
    private static final Date DATE_2022_FEBRUARY_27 = Date.from(LocalDate.of(2022, 2, 27).atTime(23,59,59).atZone(ZoneId.systemDefault()).toInstant());

    private static final Date DATE_2022 = Date.from(LocalDate.of(2023, 2, 28).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final Date DATE_2022_FEBRUARY_28_DATE = Date.from(DATE_2022_JULY_28.atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final MealDeposit voucher = new MealDeposit(1,1,23,150.00f,DATE_2022_FEBRUARY_28_DATE);
    private static final User user = new User(userId,companyId,FIRST_NAME,NAME);

    private static final Company company = new Company(companyId,"glady",180.00f);
    @Mock
    private MealDepositRepository mealDepositRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldRegisterMealDeposit(){
        assertMealDepositegisteredCorrectly(voucher,companyId,userId,DATE_2022_FEBRUARY_28_DATE);
    }

    @Test
    public void testExpirationDate(){
        assertMealDepositegisteredCorrectly(voucher,companyId,userId,DATE_2022_FEBRUARY_28_DATE);
        assert(makeNewExpirationDate(voucher.getExpirationDate()).after(DATE_2022));
        assert(geExpirationDate(expirationMonth).after(DATE_2022_FEBRUARY_27));
    }

    @Test
    public void testUsersRegistred(){
        mockMeal(new ArrayList<User>());
        assertNotNull(this.mealDepositRepository.findAll());

        mockMealSearchById(companyId,voucher);
        assertNotNull(this.mealDepositRepository.findById(1));

        mockCompanySearchById(companyId,company);
        assertNotNull(this.companyRepository.findById(companyId));

        mockUserSearchById(userId,user);
        assertNotNull(this.userRepository.findById(userId));

    }

    private void assertMealDepositegisteredCorrectly(MealDeposit mealDeposit, Integer companyId, Integer userId, Date date) {
        assertNotNull(mealDeposit.getId());
        assertEquals(companyId, mealDeposit.getCompanyId());
        assertEquals(userId, mealDeposit.getUserId());
        assertEquals(date, mealDeposit.getExpirationDate());
    }

    private void mockMeal(List<User> users) {
        when(this.userRepository.findAll()).thenReturn(Sets.newHashSet(users));
    }

    private void mockMealSearchById(Integer id, MealDeposit mealDeposit) {
        when(this.mealDepositRepository.findById(id)).thenReturn(Optional.ofNullable(mealDeposit));
    }

    private void mockCompanySearchById(Integer id, Company company) {
        when(this.companyRepository.findById(id)).thenReturn(Optional.of(company));
    }

    private void mockUserSearchById(Integer id, User user) {
        when(this.userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
    }

}
