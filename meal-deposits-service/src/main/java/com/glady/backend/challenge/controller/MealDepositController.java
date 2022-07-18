package com.glady.backend.challenge.controller;

import com.glady.backend.challenge.dao.CompanyRepository;
import com.glady.backend.challenge.dao.MealDepositRepository;
import com.glady.backend.challenge.dao.UserRepository;
import com.glady.backend.challenge.model.Company;
import com.glady.backend.challenge.model.MealDeposit;
import com.glady.backend.challenge.model.MealDeposits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

import static com.glady.backend.challenge.utils.ExpirationDateUtils.geExpirationDate;
import static com.glady.backend.challenge.utils.ExpirationDateUtils.makeNewExpirationDate;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/mealDeposit")
public class MealDepositController {

    Predicate<Date> isStillValid = x -> x.before(new Date());

    @Value("${expiration.month}")
    private Integer expirationMonth;
    @Autowired
    private MealDepositRepository mealDepositRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/distribute/{companyId}/{employeId}/{amount}")
    public ResponseEntity distributeMealDeposit(@PathVariable("companyId") int companyId,@PathVariable("employeId") int employeId,@PathVariable("id") float amount) {
        if (!this.companyRepository.existsById(companyId) || !this.userRepository.existsById(employeId))
            return new ResponseEntity<>(NOT_FOUND);
        if (this.companyRepository.findById(companyId).get().getBalance()<amount)
            return new ResponseEntity<>(NOT_ACCEPTABLE);
        if (this.userRepository.findById(employeId).get().getCompanyId() != companyId)
            return new ResponseEntity<>(NOT_ACCEPTABLE);

        Date currentYearLastExpirationDate = geExpirationDate(expirationMonth);
        if (new Date().before(currentYearLastExpirationDate))
            mealDepositRepository.save(new MealDeposit(null,companyId,employeId,amount, currentYearLastExpirationDate));
        else {
            mealDepositRepository.save(new MealDeposit(null,companyId,employeId,amount, makeNewExpirationDate(currentYearLastExpirationDate)));
        }
        Optional<Company> company = companyRepository.findById(companyId);
        company.get().setBalance(company.get().getBalance()-amount);
        companyRepository.save(company.get());
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/userBalance")
    public Float getUserBalance() {
        return new MealDeposits(mealDepositRepository.findAll().spliterator()).getMealDepositList()
                .stream().filter(mealDeposit -> isStillValid.test(mealDeposit.getExpirationDate()))
                .reduce(0.0f, (sum, mealDeposit) -> sum + mealDeposit.getAmount(), Float::sum);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMealDeposit(@PathVariable int id) {
        if (mealDepositRepository.existsById(id)) {
            mealDepositRepository.deleteById(id);
            return new ResponseEntity<>(NO_CONTENT);
        } else
            return new ResponseEntity<>(NOT_FOUND);
    }
}
