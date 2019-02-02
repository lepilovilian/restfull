package com.ilepilov.restfull.repository;

import com.ilepilov.restfull.entity.AddressEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AddressRepo extends PagingAndSortingRepository<AddressEntity, Long> {

    List<AddressEntity> findAllByUserId(Pageable pageable, Long userId);

    AddressEntity findByPublicAddressIdAndUserId(String addressId, Long userId);
}
