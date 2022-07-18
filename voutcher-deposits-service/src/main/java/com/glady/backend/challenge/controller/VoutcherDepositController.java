package com.glady.backend.challenge.controller;

import com.glady.backend.challenge.dao.CompanyRepository;
import com.glady.backend.challenge.dao.UserRepository;
import com.glady.backend.challenge.dao.VoutcherDepositRepository;
import com.glady.backend.challenge.model.Company;
import com.glady.backend.challenge.model.VoutcherDeposit;
import com.glady.backend.challenge.model.VoutcherDeposits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

import static com.glady.backend.challenge.utils.ExpirationDateUtils.makeNewExpirationDate;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/voutcherDeposit")
public class VoutcherDepositController {

    Predicate<Date> isStillValid = x -> x.before(new Date());
    @Autowired
    private VoutcherDepositRepository voutcherDepositRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @PostMapping("/distribute/{companyId}/{employeId}/{amount}")
    public ResponseEntity distributeVoutcherDeposit(@PathVariable("companyId") int companyId,@PathVariable("employeId") int employeId,@PathVariable("id") float amount) {
        if (!this.companyRepository.existsById(companyId) || !this.userRepository.existsById(employeId))
            return new ResponseEntity<>(NOT_FOUND);
        if (this.companyRepository.findById(companyId).get().getBalance()<amount)
            return new ResponseEntity<>(NOT_ACCEPTABLE);
        if (this.userRepository.findById(employeId).get().getCompanyId() != companyId)
            return new ResponseEntity<>(NOT_ACCEPTABLE);
        Date newExpirationDate =makeNewExpirationDate(new Date());
        voutcherDepositRepository.save(new VoutcherDeposit(null,companyId,employeId,amount, newExpirationDate));
        Optional<Company> company = companyRepository.findById(companyId);
        company.get().setBalance(company.get().getBalance()-amount);
        companyRepository.save(company.get());
        return new ResponseEntity<>(NO_CONTENT);
    }

    @GetMapping("/userBalance")
    public Float getUserBalance() {
        return new VoutcherDeposits(voutcherDepositRepository.findAll().spliterator()).getVoutcherDepositList()
                .stream().filter(voutcherDeposit -> isStillValid.test(voutcherDeposit.getExpirationDate()))
                .reduce(0.0f, (sum, voutcherDeposit) -> sum + voutcherDeposit.getAmount(), Float::sum);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteVoutcherDeposit(@PathVariable int id) {
        if (voutcherDepositRepository.existsById(id)) {
            voutcherDepositRepository.deleteById(id);
            return new ResponseEntity<>(NO_CONTENT);
        } else
            return new ResponseEntity<>(NOT_FOUND);
    }
}
