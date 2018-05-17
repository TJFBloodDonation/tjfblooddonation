package ro.ubb.tjfblooddonation.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ro.ubb.tjfblooddonation.config.AppLocalConfig;
import ro.ubb.tjfblooddonation.config.CacheConfig;
import ro.ubb.tjfblooddonation.config.CatalogConfig;
import ro.ubb.tjfblooddonation.config.JPAConfig;
import ro.ubb.tjfblooddonation.model.Address;
import ro.ubb.tjfblooddonation.model.Donor;
import ro.ubb.tjfblooddonation.model.IdCard;
import ro.ubb.tjfblooddonation.repository.DonorRepository;

import java.sql.Date;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CatalogConfig.class, AppLocalConfig.class,
        JPAConfig.class, CacheConfig.class})
public class DonorRepositoryTest {

    @Autowired
    DonorRepository donorRepository;

    @Test
    public void test(){
        assertNotEquals(donorRepository, null);
        Address address = new Address("a", "b", "c", "d");
        IdCard idCard = new IdCard(address, "cnp");
        Donor donor = new Donor("a", "a", "e", "0", "AB", "positive", address,
                Date.valueOf("1999-2-2"), idCard, "male");

        try{
            donorRepository.getById(donor.getId());
            assert(false);
        }catch (RuntimeException e){
            assert(true);
        }
        donor = donorRepository.add(donor);
        assertEquals(donor.getBloodType(), "AB");
        assertEquals(donorRepository.getById(donor.getId()), donor);
        donor.setBloodType("A");
        donor = donorRepository.update(donor);
        assertEquals(donorRepository.getById(donor.getId()), donor);
        assertEquals(donor.getBloodType(), "A");
        donorRepository.remove(donor.getId());
    }

}
