package com.glady.backend.challenge;

import com.glady.backend.challenge.dao.CompanyRepository;
import com.glady.backend.challenge.dao.UserRepository;
import com.glady.backend.challenge.dao.VoutcherDepositRepository;
import com.glady.backend.challenge.model.Company;
import com.glady.backend.challenge.model.User;
import com.glady.backend.challenge.model.VoutcherDeposit;
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

import static com.glady.backend.challenge.utils.ExpirationDateUtils.makeNewExpirationDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VoutcherDepositServiceTest {
    private static final String FIRST_NAME = "Anass";
    private static final String NAME = "Boudrissi";
    private static final  Integer companyId = 1;
    private static final  Integer userId = 23;
    private static final LocalDate DATE_2022_FEBRUARY_28 = LocalDate.of(2022, 7, 28);

    private static final Date DATE_2022_FEBRUARY_23 = Date.from(LocalDate.of(2023, 7, 28).atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final Date DATE_2022_FEBRUARY_28_DATE = Date.from(DATE_2022_FEBRUARY_28.atStartOfDay(ZoneId.systemDefault()).toInstant());
    private static final VoutcherDeposit voucher = new VoutcherDeposit(1,1,23,150.00f,DATE_2022_FEBRUARY_28_DATE);
    private static final User user = new User(userId,companyId,FIRST_NAME,NAME);

    private static final Company company = new Company(companyId,"glady",180.00f);
    @Mock
    private VoutcherDepositRepository voutcherDepositRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    public void shouldRegisterVoutcherDeposit(){
        assertVoutcherDepositegisteredCorrectly(voucher,companyId,userId,DATE_2022_FEBRUARY_28_DATE);
    }

    @Test
    public void testExpirationDate(){
        assertVoutcherDepositegisteredCorrectly(voucher,companyId,userId,DATE_2022_FEBRUARY_28_DATE);
        assertEquals(DATE_2022_FEBRUARY_23,makeNewExpirationDate(voucher.getExpirationDate()));
    }

    @Test
    public void testUsersRegistred(){
        mockVoutcher(new ArrayList<User>());
        assertNotNull(this.voutcherDepositRepository.findAll());

        mockVoutcherSearchById(companyId,voucher);
        assertNotNull(this.voutcherDepositRepository.findById(1));

        mockCompanySearchById(companyId,company);
        assertNotNull(this.companyRepository.findById(companyId));

        mockUserSearchById(userId,user);
        assertNotNull(this.userRepository.findById(userId));

    }

    private void assertVoutcherDepositegisteredCorrectly(VoutcherDeposit voutcherDeposit, Integer companyId, Integer userId, Date date) {
        assertNotNull(voutcherDeposit.getId());
        assertEquals(companyId, voutcherDeposit.getCompanyId());
        assertEquals(userId, voutcherDeposit.getUserId());
        assertEquals(date, voutcherDeposit.getExpirationDate());
    }

    private void mockVoutcher(List<User> users) {
        when(this.userRepository.findAll()).thenReturn(Sets.newHashSet(users));
    }

    private void mockVoutcherSearchById(Integer id, VoutcherDeposit voutcherDeposit) {
        when(this.voutcherDepositRepository.findById(id)).thenReturn(Optional.ofNullable(voutcherDeposit));
    }

    private void mockCompanySearchById(Integer id, Company company) {
        when(this.companyRepository.findById(id)).thenReturn(Optional.of(company));
    }

    private void mockUserSearchById(Integer id, User user) {
        when(this.userRepository.findById(id)).thenReturn(Optional.ofNullable(user));
    }

}
