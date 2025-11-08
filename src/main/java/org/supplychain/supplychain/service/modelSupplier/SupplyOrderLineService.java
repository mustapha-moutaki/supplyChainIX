package org.supplychain.supplychain.service.modelSupplier;

import org.supplychain.supplychain.dto.supplyOrder.SupplyOrderLineDTO;

import java.util.List;

public interface SupplyOrderLineService {

    SupplyOrderLineDTO createLine(SupplyOrderLineDTO dto);

    SupplyOrderLineDTO getLineById(Long id);

    List<SupplyOrderLineDTO> getAllLines();

    SupplyOrderLineDTO updateLine(Long id, SupplyOrderLineDTO dto);

    void deleteLine(Long id);
}
