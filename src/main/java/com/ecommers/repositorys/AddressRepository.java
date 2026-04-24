package com.ecommers.repositorys;

import com.ecommers.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID>
{
    List<Address> findByUser_Id(UUID userId);
}
