package org.supplychain.supplychain.unit.impl.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.supplychain.supplychain.model.Supplier;
import org.supplychain.supplychain.repository.approvisionnement.SupplierRepository;

@DataJpaTest
class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void testSaveSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        supplier.setContact("Contact Person");
        supplier.setEmail("test@supplier.com");
        supplier.setPhone("0612345678");
        supplier.setLeadTime(5);
        supplier.setRating(4.0);

        Supplier saved = supplierRepository.save(supplier);

        assert saved.getIdSupplier() != null;
    }

}
